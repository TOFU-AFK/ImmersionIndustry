package immersionIndustry.ui;
import arc.graphics.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import arc.math.*;
import mindustry.gen.*;
import mindustry.net.Administration.*;
import mindustry.ui.dialogs.*;
import mindustry.ui.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.content.*;
import mindustry.entities.*;

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
  float last = 0;
  float time = 60;
  float warmup;
  float warmupSpeed = 0.001f;
  
  public SmelterView() {
    bottom();
    items = new Seq<>();
    items.add(new SmelterItem(Liquids.slag,200));
    items.add(new SmelterItem(Liquids.water,200));
    update(() -> {
      warmup = Mathf.lerpDelta(warmup, 1f, warmupSpeed * timeScale);
      if(Mathf.equal(warmup, 1f, 0.001f)){
        warmup = 1f;
      }
    });
  }
  
  @Override
  public void draw(){
    super.draw();
    last = 0;
    final float b =  height / capacity;
    Draw.blend(Blending.additive);
    items.each(item -> {
      Draw.color(item.liquid.color);
      Draw.alpha((0.3f + Mathf.absin(Time.time, 2f, 0.3f)) * warmup);
      Fill.rect(x+(width/2),y+last,width,item.ml * b);
      last += item.ml * b;
      Draw.reset();
    });
    Draw.blend();
    Draw.color(Pal.place);
    line(x,y+height/2,x+width,y+height/2);
    Draw.reset();
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