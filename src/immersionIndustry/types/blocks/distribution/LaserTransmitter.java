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

public class LaserTransmitter extends Block {
  
  int maxLength = 30;
  float speed = 2;//倍数
  float interval = 2;
  public Effect craftEffect = new Effect(38f,e -> {
    color(IMColors.colorYellow,IMColors.colorWhite,e.fin());
    randLenVectors(e.id, 2, 1f + 20f * e.fout(), e.rotation, 120f, (x, y) -> {
      lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 2f + 1f);
    });
    
    alpha(e.fin()*0.5f);
    color(IMColors.colorWhite);
    Fill.circle(e.x,e.y,0.5f);
    color(IMColors.colorYellow);
    Fill.circle(e.x,e.y,1.5f);
    Drawf.light(e.x,e.y,32,IMColors.colorYellow,e.fin());
  });
  
  public LaserTransmitter(String name) {
    super(name);
    update = true;
    solid = true;
    configurable = true;
    hasItems = true;
    hasPower = true;
    rotate = true;
    sync = true;
  }
  
  @Override
  public void drawPlace(int x, int y, int rotation, boolean valid) {
    super.drawPlace(x,y,rotation,valid);
    if(!valid) return;
    if(rotation == 0) {
      for(int i = 1;i<maxLength;i++) {
        Tile tile = world.tile(x+i, y);
        if(tile.block() != null && tile.block().hasItems && tile.build != null && tile.build.isValid()) {
          //这里需要获取building才能绘制出正确的图形
          Building build = tile.build;
          Drawf.select(build.x, build.y,build.block.size * tilesize / 2f + 1f,Pal.accent);
          Drawf.dashLine(IMColors.colorYellow,x * tilesize + offset,y * tilesize + offset,tile.drawx(),y* tilesize + offset);
          return;
        }
      }
    }
    if(rotation == 1) {
      for(int i = 1;i<maxLength;i++) {
        Tile tile = world.tile(x, y+i);
        if(tile.block() != null && tile.block().hasItems && tile.build != null && tile.build.isValid()) {
          //这里需要获取building才能绘制出正确的图形
          Building build = tile.build;
          Drawf.select(build.x, build.y,build.block.size * tilesize / 2f + 1f,Pal.accent);
          Drawf.dashLine(IMColors.colorYellow,x * tilesize + offset,y * tilesize + offset,x * tilesize + offset,tile.drawy());
          return;
        }
      }
    }
    if(rotation == 2) {
      for(int i = 1;i<maxLength;i++) {
        Tile tile = world.tile(x-i, y);
        if(tile.block() != null && tile.block().hasItems && tile.build != null && tile.build.isValid()) {
          //这里需要获取building才能绘制出正确的图形
          Building build = tile.build;
          Drawf.select(build.x, build.y,build.block.size * tilesize / 2f + 1f,Pal.accent);
          Drawf.dashLine(IMColors.colorYellow,x * tilesize + offset,y * tilesize + offset,tile.drawx(),y * tilesize + offset);
          return;
        }
      }
    }
    if(rotation == 3) {
      for(int i = 1;i<maxLength;i++) {
        Tile tile = world.tile(x, y-i);
        if(tile.block() != null && tile.block().hasItems && tile.build != null && tile.build.isValid()) {
          //这里需要获取building才能绘制出正确的图形
          Building build = tile.build;
          Drawf.select(build.x, build.y,build.block.size * tilesize / 2f + 1f,Pal.accent);
          Drawf.dashLine(IMColors.colorYellow,x * tilesize + offset,y * tilesize + offset,x * tilesize + offset,tile.drawy());
          return;
        }
      }
    }
  }
  
  public class TransmitterBuild extends Building {
    
    Tile target;
    
    @Override
    public void updateTile() {
      if(target == null || target.build == null || !target.build.isValid()) {
        target = itemTo();
      }
      //传输时间
      if(target == null) return;
      float time = Mathf.dstm(x,y,target.worldx(),target.worldy()) / tilesize * speed / efficiency() * 0.1f;
      if(target != null && efficiency() > 0 && timer(timerDump,interval + time)) {
        Item item = items.first();
        if(item != null) {
          //判断是否能存储物品，因为传输之间有延迟，所以要判断两次，都返回true才可存入
          if(target.build.acceptItem(this,item)) {
            IMFx.takeItemEffect(x,y,target.worldx(),target.worldy(),item.color,time);
            Time.run(time,() -> {
              if(target.build.acceptItem(this,item)) {
                target.build.handleItem(this,item);
                items.remove(item,1);
                craftEffect.at(this);
              }
            });
          }
        }
      }
    }
    
    @Override
    public void draw() {
      super.draw();
      Draw.z(Layer.power);
      if(target != null) {
        if(rotation == 1 || rotation == 3) {
          Drawf.laser(Core.atlas.find("minelaser"),Core.atlas.find("minelaser-end"),x,y,x,target.worldy(),0.4f);
        }else {
          Drawf.laser(Core.atlas.find("minelaser"),Core.atlas.find("minelaser-end"),x,y,target.worldx(),y,0.4f);
        }
      }
      Draw.reset();
    }
    
    @Override
    public boolean acceptItem(Building source, Item item){
        return items.get(item) < getMaximumAccepted(item);
    }
    
    public Tile itemTo() {
      if(rotation == 0) {
        for(int i = 1;i<maxLength;i++) {
          Tile tile = world.tile(tileX()+i, tileY());
          if(tile.block() != null && tile.block().hasItems) {
            return tile;
          }
        }
      }
      if(rotation == 1) {
        for(int i = 1;i<maxLength;i++) {
          Tile tile = world.tile(tileX(), tileY()+i);
          if(tile.block() != null && tile.block().hasItems) {
            return tile;
          }
        }
      }
      if(rotation == 2) {
        for(int i = 1;i<maxLength;i++) {
          Tile tile = world.tile(tileX()-i, tileY());
          if(tile.block() != null && tile.block().hasItems) {
            return tile;
          }
        }
      }
      if(rotation == 3) {
        for(int i = 1;i<maxLength;i++) {
          Tile tile = world.tile(tileX(), tileY()-i);
          if(tile.block() != null && tile.block().hasItems) {
            return tile;
          }
        }
      }
      return null;
    }
    
  }
  
}