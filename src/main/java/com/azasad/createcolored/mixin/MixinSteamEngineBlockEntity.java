package com.azasad.createcolored.mixin;

import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SteamEngineBlockEntity.class)
public abstract class MixinSteamEngineBlockEntity {

    @Inject(method = "isValid", at = @At("HEAD"), cancellable = true)
    private void allowColoredTanks(CallbackInfoReturnable<Boolean> cir) {
        SteamEngineBlockEntity self = (SteamEngineBlockEntity) (Object) this;
        var level = self.getWorld();
        if (level == null) return;

        var dir = SteamEngineBlock.getFacing(self.getCachedState()).getOpposite();
        var state = level.getBlockState(self.getPos().offset(dir));

        if (state.getBlock() instanceof FluidTankBlock) {
            // Seems like a fault with create, we need to ensure to allow all types of fluid tanks.
            // Also enables creative tanks to work as a boiler!
            cir.setReturnValue(true);
        }
    }
}