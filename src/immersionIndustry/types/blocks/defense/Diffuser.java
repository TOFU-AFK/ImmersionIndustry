package immersionIndustry.types.blocks.defense;

import arc.*;
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
import mindustry.world.blocks.environment.*;
import mindustry.world.meta.*;
import mindustry.world.consumers.*;
import mindustry.world.blocks.power.*;

import static mindustry.Vars.*;
import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;

import immersionIndustry.IMColors;
import immersionIndustry.contents.IMFx;

public class Diffuser extends Block {
  
  public float radius = 100f;
  public float reload = 250f;
  public Effect spreadEffect = IMFx.spread;
  
  public Diffuser(String name) {
    super(name);
    update = true;
    solid = true;
    group = BlockGroup.projectors;
    hasPower = true;
    hasLiquids = true;
    hasItems = true;
    ambientSoundVolume = 0.08f;
    consumes.add(new ConsumeCoolant(0.1f)).boost().update(false);
  }
  
  @Override
  public void drawPlace(int x, int y, int rotation, boolean valid) {
    super.drawPlace(x, y, rotation, valid);
    Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, radius, player.team().color);
  }
  
  public class DiffuserBuild extends Building {
    
    public float charge,heat;
    
    @Override
    public void updateTile() {
      heat = Mathf.lerpDelta(heat, consValid() || cheating() ? 1f : 0f, 0.08f);
      charge += heat * edelta();
      if(charge >= reload) {
        charge = 0f;
        spreadEffect.at(x,y,0,Color.white,radius);
        /*遍历附近方块，并将电力传输到方块*/
        indexer.eachBlock(this, radius,other -> other.block.hasPower && other.team == team, other -> {
            PowerGraph ograph = other.power.graph;
            float stored = power.graph.getBatteryStored() / power.graph.getTotalBatteryCapacity();
            float ostored = ograph.getBatteryStored() / ograph.getTotalBatteryCapacity();
            
            if(stored > ostored) {
              float amount = power.graph.getBatteryStored() * (stored - ostored) / 2;
              amount = Mathf.clamp(amount, 0, ograph.getTotalBatteryCapacity() * (1 - ostored));
              power.graph.transferPower(-amount);
              ograph.transferPower(amount);
            }
        });
      }
    }
    
  }
  
}