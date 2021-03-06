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

public class IMItems implements ContentList {
  
  public static Item t1BasicChip,collapseQuantum,cuTiAlloy,thTiAlloy;
  
  @Override
  public void load() {
    
    cuTiAlloy = new Item("cu-ti-alloy",IMColors.colorPrimary){{
      cost = 2;
    }};
    
    thTiAlloy = new Item("th-ti-alloy",Color.valueOf("f9a3c7")){{
      cost = 2;
    }};
    
    t1BasicChip = new Item("T1-basic-chip",IMColors.colorPrimary){{
      cost = 2;
    }};
    
    collapseQuantum = new AnimatedItem("collapse-quantum",Pal.lancerLaser){{
      transition = 5;
      sprites = 9;
      animDelay = 4f;
    }};
    
    Events.run(Trigger.update, () -> {
      //更新物品贴图
      AnimatedItem.animitems.each(item -> {
        item.update();
      });
    });
  }
  
}