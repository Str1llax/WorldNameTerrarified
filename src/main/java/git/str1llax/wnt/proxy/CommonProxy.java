package git.str1llax.wnt.proxy;

import git.str1llax.wnt.utils.GuiInjector;
import git.str1llax.wnt.utils.WorldNameGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod.EventBusSubscriber
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {

    }

    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new GuiInjector());

        IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
        if (resourceManager instanceof IReloadableResourceManager) {
            IReloadableResourceManager reloadableManager = (IReloadableResourceManager) resourceManager;
            reloadableManager.registerReloadListener(new WorldNameGenerator());
        }
    }

    public void postInit(FMLPostInitializationEvent event) {

    }
}
