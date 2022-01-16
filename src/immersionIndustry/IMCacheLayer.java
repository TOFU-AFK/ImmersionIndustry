package immersionIndustry;

import arc.*;
import arc.graphics.*;
import arc.graphics.gl.*;
import arc.util.*;

import mindustry.graphics.*;
import mindustry.graphics.CacheLayer;
import mindustry.graphics.CacheLayer.*;

import static mindustry.Vars.*;

public class IMCacheLayer {
  
  public static CacheLayer glow;
  
  public static void init() {
    CacheLayer.add(
    glow = new ShaderLayer(IMShaders.glow)
    );
  }
  
}