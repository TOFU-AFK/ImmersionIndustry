package immersionIndustry.contents;
import arc.*;
import arc.files.*;
import arc.graphics.*;
import arc.graphics.Texture.*;
import arc.graphics.g2d.*;
import arc.graphics.g3d.*;
import arc.graphics.gl.*;
import arc.math.geom.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.type.*;
import mindustry.graphics.*;
import mindustry.graphics.Shaders.*;
import mindustry.mod.*;
import mindustry.mod.Mods.*;

import static mindustry.Vars.*;

import immersionIndustry.ImmersionIndustry;

public class IMShaders {
  
  public static FuseShader fuse;
  
  public static void init() {
    fuse = new FuseShader();
  }
  
  public static class FuseShader extends ModShader {
    
    Texture noiseTex;
    
    public FuseShader() {
      super("fuseShader","fuseShader");
      loadNoise();
    }
    
    public String textureName(){
      return "noise";
    }
    
    public void loadNoise(){
      Core.assets.load("sprites/" + textureName() + ".png", Texture.class).loaded = t -> {
        t.setFilter(TextureFilter.linear);
        t.setWrap(TextureWrap.repeat);
      };
    }
		
		@Override
		public void apply() {
		  setUniformf("u_campos", Core.camera.position.x - Core.camera.width / 2, Core.camera.position.y - Core.camera.height / 2);
      setUniformf("u_resolution", Core.camera.width, Core.camera.height);
      setUniformf("u_time", Time.time);

      if(hasUniform("u_noise")){
        if(noiseTex == null){
          noiseTex = Core.assets.get("sprites/" + textureName() + ".png", Texture.class);
        }

        noiseTex.bind(1);
        renderer.effectBuffer.getTexture().bind(0);

        setUniformi("u_noise", 1);
      }
		}
  }
  
  public static class ModShader extends Shader {

    public ModShader(String vert, String frag){
			super(getShaderFi(vert + ".vert"), getShaderFi(frag + ".frag"));
		}
  }
  
  public static Fi getShaderFi(String file){
    LoadedMod mod = mods.getMod(ImmersionIndustry.class);
    if(mod.root.child("shader").exists()){
			Fi shaders = mod.root.child("shader");
			if(shaders.child(file).exists())return shaders.child(file);
		}
    return Shaders.getShaderFi(file);
  }
  
}