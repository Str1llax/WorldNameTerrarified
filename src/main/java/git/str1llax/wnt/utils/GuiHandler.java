package git.str1llax.wnt.utils;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class GuiHandler {
    private final int NAME_BUTTON_ID = 99;
    private static Field worldName = null;
    private static Field saveDirName = null;
    private static Method calcSaveDirName = null;
    private static final WorldNameGenerator generator = new WorldNameGenerator();

    static {
        try {
            worldName = ObfuscationReflectionHelper.findField(GuiCreateWorld.class,"field_146333_g");
            worldName.setAccessible(true);

            saveDirName = ObfuscationReflectionHelper.findField(GuiCreateWorld.class, "field_146336_i");
            saveDirName.setAccessible(true);

            calcSaveDirName = ObfuscationReflectionHelper.findMethod(GuiCreateWorld.class, "func_146314_g", void.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onWorldCreationScreenOpen(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof GuiCreateWorld) {
            int x = event.getGui().width / 2 - 140;
            int y = 60;

            GuiButton genRandomNameButton = new GuiButton(NAME_BUTTON_ID, x, y, 20, 20, "G");

            event.getButtonList().add(genRandomNameButton);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRandomNameButtonPressed(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (event.getGui() instanceof GuiCreateWorld) {
            if (event.getButton().id == NAME_BUTTON_ID) {
                GuiCreateWorld newGui = (GuiCreateWorld)event.getGui();
                try {
                    GuiTextField newName = (GuiTextField) worldName.get(newGui);
                    newName.setText(generator.generateRandomName());
                    String newDirName = (String) saveDirName.get(newGui);
                    newDirName = newName.getText();
                    calcSaveDirName.invoke(newGui);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                event.setCanceled(true);
            }
        }
    }
}
