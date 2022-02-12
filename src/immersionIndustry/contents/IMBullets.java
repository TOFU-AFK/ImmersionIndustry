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
	  glowLaser = new ContinuousLaserBulletType(10){
	    
	    {
	      colors = new Color[]{IMColors.colorPrimary,IMColors.colorDarkPrimary,Color.white};
	    }
	    
	    @Override
	    public void draw(Bullet b) {
	      DriverBuildData data =  (DriverBuildData) b.data;
	      float realLength = data.from.dst(data.to);
	      float fout = Mathf.clamp(b.time > b.lifetime - fadeTime ? 1f - (b.time - (lifetime - fadeTime)) / fadeTime : 1f);
        float baseLen = realLength * fout;

        Lines.lineAngle(b.x, b.y, b.rotation(), baseLen);
        for(int s = 0; s < colors.length; s++){
          Draw.color(Tmp.c1.set(colors[s]).mul(1f + Mathf.absin(Time.time, 1f, 0.1f)));
          for(int i = 0; i < tscales.length; i++){
            Tmp.v1.trns(b.rotation() + 180f, (lenscales[i] - 1f) * spaceMag);
            Lines.stroke((width + Mathf.absin(Time.time, oscScl, oscMag)) * fout * strokes[s] * tscales[i]);
            Lines.lineAngle(b.x + Tmp.v1.x, b.y + Tmp.v1.y, b.rotation(), baseLen * lenscales[i], false);
          }
        }
        
        Draw.color(data.liquid.color);
        Angles.randLenVectors(b.id, 10, 440 * b.fin() / 2 + 460 / 2,b.rotation(), 0,(x,y) -> {
          Lines.lineAngle(b.x + x, b.y + y, Mathf.angle(x, y),b.fslope() * 17 + 2);
        });

        Tmp.v1.trns(b.rotation(), baseLen * 1.1f);

        Drawf.light(b.team, b.x, b.y, b.x + Tmp.v1.x, b.y + Tmp.v1.y, lightStroke, lightColor, 0.7f);
        Draw.reset();
	    }
	    
	  };
	}
}