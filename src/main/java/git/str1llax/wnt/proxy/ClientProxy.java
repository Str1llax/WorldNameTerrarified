package git.str1llax.wnt.proxy;

import git.str1llax.wnt.WorldNameTerrarified;
import git.str1llax.wnt.config.ModConfig;
import git.str1llax.wnt.handler.ResourceEventHandler;
import git.str1llax.wnt.utils.ModKeybinds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        ModConfig.init(new File(event.getModConfigurationDirectory().getPath(), WorldNameTerrarified.MOD_ID + ".cfg"));
        ClientRegistry.registerKeyBinding(ModKeybinds.genRandomNameKey);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
        if (resourceManager instanceof IReloadableResourceManager) {
            IReloadableResourceManager reloadableManager = (IReloadableResourceManager) resourceManager;
            reloadableManager.registerReloadListener(new ResourceEventHandler());
        }
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);

        if (ModConfig.config.hasChanged()) {
            ModConfig.config.save();
        }
    }
}
