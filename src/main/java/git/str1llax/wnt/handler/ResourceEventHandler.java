package git.str1llax.wnt.handler;

import git.str1llax.wnt.config.ConfigData;
import git.str1llax.wnt.utils.WorldNameGenerator;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.resource.VanillaResourceType;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class ResourceEventHandler implements ISelectiveResourceReloadListener {
    public static final ResourceEventHandler INSTANCE = new ResourceEventHandler();

    private ResourceEventHandler() {}

    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager, @Nonnull Predicate<IResourceType> resourcePredicate) {
        if (ConfigData.useMcLocale.get() && resourcePredicate.test(VanillaResourceType.TEXTURES) || resourcePredicate.test(VanillaResourceType.LANGUAGES)) {
            WorldNameGenerator.reloadResources(resourceManager);
        }
    }
}
