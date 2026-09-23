package dot.lighteater.nyx_rotl.event;

import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.item.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public class ModItemProperties {

    public static void register() {

        ItemProperties.register(
                ModItems.METEOR_BOW.get(),
                new ResourceLocation("pulling"),
                (stack, level, entity, seed) -> entity != null
                        && entity.isUsingItem()
                        && entity.getUseItem() == stack
                        ? 1.0F
                        : 0.0F
        );

        ItemProperties.register(
                ModItems.METEOR_BOW.get(),
                new ResourceLocation("pull"),
                (stack, level, entity, seed) -> {

                    if (entity == null || !entity.isUsingItem()) {
                        return 0.0F;
                    }

                    int useTicks = stack.getUseDuration() - entity.getUseItemRemainingTicks();

                    float pull = useTicks / 30.0F;

                    if (pull > 1.0F) {
                        pull = 1.0F;
                    }

                    return pull;
                }
        );
    }
}