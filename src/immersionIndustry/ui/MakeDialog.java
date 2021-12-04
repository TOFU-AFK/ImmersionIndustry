package immersionIndustry.ui;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.net.Administration.*;
import mindustry.ui.dialogs.*;

public class MakeDialog extends BaseDialog {
  
  public MakeDialog() {
    super("");
    addCloseButton();
    setup();
    shown(this::setup);
  }
  
  private void setup() {
    cont.clear();
    cont.add(new ViewPager());
  }
  
}