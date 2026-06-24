package git.str1llax.wnt.config;

import git.str1llax.wnt.WorldNameTerrarified;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

public class ModConfigGUI extends GuiConfig {
    public ModConfigGUI(GuiScreen parentScreen) {
        super(
                parentScreen,
                getConfigElements(),
                WorldNameTerrarified.MOD_ID,
                false,
                false,
                WorldNameTerrarified.MOD_NAME);
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> elements = new ArrayList<>();

        elements.add(new ConfigElement(ModConfig.config.getCategory(ModConfig.CATEGORY_GENERAL)));
        elements.add(new ConfigElement(ModConfig.config.getCategory(ModConfig.CATEGORY_MC_RESTART)));

        return elements;
    }
}
