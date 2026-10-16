package dot.lighteater.nyx_rotl.compat.jade;

import dot.lighteater.nyx_rotl.Config;
import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.blocks.CrystalBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum JadeCrystalProvider
        implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    INSTANCE;

    private static final ResourceLocation UID =
            new ResourceLocation(NyxROTL.MODID, "crystal");

    @Override
    public void appendServerData(
            CompoundTag data,
            BlockAccessor accessor
    ) {
        if (!(accessor.getBlockEntity() instanceof CrystalBlockEntity crystal)) {
            NyxROTL.LOGGER.warn("[Jade] Crystal provider found no CrystalBlockEntity");
            return;
        }

        NyxROTL.LOGGER.info(
                "[Jade] Sending crystal durability: {}",
                crystal.durability
        );

        data.putInt("durability", crystal.durability);
    }

    @Override
    public void appendTooltip(
            ITooltip tooltip,
            BlockAccessor accessor,
            IPluginConfig config
    ) {
        CompoundTag data = accessor.getServerData();

        NyxROTL.LOGGER.info(
                "[Jade] Client received crystal data: {}",
                data
        );

        if (!data.contains("durability")) {
            return;
        }

        int durability = data.getInt("durability");
        int maxDurability = Config.crystalDurability.get();

        tooltip.add(
                Component.literal(
                        "Crystal Durability: "
                                + durability
                                + " / "
                                + maxDurability
                )
        );
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}