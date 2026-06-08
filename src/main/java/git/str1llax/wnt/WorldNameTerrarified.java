package git.str1llax.wnt;

import git.str1llax.wnt.utils.GuiHandler;
import git.str1llax.wnt.utils.WorldNameGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = WorldNameTerrarified.MOD_ID, name = WorldNameTerrarified.MOD_NAME, version = WorldNameTerrarified.MOD_VERSION)
public class WorldNameTerrarified
{
    public static final String MOD_ID = "wnt";
    public static final String MOD_NAME = "World Name Terrarified";
    public static final String MOD_VERSION = "0.1";

    @Mod.Instance
    public static WorldNameTerrarified instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new GuiHandler());

        IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
        if (resourceManager instanceof IReloadableResourceManager) {
            IReloadableResourceManager reloadableManager = (IReloadableResourceManager) resourceManager;
            reloadableManager.registerReloadListener(new WorldNameGenerator());
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
