package immersionIndustry.types.blocks.power;

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
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.power.PowerGenerator.GeneratorBuild.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;
import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;

import immersionIndustry.IMSounds;
import immersionIndustry.IMColors;
import immersionIndustry.contents.IMFx;
import immersionIndustry.contents.blocks.*;

public class GlowReleaser extends PowerGenerator {
  
  public final int timerUse = timers++;
  
  public float warmupSpeed = 0.001f;
  public float itemDuration = 60f;
  public int explosionRadius = 23;
  public int explosionDamage = 1900;
  public Effect explodeEffect = Fx.impactReactorExplosion;
  
  public Color plasma1 = IMColors.colorPrimary, plasma2 = IMColors.colorYellow;
  
  public TextureRegion bottomRegion;
  public TextureRegion[] plasmaRegions;
  public int plasmas = 4;
  
  //污染范围，爆炸时*3
  public float range = 120;
  //污染物上限
  public int maxPollutant = 150;
  
  public GlowReleaser(String name) {
    super(name);
    hasPower = true;
    hasLiquids = true;
    liquidCapacity = 120f;
    hasItems = true;
    outputsPower = consumesPower = true;
    flags = EnumSet.of(BlockFlag.reactor, BlockFlag.generator);
    lightRadius = 115f;
    emitLight = true;
    envEnabled = Env.any;
  }
  
  @Override
  public void load() {
    super.load();
    bottomRegion = Core.atlas.find(name + "-bottom");
    plasmaRegions = new TextureRegion[plasmas];
    for(int i = 0;i<plasmas;i++) {
      plasmaRegions[i] = Core.atlas.find(name + "-plasma-" + i);
    }
  }
  
  @Override
  public void setBars(){
    super.setBars();
    addBar("poweroutput", (GeneratorBuild entity) -> new Bar(() ->
      Core.bundle.format("bar.poweroutput",
        Strings.fixed(Math.max(entity.getPowerProduction() - consPower.usage, 0) * 60 * entity.timeScale(), 1)),
      () -> Pal.powerBar,
      () -> entity.productionEfficiency));
  }

  @Override
  public void setStats(){
    super.setStats();
    if(hasItems){
      stats.add(Stat.productionTime, itemDuration / 60f, StatUnit.seconds);
    }
  }
    
  @Override
  public TextureRegion[] icons(){
    return new TextureRegion[]{bottomRegion, region};
  }
  
  public class ReleaserBuild extends GeneratorBuild {
    
    public float warmup;
    public int pollutant = 0;
    
    @Override
    public void updateTile(){
      if(efficiency >= 0.9999f && power.status >= 0.99f){
        boolean prevOut = getPowerProduction() <= consPower.requestedPower(this);

        warmup = Mathf.lerpDelta(warmup, 1f, warmupSpeed * timeScale);
        if(Mathf.equal(warmup, 1f, 0.001f)){
          warmup = 1f;
        }

        if(timer(timerUse, itemDuration / timeScale)){
          IMSounds.energyShockWave.at(this);
          IMFx.sphere.at(x + Mathf.range(size * 4f), y + Mathf.range(size * 4));
          consume();
          pollute();
        }
        
        IMFx.radiation.at(this,Time.time * 1.5f);
      }else{
        warmup = Mathf.lerpDelta(warmup, 0f, 0.01f);
      }

      productionEfficiency = Mathf.pow(warmup, 5f);
    }
    
    @Override
    public void draw(){
      Draw.rect(bottomRegion, x, y);

      for(int i = 0; i < plasmaRegions.length; i++){
        float r = size * tilesize - 3f + Mathf.absin(Time.time, 2f + i * 1f, 5f - i * 0.5f);

        Draw.color(plasma1, plasma2, (float)i / plasmaRegions.length);
        Draw.alpha((0.3f + Mathf.absin(Time.time, 2f + i * 2f, 0.3f + i * 0.05f)) * warmup);
        Draw.blend(Blending.additive);
        Draw.rect(plasmaRegions[i], x, y, r, r, Time.time * (12 + i * 6f) * warmup);
        Draw.blend();
      }

      Draw.color();

      Draw.rect(region, x, y);

      Draw.color();
    }
    
    //污染周围环境
    protected void pollute() {
      if(pollutant < maxPollutant) {
        Tile t = getTile(tile);
        if(t != null) {
          replace(t);
        }
      }
    }
    
    protected Tile getTile(Tile tile) {
      Tile t = tile.nearby(Mathf.random(0,3));
      if(t == null || t.block() instanceof GlowReleaser || t.floor() == IMFloors.glow) return getTile(t);
      return t;
    }
    
    protected void replace(Tile tile) {
      tile.setFloor(IMFloors.glow);
      if(tile.build != null) tile.build.killed();
      pollutant++;
    }

    @Override
    public float ambientVolume(){
      return warmup;
    }
    
    @Override
    public void drawLight(){
      Drawf.light(x, y, (110f + Mathf.absin(5, 5f)) * warmup, Tmp.c1.set(plasma2).lerp(plasma1, Mathf.absin(7f, 0.2f)), 0.8f * warmup);
    }
        
    @Override
    public double sense(LAccess sensor){
      if(sensor == LAccess.heat) return warmup;
      return super.sense(sensor);
    }

    @Override
    public void onDestroyed(){
      super.onDestroyed();

      if(warmup < 0.3f || !state.rules.reactorExplosions) return;

      Sounds.explosionbig.at(this);

      Damage.damage(x, y, explosionRadius * tilesize, explosionDamage * 4);

      Effect.shake(6f, 16f, x, y);
      explodeEffect.at(x, y);
    }

    @Override
    public void write(Writes write){
      super.write(write);
      write.f(warmup);
    }

    @Override
    public void read(Reads read, byte revision){
      super.read(read, revision);
      warmup = read.f();
    }
    
  }
}