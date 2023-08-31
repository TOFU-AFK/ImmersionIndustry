package immersionIndustry.contents.drawer;

import mindustry.world.draw.*;
import arc.graphics.g2d.*;
import arc.Core;
import arc.math.*;
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

public class DrawLightBlock extends DrawBlock {
  
  public TextureRegion light;
  
  @Override
  public void draw(Building b){
    GenericCrafterBuild build = (GenericCrafterBuild) b;
    Draw.rect(build.block.region, build.x, build.y, build.block.rotate ? build.rotdeg() : 0);
    
    Draw.color(IMColors.colorPrimary,IMColors.colorDarkPrimary,build.warmup);
    Draw.z(Layer.effect);
    
    Lines.stroke((0.7f + Mathf.absin(20, 0.7f)), IMColors.colorPrimary);
    Lines.square(build.x,build.y,(build.block.size * tilesize) / 3,90 + Time.time);
    
    if(build.efficiency() > 0 && Mathf.chanceDelta(0.04f)){
      IMFx.crystallizationEnergy.at(build.x + Mathf.range(build.block.size * tilesize), build.y + Mathf.range(build.block.size * tilesize),build.rotation,build);
    }
    
    Drawf.light(build.x, build.y,build.block.size * tilesize * 1.5f, IMColors.colorPrimary, build.warmup);
    Draw.reset();
  }
  
  @Override
  public void load(Block block) {
    light = Core.atlas.find(block.name + "-light");
  }
  
}