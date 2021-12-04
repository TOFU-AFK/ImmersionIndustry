package immersionIndustry.ui;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.net.Administration.*;
import mindustry.ui.dialogs.*;

public class ViewPager extends IMTable {
  
  public ViewPager() {
    setWidth(400);
    initToolbar();
  }
  
  private void initToolbar() {
    button(Icon.left,() -> {
      
    }).left();
    button(Icon.right,() -> {
      
    }).right();
  }
  
  public interface BaseAdapter {
    
    int getCount();
    Table instantiateItem(Table parent,int index);
    
  }
  
}