package immersionIndustry.contents;
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
import mindustry.world.blocks.experimental.*;
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
import static mindustry.content.TechTree.*;

import static immersionIndustry.contents.IMItems.*;
import static immersionIndustry.contents.blocks.IMBlocks.*;

public class IMTechTree implements ContentList {
  
  @Override
  public void load() {
    //方块
    node(Blocks.coreShard,t1ChipFactory);
    node(t1ChipFactory,auroraGuide);
    node(t1ChipFactory,collapseQuantumCultivation);
    node(t1ChipFactory,collapseExtractor);
    
    //物品
    node(Items.copper,t1BasicChip);
    node(t1BasicChip,cuTiAlloy);
    node(cuTiAlloy,thTiAlloy);
    node(thTiAlloy,collapseQuantum);
    
  }
  
  static TechNode node(UnlockableContent parent, UnlockableContent content) {
    return new TechNode(get(parent),content,content.researchRequirements());
  }
  
}