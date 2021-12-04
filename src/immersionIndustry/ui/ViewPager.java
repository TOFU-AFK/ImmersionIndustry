package immersionIndustry.ui;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.net.Administration.*;
import mindustry.ui.dialogs.*;

public class ViewPager extends IMTable {
  
  BaseAdapter adapter;
  public int index;
  Table cont;
  
  public ViewPager() {
    initToolbar();
    index = 0;
    cont = new Table();
  }
  
  public void setAdapter(BaseAdapter adapter) {
    this.adapter = adapter;
    initPage();
  }
  
  private void initToolbar() {
    button(Icon.left,() -> {
      if(index<1) {
        index = adapter.getCount()-1;
      }else {
        index--;
      }
      initPage();
    }).left();
    button(Icon.right,() -> {
      if(index> (adapter.getCount()-2)) {
        index = 0;
      }else {
        index++;
      }
      initPage();
    }).right();
    row();
    add(cont);
  }
  
  private initPage() {
    cont.clear();
    if(adapter != null) {
      cont.add(adapter.instantiateItem(index)).width(400f);
    }
  }
  
  public interface BaseAdapter {
    
    public int getCount();
    public Table instantiateItem(int index);
    
  }
  
}