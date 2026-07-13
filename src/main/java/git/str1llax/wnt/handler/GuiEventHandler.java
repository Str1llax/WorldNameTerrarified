package git.str1llax.wnt.handler;

import git.str1llax.wnt.WorldNameTerrarified;
import git.str1llax.wnt.config.ModConfig;
import git.str1llax.wnt.utils.CustomButton;
import git.str1llax.wnt.utils.GuiInjector;
import git.str1llax.wnt.utils.ModKeybinds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Level;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber(modid = WorldNameTerrarified.MOD_ID, value = Side.CLIENT)
public class GuiEventHandler {
    private static CustomButton genRandomNameButton;
    private static GuiScreen lastScreenInstance = null;

    @SubscribeEvent
    public static void onWorldCreationScreenInit(GuiScreenEvent.InitGuiEvent.Post event) {
        GuiScreen currentScreen = event.getGui();
        if (currentScreen instanceof GuiCreateWorld) {

            int x = currentScreen.width / 2 + ModConfig.buttonX;
            int y = ModConfig.buttonY;

            genRandomNameButton = ModConfig.useTexturedButton ?
                    new CustomButton(
                            ModConfig.genNameButtonID,
                            x, y,
                            ModConfig.buttonSize, ModConfig.buttonSize,
                            18, 18,
                            0, 0,
                            18,
                            new ResourceLocation(WorldNameTerrarified.MOD_ID, "textures/gui/buttons.png"),
                            "button.gen_random_name.tooltip") :
                    new CustomButton(
                            ModConfig.genNameButtonID,
                            x, y,
                            ModConfig.buttonSize, ModConfig.buttonSize,
                            "R", "button.gen_random_name.tooltip");

            event.getButtonList().add(genRandomNameButton);

            if (currentScreen != lastScreenInstance) {
                if (ModConfig.startWithRandom) {
                    GuiInjector.insertRandomName((GuiCreateWorld) currentScreen);
                }
                lastScreenInstance = currentScreen;
                WorldNameTerrarified.Logger.log(Level.DEBUG, String.format("%s: Successfully created new button.", WorldNameTerrarified.MOD_NAME));
            }
        }
    }

    @SubscribeEvent
    public static void onRandomNameButtonPressed(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.getGui() instanceof GuiCreateWorld && event.getButton().id == ModConfig.genNameButtonID) {
            GuiInjector.insertRandomName((GuiCreateWorld) event.getGui());
        }
    }

    @SubscribeEvent
    public static void onGrnKeyPressed(GuiScreenEvent.KeyboardInputEvent.Post event) {
        if (event.getGui() instanceof GuiCreateWorld && Keyboard.getEventKey() == ModKeybinds.genRandomNameKey.getKeyCode() && Keyboard.getEventKeyState() && genRandomNameButton.visible) {
            GuiInjector.insertRandomName((GuiCreateWorld) event.getGui());
            genRandomNameButton.playPressSound(Minecraft.getMinecraft().getSoundHandler());
        }
    }

    @SubscribeEvent
    public static void onGuiKeyboardPressed(GuiScreenEvent.KeyboardInputEvent.Pre event) {
        if (event.getGui() instanceof GuiCreateWorld) {
            GuiCreateWorld newGui = (GuiCreateWorld) event.getGui();
            try {
                GuiTextField newName = (GuiTextField) GuiInjector.worldNameField.get(newGui);
                newName.setMaxStringLength(ModConfig.worldNameLength);
            } catch (Exception e) {
                WorldNameTerrarified.Logger.log(Level.ERROR, String.format("%s: Couldn't extend max world name length.", WorldNameTerrarified.MOD_NAME), e);
            }
        }
    }

    @SubscribeEvent
    public static void onDrawScreenPre(GuiScreenEvent.DrawScreenEvent.Pre event) {
        if (event.getGui() instanceof GuiCreateWorld) {
            GuiCreateWorld guiCreateWorld = (GuiCreateWorld) event.getGui();
            try {
                boolean inMoreSettings = GuiInjector.inMoreWorldSettings.getBoolean(guiCreateWorld);
                genRandomNameButton.visible = !inMoreSettings;
            } catch (Exception e) {
                WorldNameTerrarified.Logger.log(Level.ERROR, String.format("%s: Something went wrong when tried to hide button.", WorldNameTerrarified.MOD_NAME), e);
            }
        }
    }

    @SubscribeEvent
    public static void onDrawScreenPost(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (event.getGui() instanceof GuiCreateWorld) {
            GuiCreateWorld guiCreateWorld = (GuiCreateWorld) event.getGui();
            if (ModConfig.enableButtonTooltip && genRandomNameButton.isMouseOver()) {
                genRandomNameButton.drawTooltip(event.getMouseX(), event.getMouseY(), guiCreateWorld);
            }
        }
    }
}
