package immersionIndustry.types.items;

import arc.*;
import arc.struct.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.util.*;
import betamindy.graphics.*;
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
                    animRegions[i * (transition + 1) + j] = Drawm.blendSprites(spriteArr[i], spriteArr[(i >= sprites - 1) ? 0 : i + 1], f, name + i);
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

}
