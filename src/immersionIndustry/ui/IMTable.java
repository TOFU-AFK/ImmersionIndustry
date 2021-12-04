package immersionIndustry.ui;
import arc.graphics.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.net.Administration.*;
import mindustry.ui.dialogs.*;

import immersionIndustry.IMColors;

public class IMTable extends Table {
  
  public float thickness = 4f, pad = 0f;
  public Color borderColor = IMColors.colorPrimary;
  
  public IMTable() {
    super(IMStyles.gray);
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