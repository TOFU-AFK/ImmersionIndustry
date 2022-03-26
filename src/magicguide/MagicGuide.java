package immersionIndustry;

//导入原版包
import arc.*;
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

//继承Mod类
public class MagicGuide extends Mod{
    
    public static final String NAME = "immersionindustry";
    public static LoadedMod MOD;
    
    //当加载模组内容时被调用
    @Override
    public void loadContent() {
      MOD = Vars.mods.getMod(getClass());
      new MGBlocks().load();
    }
	
}
