package immersionIndustry.contents;
import arc.*;
import arc.graphics.*;
import mindustry.game.EventType.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.type.*;
import mindustry.graphics.Pal;

import immersionIndustry.IMColors;
import immersionIndustry.types.items.*;

import static mindustry.Vars.*;

public class IMItems {
  
  public static Item t1BasicChip,glow,cuTiAlloy,thTiAlloy;
  
  public static void load() {
    
    cuTiAlloy = new Item("cu-ti-alloy",IMColors.colorPrimary){{
      cost = 2;
    }};
    
    thTiAlloy = new Item("th-ti-alloy",Color.valueOf("f9a3c7")){{
      cost = 2;
    }};
    
    t1BasicChip = new Item("T1-basic-chip",IMColors.colorPrimary){{
      cost = 2;
    }};
    
    glow = new Item("glow",Pal.lancerLaser){{
      transitionFrames = 5;
      frames = 9;
      frameTime = 4f;
    }};
    
  }
  
}