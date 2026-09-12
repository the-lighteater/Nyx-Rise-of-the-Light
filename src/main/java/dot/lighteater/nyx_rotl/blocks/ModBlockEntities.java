package dot.lighteater.nyx_rotl.blocks;

import dot.lighteater.nyx_rotl.NyxROTL;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, NyxROTL.MODID);

    public static final RegistryObject<BlockEntityType<CrystalBlockEntity>> CRYSTAL =
            BLOCK_ENTITIES.register("crystal", () ->
                    BlockEntityType.Builder.of(CrystalBlockEntity::new,
                            ModBlocks.CRYSTAL.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
