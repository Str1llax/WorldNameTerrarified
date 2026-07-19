package git.str1llax.wnt.utils;

import git.str1llax.wnt.WorldNameTerrarified;
import git.str1llax.wnt.config.ConfigData;
import net.minecraft.client.gui.screen.CreateWorldScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class GuiInjector {
    public static Field worldName = null;
    public static Field worldNameField = null;
    public static Field inMoreWorldSettings = null;
    public static Method calcSaveDirName = null;

    static {
        try {
            worldName = WNTUtilities.findField(CreateWorldScreen.class, "worldName","field_146330_J");
            WorldNameTerrarified.LOGGER.log(Level.DEBUG, String.format("%s: Successfully deobfuscated worldName vanilla field from Minecraft.", WorldNameTerrarified.MOD_NAME));

            worldNameField = WNTUtilities.findField(CreateWorldScreen.class, "worldNameField", "field_146333_g");
            WorldNameTerrarified.LOGGER.log(Level.DEBUG, String.format("%s: Successfully deobfuscated worldNameField vanilla field from Minecraft.", WorldNameTerrarified.MOD_NAME));

            inMoreWorldSettings = WNTUtilities.findField(CreateWorldScreen.class, "inMoreWorldOptionsDisplay","field_146344_y");
            WorldNameTerrarified.LOGGER.log(Level.DEBUG, String.format("%s: Successfully deobfuscated inMoreWorldOptionsDisplay vanilla field from Minecraft.", WorldNameTerrarified.MOD_NAME));

            calcSaveDirName = WNTUtilities.findMethod(CreateWorldScreen.class, "calcSaveDirName", "func_146314_g");
            WorldNameTerrarified.LOGGER.log(Level.DEBUG, String.format("%s: Successfully deobfuscated calcSaveDir vanilla method from Minecraft.", WorldNameTerrarified.MOD_NAME));
        } catch (Exception e) {
            WorldNameTerrarified.LOGGER.log(Level.ERROR, String.format("%s: Error while deobfuscating private methods.", WorldNameTerrarified.MOD_NAME), e);
        }
    }

    public static void insertRandomName(@Nonnull CreateWorldScreen createWorldScreen) {
        try {
            String name = WorldNameGenerator.generateRandomName();
            worldName.set(createWorldScreen, name);

            TextFieldWidget guiNameField = (TextFieldWidget) worldNameField.get(createWorldScreen);
            guiNameField.setMaxStringLength(ConfigData.worldNameLength.get());
            guiNameField.setText(name);

            calcSaveDirName.invoke(createWorldScreen);
            WorldNameTerrarified.LOGGER.log(Level.DEBUG, String.format("%s: New random name generated.", WorldNameTerrarified.MOD_NAME));
        } catch (Exception e) {
            WorldNameTerrarified.LOGGER.log(Level.ERROR, String.format("%s: Error when pressing a genName button.", WorldNameTerrarified.MOD_NAME), e);
        }
    }

}
