package git.str1llax.wnt.utils;

import git.str1llax.wnt.WorldNameTerrarified;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.function.Predicate;

@SideOnly(Side.CLIENT)
public class WorldNameGenerator implements ISelectiveResourceReloadListener {
    private static String[] Compositions;
    private static String[] Adjectives;
    private static String[] Locations;
    private static String[] Nouns;

    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
        ISelectiveResourceReloadListener.super.onResourceManagerReload(resourceManager);
    }

    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        if (resourcePredicate.test(VanillaResourceType.TEXTURES)) {
            try {
                Compositions = readFromFile("compositions.txt", resourceManager);
                Adjectives = readFromFile("adjectives.txt", resourceManager);
                Locations = readFromFile("locations.txt", resourceManager);
                Nouns = readFromFile("nouns.txt", resourceManager);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public final String generateRandomName() {
        Random random = new Random();

        return Compositions[random.nextInt(Compositions.length)]
                .replace("@", Adjectives[random.nextInt(Adjectives.length)])
                .replace("#", Locations[random.nextInt(Locations.length)])
                .replace("$", Nouns[random.nextInt(Nouns.length)]);
    }

    private static String[] readFromFile(String fileName, IResourceManager resourceManager) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(resourceManager.getResource(
                                new ResourceLocation(WorldNameTerrarified.MOD_ID, "texts/" + fileName)).getInputStream()));

        return reader.lines().toArray(String[]::new);
    }
}
