package immersionIndustry.types.blocks.distribution;

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

import static mindustry.Vars.*;
import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;

import immersionIndustry.IMColors;
import immersionIndustry.contents.IMFx;
import immersionIndustry.contents.IMBullets;

public class LiquidMassDriver extends Block {
  
  public TextureRegion baseRegion;
  public float reloadTime = 100f;
  public float range = 420f;
  public float knockback = 4f;
  public float rotateSpeed = 5f;
  public float shootCone = 8f;
  public float translation = 7f;
  public float shootLength;
  public BulletType type = IMBullets.glowLaser;
  protected Vec2 tr = new Vec2();
  protected final int timerCharge = timers++;
  
  public LiquidMassDriver(String name) {
    super(name);
    update = true;
    solid = true;
    configurable = true;
    hasItems = false;
    hasLiquids = true;
    hasPower = true;
    outlineIcon = true;
    sync = true;
    liquidCapacity = 100;
    shootLength = size * tilesize / 2f;
  }
  
  @Override
  public void load() {
    super.load();
    baseRegion = Core.atlas.find(name + "-base");
  }
  
  @Override
  public TextureRegion[] icons(){
    return new TextureRegion[]{baseRegion, region};
  }
  
  @Override
  public void drawPlace(int x, int y, int rotation, boolean valid) {
    super.drawPlace(x, y, rotation, valid);
    Drawf.dashCircle(x * tilesize, y * tilesize, range, Pal.accent);
  }
  
  public class DriverBuildData {
    
    public Building from,to;
    public Liquid liquid;
    
  }
  
  public class DriverBuild extends Building {
    
    public int link = -1;
    public float rotation = 90;
    public float reload = 0f;
    public boolean canShoot;
    public Bullet bullet;
    
    @Override
    public void updateTile() {
      if(!linkValid() || !cons.valid()) return;
      
      if(charge() && timer(timerCharge,6)) {
        Fx.lancerLaserCharge.at(this);
      } 
      
      Building link = world.build(this.link);
      
      if(reload > 0f){
        reload = Mathf.clamp(reload - edelta() / reloadTime);
      }
      
      if(reload <= 0.0001f){
        float targetRotation = angleTo(link);
        rotation = Angles.moveToward(rotation, targetRotation, rotateSpeed * efficiency());
        if(liquids.total() - liquids.get(liquids.current()) <= 0.0001f) {
          canShoot = true;
        }
      }
      
      if(Angles.angleDist(rotation, angleTo(link)) < shootCone && canShoot) {
        float angle = angleTo(link);
        DriverBuildData data = new DriverBuildData();
        data.from = this;
        data.to = this;
        data.liquid = liquids.current();
        bullet = type.create(this, team,
                x + Angles.trnsx(angle, translation), y + Angles.trnsy(angle, translation),
                angle, -1f, 0, 1, data);
        fire();
      }
    }
    
    public void fire() {
      if(bullet != null) {
        tr.trns(rotation, shootLength , 0f);
        bullet.rotation(rotation);
        bullet.set(x + tr.x, y + tr.y);
        bullet.time(0f);
      }
    }
    
    @Override
    public void draw() {
      Draw.rect(baseRegion, x, y);

      Draw.z(Layer.turret);

      Drawf.shadow(region,x + Angles.trnsx(rotation + 180f, reload * knockback) - (size / 2),y + Angles.trnsy(rotation + 180f, reload * knockback) - (size / 2), rotation - 90);
      Draw.rect(region,x + Angles.trnsx(rotation + 180f, reload * knockback),y + Angles.trnsy(rotation + 180f, reload * knockback), rotation - 90);
    }
    
    @Override
    public void drawConfigure() {
      float sin = Mathf.absin(Time.time, 6f, 1f);
      Draw.color(Pal.accent);
      Lines.stroke(1f);
      Drawf.circles(x, y, (block.size / 2f + 1) * tilesize + sin - 2f, Pal.accent);
      
      if(linkValid()){
        Building target = world.build(link);
        Drawf.circles(target.x, target.y, (target.block().size / 2f + 1) * tilesize + sin - 2f, Pal.place);
        Drawf.arrow(x, y, target.x, target.y, size * tilesize + sin, 4f + sin);
      }

      Drawf.dashCircle(x, y, range, Pal.accent);
      
      if(charge()) drawCharge();
    }
    
    public void drawCharge() {
      float p = liquids.get(liquids.current()) / liquids.total();
      Draw.color(IMColors.colorPrimary,IMColors.colorDarkPrimary,p);
      Fill.circle(x, y, 2 * p);
      Fill.circle(x, y, 1 * p);
    }
    
    protected boolean charge() {
      return !canShoot && cons.valid() && liquids.get(liquids.current()) >= 0.0001f;
    }
    
    protected boolean linkValid(){
      if(link == -1) return false;
      Building other = world.build(this.link);
      return other != null && other.block.hasLiquids && other.team == team && within(other, range);
    }
    
    @Override
    public boolean onConfigureTileTapped(Building other) {
      //如果选中的是自己，取消选中
      if(this == other){
        link = -1;
        return false;
      }
      //如果选中的是已被选中方块，取消选中
      if(link == other.pos()){
        link = -1;
        return false;
      }else if(other.block.hasLiquids && other.dst(tile) <= range && other.team == team) {
        link = other.pos();
        return false;
      }
      return true;
    }
    
  }
  
}