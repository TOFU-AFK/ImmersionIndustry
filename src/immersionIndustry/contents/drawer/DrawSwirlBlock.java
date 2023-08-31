package immersionIndustry.contents.drawer;

import mindustry.world.draw.*;
import arc.graphics.g2d.*;
import arc.Core;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import arc.graphics.Blending;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.Layer;
import mindustry.graphics.Drawf;
import mindustry.graphics.Shaders;
import mindustry.world.*;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.production.GenericCrafter.*;
import static mindustry.Vars.*;

import immersionIndustry.IMColors;
import immersionIndustry.contents.IMFx;

public class DrawSwirlBlock extends DrawBlock {
  
  @Override
  public void draw(Building b){
    GenericCrafterBuild build = (GenericCrafterBuild) b;
    Draw.rect(build.block.region, build.x, build.y, build.block.rotate ? build.rotdeg() : 0);
    if(build.efficiency > 0) {
      GenericCrafter block = (GenericCrafter) build.block;
      if(Mathf.chanceDelta(block.updateEffectChance)){
        IMFx.absorbedEnergy.at(build.x + Mathf.range(block.size * tilesize), build.y + Mathf.range(block.size * tilesize),build.rotation,build);
      }
    }
    
    float orbRadius = 5 * (1f + Mathf.absin(20, 0.1f)) * build.efficiency;
    
    Draw.color(IMColors.colorPrimary);
    Draw.z(Layer.effect);
                
    Draw.alpha(build.efficiency);
                
    Fill.circle(build.x, build.y, orbRadius);
    Draw.color();
    Fill.circle(build.x, build.y, orbRadius / 2);
                
    Lines.stroke((0.7f + Mathf.absin(20, 0.7f)), IMColors.colorPrimary);
                
    for(int i = 0; i < 5; i++){
      float rot = build.rotation + i * 360f/5 - Time.time * 0.5f;
      Lines.arc(build.x, build.y, orbRadius + 3f, 0.14f, rot);
    }
    Drawf.light(build.x, build.y, tilesize * 1.5f, IMColors.colorPrimary, build.warmup);
    Draw.reset();
  }
  
}