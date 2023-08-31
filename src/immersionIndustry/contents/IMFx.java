package immersionIndustry.contents;
import arc.*;
import arc.struct.*;
import arc.util.*;
import arc.math.*;
import arc.math.geom.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.ctype.*;
import mindustry.content.*;

import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.blocks.campaign.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.legacy.*;
import mindustry.world.blocks.liquid.*;
import mindustry.world.blocks.logic.*;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.sandbox.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.blocks.units.*;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;
import static mindustry.Vars.*;
import immersionIndustry.IMColors;

import immersionIndustry.types.blocks.defense.Diffuser;

public class IMFx {
  public static Effect dispersion,shockWave,absorbedEnergy,crystallizationEnergy,absorptionHeat,lossHeat,spread,absorb,radiation,sphere;
  
  public static void takeItemEffect(float x,float y,float x2,float y2,Color color,float lifeTime) {
    new Effect(lifeTime, e -> {
      Vec2 vec = new Vec2(e.x,e.y);
      vec.lerp(x2, y2, Interp.sineIn.apply(e.fin()));
      Draw.color(color);
      Fill.circle(vec.x + e.fslope() * 3, vec.y, 2 * e.fin());
      Draw.color();
      Fill.circle(vec.x + e.fslope() * 3, vec.y, 1 * e.fin());
    }).at(x,y);
  }
  
  public static void load() {

    sphere = new Effect(60,e -> {
      color(IMColors.colorPrimary,IMColors.colorYellow,e.fin());
      randLenVectors(e.id, e.fin(Interp.pow10Out), 11, 22, (x, y, in, out) -> {
        float rad = e.fout(Interp.pow5Out) * Mathf.rand.random(0.5f, 1f) * 2f;
        Fill.circle(e.x +  x, e.y + y, rad);
        Drawf.light(e.x + x, e.y + y, rad * 2.5f, IMColors.colorYellow, 0.5f);
      });
      Lines.stroke(2f * e.fin());
      Lines.circle(e.x, e.y,12f * e.fout());
    });
    
    radiation = new Effect(45,e -> {
      color(IMColors.colorPrimary,IMColors.colorYellow,e.fin());
      Tmp.v1.set(e.x,e.y).trns(e.rotation,16);
      Fill.circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, 2 * e.fout());
      Fill.circle(e.x - Tmp.v1.x, e.y - Tmp.v1.y, 2 * e.fout());
      color();
      Fill.circle(e.x + Tmp.v1.x, e.y + Tmp.v1.y, 1f * e.fout());
      Fill.circle(e.x - Tmp.v1.x, e.y - Tmp.v1.y, 1f * e.fout());
    });
    
    absorb = new Effect(30,e -> {
      color(IMColors.colorPrimary,IMColors.colorDarkPrimary,e.fin());
      alpha(0.7f);
      randLenVectors(e.id, e.fin(Interp.pow10Out), 12, 22, (x, y, in, out) -> {
          float rad = e.fout(Interp.pow5Out) * Mathf.rand.random(0.5f, 1f) * 2f;

          Fill.circle(e.x + x, e.y + y, rad);
          Drawf.light(e.x + x, e.y + y, rad * 2.5f, IMColors.colorDarkPrimary, 0.5f);
        });
    });
    
    spread = new Effect(45,e -> {
      float r = 60;
      if(e.data instanceof Float f) r = f;
      Draw.color(IMColors.colorYellow,IMColors.colorWhite,e.fin());
      Lines.stroke(3f * e.fin());
      Lines.circle(e.x, e.y, r*e.fin());
      Draw.color();
    });
    
    absorptionHeat = new Effect(30f,e -> {
      color(Color.orange,Color.white,e.fin());
      Fill.circle(e.x, e.y, 1 * e.fslope());
      randLenVectors(e.id, 2, 1f + 20f * e.fout(), e.rotation, 60f, (x, y) -> {
        lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 1.2f + 0.5f);
      });
    });
    
    lossHeat = new Effect(30f,e -> {
      color(Color.orange,Color.white,e.fin());
      Fill.circle(e.x, e.y, 1 * e.fslope());
      randLenVectors(e.id, 2, 1f + 20f * e.fout(), e.rotation, 60f, (x, y) -> {
        lineAngle(e.x - x, e.y - y, Mathf.angle(x, y), e.fslope() * 1.2f + 0.5f);
      });
    });
    
    dispersion = new Effect(60f,e -> {
      color(IMColors.colorPrimary,IMColors.colorDarkPrimary,e.fout());
      randLenVectors(e.id, 4, 1f + 20f * e.fout(), e.rotation, 64, (x, y) -> {
        lineAngle(e.x - x, e.y - y, Mathf.angle(x, y), e.fslope() * 3f + 1f);
      });
    });
    
    crystallizationEnergy = new Effect(80f, e -> {
      if(e.data instanceof Building entity) {
        Draw.z(Layer.effect);
        color(IMColors.colorPrimary,IMColors.colorDarkPrimary,e.fslope());
        Vec2 vec = new Vec2(e.x,e.y);
        vec.lerp(entity.x, entity.y, Interp.sineIn.apply(e.fin()));
        Fill.square(vec.x, vec.y, 2 * e.fslope());
        Draw.color();
        Fill.square(vec.x, vec.y, 1 * e.fslope());
      }
    });
    
    absorbedEnergy = new Effect(80f, e -> {
      if(e.data instanceof Building entity) {
        Draw.z(Layer.effect);
        color(IMColors.colorPrimary,IMColors.colorDarkPrimary,e.fout());
        Vec2 vec = new Vec2(e.x,e.y);
        vec.lerp(entity.x, entity.y, Interp.sineIn.apply(e.fin()));
        Fill.circle(e.x, e.y, 2 * e.fslope());
        Draw.color();
        Fill.circle(e.x, e.y, 1 * e.fslope());
        Draw.alpha(e.fslope());
        Drawf.laser(Core.atlas.find("parallax-laser"),Core.atlas.find("parallax-laser-end"),vec.x,vec.y,e.x,e.y,0.4f);
      }
    });
    
    shockWave = new Effect(60f,e -> {
      color(IMColors.colorPrimary,Color.white,e.fin());
      Lines.circle(e.x,e.y,64*e.fin());
      Lines.circle(e.x,e.y,46*e.fin());
      Lines.circle(e.x,e.y,32*e.fin());
      randLenVectors(e.id, 12, 7f + e.fin() * 13f, (x, y) -> {
        Fill.square(e.x + x, e.y + y, e.fout() * 2.1f + 0.5f, 45);
      });
    });
  }
  
}