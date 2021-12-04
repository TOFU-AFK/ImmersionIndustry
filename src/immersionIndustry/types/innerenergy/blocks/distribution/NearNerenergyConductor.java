package immersionIndustry.types.innerenergy.blocks.distribution;

import arc.*;
import arc.scene.*;
import arc.scene.style.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
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
import mindustry.world.blocks.power.NuclearReactor.*;
import mindustry.world.blocks.production.GenericCrafter.*;

import static mindustry.Vars.*;
import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;

import immersionIndustry.IMColors;
import immersionIndustry.contents.IMFx;
import immersionIndustry.types.innerenergy.blocks.*;
import immersionIndustry.types.innerenergy.blocks.InnerenergyBlock.*;

public class NearNerenergyConductor extends InnerenergyBlock {
  
  public NearNerenergyConductor(String name) {
    super(name);
    update = true;
    solid = true;
    rotate = true;
  }
  
  @Override
  public void drawPlace(int x, int y, int rotation, boolean valid) {
    super.drawPlace(x,y,rotation,valid);
    for(int i = 0;i<4;i++) {
      //如果是前方的方块，跳过
      if(i == rotation) continue;
      Building build = world.tiles.get(x,y).nearbyBuild(i);
      if(build != null && build.isValid()) {
        if(getBuildingInnerenergy(build) > 0) {
          drawOtherConfigure(build);
        }
      }
    }
  }
  
  @Override
  public float getBuildingInnerenergy(Building build) {
    if(build != null && build.isValid()) {
      if(build instanceof InnerenergyBuilding entity) {
        return entity.inner;
      }
      if(build instanceof NuclearReactorBuild entity) {
        return entity.heat;
      }
      if(build instanceof GenericCrafterBuild entity) {
        return entity.progress;
      }
    }
    return 0;
  }
  @Override
  public void addOtherInnerenergy(Building build,float add) {
    if(build != null && build.isValid()) {
      if(build instanceof InnerenergyBuilding entity) {
        if(entity.acceptInner(build,add)) entity.handleInner(build,add);
      }
      if(build instanceof NuclearReactorBuild entity) {
        entity.heat += add;
      }
      if(build instanceof GenericCrafterBuild entity) {
        entity.progress += add;
      }
    }
  }
  
  @Override
  public void drawOtherConfigure(Building build) {
    Draw.color(Pal.breakInvalid);
    Lines.stroke(1f);
    Lines.square(build.x, build.y, build.block.size * tilesize / 2f + 1f);
    Draw.reset();
  }
  
  public class ConductorBuilding extends InnerenergyBuilding {
    
    @Override
    public void absorb() {
      //获取除前方以外的所有Building，提取内能
      for(int i = 0;i<4;i++) {
        if(i==rotation) continue;
        Building build = nearby(i);
        float in = efficiency(build);
        if(in > 0) {
          inner += in;
          addOtherInnerenergy(build,-in);
        }

      }
    }
    
    @Override
    public void drawAbsorb() {
      InnerenergyBuilding front = front();
      if(front!=null&&front.isValid() && inner > 0.01f) {
        Draw.color(heatColor, inner);
        Draw.blend(Blending.additive);
        Draw.rect(heat, x, y);
        Draw.blend();
        Draw.color();
      }
    }
    
    @Override
    public void drawConfigure() {
      InnerenergyBuilding front = front();
      if(front!=null&&front.isValid()) {
        Drawf.select(front.x, front.y,front.block.size * tilesize / 2f + 2f + Mathf.absin(Time.time, 4f, 1f),Pal.place);
      }
      
      for(int i = 0;i<4;i++) {
      //如果是前方的方块，跳过
        if(i == rotation) continue;
        Building build = nearby(i);
        if(build != null && build.isValid()) {
          if(getBuildingInnerenergy(build) > 0) {
            drawOtherConfigure(build);
          }
        }
      }
      
    }
    
    @Override
    public boolean acceptInner(Building source,float amount) {
      //如果不是前方的方块，则返回false
      if(source.equals(front())) {
        return true;
      }
      return false;
    }
    
    @Override
    public float efficiency(Building build) {
      float gap = getBuildingInnerenergy(build) - inner;
      return gap * multiple;
    }
    
    public @Nullable InnerenergyBuilding front() {
      int trns = block.size/2 + 1;
      Building build = nearby(Geometry.d4(rotation).x * trns, Geometry.d4(rotation).y * trns);
      if(build instanceof InnerenergyBuilding entity) {
        return entity;
      }
      return null;
    }
    
  }
  
}