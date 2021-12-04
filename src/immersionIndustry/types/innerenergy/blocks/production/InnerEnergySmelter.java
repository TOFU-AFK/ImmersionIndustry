package immersionIndustry.types.innerenergy.blocks.production;

import arc.*;
import arc.scene.*;
import arc.scene.style.*;
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
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;
import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;

import immersionIndustry.IMColors;
import immersionIndustry.contents.IMFx;
import immersionIndustry.types.innerenergy.blocks.*;
import immersionIndustry.types.innerenergy.blocks.InnerenergyBlock.*;

public class InnerEnergySmelter extends InnerenergyBlock {
  
  public InnerEnergySmelter(String name) {
    super(name);
    solid = true;
    hasItems = true;
    ambientSound = Sounds.machine;
    sync = true;
    ambientSoundVolume = 0.03f;
    flags = EnumSet.of(BlockFlag.factory);
  }
  
  @Override
  public void init() {
    super.init();
  }
  
  public class SmelterBuild extends InnerenergyBuilding {
    
    @Override
    public void draw() {
      super.draw();
    }
    
  }
  
}
