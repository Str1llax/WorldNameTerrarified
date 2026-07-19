package git.str1llax.wnt.config;

public class ConfigData {
    public static ConfigEntry<Boolean> enableButtonTooltip = new ConfigEntry<>(Boolean.class, "enableButtonTooltip", "Enables button tooltip when hovered.", true);
    public static ConfigEntry<Boolean> useTexturedButton = new ConfigEntry<>(Boolean.class, "useTexturedButton", "Use new fancy button texture instead of default square.", true);;
    public static ConfigEntry<Boolean> useMcLocale = new ConfigEntry<>(Boolean.class, "useMcLocale", "Generates names in the same language as Minecraft (if exists, else uses English).", true);
    public static ConfigEntry<Boolean> startWithRandom = new ConfigEntry<>(Boolean.class, "startWithRandom", "When opening world creation screen instantly generate random name.", true);
    public static ConfigEntry<String> localeCode = new ConfigEntry<>(String.class, "localeCode", "Specify to use explicitly (If doesn't exist uses en_us)", "en_us");
    public static ConfigEntry<Integer> worldNameLength = new ConfigEntry<>(Integer.class, "worldNameLength", "Specifies the maximum length of world name when creating a new world.", 128, 32, Integer.MAX_VALUE);
    public static ConfigEntry<Integer> buttonX = new ConfigEntry<>(Integer.class, "buttonX", "(horizontal) OFFSET from the CENTER of the screen.", -140, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static ConfigEntry<Integer> buttonY = new ConfigEntry<>(Integer.class, "buttonY", "(vertical) coordinate of the button starting from the TOP.", 60, 0, Integer.MAX_VALUE);
    public static ConfigEntry<Integer> buttonSize = new ConfigEntry<>(Integer.class, "buttonSize", "Preferred size of button in pixels.", 20, 1, Integer.MAX_VALUE);
}
