package git.str1llax.wnt.utils;

import git.str1llax.wnt.WorldNameTerrarified;
import git.str1llax.wnt.config.ConfigData;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class WorldNameGenerator  {
    private static String[] Compositions = null;
    private static String[] Adjectives = null;
    private static String[] Locations = null;
    private static String[] Nouns = null;

    public static String generateRandomName() {
        Random random = new Random();

        if (Compositions == null ||  Adjectives == null || Locations == null || Nouns == null) {
            reloadResources(Minecraft.getInstance().getResourceManager());
        }

        return Compositions[random.nextInt(Compositions.length)]
                .replace("@", Adjectives[random.nextInt(Adjectives.length)])
                .replace("#", Locations[random.nextInt(Locations.length)])
                .replace("$", Nouns[random.nextInt(Nouns.length)]);
    }

    private static String[] readFromFile(String fileName, IResourceManager resourceManager, String locale) throws IOException {
        BufferedReader reader;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(resourceManager.getResource(
                            new ResourceLocation(WorldNameTerrarified.MOD_ID, "compositions/" + locale + "/" + fileName)).getInputStream(), StandardCharsets.UTF_8));

        } catch (Exception e) {
            WorldNameTerrarified.LOGGER.log(Level.WARN, String.format("%s: Specified locale not found. Using default (en_us) locale", WorldNameTerrarified.MOD_NAME), e);
            reader = new BufferedReader(
                    new InputStreamReader(resourceManager.getResource(
                            new ResourceLocation(WorldNameTerrarified.MOD_ID, "compositions/en_us/" + fileName)).getInputStream(), StandardCharsets.UTF_8));
        }
        WorldNameTerrarified.LOGGER.log(Level.DEBUG, String.format("%s: Reloaded %s with locale %s", WorldNameTerrarified.MOD_NAME, fileName, locale));
        return reader.lines().toArray(String[]::new);
    }

    public static void reloadResources(IResourceManager resourceManager) {
        String currentLocale = ConfigData.useMcLocale.get() ? Minecraft.getInstance().getLanguageManager().getCurrentLanguage().getCode().toLowerCase() : ConfigData.localeCode.get().toLowerCase();
        try {
            Compositions = readFromFile("compositions.txt", resourceManager, currentLocale);
            Adjectives = readFromFile("adjectives.txt", resourceManager, currentLocale);
            Locations = readFromFile("locations.txt", resourceManager, currentLocale);
            Nouns = readFromFile("nouns.txt", resourceManager, currentLocale);
        } catch (Exception e) {
            WorldNameTerrarified.LOGGER.log(Level.ERROR, String.format("%s: Error while loading localization files.", WorldNameTerrarified.MOD_NAME), e);
        }
    }
}
