package immersionIndustry.ui;
import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.g2d.TextureAtlas.*;
import arc.scene.style.*;
import arc.scene.ui.Button.*;
import arc.scene.ui.CheckBox.*;
import arc.scene.ui.Dialog.*;
import arc.scene.ui.ImageButton.*;
import arc.scene.ui.Label.*;
import arc.scene.ui.ScrollPane.*;
import arc.scene.ui.Slider.*;
import arc.scene.ui.TextButton.*;
import arc.scene.ui.TextField.*;
import arc.scene.ui.TreeElement.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.dialogs.*;
import mindustry.ui.*;

import static mindustry.gen.Tex.*;
import static immersionIndustry.ImmersionIndustry.*;

public class IMStyles {
  public static Drawable gray;
  
  public static void load() {
    gray = new TextureRegionDrawable(Core.atlas.find(NAME+"-grayBackground"));
  }
}