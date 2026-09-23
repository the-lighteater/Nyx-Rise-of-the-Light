package dot.lighteater.nyx_rotl.enchantments;

import dot.lighteater.nyx_rotl.NyxROTL;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(
                    ForgeRegistries.ENCHANTMENTS,
                    NyxROTL.MODID
            );

    public static final RegistryObject<Enchantment> LUNAR_EDGE =
            ENCHANTMENTS.register(
                    "lunar_edge",
                    LunarEdge::new
            );

    public static final RegistryObject<Enchantment> LUNAR_SHIELD =
            ENCHANTMENTS.register(
                    "lunar_shield",
                    LunarShield::new
            );

    public static void register(IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
    }
}