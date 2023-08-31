package immersionIndustry.contents.blocks;

import arc.*;
import arc.math.*;
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

import immersionIndustry.contents.IMItems;
import immersionIndustry.contents.IMFx;
import immersionIndustry.contents.drawer.*;

import immersionIndustry.IMCacheLayer;

public class IMFloors {
  
  public static Floor glow;
  
  public static void load() {
    glow = new Floor("pooled-glow"){{
      statusDuration = 240f;
      speedMultiplier = 0.5f;
      variants = 0;
      drownTime = 150f;
      isLiquid = true;
      cacheLayer = IMCacheLayer.glow;

      emitLight = true;
      lightRadius = 25f;
      lightColor = Color.cyan.cpy().a(0.19f);
    }};
  }
  
}