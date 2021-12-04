package immersionIndustry.ui;
import arc.struct.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.net.Administration.*;
import mindustry.ui.dialogs.*;
import mindustry.content.*;
import mindustry.type.*;

import immersionIndustry.contents.*;

public class MakeDialog extends BaseDialog {
  
  public MakeDialog() {
    super("");
    setFillParent(false);
    addCloseButton();
    setup();
    shown(this::setup);
  }
  
  private void setup() {
    cont.clear();
    cont.add(new ViewPager());
  }
  
  private class Adapter implements ViewPager.BaseAdapter {
    
    Item item;
    Seq<Liquid> liquids;
    
    public Adapter(Item item,Seq<Liquid> liquids) {
      this.item = item;
      this.liquids = liquids;
    }
    
    public int getCount() {
      return 1;
    }
    
    public Table instantiateItem(int index) {
      Table table = new Table();
      table.add("索引"+index);
      return table;
    }
  }
  
}