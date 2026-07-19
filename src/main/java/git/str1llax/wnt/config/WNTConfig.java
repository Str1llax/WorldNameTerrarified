package git.str1llax.wnt.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import git.str1llax.wnt.WorldNameTerrarified;
import git.str1llax.wnt.utils.WorldNameGenerator;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.Level;

import java.nio.file.Path;

class ForgeConfigEntry<T extends Comparable<? super T>> extends ConfigEntry<T> {
    public ForgeConfigSpec.ConfigValue<T> forgeValue;

    ForgeConfigEntry(ConfigEntry<T> entry) {
        super(entry);
        this.forgeValue = null;
    }

    public void build(ForgeConfigSpec.Builder builder) {
        if (this.isRanged()) {
            this.forgeValue = builder.comment(String.format("%s Default: %s, [%s, %s]", this.comment, this.defaultValue, this.min, this.max)).defineInRange(this.key, this.defaultValue, this.min, this.max, this.clazz);
        } else {
            this.forgeValue = builder.comment(String.format("%s Default: %s", this.comment, this.defaultValue)).define(this.key, this.defaultValue);
        }
    }
}

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class WNTConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new  ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    private static final ForgeConfigEntry<Boolean> ENABLE_BUTTON_TOOLTIP = new ForgeConfigEntry<>(ConfigData.enableButtonTooltip);
    private static final ForgeConfigEntry<Boolean> USE_TEXTURED_BUTTON = new ForgeConfigEntry<>(ConfigData.useTexturedButton);
    private static final ForgeConfigEntry<Boolean> START_WITH_RANDOM = new ForgeConfigEntry<>(ConfigData.startWithRandom);
    private static final ForgeConfigEntry<Boolean> USE_MC_LOCALE = new ForgeConfigEntry<>(ConfigData.useMcLocale);
    private static final ForgeConfigEntry<String> LOCALE_CODE = new ForgeConfigEntry<>(ConfigData.localeCode);
    private static final ForgeConfigEntry<Integer> BUTTON_SIZE = new ForgeConfigEntry<>(ConfigData.buttonSize);
    private static final ForgeConfigEntry<Integer> BUTTON_X = new ForgeConfigEntry<>(ConfigData.buttonX);
    private static final ForgeConfigEntry<Integer> BUTTON_Y = new ForgeConfigEntry<>(ConfigData.buttonY);
    private static final ForgeConfigEntry<Integer> WORLD_NAME_LENGTH = new ForgeConfigEntry<>(ConfigData.worldNameLength);

    static {
        BUILDER.push("General");

        ENABLE_BUTTON_TOOLTIP.build(BUILDER);
        USE_TEXTURED_BUTTON.build(BUILDER);
        BUTTON_SIZE.build(BUILDER);
        BUTTON_X.build(BUILDER);
        BUTTON_Y.build(BUILDER);
        WORLD_NAME_LENGTH.build(BUILDER);
        USE_MC_LOCALE.build(BUILDER);
        LOCALE_CODE.build(BUILDER);
        START_WITH_RANDOM.build(BUILDER);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    private static void reloadResourcesOnChange() {
        if (USE_MC_LOCALE.isChanged() ||
            !USE_MC_LOCALE.get() && LOCALE_CODE.isChanged()) {
            WorldNameGenerator.reloadResources(Minecraft.getInstance().getResourceManager());
        }
    }

    private static void sync() {
        reloadResourcesOnChange();

        ConfigData.enableButtonTooltip.set(ENABLE_BUTTON_TOOLTIP);
        ConfigData.useTexturedButton.set(USE_TEXTURED_BUTTON);
        ConfigData.buttonSize.set(BUTTON_SIZE);
        ConfigData.buttonX.set(BUTTON_X);
        ConfigData.buttonY.set(BUTTON_Y);
        ConfigData.worldNameLength.set(WORLD_NAME_LENGTH);
        ConfigData.useMcLocale.set(USE_MC_LOCALE);
        ConfigData.localeCode.set(LOCALE_CODE);
        ConfigData.startWithRandom.set(START_WITH_RANDOM);
    }

    public static void save() {
        ENABLE_BUTTON_TOOLTIP.set(ConfigData.enableButtonTooltip);
        USE_TEXTURED_BUTTON.set(ConfigData.useTexturedButton);
        BUTTON_SIZE.set(ConfigData.buttonSize);
        BUTTON_X.set(ConfigData.buttonX);
        BUTTON_Y.set(ConfigData.buttonY);
        WORLD_NAME_LENGTH.set(ConfigData.worldNameLength);
        USE_MC_LOCALE.set(ConfigData.useMcLocale);
        LOCALE_CODE.set(ConfigData.localeCode);
        START_WITH_RANDOM.set(ConfigData.startWithRandom);

        SPEC.save();

        sync();
    }

    public static void resetAll() {
        ConfigData.enableButtonTooltip.reset();
        ConfigData.useTexturedButton.reset();
        ConfigData.buttonSize.reset();
        ConfigData.buttonX.reset();
        ConfigData.buttonY.reset();
        ConfigData.worldNameLength.reset();
        ConfigData.useMcLocale.reset();
        ConfigData.localeCode.reset();
        ConfigData.startWithRandom.reset();
    }

    @SubscribeEvent
    public static void onConfigLoad(final ModConfig.Loading event) {
        if (event.getConfig().getSpec() == WNTConfig.SPEC) {
            sync();
        }
    }

    @SubscribeEvent
    public static void onConfigReload(final ModConfig.ConfigReloading event) {
        if (event.getConfig().getSpec() == WNTConfig.SPEC) {
            sync();
        }
    }
}
