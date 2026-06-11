package git.str1llax.wnt.utils;

import git.str1llax.wnt.WorldNameTerrarified;
import git.str1llax.wnt.config.ModConfig;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class GuiInjector {
    private final int NAME_BUTTON_ID = ModConfig.genNameButtonID;

    private static Field worldName = null;
    private static Method calcSaveDirName = null;
    private static final WorldNameGenerator generator = new WorldNameGenerator();

    static {
        try {
            worldName = ObfuscationReflectionHelper.findField(GuiCreateWorld.class,"field_146333_g");
            worldName.setAccessible(true);

            calcSaveDirName = ObfuscationReflectionHelper.findMethod(GuiCreateWorld.class, "func_146314_g", void.class);
            calcSaveDirName.setAccessible(true);
        } catch (Exception e) {
            WorldNameTerrarified.Logger.log(Level.ERROR, "Error while deobfuscating private methods.", e);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onWorldCreationScreenOpen(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof GuiCreateWorld && ModConfig.enableMod) {
            int x = event.getGui().width / 2 + ModConfig.buttonX;
            int y = ModConfig.buttonY;

            GuiButton genRandomNameButton = new GuiButton(NAME_BUTTON_ID, x, y, 20, 20, "R");

            event.getButtonList().add(genRandomNameButton);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRandomNameButtonPressed(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.getGui() instanceof GuiCreateWorld) {
            if (event.getButton().id == NAME_BUTTON_ID) {
                GuiCreateWorld newGui = (GuiCreateWorld)event.getGui();
                try {
                    GuiTextField newName = (GuiTextField) worldName.get(newGui);
                    newName.setText(generator.generateRandomName());
                    calcSaveDirName.invoke(newGui);
                } catch (Exception e) {
                    WorldNameTerrarified.Logger.log(Level.ERROR, "Error when pressing a genName button", e);
                }
            }
        }
    }
}
