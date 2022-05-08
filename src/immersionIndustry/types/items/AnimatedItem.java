package immersionIndustry.types.items;

import arc.*;
import arc.struct.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.graphics.MultiPacker.*;
import mindustry.world.*;
import mindustry.type.*;
import mindustry.ui.*;

import static arc.Core.atlas;

public class AnimatedItem extends Item {
    public final TextureRegion animIcon = new TextureRegion();
    public TextureRegion[] animRegions;

    public float animDelay = 3f;
    protected int animateLevel = 2;

    public int sprites = 10;
    public int transition = 0;

    public int n;
    
    public static Seq<AnimatedItem> animitems = new Seq<>();

    public AnimatedItem(String name, Color color){
        super(name, color);
        animitems.add(this);
    }

    @Override
    public void load(){
        super.load();
        TextureRegion[] spriteArr = new TextureRegion[sprites];
        for(int i = 0; i < sprites; i++){
            spriteArr[i] = atlas.find(name + i, name);
        }

        n = sprites * (1 + transition);
        animRegions = new TextureRegion[n];
        for(int i = 0; i < sprites; i++){
            if(transition <= 0) animRegions[i] = spriteArr[i];
            else{
                animRegions[i * (transition + 1)] = spriteArr[i];
                for(int j = 1; j <= transition; j++){
                    float f = (float)j / (transition + 1);
                    animRegions[i * (transition + 1) + j] = blendSprites(spriteArr[i], spriteArr[(i >= sprites - 1) ? 0 : i + 1], f, name + i);
                }
            }
        }
        animIcon.set(animRegions[0]);
    }

    //在Trigger.update中调用
    public void update(){
        animateLevel = Core.settings.getInt("animlevel", 2);
        if(animateLevel >= 1){
            fullIcon.set(animRegions[(int) (Time.globalTime / animDelay) % n]);
            if(animateLevel >= 2) uiIcon.set(fullIcon);
        }
    }

    public static TextureRegion blendSprites(TextureRegion a, TextureRegion b, float f, String name){
        PixmapRegion r1 = Core.atlas.getPixmap(a);
        PixmapRegion r2 = Core.atlas.getPixmap(b);

        Pixmap out = new Pixmap(r1.width, r1.height);
        Color color1 = new Color();
        Color color2 = new Color();

        for(int x = 0; x < r1.width; x++){
            for(int y = 0; y < r1.height; y++){
                out.setRaw(x, y, color1.set(r1.get(x, y)).lerp(color2.set(r2.get(x, y)), f).rgba());
            }
        }

        Texture texture  = new Texture(out);
        return Core.atlas.addRegion(name + "-blended-" + (int)(f * 100), new TextureRegion(texture));
    }
} 