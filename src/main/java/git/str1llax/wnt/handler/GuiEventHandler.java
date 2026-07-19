package git.str1llax.wnt.handler;

import git.str1llax.wnt.WorldNameTerrarified;
import git.str1llax.wnt.config.ConfigData;
import git.str1llax.wnt.utils.CustomButton;
import git.str1llax.wnt.utils.GuiInjector;
import git.str1llax.wnt.utils.ModKeybinds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.CreateWorldScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Level;

@Mod.EventBusSubscriber(modid = WorldNameTerrarified.MOD_ID, value = Dist.CLIENT)
public class GuiEventHandler {
    private static CustomButton genRandomNameButton;
    private static Screen lastScreenInstance = null;

    @SubscribeEvent
    public static void onWorldCreationScreenInit(GuiScreenEvent.InitGuiEvent.Post event) {
        Screen currentScreen = event.getGui();
        if (currentScreen instanceof CreateWorldScreen) {

            int x = currentScreen.width / 2 + ConfigData.buttonX.get();
            int y = ConfigData.buttonY.get();

            genRandomNameButton = ConfigData.useTexturedButton.get() ?
                    new CustomButton(
                            x, y,
                            ConfigData.buttonSize.get(), ConfigData.buttonSize.get(),
                            18, 18,
                            0, 0,
                            18,
                            new ResourceLocation(WorldNameTerrarified.MOD_ID, "textures/gui/buttons.png"),
                            "button.gen_random_name.tooltip", button -> {
                        GuiInjector.insertRandomName((CreateWorldScreen) currentScreen);
                    }) :
                    new CustomButton(
                            x, y,
                            ConfigData.buttonSize.get(), ConfigData.buttonSize.get(),
                            "R", "button.gen_random_name.tooltip", button -> {
                        GuiInjector.insertRandomName((CreateWorldScreen) currentScreen);
                    });

            event.addWidget(genRandomNameButton);

            if (currentScreen != lastScreenInstance) {
                if (ConfigData.startWithRandom.get()) {
                    GuiInjector.insertRandomName((CreateWorldScreen) currentScreen);
                }
                lastScreenInstance = currentScreen;
                WorldNameTerrarified.LOGGER.log(Level.DEBUG, String.format("%s: Successfully created new button.", WorldNameTerrarified.MOD_NAME));
            }
        }
    }

    @SubscribeEvent
    public static void onGrnKeyPressed(GuiScreenEvent.KeyboardKeyPressedEvent.Post event) {
        if (event.getGui() instanceof CreateWorldScreen && event.getKeyCode() == ModKeybinds.genRandomNameKey.getKey().getKeyCode() && genRandomNameButton.visible) {
            GuiInjector.insertRandomName((CreateWorldScreen) event.getGui());
            genRandomNameButton.playDownSound(Minecraft.getInstance().getSoundHandler());
        }
    }

    @SubscribeEvent
    public static void onGuiKeyboardPressed(GuiScreenEvent.KeyboardCharTypedEvent.Pre event) {
        if (event.getGui() instanceof CreateWorldScreen) {
            CreateWorldScreen newGui = (CreateWorldScreen) event.getGui();
            try {
                TextFieldWidget newName = (TextFieldWidget) GuiInjector.worldNameField.get(newGui);
                newName.setMaxStringLength(ConfigData.worldNameLength.get());
            } catch (Exception e) {
                WorldNameTerrarified.LOGGER.log(Level.ERROR, String.format("%s: Couldn't extend max world name length.", WorldNameTerrarified.MOD_NAME), e);
            }
        }
    }

    @SubscribeEvent
    public static void onDrawScreenPre(GuiScreenEvent.DrawScreenEvent.Pre event) {
        if (event.getGui() instanceof CreateWorldScreen) {
            CreateWorldScreen createWorldScreen = (CreateWorldScreen) event.getGui();
            try {
                boolean inMoreSettings = GuiInjector.inMoreWorldSettings.getBoolean(createWorldScreen);
                genRandomNameButton.visible = !inMoreSettings;
            } catch (Exception e) {
                WorldNameTerrarified.LOGGER.log(Level.ERROR, String.format("%s: Something went wrong when tried to hide button.", WorldNameTerrarified.MOD_NAME), e);
            }
        }
    }

    @SubscribeEvent
    public static void onDrawScreenPost(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (event.getGui() instanceof CreateWorldScreen) {
            CreateWorldScreen createWorldScreen = (CreateWorldScreen) event.getGui();
            if (ConfigData.enableButtonTooltip.get() && genRandomNameButton.isHovered()) {
                genRandomNameButton.drawTooltip(event.getMouseX(), event.getMouseY(), createWorldScreen);
            }
        }
    }
}
