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
  public Color diffusionColor = IMColors.colorPrimary;
  //反弹速度
  public float knockback;
  //反弹敌人后给的状态
  public StatusEffect status = StatusEffects.none;
  //状态持续时间
  public float statusDuration = 60 * 8f;
  public boolean impact;
  public Effect absorbEffect = IMFx.absorb;
  public float powerUse = 1;
  
  public Diffuser(String name) {
    super(name);
    update = true;
    solid = true;
    group = BlockGroup.projectors;
    hasPower = true;
    hasLiquids = true;
    hasItems = true;
    ambientSoundVolume = 0.08f;
    knockback = 12f;
  }
  
  @Override
  public void load() {
    super.load();
    baseRegion = Core.atlas.find("block-" + size);
  }
  
  public class DiffuserBuild extends ReloadTurretBuild {
    
    //锁定的目标
    public @Nullable Posc target;
    //防御范围
    public float defendRange = range;
    
    @Override
    public void updateTile() {
      
      defendRange = range * baseReloadSpeed();
      
      if(efficiency <= 0) return;
      
      Groups.bullet.intersect(x - defendRange, y - defendRange, defendRange * 2, defendRange * 2, bullet -> {
        if(bullet.team != team && bullet.within(this,defendRange) && isInRange(angleTo(bullet))) {
          shieldConsumer(bullet);
        }
      });
      
      Groups.unit.intersect(x - defendRange, y - defendRange, defendRange * 2, defendRange * 2, unit -> {
        if(unit.team != team && unit.within(this,defendRange) && isInRange(angleTo(unit))) {
          shieldConsumer(unit);
        }
      });
      
      if(target != null && target.within(this, defendRange)) {
        turnToTarget(angleTo(target));
      }
    }
    
    //是否在防御的范围
    protected boolean isInRange(float to) {
      to = to - rotation;
      if(to > -90 && to < 90) return true;
      return false;
    }
    
    public boolean isActive(){
      return target != null && enabled;
    }
    
    @Override
    public void draw(){
      Draw.rect(baseRegion, x, y);
      Draw.color();

      Draw.z(Layer.turret);

      Drawf.shadow(region, x - elevation, y - elevation, rotation - 90);
      Draw.rect(region, x, y, rotation - 90);
      
      //if(!isActive()) return;
      Draw.z(Layer.effect);
      color(diffusionColor,baseReloadSpeed());
      stroke((0.7f + Mathf.absin(20, 0.7f)));
      arc(x,y,defendRange,0.5f,rotation-90);
      Drawf.light(x, y, defendRange, diffusionColor, 1);
    }
    
    protected void turnToTarget(float targetRot){
      rotation = Angles.moveToward(rotation, targetRot, rotateSpeed * delta() * baseReloadSpeed());
    }
    
    protected void shieldConsumer(Posc p) {
      if(target instanceof Bullet bullet) {
        bullet.hit = true;
        bullet.absorb();
        absorbEffect.at(bullet.x,bullet.y);
        power.graph.useBatteries(bullet.damage);
      }
      else if(target instanceof Unit unit) {
        Tmp.v3.set(unit).nor().scl(knockback * 80f);
        if(impact) Tmp.v3.setAngle(rotation-90 + (knockback < 0 ? 180f : 0f));
          unit.impulse(Tmp.v3);
          unit.apply(status, statusDuration);
          absorbEffect.at(unit.x,unit.y);
          power.graph.useBatteries(unit.hitSize);
       }
       target = p;
    }
    
    @Override
    public float efficiency() {
      if(!enabled) return 0;
      return power == null ? 1 : power.status;
    }
    
  }
  
  
}