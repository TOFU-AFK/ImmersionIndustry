package immersionIndustry.ui;
import arc.graphics.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import mindustry.gen.*;
import mindustry.net.Administration.*;
import mindustry.ui.dialogs.*;
import mindustry.ui.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.content.*;

import static mindustry.Vars.*;
import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;

import immersionIndustry.IMColors;

public class SmelterView extends IMTable {
  
  Seq<SmelterItem> items;
  float capacity = 1000;
  float space = 100;
  
  public SmelterView() {
    items = new Seq<>();
    items.add(new SmelterItem(Liquids.slag,200));
    items.add(new SmelterItem(Liquids.water,200));
  }
  
  @Override
  public void draw(){
    super.draw();
    float b =  height / capacity;
    Draw.color(Pal.place);
    for(i=100;i <= capacity;i+=space) {
      if(i % (space*2) == 0) {
        line(x,height*b*i+y,width/3+x,height*b*i+y);
      }else {
        line(x,height*b*i+y,width/4+x,height*b*i+y);
      }
    }
    
    float last = 0;
    items.each(item -> {
      Draw.color(item.liquid.color);
      Draw.rect(x,y+last,width,item.ml * b);
      last = item.ml * b;
    });
    Draw.reset();
  }
  
  public float getTotal() {
    float amount = 0;
    items.each(item -> {
      amount += item.ml;
    });
    return amount;
  }
  
  public class SmelterItem {
    
    public Liquid liquid;
    public float ml;
    
    public SmelterItem(Liquid liquid,float ml) {
      this.liquid = liquid;
      this.ml = ml;
    }
    
  }
  
}