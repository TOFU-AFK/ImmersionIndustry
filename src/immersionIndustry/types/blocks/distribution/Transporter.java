package immersionIndustry.types.blocks.distribution;

import arc.*;
import arc.func.*;
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
import mindustry.entities.Units.*;
import mindustry.entities.bullet.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.meta.*;
import mindustry.world.consumers.*;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.blocks.defense.turrets.ReloadTurret.*;

import static mindustry.Vars.*;
import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;

import immersionIndustry.IMColors;
import immersionIndustry.contents.IMFx;

//运输载荷
public class Transporter extends PayloadBlock {
  
  public Color baseColor = IMColors.colorPrimary,healColor = IMColors.colorDarkPrimary;
  public float speed = 0f;
  public float displayedSpeed = 0f;
  
  public Transporter(String name) {
    super(name);
    update = true;
    sync = true;
    solid = true;
    configurable = true;
  }
  
  @Override
  public void setStats(){
    super.setStats();
    stats.add(Stat.itemsMoved, displayedSpeed, StatUnit.itemsSecond);
  }
  
  public class TransporterBuild extends PayloadBlockBuild<Payload> {
    
    public Seq<Payload> payloads = new Seq<>();
    float phaseHeat;
    int link = -1;
    
    @Override
    public void updateTile() {
      super.updateTile();
      phaseHeat = Mathf.lerpDelta(phaseHeat, Mathf.num(cons.optionalValid()), 0.1f);
      if(linkValid()) {
        Building link = world.build(this.link);
        
        if(payload != null && canMoveOut()) {
          payloads.add(payload);
          payload = null;
        }
      
        if(payloads.size > 0) {
          float moved = speed * edelta();
          payloads.each(pay -> {
            Tmp.v3.set(x, y).lerp(link.x, link.y, speed);
            pay.update(false);
            pay.set(pay.x() + Tmp.v3.x , pay.y() - Tmp.v3.y, pay.rotation());
            if(pay.dump()){
              payloads.remove(pay);
            }else{
              pay.set(pay.x() - Tmp.v3.x, pay.y() - Tmp.v3.y, pay.rotation());
            }
          });
        }
      }
    }
    
    public boolean canMoveOut() {
      if(payloads.size == 0) return true;
      return dst(payloads.get(payloads.size)) > 32;
    }
    
    @Override
    public boolean onConfigureTileTapped(Building other) {
      if(this == other) {
        if(link == -1) deselect();
        link = -1;
        return false;
      }
      if(link == other.pos()){
        link = -1;
        return false;
      }
      else if(other.block == block){
        link = other.pos();
        return false;
      }

      return true;
    }
    
    @Override
    public void drawConfigure() {
      float sin = Mathf.absin(Time.time, 6f, 1f);
      Draw.color(Pal.accent);
      Lines.stroke(1f);
      Drawf.circles(x, y, (tile.block().size / 2f + 1) * tilesize + sin - 2f, Pal.accent);
      
      if(linkValid()){
        Building target = world.build(link);
        Drawf.circles(target.x, target.y, (target.block().size / 2f + 1) * tilesize + sin - 2f, Pal.place);
        Drawf.arrow(x, y, target.x, target.y, size * tilesize + sin, 4f + sin);
      }
    }
    
    @Override
    public void draw() {
      Draw.rect(block.region, x, y, block.rotate ? rotdeg() : 0);
      if(linkValid()) {
        Building link = world.build(this.link);
        if(payloads.size > 0) {
          Draw.z(Layer.blockOver);
          payloads.each(pay -> {
            pay.draw();
          });
        }
        Vec2 right = Tmp.v1.trns(Angles.angle(x, y, link.x, link.y), 20, block.size * tilesize / 2f);
        Vec2 left = Tmp.v2.trns(Angles.angle(x, y, link.x, link.y), 20, -block.size * tilesize / 2f);
        Draw.z(Layer.effect);
        Lines.stroke(2);
        Draw.color(baseColor,healColor,phaseHeat);
        Lines.line(x + left.x, y + left.y, link.x - right.x, link.y - right.y);
        Lines.line(x + right.x, y + right.y, link.x - left.x, link.y - left.y);
        int ic = (int) dst(link) / (block.size*tilesize);
        for(int i = 0; i < ic; i++){
          Tmp.v3.set(x, y).lerp(link.x, link.y,i / ic);
          Drawf.square(Tmp.v3.x, Tmp.v3.y,tilesize*block.size/4,Angles.angle(x, y, link.x, link.y),baseColor);
        }

        Draw.reset();
      }
    }
    
    protected boolean linkValid(){
      if(link == -1) return false;
      return world.build(this.link) instanceof TransporterBuild other && other.block == block && other.team == team;
    }
    
  }

}