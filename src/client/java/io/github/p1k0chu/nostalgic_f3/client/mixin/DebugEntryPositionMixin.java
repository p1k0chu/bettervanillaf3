package io.github.p1k0chu.nostalgic_f3.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugEntryPosition;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

@Mixin(DebugEntryPosition.class)
public class DebugEntryPositionMixin {
    /// because I got rid of groups, I add newline manually here.
    /// groups are no more, because they are bad (opinionated, there's a group with just two lines)
    @Inject(method = "display", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/debug/DebugScreenDisplayer;addToGroup(Lnet/minecraft/resources/Identifier;Ljava/util/Collection;)V"))
    void addNewlineBefore(DebugScreenDisplayer debugScreenDisplayer, Level level, LevelChunk levelChunk, LevelChunk levelChunk2, CallbackInfo ci) {
        debugScreenDisplayer.addLine("");
    }

    @WrapOperation(method = "display", at = @At(value = "INVOKE", target = "Ljava/lang/String;format(Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", ordinal = 1))
    String addChunkRelativePos(Locale l, String format, Object[] args, Operation<String> original) {
        var entity = Minecraft.getInstance().getCameraEntity();
        if (entity != null) {
            var blockPos = entity.blockPosition();
            return String.format(
                    "%s [%d %d %d]",
                    original.call(l, format, args),
                    blockPos.getX() & 15,
                    blockPos.getY() & 15,
                    blockPos.getZ() & 15
            );
        }
        return original.call(l, format, args);
    }
}
