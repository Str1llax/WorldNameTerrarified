package git.str1llax.wnt.utils;

import git.str1llax.wnt.WorldNameTerrarified;
import git.str1llax.wnt.config.ModConfig;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiTextField;
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
            worldName = WNTUtilities.findField(GuiCreateWorld.class, "worldName","field_146330_J");
            WorldNameTerrarified.Logger.log(Level.DEBUG, String.format("%s: Successfully deobfuscated worldName vanilla field from Minecraft.", WorldNameTerrarified.MOD_NAME));

            worldNameField = WNTUtilities.findField(GuiCreateWorld.class, "worldNameField", "field_146333_g");
            WorldNameTerrarified.Logger.log(Level.DEBUG, String.format("%s: Successfully deobfuscated worldNameField vanilla field from Minecraft.", WorldNameTerrarified.MOD_NAME));

            inMoreWorldSettings = WNTUtilities.findField(GuiCreateWorld.class, "inMoreWorldOptionsDisplay","field_146344_y");
            WorldNameTerrarified.Logger.log(Level.DEBUG, String.format("%s: Successfully deobfuscated inMoreWorldOptionsDisplay vanilla field from Minecraft.", WorldNameTerrarified.MOD_NAME));

            calcSaveDirName = WNTUtilities.findMethod(GuiCreateWorld.class, "calcSaveDirName", "func_146314_g");
            WorldNameTerrarified.Logger.log(Level.DEBUG, String.format("%s: Successfully deobfuscated calcSaveDir vanilla method from Minecraft.", WorldNameTerrarified.MOD_NAME));
        } catch (Exception e) {
            WorldNameTerrarified.Logger.log(Level.ERROR, String.format("%s: Error while deobfuscating private methods.", WorldNameTerrarified.MOD_NAME), e);
        }
    }

    public static void insertRandomName(@Nonnull GuiCreateWorld guiCreateWorld) {
        try {
            String name = WorldNameGenerator.generateRandomName();
            worldName.set(guiCreateWorld, name);

            GuiTextField guiNameField = (GuiTextField) worldNameField.get(guiCreateWorld);
            guiNameField.setMaxStringLength(ModConfig.worldNameLength);
            guiNameField.setText(name);

            calcSaveDirName.invoke(guiCreateWorld);
            WorldNameTerrarified.Logger.log(Level.DEBUG, String.format("%s: New random name generated.", WorldNameTerrarified.MOD_NAME));
        } catch (Exception e) {
            WorldNameTerrarified.Logger.log(Level.ERROR, String.format("%s: Error when pressing a genName button.", WorldNameTerrarified.MOD_NAME), e);
        }
    }

}
