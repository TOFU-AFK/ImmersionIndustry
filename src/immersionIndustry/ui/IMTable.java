package immersionIndustry.ui;
import arc.graphics.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.net.Administration.*;
import mindustry.ui.dialogs.*;
import mindustry.ui.*;

import static mindustry.Vars.*;
import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;
import arc.graphics.g2d.*;

import immersionIndustry.IMColors;

public class IMTable extends Table {

  public float thickness = 2f,pad = 0f;
  public Color borderColor = Pal.darkMetal;
  
  public IMTable() {
    super(Styles.black);
  }
  
  @Override
  public void draw(){
    super.draw();
    Draw.color(borderColor);
    Draw.alpha(parentAlpha);
    Lines.stroke(Scl.scl(thickness));
    Lines.rect(x - pad, y - pad, width + pad*2, height + pad*2);
    Draw.reset();
  }
  
}