package git.str1llax.wnt.config;

import git.str1llax.wnt.WorldNameTerrarified;
import git.str1llax.wnt.utils.WorldNameGenerator;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

import java.io.File;

public class ModConfig {
    public static Configuration config;

    private static final String CONFIG_VERSION = "1.1";

    public static final String CATEGORY_GENERAL = "general";

    public static boolean enableButtonTooltip = true;
    public static boolean useTexturedButton = true;
    public static boolean useMcLocale = true;
    public static boolean startWithRandom = true;
    public static String localeCode = "en_us";
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

    public static void loadConfig() {
        initCategories(config);
        if (config.get(CATEGORY_GENERAL, "useMcLocale", true).hasChanged() ||
                config.get(CATEGORY_GENERAL, "useMcLocale", true).getBoolean() &&
                        config.get(CATEGORY_GENERAL, "localeCode", "en_us").hasChanged()) {
            WorldNameGenerator.reloadResources(Minecraft.getMinecraft().getResourceManager());
        }
        if (config.hasChanged()) {
            config.save();
        }
    }

    private static void initCategories(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General Configuration");
        cfg.setCategoryRequiresMcRestart(CATEGORY_GENERAL, false);
        cfg.setCategoryRequiresWorldRestart(CATEGORY_GENERAL, false);
        cfg.getCategory(CATEGORY_GENERAL).setLanguageKey("config.category.general");
        genNameButtonID = cfg.getInt("genNameButtonID", CATEGORY_GENERAL, 99, 0, Integer.MAX_VALUE, "Specifies the id of the random name generation button in the world creation menu (change in case of conflicts)", "config.property.genNameButtonID");
        enableButtonTooltip = cfg.getBoolean("enableButtonTooltip", CATEGORY_GENERAL, true, "Enables button tooltip", "config.property.enableButtonTooltip");
        useTexturedButton = cfg.getBoolean("useTexturedButton", CATEGORY_GENERAL, true, "Use new fancy button texture instead of default square", "config.property.useTexturedButton");
        buttonSize = cfg.getInt("buttonSize", CATEGORY_GENERAL, 20, 1, Integer.MAX_VALUE, "The preferred size in px of the button", "config.property.buttonSize");
        buttonX = cfg.getInt("buttonX", CATEGORY_GENERAL, -140, Integer.MIN_VALUE, Integer.MAX_VALUE, "Specifies horizontal OFFSET from the CENTER of the screen", "config.property.buttonX");
        buttonY = cfg.getInt("buttonY", CATEGORY_GENERAL, 60, 0, Integer.MAX_VALUE, "Specifies the Y coordinate of the button starting from the TOP LEFT corner", "config.property.buttonY");
        worldNameLength = cfg.getInt("worldNameLength", CATEGORY_GENERAL, 128, 32, Integer.MAX_VALUE, "Specifies the maximum length of world name when creating a new world", "config.property.worldNameLength");
        useMcLocale = cfg.getBoolean("useMcLocale", CATEGORY_GENERAL, true, "Uses the same locale as Minecraft set on for name generation (if exists)", "config.property.useMcLocale");
        localeCode = cfg.getString("localeCode", CATEGORY_GENERAL, "en_us", "Locale code to use instead of Minecraft locale", "config.property.localeCode");
        startWithRandom = cfg.getBoolean("startWithRandom", CATEGORY_GENERAL, true, "When opening world creation screen, random name sets automatically.", "config.property.startWithRandom");
    }
}
