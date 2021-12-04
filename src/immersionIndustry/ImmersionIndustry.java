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

//导入模组类
import immersionIndustry.contents.blocks.IMBlocks;
import immersionIndustry.contents.IMTechTree;
import immersionIndustry.contents.IMItems;
import immersionIndustry.contents.IMFx;
import immersionIndustry.contents.IMBullets;

//继承Mod类
public class ImmersionIndustry extends Mod{
    
    //当加载模组内容时被调用
    @Override
    public void loadContent() {
      new IMFx().load();
      new IMBullets().load();
      new IMItems().load();
      new IMBlocks().load();
      new IMTechTree().load();
    }
	
}
