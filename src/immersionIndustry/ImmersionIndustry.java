package immersionIndustry;

//导入原版包
import arc.*;
import mindustry.core.*;
import mindustry.graphics.*;
import arc.graphics.g2d.*;
import arc.scene.ui.*;
import arc.scene.style.TextureRegionDrawable;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.mod.*;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.*;
import mindustry.mod.Mods.*;

import static mindustry.Vars.*;

//导入模组类
import immersionIndustry.contents.blocks.*;
import immersionIndustry.contents.IMTechTree;
import immersionIndustry.contents.IMItems;
import immersionIndustry.contents.IMFx;
import immersionIndustry.contents.IMBullets;

//继承Mod类
public class ImmersionIndustry extends Mod{
    
    public static final String NAME = "immersionindustry";
    public static LoadedMod MOD;
    
    @Override
    public void init() {
    }
    
    //当加载模组内容时被调用
    @Override
    public void loadContent() {
      MOD = Vars.mods.getMod(getClass());
      IMSounds.load();
      IMShaders.load();
      IMCacheLayer.init();
      
      IMFx.load();
      IMBullets.load();
      IMItems.load();
      IMBlocks.load();
      IMFloors.load();
      IMTechTree.load();
    }
	
}
