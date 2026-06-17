package git.str1llax.wnt.utils;

import git.str1llax.wnt.WorldNameTerrarified;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.function.Predicate;

@SideOnly(Side.CLIENT)
public class WorldNameGenerator implements ISelectiveResourceReloadListener {
    private static String[] Compositions;
    private static String[] Adjectives;
    private static String[] Locations;
    private static String[] Nouns;
    private String currentLocale = null;

    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
        ISelectiveResourceReloadListener.super.onResourceManagerReload(resourceManager);
    }

    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        if (resourcePredicate.test(VanillaResourceType.TEXTURES) || resourcePredicate.test(VanillaResourceType.LANGUAGES)) {
            reloadResources(resourceManager);
        }
    }

    public final String generateRandomName() {
        Random random = new Random();

        return Compositions[random.nextInt(Compositions.length)]
                .replace("@", Adjectives[random.nextInt(Adjectives.length)])
                .replace("#", Locations[random.nextInt(Locations.length)])
                .replace("$", Nouns[random.nextInt(Nouns.length)]);
    }

    private String[] readFromFile(String fileName, IResourceManager resourceManager, String locale) throws IOException {
        BufferedReader reader;
        try {
            reader = new BufferedReader(
                new InputStreamReader(resourceManager.getResource(
                                new ResourceLocation(WorldNameTerrarified.MOD_ID, locale + "/" + fileName)).getInputStream(), StandardCharsets.UTF_8));

        } catch (Exception e) {
            WorldNameTerrarified.Logger.log(Level.WARN, String.format("%s: Specified locale not found. Using default (en_us) locale", WorldNameTerrarified.MOD_NAME), e);
            reader = new BufferedReader(
                    new InputStreamReader(resourceManager.getResource(
                            new ResourceLocation(WorldNameTerrarified.MOD_ID, "en_us/" + fileName)).getInputStream(), StandardCharsets.UTF_8));
        }
        WorldNameTerrarified.Logger.log(Level.DEBUG, String.format("%s: Reloaded %s with locale %s", WorldNameTerrarified.MOD_NAME, fileName, locale));
        return reader.lines().toArray(String[]::new);
    }

    private void reloadResources(IResourceManager resourceManager) {
        currentLocale = MinecraftForgeClient.getLocale().toString();
        try {
            Compositions = readFromFile("compositions.txt", resourceManager, currentLocale);
            Adjectives = readFromFile("adjectives.txt", resourceManager, currentLocale);
            Locations = readFromFile("locations.txt", resourceManager, currentLocale);
            Nouns = readFromFile("nouns.txt", resourceManager, currentLocale);
        } catch (Exception e) {
            WorldNameTerrarified.Logger.log(Level.ERROR, String.format("%s: Error while loading localization files.", WorldNameTerrarified.MOD_NAME), e);
        }
    }
}
