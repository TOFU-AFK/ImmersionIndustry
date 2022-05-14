package immersionIndustry.types.blocks.turret;

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
import mindustry.entities.bullet.*;
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
import mindustry.world.blocks.defense.turrets.*;

import static mindustry.Vars.*;
import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;

import immersionIndustry.IMColors;
import immersionIndustry.contents.IMFx;

public class Catcher extends ReloadTurret {
  
  public TextureRegion turret;
  public float shootCone = 6f;
  public BulletType bullet;
  
  public Catcher(String name){
    super(name);
    bullet = new BulletType(22f,0){
      {
        hittable = false;
        absorbable = false;
        collidesTiles = false;
        collidesTeam = true;
      }
      
      @Override
      public void draw(Bullet b) {
        Draw.z(Layer.bullet);
        Draw.rect(turret,b.x,b.y,b.rotation-90);
        Drawf.laser(b.team(), Core.atlas.find("minelaser"), Core.atlas.find("minelaser-end"), b.x, b.y, owner.x, owner.y, 1f);
        
      }
      
      @Override
      public void hitEntity(Bullet b, Hitboxc entity, float health) {
        if(entity instanceof Unit unit){
          b.vel.setAngle(b.angleTo(target));
          unit.disarmed = true;
          unit.x = b.x;
          unit.y = b.y;
        }else if(entity instanceof owner) {
          b.x = owner.x;
          b.y = owner.y;
        }
      }
      
    };
  }
  
  @Override
  public void load() {
    super.load();
    turret = Core.atlas.find(name + "-turret");
  }
  
  public class CatcherBuild extends ReloadTurretBuild {
    
    public boolean catching;
    float reload;
    @Nullable Posc target;
    Sortf unitSort = UnitSorts.closest;
    
    @Override
    public void updateTile() {
      if(!catching) {
        findTarget();
        if(target != null) {
          float targetRot = angleTo(targetPos);
          turnToTarget(targetRot);
          if(Angles.angleDist(rotation, targetRot) < shootCone){
            updateShooting();
          }
        }
      }
      if(acceptCoolant){
        updateCooling();
      }
    }
    
    
    public void updateShooting() {
      reload += delta() * baseReloadSpeed();
      if(reload >= reloadTime && !catching) {
        bullet.create(this,x,y,rotation);
      }
    }
    
    @Override
    public void draw() {
      Draw.rect(region, x, y);
      Draw.color();

      if(!catching) {
        Draw.z(Layer.turret);
        Drawf.shadow(turret, x, y, rotation - 90);
        Draw.rect(turret,x,y,rotation - 90);
      }
    }
    
    protected void findTarget(){
      target = Units.bestTarget(team, x, y, range, e -> !e.dead(), b -> targetGround, unitSort);
    }
    
    protected void turnToTarget(float targetRot){
      rotation = Angles.moveToward(rotation, targetRot, rotateSpeed * delta() * baseReloadSpeed());
    }
    
  }
  
}