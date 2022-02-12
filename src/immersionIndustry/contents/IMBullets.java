package immersionIndustry.contents;
 
import arc.audio.*;
import arc.func.*;
import arc.math.geom.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import arc.struct.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.io.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;

import static mindustry.Vars.*;
import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;

import immersionIndustry.IMColors;
import immersionIndustry.types.blocks.distribution.LiquidMassDriver.*;

public class IMBullets implements ContentList {
  
  public static BulletType glowLaser;
  
	@Override
	public void load() {
	  glowLaser = new LaserBulletType(10){
	    
	    {
	      colors = new Color[]{IMColors.colorPrimary,IMColors.colorDarkPrimary,Color.white};
	    }
	    
	    @Override
	    public void draw(Bullet b) {
	      DriverBuildData data =  (DriverBuildData) b.data;
	      float realLength = b.fdata;

        float f = Mathf.curve(b.fin(), 0f, 0.2f);
        float baseLen = realLength * f;
        float cwidth = width;
        float compound = 1f;

        Lines.line(b.x, b.y, data.to.x,data.to.y);
        for(Color color : colors){
            Draw.color(color);
            Lines.stroke((cwidth *= lengthFalloff) * b.fout());
            Lines.line(b.x, b.y, data.to.x,data.to.y);
            Tmp.v1.trns(b.rotation(), baseLen);
            Drawf.tri(b.x + Tmp.v1.x, b.y + Tmp.v1.y, Lines.getStroke() * 1.22f, cwidth * 2f + width / 2f, b.rotation());

            Fill.circle(b.x, b.y, 1f * cwidth * b.fout());
            for(int i : Mathf.signs){
                Drawf.tri(b.x, b.y, sideWidth * b.fout() * cwidth, sideLength * compound, b.rotation() + sideAngle * i);
            }

            compound *= lengthFalloff;
        }
        Draw.reset();

        Tmp.v1.trns(b.rotation(), baseLen * 1.1f);
        Drawf.light(b.team, b.x, b.y, b.x + Tmp.v1.x, b.y + Tmp.v1.y, width * 1.4f * b.fout(), colors[0], 0.6f);
	    }
	    
	    @Override
	    public void despawned(Bullet b) {
	      super.despawned(b);
	      DriverBuildData data =  (DriverBuildData) b.data;
	      data.transmit();
	    }
	    
	    @Override
	    public void hit(Bullet b, float x, float y) {
	      super.hit(b,x,y);
	      DriverBuildData data =  (DriverBuildData) b.data;
	      float range = 120;
	      indexer.eachBlock(this, range, other -> {
	        return other.block.hasLiquids;
	      } , other -> {
            data.add(other);
        });
	    }
	    
	  };
	}
}