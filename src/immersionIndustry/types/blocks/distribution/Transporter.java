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
import mindustry.world.blocks.defense.turrets.ReloadTurret.*;

import static mindustry.Vars.*;
import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;

import immersionIndustry.IMColors;
import immersionIndustry.contents.IMFx;

//运输载荷
public class Transporter extends Block {
  
  public Color baseColor = IMColors.colorPrimary,healColor = IMColors.colorDarkPrimary;
  
  public Transporter(String name) {
    super(name);
    update = true;
    sync = true;
    solid = true;
    group = BlockGroup.payloads;
  }
  
  public class TransporterBuild extends Building {
    
    float phaseHeat;
    int link = -1;
    
    @Override
    public void updateTile() {
      phaseHeat = Mathf.lerpDelta(phaseHeat, Mathf.num(cons.optionalValid()), 0.1f);
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
      super.draw();
      /*float range = block.size * tilesize / 2f + 1f;
      Draw.color(baseColor,healColor,phaseHeat);
      Lines.stroke((0.7f + Mathf.absin(20, 0.7f)));
      Draw.alpha((0.3f + Mathf.absin(Time.time, 2f, 0.3f)) * phaseHeat);
      Draw.blend(Blending.additive);
      Lines.square(x,y,range,Time.time * 1.5f);
      
      Lines.square(x,y,range,45 + Time.time * 1.5f);
      
      Lines.circle(x,y,range);
      
      for(int i = 0; i < 5; i++){
        float rot = rotation + i * 360f/5 - Time.time * 0.5f;
        Lines.swirl(x, y, range + tilesize / 2, 0.14f, rot);
      }
      Drawf.light(x, y, range * 1.5f,healColor, phaseHeat);
      
      Draw.blend();
      Draw.color();*/
      if(linkValid()) {
        Building link = world.build(this.link);
        Vec2 right = Tmp.v1.trns(Angles.angle(x, y, link.x, link.y), 20, block.size * tilesize / 2f);
        Vec2 left = Tmp.v2.trns(Angles.angle(x, y, link.x, link.y), 20, -block.size * tilesize / 2f);
        Draw.z(Layer.effect);
        Lines.stroke(phaseHeat * 1.2f, Pal.accent);
        Lines.line(x + left.x, y + left.y, link.x - right.x, link.y - right.y);
        Lines.line(x + right.x, y + right.y, link.x - left.x, link.y - left.y);
        int ic = (int) dst(link) / (block.size*tilesize);
        for(int i = 0; i < ic; i++){
          Tmp.v3.set(x, y).lerp(link.x, link.y, 0.5f + (i - 2) * 0.1f);
          Drawf.square(Tmp.v3.x, Tmp.v3.y,tilesize*block.size/4,Angles.angle(x, y, link.x, link.y));
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