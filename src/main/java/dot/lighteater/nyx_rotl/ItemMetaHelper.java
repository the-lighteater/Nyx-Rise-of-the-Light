package dot.lighteater.nyx_rotl;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ItemMetaHelper {

    private static final Logger LOGGER = NyxROTL.LOGGER;

    public static Set<ItemStack> getFromString(String debugString, String s) {

        if (s == null || s.isEmpty()) {
            LOGGER.warn("Empty {}", debugString);
            return Set.of(ItemStack.EMPTY);
        }

        // ------------------------------------------------------------
        // TAG HANDLING (#minecraft:wool)
        // ------------------------------------------------------------
        if (s.startsWith("#")) {
            ResourceLocation tagId = ResourceLocation.tryParse(s.substring(1));

            if (tagId == null) {
                LOGGER.warn("Invalid tag {} '{}'", debugString, s);
                return Set.of(ItemStack.EMPTY);
            }

            TagKey<Item> tag = TagKey.create(Registries.ITEM, tagId);

            var items = StreamSupport.stream(
                    BuiltInRegistries.ITEM.getTagOrEmpty(tag).spliterator(),
                    false
            ).map(ItemStack::new).collect(Collectors.toSet());

            if (items.isEmpty()) {
                LOGGER.warn("Empty tag {} '{}'", debugString, s);
                return Set.of(ItemStack.EMPTY);
            }

            return items;
        }

        // ------------------------------------------------------------
        // SINGLE ITEM HANDLING (minecraft:stone)
        // ------------------------------------------------------------
        ResourceLocation id = ResourceLocation.tryParse(s);

        if (id == null) {
            LOGGER.warn("Invalid item {} '{}'", debugString, s);
            return Set.of(ItemStack.EMPTY);
        }

        Item item = BuiltInRegistries.ITEM.get(id);

        if (item == null || item == ItemStack.EMPTY.getItem()) {
            LOGGER.warn("Unknown item {} '{}'", debugString, s);
            return Set.of(ItemStack.EMPTY);
        }

        return Set.of(new ItemStack(item));
    }

    public static Set<ItemStack> getFromStringArray(String debugString, String[] a) {
        return Arrays.stream(a)
                .flatMap(s -> getFromString(debugString, s).stream())
                .collect(Collectors.toSet());
    }

    public static Set<ItemStack> getFromStringCollection(String debugString, Collection<String> c) {
        return getFromStringArray(debugString, c.toArray(new String[0]));
    }
}