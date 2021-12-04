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
    titleTable.clear();
    getStyle().stageBackground = Styles.none;
    setup();
    shown(this::setup);
  }
  
  private void setup() {
    cont.clear();
    cont.pane(table -> {
      table.add(new SmelterView());
    });
    cont.row();
    cont.defaults().size(210f, 64f);
    buttons.button("@back", Icon.left, this::hide).size(210f, 64f);

    addCloseListener();
  }
  
}