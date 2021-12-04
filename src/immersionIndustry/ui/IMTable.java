package immersionIndustry.ui;
import arc.graphics.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.net.Administration.*;
import mindustry.ui.dialogs.*;

import static mindustry.Vars.*;
import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;
import arc.graphics.g2d.*;

import immersionIndustry.IMColors;

public class IMTable extends Table {
  
  public float thickness = 1f;
  public Color borderColor = IMColors.colorPrimary;
  
  public IMTable() {
    super(IMStyles.gray);
  }
  
  @Override
  public void draw(){
    super.draw();
    Draw.color(borderColor);
    Draw.alpha(parentAlpha);
    stroke(Scl.scl(thickness));
    line(x-2,y+height+1,x+width+1,y+height+1);
    line(x-1,y+height+2,x+width,y+height+2);
    line(x-1,y-1,x-1,y+height+1);
    line(x-2,y,x-2,height+1);
    Fill.square(x,height,1);
    
    Draw.color(borderColor.cpy().mul(0.8f, 0.8f, 0.8f, 1f));
    line(x,y-1,x+width+1,y-1);
    line(x,y-2,x+width+1,y-2);
    line(x+width+1,y-2,x+width+1,y+height);
    line(x+width+2,y-1,x+width+2,y+height);
    Fill.square(x+width,y,1);
    
    Draw.color(Color.black);
    line(x-3,y,x-3,y+height+1);
    line(x-1,y+height+2,x+width,y+height+2);
    line(x+width+3,y-1,x+width+3,y+height);
    line(x,y-3,x+width+1,y-3);
    Fill.square(x-2,y+height+2,1);
    Fill.square(x+width+1,y+height+1,1);
    Fill.square(x+width+2,y+height+1,1);
    Fill.square(x+width+2,y-2,1);
    Fill.square(x-2,y-1,1);
    Fill.square(x-1,y-2,1);
    
    Draw.reset();
  }
  
}