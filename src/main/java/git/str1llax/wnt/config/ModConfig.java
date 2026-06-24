package git.str1llax.wnt.config;

import git.str1llax.wnt.WorldNameTerrarified;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;

public class ModConfig {
    public static Configuration config;

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_MC_RESTART = "requires_mc_restart";

    public static boolean enableMod = true;
    public static int genNameButtonID = 99;
    public static int worldNameLength = 128;
    public static int buttonX = -140;
    public static int buttonY = 60;

    public static void readConfig() {
        try {
            config.load();
            initConfig(config);
        } catch (Exception e) {
            WorldNameTerrarified.Logger.log(Level.ERROR, "Error while reading config file", e);
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }

    private static void loadConfig() {
        initConfig(config);

        if (config.hasChanged()) {
            config.save();
        }
    }

    private static void initConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General Configuration");
        cfg.setCategoryRequiresMcRestart(CATEGORY_GENERAL, false);
        cfg.setCategoryRequiresWorldRestart(CATEGORY_GENERAL, false);
        cfg.getCategory(CATEGORY_GENERAL).setLanguageKey("config.category.general");
        enableMod = cfg.getBoolean("enableMod", CATEGORY_GENERAL, true, "Enables or disables custom button and name generation in the world creation menu", "config.property.enableMod");
        buttonX = cfg.getInt("buttonX", CATEGORY_GENERAL, -140, Integer.MIN_VALUE, Integer.MAX_VALUE, "Specifies horizontal OFFSET from the CENTER of the screen", "config.property.buttonX");
        buttonY = cfg.getInt("buttonY", CATEGORY_GENERAL, 60, 0, Integer.MAX_VALUE, "Specifies the Y coordinate of the button starting from the TOP LEFT corner", "config.property.buttonY");

        cfg.addCustomCategoryComment(CATEGORY_MC_RESTART, "Category that requires MC restart to apply changes");
        cfg.setCategoryRequiresMcRestart(CATEGORY_MC_RESTART, true);
        cfg.setCategoryRequiresWorldRestart(CATEGORY_MC_RESTART, false);
        cfg.getCategory(CATEGORY_MC_RESTART).setLanguageKey("config.category.requires_mc_restart");
        genNameButtonID = cfg.getInt("genNameButtonID", CATEGORY_MC_RESTART, 99, 0, Integer.MAX_VALUE, "Specifies the id of the random name generation button in the world creation menu (change in case of conflicts)", "config.property.genNameButtonID");
        worldNameLength = cfg.getInt("worldNameLength", CATEGORY_MC_RESTART, 128, 10, Integer.MAX_VALUE, "Specifies the maximum length of world name when creating a new world", "config.property.worldNameLength");
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if(event.getModID().equals(WorldNameTerrarified.MOD_ID)) {
            loadConfig();
        }
    }
}
