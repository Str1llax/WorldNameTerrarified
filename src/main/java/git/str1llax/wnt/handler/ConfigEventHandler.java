package git.str1llax.wnt.handler;

import git.str1llax.wnt.WorldNameTerrarified;
import git.str1llax.wnt.config.ModConfig;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = WorldNameTerrarified.MOD_ID, value = Side.CLIENT)
public class ConfigEventHandler {

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(WorldNameTerrarified.MOD_ID)) {
            ModConfig.loadConfig();
        }
    }
}
