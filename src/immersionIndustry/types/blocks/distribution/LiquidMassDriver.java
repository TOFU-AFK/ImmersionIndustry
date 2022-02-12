package immersionIndustry.types.blocks.distribution;

import arc.*;
import arc.struct.*;
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
import mindustry.entities.bullet.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.defense.turrets.PowerTurret.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;
import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;

import immersionIndustry.IMColors;
import immersionIndustry.contents.IMFx;
import immersionIndustry.contents.IMBullets;

public class LiquidMassDriver extends PowerTurret {
  
  public float sputteringRange = 110f;
  
  public LiquidMassDriver(String name) {
    super(name);
    configurable = true;
    hasItems = false;
    hasLiquids = true;
    range = 420f;
  }
  
  public class DriverBuildData {
    
    public Building from,to;
    public Liquid liquid;
    public float amount;
    Seq<Building> seq;
    public float range = sputteringRange;
    
    public DriverBuildData(Building from,Building to,Liquid liquid) {
      this.from = from;
      this.to = to;
      this.liquid = liquid;
      this.amount = from.liquids.get(liquid);
      from.liquids.remove(liquid,amount);
      seq = new Seq<>();
      //这样seq会存储两个to，让to获得的液体最多
      seq.add(to);
    }
    
    public void boolean check() {
      if(from == null || to == null || liquid == null) {
        return false;
      }
      return true;
    }
    
    public void add(Building entity) {
      seq.add(entity);
    }
    
    public void transmit() {
      float a = amount / seq.size;
      Seq<Vec2> lines = new Seq<>();
      seq.each(entity -> {
        if(entity.acceptLiquid(from,liquid)) {
          entity.handleLiquid(from,liquid,a);
          lines.add(new Vec2(entity.x + Mathf.range(3f), entity.y + Mathf.range(3f)));
        }
      });
      Fx.lightning.at(to.x, to.y, from.rotation, liquid.color, lines);
    }
  }
  
  public class DriverBuild extends PowerTurretBuild {
    
    public int link = -1;
    
    @Override
    public void updateTile() {
      super.updateTile();
    }
    
    @Override
    public void drawConfigure() {
      float sin = Mathf.absin(Time.time, 6f, 1f);
      Draw.color(Pal.accent);
      Lines.stroke(1f);
      Drawf.circles(x, y, (block.size / 2f + 1) * tilesize + sin - 2f, Pal.accent);
      
      if(linkValid()){
        Building target = world.build(link);
        Drawf.circles(target.x, target.y, (target.block().size / 2f + 1) * tilesize + sin - 2f, Pal.place);
        Drawf.arrow(x, y, target.x, target.y, size * tilesize + sin, 4f + sin);
        Drawf.dashCircle(target.x, target.y, sputteringRange, Pal.accent);
      }

      Drawf.dashCircle(x, y, range, Pal.accent);
    }
    
    @Override
    protected boolean validateTarget(){
      return linkValid();
    }
    
    @Override
    protected void findTarget() {
      target = world.build(link);
    }
    
    @Override
    protected void bullet(BulletType type, float angle){
      float lifeScl = type.scaleVelocity ? Mathf.clamp(Mathf.dst(x + tr.x, y + tr.y, targetPos.x, targetPos.y) / type.range(), minRange / type.range(), range / type.range()) : 1f;
      type.create(this, team, x + tr.x, y + tr.y, angle,-1f,1f + Mathf.range(velocityInaccuracy), lifeScl,new DriverBuildData(this,world.build(link),liquids.current()));
    }
    
    protected boolean linkValid(){
      if(link == -1) return false;
      Building other = world.build(this.link);
      return other != null && other.block.hasLiquids && other.team == team && within(other, range);
    }
    
    @Override
    public boolean onConfigureTileTapped(Building other) {
      //如果选中的是自己，取消选中
      if(this == other){
        link = -1;
        return false;
      }
      //如果选中的是已被选中方块，取消选中
      if(link == other.pos()){
        link = -1;
        return false;
      }else if(other.block.hasLiquids && other.dst(tile) <= range && other.team == team) {
        link = other.pos();
        return false;
      }
      return true;
    }

    @Override
    public boolean acceptLiquid(Building source, Liquid liquid){
      return liquids.get(liquid) < liquidCapacity;
    }
    
    @Override
    public void write(Writes write){
      super.write(write);
      write.i(link);
    }

    @Override
    public void read(Reads read, byte revision){
      super.read(read, revision);
      link = read.i();
    }
    
  }
  
}