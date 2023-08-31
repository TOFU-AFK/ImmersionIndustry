package immersionIndustry.ai;

import mindustry.ai.types.*;
import arc.math.*;
import arc.util.*;
import mindustry.ai.*;
import mindustry.type.*;
import mindustry.content.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

import immersionIndustry.contents.IMFx;
import immersionIndustry.IMColors;

public class TransportAi extends AIController {
  
  Building from,to;
  boolean wait;
  float waitTime = 160;
  
  public TransportAi(Building from,Building to) {
    this.from = from;
    this.to = to;
    target = to;
  }
  
  @Override
  public void updateMovement() {
    if(from == null || to == null || wait) return;
    if(unit.stack.amount > 0) {
      //运送物品
      if(unit.inRange(to)) {
        int i = to.acceptStack(unit.stack.item,unit.stack.amount,unit);
        if(i > 0) {
          wait = true;
          IMFx.takeItemEffect(unit.x,unit.y,to.x,to.y,unit.stack.item.color,40);
          Time.run(waitTime,() -> {
            to.handleStack(unit.stack.item,i,unit);
            unit.stack.set(unit.stack.item,unit.stack.amount - i);
            wait = false;
          });
        }
      }else {
        moveTarget(to);
        //unit.movePref(vec.trns(unit.angleTo(to.x, to.y), unit.speed()));
      }
    }else {
      Item item = from.items.first();
      if(unit.inRange(from) && item != null) {
        wait = true;
        IMFx.takeItemEffect(from.x,from.y,unit.x,unit.y,item.color,40);
        Time.run(waitTime,() -> {
          int i = from.removeStack(item,unit.type.itemCapacity);
          unit.stack.set(item,i);
          wait = false;
        });
      }else {
        moveTarget(from);
        //unit.movePref(vec.trns(unit.angleTo(from.x, from.y), unit.speed()));
      }
    }
    
    faceTarget();
  }
  
  private void moveTarget(Building to) {
    unit.movePref(vec.trns(unit.angleTo(findNear(to)), unit.speed()));
  }
  
  /*
  * 寻找前往的下一个方块
  * 根据单位所在的Tile的周围四个Tile计算曼哈顿距离，得到离目标最近的一个位置
  */
  private Tile findNear(Building to) {
    Tile tile = unit.tileOn();
    float value = -1;
    Tile ti = tile;
    for(int i = 0;i<4;i++) {
      Tile t = tile.nearby(i);
      if(t != null && t.floor().isLiquid && t.block() == Blocks.air) {
        float d = Mathf.dst(t.drawx(),t.drawy(),to.x,to.y);
        if(value == -1 || value > d) {
          value = d;
          ti = t;
        }
      }
    }
    return ti;
  }
  
}