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
    private final int WORLD_NAME_LENGTH = ModConfig.worldNameLength;

    private static Field worldName = null;
    private static Field inMoreWorldSettings = null;
    private static Method calcSaveDirName = null;

    private static final WorldNameGenerator generator = new WorldNameGenerator();

    private GuiButton genRandomNameButton;

    static {
        try {
            worldName = ObfuscationReflectionHelper.findField(GuiCreateWorld.class,"field_146333_g");
            worldName.setAccessible(true);
            WorldNameTerrarified.Logger.log(Level.DEBUG, String.format("%s: Successfully deobfuscated worldName vanilla field from Minecraft.", WorldNameTerrarified.MOD_NAME));

            inMoreWorldSettings = ObfuscationReflectionHelper.findField(GuiCreateWorld.class, "field_146344_y");
            inMoreWorldSettings.setAccessible(true);
            WorldNameTerrarified.Logger.log(Level.DEBUG, String.format("%s: Successfully deobfuscated inMoreWorldOptionsDisplay vanilla field from Minecraft.", WorldNameTerrarified.MOD_NAME));

            calcSaveDirName = ObfuscationReflectionHelper.findMethod(GuiCreateWorld.class, "func_146314_g", void.class);
            calcSaveDirName.setAccessible(true);
            WorldNameTerrarified.Logger.log(Level.DEBUG, String.format("%s: Successfully deobfuscated calcSaveDir vanilla method from Minecraft.", WorldNameTerrarified.MOD_NAME));
        } catch (Exception e) {
            WorldNameTerrarified.Logger.log(Level.ERROR, String.format("%s: Error while deobfuscating private methods.", WorldNameTerrarified.MOD_NAME), e);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onWorldCreationScreenOpen(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof GuiCreateWorld && ModConfig.enableMod) {
            int x = event.getGui().width / 2 + ModConfig.buttonX;
            int y = ModConfig.buttonY;

            genRandomNameButton = new GuiButton(NAME_BUTTON_ID, x, y, 20, 20, "R");
            event.getButtonList().add(genRandomNameButton);

            WorldNameTerrarified.Logger.log(Level.DEBUG, String.format("%s: Successfully created new button.", WorldNameTerrarified.MOD_NAME));
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRandomNameButtonPressed(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.getGui() instanceof GuiCreateWorld) {
            GuiCreateWorld newGui = (GuiCreateWorld)event.getGui();
            if (event.getButton().id == NAME_BUTTON_ID) {
                try {
                    GuiTextField newName = (GuiTextField) worldName.get(newGui);
                    newName.setMaxStringLength(WORLD_NAME_LENGTH);
                    newName.setText(generator.generateRandomName());
                    calcSaveDirName.invoke(newGui);
                    WorldNameTerrarified.Logger.log(Level.DEBUG, String.format("%s: New random name generated.", WorldNameTerrarified.MOD_NAME));
                } catch (Exception e) {
                    WorldNameTerrarified.Logger.log(Level.ERROR, String.format("%s: Error when pressing a genName button.", WorldNameTerrarified.MOD_NAME), e);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onGuiKeyboardPressed(GuiScreenEvent.KeyboardInputEvent.Pre event) {
        if (event.getGui() instanceof GuiCreateWorld) {
            GuiCreateWorld newGui = (GuiCreateWorld) event.getGui();
            try {
                GuiTextField newName = (GuiTextField) worldName.get(newGui);
                newName.setMaxStringLength(WORLD_NAME_LENGTH);
            } catch (Exception e) {
                WorldNameTerrarified.Logger.log(Level.ERROR, String.format("%s: Couldn't extend max world name length.", WorldNameTerrarified.MOD_NAME), e);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Pre event) {
        if (event.getGui() instanceof GuiCreateWorld) {
            GuiCreateWorld guiCreateWorld = (GuiCreateWorld) event.getGui();
            try {
                boolean inMoreSettings = inMoreWorldSettings.getBoolean(guiCreateWorld);
                genRandomNameButton.visible = !inMoreSettings;
            } catch (Exception e) {
                WorldNameTerrarified.Logger.log(Level.ERROR, String.format("%s: Something went wrong when tried to hide button.", WorldNameTerrarified.MOD_NAME), e);
            }
        }
    }
}
