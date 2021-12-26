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

public class Diffuser extends ReloadTurret {
  
  public TextureRegion baseRegion;
  public float elevation = -1f;
  //对空，包括子弹
  public boolean targetAir = true;
  //对陆
  public boolean targetGround = true;
  public Color diffusionColor = IMColors.colorPrimary;
  //反弹速度
  public float knockback;
  //反弹敌人后给的状态
  public StatusEffect status = StatusEffects.none;
  //状态持续时间
  public float statusDuration = 60 * 8f;
  public boolean impact;
  
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
    knockback = 12f;
  }
  
  public class DiffuserBuild extends ReloadTurretBuild {
    
    //锁定的目标
    public @Nullable Posc target;
    
    @Override
    public void updateTile() {
      if(!cons.valid()) return;
      
      Groups.bullet.intersect(x, y, range, range, bullet -> {
        if(isInRange(bullet)) {
          shieldConsumer(bullet);
        }
      });
      
      Groups.unit.intersect(x, y, range, range, unit -> {
        if(isInRange(unit)) {
          shieldConsumer(unit);
        }
      });
      
      findTarget();
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
      Draw.rect(region, x, y, rotation - 90);
      
      Draw.z(Layer.effect);
      color(diffusionColor,new Color(baseReloadSpeed(),edelta(),0.5f),baseReloadSpeed());
      stroke((0.7f + Mathf.absin(20, 0.7f)));
      swirl(x,y,range,0.5f,rotation-90);
      Drawf.light(x, y, range, diffusionColor, 1);
    }
    
    protected void findTarget(){
      
      //首先寻找子弹
      target = Groups.bullet.intersect(x - range, y - range, range*2, range*2).min(b -> b.team != team && b.type().hittable, b -> b.dst2(this));
      
      if(target != null) return;
      
      //其次寻找单位
      if(targetAir && !targetGround){
        target = Units.closestEnemy(team, x, y, range, e -> !e.dead() && !e.isGrounded());
      }else{
        target = Units.closestEnemy(team, x, y, range, e -> !e.dead() && (e.isGrounded() || targetAir) && (!e.isGrounded() || targetGround));
      }
    }
    
    protected void turnToTarget(float targetRot){
      rotation = Angles.moveToward(rotation, targetRot, rotateSpeed * delta() * baseReloadSpeed());
    }
    
    protected void shieldConsumer(Posc p) {
      if(target instanceof Bullet bullet) {
        bullet.absorb();
      }
      else if(target instanceof Unit unit) {
        Tmp.v3.set(unit).nor().scl(knockback * 80f);
        if(impact) Tmp.v3.setAngle(rotation-90 + (knockback < 0 ? 180f : 0f));
          unit.impulse(Tmp.v3);
          unit.apply(status, statusDuration);
       }
    }
    
    protected boolean isInRange() {
      return isInRange(target);
    }
    
    protected boolean isInRange(Posc p) {
      if(p==null) return false;
      float rot = Angles.moveToward(rotation, angleTo(p),1);
      if(p.within(this, range) && rot <= 90 && rot >= -90) {
        return true;
      }
      return false;
    }
    
  }
  
  
}