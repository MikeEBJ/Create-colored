package com.azasad.createcolored.mixin;

import com.azasad.createcolored.content.block.ColoredFluidTankBlock;
import com.azasad.createcolored.content.models.ColoredPartials;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.fluids.tank.FluidTankRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FluidTankRenderer.class)
public class FluidTankRendererMixin {
    @Unique
    private PartialModel partialModelToUse;

    @Inject(method = "renderAsBoiler", at = @At("HEAD"), cancellable = false)
    private void chooseGaugeColor(FluidTankBlockEntity be, float partialTicks, MatrixStack ms, VertexConsumerProvider buffer, int light, int overlay, CallbackInfo ci) {
        if (be.getCachedState().getBlock() instanceof ColoredFluidTankBlock colored) {
            DyeColor color = colored.getColor();
            partialModelToUse = ColoredPartials.COLORED_GAUGES.get(color);
        } else {
            partialModelToUse = AllPartialModels.BOILER_GAUGE;
        }
    }

    @Redirect(
            method = "renderAsBoiler",
            at = @At(value = "FIELD", target = "Lcom/simibubi/create/AllPartialModels;BOILER_GAUGE:Ldev/engine_room/flywheel/lib/model/baked/PartialModel;")

    )
    private PartialModel overrideRender() {
        return partialModelToUse;
    }
}