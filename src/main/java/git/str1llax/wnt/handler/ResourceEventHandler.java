package git.str1llax.wnt.handler;

import git.str1llax.wnt.WorldNameTerrarified;
import git.str1llax.wnt.utils.WorldNameGenerator;
import net.minecraft.client.resources.IResourceManager;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = WorldNameTerrarified.MOD_ID, value = Side.CLIENT)
public class ResourceEventHandler implements ISelectiveResourceReloadListener {

    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        if (resourcePredicate.test(VanillaResourceType.TEXTURES) || resourcePredicate.test(VanillaResourceType.LANGUAGES)) {
            WorldNameGenerator.reloadResources(resourceManager);
        }
    }
}
