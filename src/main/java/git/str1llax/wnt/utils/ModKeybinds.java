package git.str1llax.wnt.utils;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class ModKeybinds {
    private static final String CATEGORY = "key.category.wnt";

    public static KeyBinding genRandomNameKey = new KeyBinding("key.gen_random_name", InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_F4, CATEGORY);
}
