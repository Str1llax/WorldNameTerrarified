package git.str1llax.wnt.config;

import git.str1llax.wnt.WorldNameTerrarified;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;

import java.util.stream.Collectors;

public class ModConfigGUI extends GuiConfig {
    public ModConfigGUI(GuiScreen parentScreen) {
        super(
                parentScreen,
                ModConfig.config.getCategory(ModConfig.CATEGORY_GENERAL).getOrderedValues().stream().map(ConfigElement::new).collect(Collectors.toList()),
                WorldNameTerrarified.MOD_ID,
                false,
                false,
                WorldNameTerrarified.MOD_NAME);
    }
}
