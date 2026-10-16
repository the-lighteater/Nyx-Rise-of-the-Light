package dot.lighteater.nyx_rotl.compat.jade;

import dot.lighteater.nyx_rotl.NyxROTL;
import dot.lighteater.nyx_rotl.blocks.CrystalBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;

public class NyxJadePlugin implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        NyxROTL.LOGGER.info("[Jade] Registering Nyx Jade plugin");

        registration.registerBlockComponent(
                JadeCrystalProvider.INSTANCE,
                CrystalBlock.class
        );
    }
}