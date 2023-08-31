package immersionIndustry.types.blocks.production;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.Vec2;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import arc.scene.ui.layout.Table;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;
import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;

import immersionIndustry.IMColors;
import immersionIndustry.contents.IMFx;

/*
*智能矿机
*/
public class IntelligentMiningMachine extends Block {
  
  //挖掘等级
  public int tier;
  //挖掘矿物的基本时间，单位帧
  public float drillTime = 10;
  public float range = 300;
  public float baseMineRange = 64;
  public int maxMine = 3;
  public float warmupSpeed = 0.015f;
  public Effect transferEffect = new Effect(48f, 600f, e -> {
    if(!(e.data instanceof Res data)) return;
    Vec2 vec = new Vec2(e.x,e.y);
    vec.lerp(data.inputX, data.inputY, Interp.sineIn.apply(e.fin()));
    data.set(vec.x,vec.y);
    }).layer(Layer.flyingUnitLow - 1);
  public Effect mineEffect = new Effect(24f, 600f, e -> {
    if(!(e.data instanceof ResData data)) return;
      Vec2 vec = new Vec2(e.x,e.y);
      vec.lerp(data.x, data.y, Interp.sineIn.apply(e.fin()));
      Draw.color(data.item.color);
      Fill.circle(vec.x, vec.y, 2 * e.fslope());
      Draw.color();
      Fill.circle(vec.x, vec.y, 1 * e.fslope());
  }).layer(Layer.flyingUnitLow - 1);
  
  public IntelligentMiningMachine(String name) {
    super(name);
    //此方块是否会更新，如果为false，则update方法和其他相关方法不会触发
    update = true;
    solid = true;
    group = BlockGroup.drills;
    hasItems = true;
    ambientSound = Sounds.drill;
    ambientSoundVolume = 0.018f;
    configurable = true;
  }
  
  @Override
  public void drawPlace(int x, int y, int rotation, boolean valid){
    super.drawPlace(x, y, rotation, valid);
    Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, range, Pal.placing);
  }
  
  public class IntelligentMiningMachineBuild extends Building {
    
    float inputX,inputY;
    int mineRange = 0;
    boolean inputRight;
    public Seq<Res> resources;
    public float progress;
    public float totalProgress;
    public float warmup;
    
    public IntelligentMiningMachineBuild() {
      resources = new Seq<Res>();
    }
    
    @Override
    public void updateTile() {
      inputX = player.x;
      inputY = player.y;
      inputRight = pointInsideCircle();
      
      progress += getProgressIncrease(drillTime);
      totalProgress += delta();
      warmup = Mathf.approachDelta(warmup, 1f, warmupSpeed);
      
      if(progress > 1 && timer(timerDump,drillTime)) {
        resources.each(res -> {
          res.update(this);
        });
      }
      dump();
    }
    
    @Override
    public void draw() {
      super.draw();
      resources.each(res -> {
        res.draw(this);
      });
      float orbRadius = 5 * (1f + Mathf.absin(20, 0.1f)) * efficiency();
      Draw.color(IMColors.colorPrimary);
      Draw.z(Layer.effect);
            
      Draw.alpha(efficiency());
            
      Fill.circle(x, y, orbRadius);
      Draw.color();
      Fill.circle(x, y, orbRadius / 2);
            
      Lines.stroke((0.7f + Mathf.absin(20, 0.7f)), IMColors.colorPrimary);
            
      for(int i = 0; i < resources.size; i++){
        float rot = rotation + i * 360f/resources.size - Time.time * 0.5f;
        Lines.arc(x, y, orbRadius + 3f, 0.14f, rot);
      }
      Drawf.light(x, y, tilesize * 1.5f, IMColors.colorPrimary, warmup);
            
      Draw.reset();
    }
    
    @Override
    public void buildConfiguration(Table table) {
      table.button("投放",() -> {
        if(resources.size < maxMine && inputRight && efficiency() > 0) {
          Res res = new Res(inputX,inputY,mineRange);
          resources.add(res);
          transferEffect.at(x, y, rotation,res);
        }
      });
      table.row();
      table.add("范围");
      table.row();
      table.slider(0,4,1,mineRange,e -> {
        mineRange = (int)e;
      });
      table.row();
      table.table(cont -> {
        setup(cont);
      });
    }
    
    private void setup(Table table) {
      table.clear();
      for(int i = 0;i<resources.size;i++) {
        final int a = i;
        table.button("撤销第"+(a+1)+"个",() -> {
          resources.remove(a);
          setup(table);
        });
        table.row();
      }
    }
    
    @Override
    public void drawConfigure(){
      Drawf.dashCircle(x, y, range, team.color);
      Drawf.dashCircle(inputX, inputY, baseMineRange + mineRange * tilesize, inputRight ? team.color : Pal.meltdownHit);
    }
    
    @Override
    public void write(Writes write) {
      super.write(write);
      write.i(mineRange);
      write.i(resources.size);
      write.f(warmup);
      write.f(progress);
      write.f(totalProgress);
      resources.each(res -> {
        write.f(res.x);
        write.f(res.y);
      });
    }
    
    @Override
    public void read(Reads read, byte revision) {
      super.read(read,revision);
      mineRange = read.i();
      int size = read.i();
      warmup = read.f();
      progress = read.f();
      totalProgress = read.f();
      for(int i = 0;i<size;i++) {
        Res res = new Res(read.f(),read.f(),mineRange);
        resources.add(res);
        transferEffect.at(x, y, rotation,res);
      }
    }
    
    public boolean pointInsideCircle() {
      float dx = x - inputX;
      float dy = y - inputY;
      return dx * dx + dy * dy <= range * range;
    }
    
  }
  
  public class ResData {
    public float x,y;
    public Item item;
    
    public ResData(float x, float y,Item item) {
      this.x = x;
      this.y = y;
      this.item = item;
    }
    
  }
  
  private class Res {
    
    public float inputX,inputY,x = -1,y = -1;
    float resRange;
    Seq<Tile> generate;
    float curStroke;
    
    public Res(float x,float y,float mineRange) {
      this.inputX = x;
      this.inputY = y;
      generate = new Seq<Tile>();
      resRange = baseMineRange + mineRange * tilesize;
      //获取inputX,Y位置上的矿物
      world.tiles.eachTile(tile -> {
        //判断是否在圈内
        //使用arc中的Mathf.within可以达到相同的效果
        float dx = x - tile.worldx();
        float dy = y - tile.worldy();
        if(dx * dx + dy * dy <= resRange * resRange) {
          if(tile.drop() != null) {
            generate.add(tile);
          }
        }
      });
    }
    
    public void update(IntelligentMiningMachineBuild entity) {
      curStroke = Mathf.lerpDelta(curStroke,entity.efficiency(), 0.09f);
      generate.each(tile -> {
        Item item = tile.drop();
        if(item == null) {
          generate.remove(tile);
          return;
        }
        if(entity.items.get(item) < itemCapacity) {
          mineEffect.at(tile.drawx(),tile.drawy(),entity.rotation,new ResData(x,y,item));
          entity.offload(item);
        }
      });
      entity.progress %= 1f;
    }
    
    public void set(float x,float y) {
      this.x = x;
      this.y = y;
    }
    
    public void draw(IntelligentMiningMachineBuild entity) { 
      if(x == -1 || y == -1 ) return;
      float orbRadius = 5 * (1f + Mathf.absin(20, 0.1f)) * entity.efficiency();
      Draw.color(IMColors.colorPrimary);
      Draw.z(Layer.effect);
      
      Draw.alpha(entity.efficiency());
      
      Drawf.laser(Core.atlas.find("parallax-laser"),Core.atlas.find("parallax-laser-end"),entity.x,entity.y,x,y);
      
      Fill.circle(x, y, orbRadius);
      Draw.color();
      Fill.circle(x, y, orbRadius / 2);
      
      Lines.stroke((0.7f + Mathf.absin(20, 0.7f)), IMColors.colorPrimary);
      
      for(int i = 0; i < 5; i++){
        float rot = entity.rotation + i * 360f/5 - Time.time * 0.5f;
        Lines.arc(x, y, orbRadius + 3f, 0.14f, rot);
      }
      
      Lines.stroke(Lines.getStroke() * curStroke);
      
      if(curStroke > 0 && entity.efficiency() > 0) {
        for(int i = 0; i < 5; i++){
          float rot = entity.rotation + i * 360f/5 + Time.time * 0.5f;
          Lines.arc(x, y, resRange, 0.14f, rot);
        }
      }
      Drawf.light(x, y, resRange * 1.5f, IMColors.colorPrimary, curStroke * 0.8f);
      
      Draw.reset();
    }
    
  }
}