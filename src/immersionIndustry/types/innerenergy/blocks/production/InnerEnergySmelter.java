package immersionIndustry.types.innerenergy.blocks.production;

import arc.*;
import arc.scene.*;
import arc.scene.style.*;
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
import mindustry.world.blocks.environment.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;
import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;

import immersionIndustry.ui.*;
import immersionIndustry.IMColors;
import immersionIndustry.contents.IMFx;
import immersionIndustry.types.innerenergy.blocks.*;
import immersionIndustry.types.innerenergy.blocks.InnerenergyBlock.*;

public class InnerEnergySmelter extends InnerenergyBlock {
  
  //液体条的宽高
  public float barWidth = tilesize/2;
  public float barHeight;
  //偏移，方块底部开始
  public float ey;
  public float ex;
  
  public InnerEnergySmelter(String name) {
    super(name);
    solid = true;
    hasItems = true;
    itemCapacity = 100;
    ambientSound = Sounds.machine;
    sync = true;
    ambientSoundVolume = 0.03f;
    flags = EnumSet.of(BlockFlag.factory);
    liquidCapacity = 100;
    barHeight = size * tilesize / 2 + 4;
    ey = barHeight / 2;
    ex = barWidth / 2;
  }
  
  @Override
  public void init() {
    super.init();
  }
  
  public class SmelterBuild extends InnerenergyBuild {
    
    Vbar vbar;
    ItemGrid grid;
    
    @Override
    public Building create(Block block, Team team) {
      super.create(block,team);
      vbar = new Vbar(barWidth,barHeight);
      grid = new ItemGrid();
      vbar.add(Color.orange,"熔融铅",400);
      vbar.add(Color.orange,"熔融铜",400);
      return self();
    }
    
    @Override
    public void draw() {
      super.draw();
      vbar.draw(x - ex,y - ey);
    }
    
    @Override
    public void buildConfiguration(Table table) {
      if(!items.any()) return;
      grid.clear();
      items.each((Item item, int amount) -> {
          grid.add(item,amount);
      });
      table.add(grid);
    }
    
    @Override
    public boolean acceptItem(Building source, Item item){
      return items.get(item) < getMaximumAccepted(item);
    }
    
    public class ItemGrid extends Table {
      
      public int c = 4;
      int i = 0;
      
      public void add(Item item,int amount) {
        i++;
        if(i>4) {
          row();
          i = 0;
        }
        add(new ItemDisplay(item,amount));
      }
      
      @Override
      public void clear(){
        super.clear();
        i = 0;
      }
      
    }
    
  }
  
}