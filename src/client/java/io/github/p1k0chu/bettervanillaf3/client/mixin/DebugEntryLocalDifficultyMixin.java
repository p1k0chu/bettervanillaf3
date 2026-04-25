package io.github.p1k0chu.bettervanillaf3.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.p1k0chu.bettervanillaf3.client.BetterVanillaF3Config;
import net.minecraft.client.gui.components.debug.DebugEntryLocalDifficulty;
import net.minecraft.server.level.ServerLevel;
//? >=1.21.11
import net.minecraft.world.timeline.Timelines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Locale;

@Mixin(DebugEntryLocalDifficulty.class)
class DebugEntryLocalDifficultyMixin {
    //? >=26.1 {
    @WrapOperation(method = "display", at = @At(value = "INVOKE", target = "Ljava/lang/String;format(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;"))
    private String addDayCounter(Locale l, String format, Object[] args, Operation<String> original, @Local /*? >=26.1 >>+')' */(name = "serverLevel") ServerLevel serverLevel) {
        if (BetterVanillaF3Config.getInstance().isAddDayCounterToLocalDifficulty()) {
            String s = original.call(l, format, args);
            var optionalTimeLine = serverLevel.registryAccess().get(Timelines.OVERWORLD_DAY);
            return optionalTimeLine.map(
                    timelineReference -> String.format("%s (Day #%d)", s, timelineReference.value().getPeriodCount(serverLevel.clockManager()))
            )
                    .orElse(s);
        }
        return original.call(l, format, args);
    }
    //? }
}
