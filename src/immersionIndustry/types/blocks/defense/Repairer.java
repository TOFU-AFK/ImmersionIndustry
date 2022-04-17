package immersionIndustry.types.blocks.defense;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.Vec2;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import arc.scene.ui.layout.Table;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.entities.Units.*;
import mindustry.entities.bullet.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.meta.*;
import mindustry.world.consumers.*;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.defense.turrets.ReloadTurret.*;

import static mindustry.Vars.*;
import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;

import immersionIndustry.IMColors;
import immersionIndustry.contents.IMFx;

public class Repairer extends Block {
  
  public Color baseColor = IMColors.colorWhite,healColor = IMColors.colorYellow;
  
  public Repairer(String name) {
    super(name);
    update = true;
    solid = true;
    group = BlockGroup.projectors;
  }
  
  public class RepairerBuild extends Building {
    
    float phaseHeat;
    
    @Override
    public void updateTile() {
      phaseHeat = Mathf.lerpDelta(phaseHeat, Mathf.num(cons.optionalValid()), 0.1f);
    }
    
    @Override
    public void draw() {
      super.draw();
      Draw.color(baseColor,healColor,phaseHeat);
      Draw.alpha((0.3f + Mathf.absin(Time.time, 2f, 0.3f)) * phaseHeat);
      Draw.blend(Blending.additive);
      Lines.square(x,y,block.size * tilesize / 2f + 1f,Time.time * 1.5f);
      
      Lines.square(x,y,block.size * tilesize / 2f + 1f,45 + Time.time * 1.5f);
      
      Lines.circle(x,y,block.size * tilesize / 2f + 1f);
      Draw.blend();
      Draw.color();
    }
    
  }

}