package immersionIndustry.ui;
import arc.struct.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.net.Administration.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import mindustry.content.*;
import mindustry.type.*;

import immersionIndustry.contents.*;

public class MakeDialog extends BaseDialog {
  
  public MakeDialog() {
    super("");
    getStyle().stageBackground = Styles.none;
    addCloseButton();
    setup();
    shown(this::setup);
  }
  
  private void setup() {
    cont.clear();
    cont.table(table -> {
      
    }).width(400);
  }
  
}