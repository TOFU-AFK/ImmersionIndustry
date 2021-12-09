package immersionIndustry.ui;
import arc.*;
import arc.func.*;
import arc.struct.*;
import arc.util.*;
import arc.util.pooling.*;
import arc.graphics.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.net.Administration.*;
import mindustry.ui.dialogs.*;
import mindustry.ui.*;
import mindustry.graphics.*;

import static mindustry.Vars.*;
import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;
import arc.graphics.g2d.*;

import immersionIndustry.IMColors;

public class Vbar {
  
  public float width;
  public float height;
  public Color background = Color.lightGray,color = Color.white;
  Seq<VbarData> datas = new Seq<>();
  float drawh = 0;
  float scale = 0.25f / Scl.scl(1f);
  
  public Vbar() {
    width = tilesize/2;
    height = tilesize*2;
  }
  
  public Vbar(float height) {
    width = tilesize/2;
    this.height = height;
  }
  
  public void add(Color color,String name,float amount) {
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
    Draw.z(Layer.playerName);
    Draw.color(background);
    Fill.crect(x,y,width,height);
    if(datas.size < 1) return;
    Draw.color(color);
    float b = height / getTotal();
    for(int i=0;i<datas.size;i++) {
      float ih = datas.get(i).amount * b + drawh;
      drawh += ih;
      Fill.crect(x,y,width,ih);
      stroke(2);
      line(x,y,x+width,y);
      float ty = i * tilesize;
      line(x+width,y,x+width*20,y+ty);
      drawText(x+width*20,y+ty,datas.get(i).name + " " + ih*100 + "%");
    }
    Draw.reset();
  }
  
  public void drawText(float x,float y,String message) {
    drawText(x,y,new StringBuilder(message));
  }
  
  public void drawText(float x,float y,StringBuilder message) {
    if(renderer.pixelator.enabled()) return;
      Font font = Fonts.outline;
      GlyphLayout l = Pools.obtain(GlyphLayout.class, GlyphLayout::new);
      boolean ints = font.usesIntegerPositions();
      font.getData().setScale(scale);
      font.setUseIntegerPositions(false);

      CharSequence text = message == null || message.length() == 0 ? "[lightgray]" + Core.bundle.get("empty") : message;

      l.setText(font, text, Color.white, 90f, Align.left, true);
      float offset = 1f;

      Draw.color(0f, 0f, 0f, 0.2f);
      Fill.rect(x, y - tilesize/2f - l.height/2f - offset, l.width + offset*2f, l.height + offset*2f);
      Draw.color();
      font.setColor(Color.white);
      font.draw(text, x - l.width/2f, y - tilesize/2f - offset, 90f, Align.left, true);
      font.setUseIntegerPositions(ints);
      font.getData().setScale(1f);
      Pools.free(l);
  }
  
  public class VbarData {
    
    Color color;
    String name;
    float amount;
    
    public VbarData(Color color,String name,float amount) {
      this.color = color;
      this.name = name;
      this.amount = amount;
    }
    
  }
  
}