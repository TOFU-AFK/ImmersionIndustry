package immersionIndustry.ui;
import arc.*;
import arc.func.*;
import arc.struct.*;
import arc.util.*;
import arc.util.pooling.*;
import arc.graphics.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.graphics.g2d.*;
import mindustry.gen.*;
import mindustry.net.Administration.*;
import mindustry.ui.dialogs.*;
import mindustry.ui.*;
import mindustry.graphics.*;
import mindustry.graphics.Shaders;

import static mindustry.Vars.*;
import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;

import immersionIndustry.IMColors;

public class Vbar {
  
  public float width;
  public float height;
  private float capacity = 600;
  public Color background = new Color(0,0,0,0.2f);
  Seq<VbarData> datas = new Seq<>();
  float drawh = 0;
  float prop;
  
  public Vbar() {
    width = tilesize/4;
    height = tilesize*2;
    prop = height / capacity;
  }
  
  public Vbar(float height) {
    width = tilesize/2;
    this.height = height;
    prop = height / capacity;
  }
  
  public Vbar(float width,float height) {
    this.width = width;
    this.height = height;
    prop = height / capacity;
  }
  
  public void setProp(float prop) {
    this.prop = prop;
    prop = height / capacity;
  }
  
  public void add(Color color,String name,float amount) {
    for(int i = 0;i<datas.size;i++) {
      VbarData data = datas.get(i);
      if(data.name.equals(name)) {
        data.amount += amount;
        return;
      }
    }
    datas.add(new VbarData(color,name,amount));
  }
  
  public float getTotal() {
    float v = 0;
    for(int i=0;i<datas.size;i++) {
      v += datas.get(i).amount;
    }
    return v;
  }
  
  public void draw(float x,float y) {
    drawh = 0;
    Draw.z(Layer.effect);
    Draw.color(background);
    Fill.crect(x,y,width,height);
    if(datas.size < 1) return;
    Draw.draw(Layer.effect, () -> {
      Draw.shader(Shaders.slag);
      for(int i=0;i<datas.size;i++) {
        float a = datas.get(i).amount * prop;
        Draw.color(datas.get(i).color);
        Fill.crect(x,y + drawh,width,a);
        Draw.color();
        drawh += a;
      }
      Draw.shader();
    });
    Draw.reset();
  }
  
  public class VbarData {
    
    public Color color;
    public String name;
    public float amount;
    
    public VbarData(Color color,String name,float amount) {
      this.color = color;
      this.name = name;
      this.amount = amount;
    }
    
  }
  
}