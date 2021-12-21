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

public class Diffuser extends ReloadTurret {
  
  public TextureRegion baseRegion;
  public float elevation = -1f;
  //对空，包括子弹
  public boolean targetAir = true;
  //对陆
  public boolean targetGround = true;
  public Cons<DiffuserBuild> drawer = tile -> Draw.rect(region, tile.x, tile.y, tile.rotation - 90);
  
  public Diffuser(String name) {
    super(name);
    update = true;
    solid = true;
    group = BlockGroup.projectors;
    hasPower = true;
    hasLiquids = true;
    hasItems = true;
    ambientSoundVolume = 0.08f;
    baseRegion = Core.atlas.find("block-" + size);
  }
  
  public class DiffuserBuild extends ReloadTurretBuild {
    
    public @Nullable Posc target;
    
    @Override
    public void updateTile() {
      if(target != null && target.within(this, range)) {
        turnToTarget(angleTo(target));
      }
    }
    
    @Override
    public void draw(){
      Draw.rect(baseRegion, x, y);
      Draw.color();

      Draw.z(Layer.turret);

      Drawf.shadow(region, x - elevation, y - elevation, rotation - 90);
      drawer.get(this);
      
    }
    
    protected void findTarget(){
      //首先寻找子弹
      target = Groups.bullet.intersect(x - range, y - range, range*2, range*2).min(b -> b.team != team && b.type().hittable, b -> b.dst2(this));
      
      if(target != null) return;
      
      //其次寻找单位
      if(targetAir && !targetGround){
        target = Units.closestEnemy(team, x, y, range, e -> !e.dead() && !e.isGrounded());
      }else{
        target = Units.closestEnemy(team, x, y, range, e -> !e.dead() && (e.isGrounded() || targetAir) && (!e.isGrounded() || targetGround), b -> targetGround);
      }
    }
    
    protected void turnToTarget(float targetRot){
      rotation = Angles.moveToward(rotation, targetRot, rotateSpeed * delta() * baseReloadSpeed());
    }
    
  }
  
  
}