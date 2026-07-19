package git.str1llax.wnt;

import git.str1llax.wnt.config.WNTConfig;
import git.str1llax.wnt.config.WNTConfigScreen;
import git.str1llax.wnt.handler.ResourceEventHandler;
import git.str1llax.wnt.utils.ModKeybinds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

@Mod(WorldNameTerrarified.MOD_ID)
public class WorldNameTerrarified {
    public static final String MOD_ID = "wnt";
    public static final String MOD_NAME = "World Name Terrarified";

    public static Logger LOGGER = LogManager.getLogger();

    public WorldNameTerrarified() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, WNTConfig.SPEC);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ModLoadingContext.get().registerExtensionPoint(
                    ExtensionPoint.CONFIGGUIFACTORY,
                    () -> ((minecraft, screen) -> new WNTConfigScreen(screen))
            );
        }

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        if (resourceManager instanceof IReloadableResourceManager) {
            ((IReloadableResourceManager) resourceManager).addReloadListener(ResourceEventHandler.INSTANCE);
        }

        ClientRegistry.registerKeyBinding(ModKeybinds.genRandomNameKey);
    }
}
