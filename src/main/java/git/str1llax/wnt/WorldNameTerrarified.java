package git.str1llax.wnt;

import git.str1llax.wnt.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = WorldNameTerrarified.MOD_ID, name = WorldNameTerrarified.MOD_NAME)
public class WorldNameTerrarified
{
    public static final String MOD_ID = "wnt";
    public static final String MOD_NAME = "World Name Terrarified";

    @Mod.Instance
    public static WorldNameTerrarified instance;

    @SidedProxy(clientSide = "git.str1llax."+MOD_ID+".proxy.ClientProxy", serverSide = "git.str1llax."+MOD_ID+".proxy.ServerProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
