package git.str1llax.wnt.config;

import git.str1llax.wnt.WorldNameTerrarified;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;

import java.io.File;

public class ModConfig {
    public static Configuration config;

    private static final String CONFIG_VERSION = "1.0";

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_MC_RESTART = "requires_mc_restart";
    public static final String BUTTON_VISUALS = "button_visuals";

    public static boolean enableMod = true;
    public static boolean enableButtonTooltip = true;
    public static boolean useTexturedButton = true;
    public static int genNameButtonID = 99;
    public static int worldNameLength = 128;
    public static int buttonX = -140;
    public static int buttonY = 60;
    public static int buttonSize = 20;

    public static void init(File file) {
        config = new Configuration(file, CONFIG_VERSION);
        readConfig();
    }

    public static void readConfig() {
        try {
            config.load();

            String loadedVersion = config.getLoadedConfigVersion();

            if (loadedVersion == null || !loadedVersion.equals(CONFIG_VERSION)) {
                for (String category : config.getCategoryNames()) {
                    config.removeCategory(config.getCategory(category));
                }
                config.save();
            }

            initCategories(config);
        } catch (Exception e) {
            WorldNameTerrarified.Logger.log(Level.ERROR, "Error while reading config file", e);
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }

    private static void loadConfig() {
        initCategories(config);

        if (config.hasChanged()) {
            config.save();
        }
    }

    private static void initCategories(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General Configuration");
        cfg.setCategoryRequiresMcRestart(CATEGORY_GENERAL, false);
        cfg.setCategoryRequiresWorldRestart(CATEGORY_GENERAL, false);
        cfg.getCategory(CATEGORY_GENERAL).setLanguageKey("config.category.general");
        enableMod = cfg.getBoolean("enableMod", CATEGORY_GENERAL, true, "Enables or disables custom button and name generation in the world creation menu", "config.property.enableMod");

        cfg.addCustomCategoryComment(BUTTON_VISUALS, "Button visual settings");
        cfg.setCategoryRequiresMcRestart(BUTTON_VISUALS, false);
        cfg.setCategoryRequiresWorldRestart(BUTTON_VISUALS, false);
        cfg.getCategory(BUTTON_VISUALS).setLanguageKey("config.category.button_visuals");
        enableButtonTooltip = cfg.getBoolean("enableButtonTooltip", BUTTON_VISUALS, true, "Enables button tooltip", "config.property.enableButtonTooltip");
        useTexturedButton = cfg.getBoolean("useTexturedButton", BUTTON_VISUALS, true, "Use new fancy button texture instead of default square", "config.property.useTexturedButton");
        buttonSize = cfg.getInt("buttonSize", BUTTON_VISUALS, 20, 1, Integer.MAX_VALUE, "The preferred size in px of the button", "config.property.buttonSize");
        buttonX = cfg.getInt("buttonX", BUTTON_VISUALS, -140, Integer.MIN_VALUE, Integer.MAX_VALUE, "Specifies horizontal OFFSET from the CENTER of the screen", "config.property.buttonX");
        buttonY = cfg.getInt("buttonY", BUTTON_VISUALS, 60, 0, Integer.MAX_VALUE, "Specifies the Y coordinate of the button starting from the TOP LEFT corner", "config.property.buttonY");

        cfg.addCustomCategoryComment(CATEGORY_MC_RESTART, "Category that requires MC restart to apply changes");
        cfg.setCategoryRequiresMcRestart(CATEGORY_MC_RESTART, true);
        cfg.setCategoryRequiresWorldRestart(CATEGORY_MC_RESTART, false);
        cfg.getCategory(CATEGORY_MC_RESTART).setLanguageKey("config.category.requires_mc_restart");
        genNameButtonID = cfg.getInt("genNameButtonID", CATEGORY_MC_RESTART, 99, 0, Integer.MAX_VALUE, "Specifies the id of the random name generation button in the world creation menu (change in case of conflicts)", "config.property.genNameButtonID");
        worldNameLength = cfg.getInt("worldNameLength", CATEGORY_MC_RESTART, 128, 10, Integer.MAX_VALUE, "Specifies the maximum length of world name when creating a new world", "config.property.worldNameLength");
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(WorldNameTerrarified.MOD_ID)) {
            loadConfig();
        }
    }
}
