package io.github.p1k0chu.bettervanillaf3.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.p1k0chu.bettervanillaf3.client.DebugScreenEntriesSides;
import io.github.p1k0chu.bettervanillaf3.client.SidedDebugScreenDisplayer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mixin(DebugScreenOverlay.class)
class DebugScreenOverlayMixin {
    @Inject(method = /*$ extractRenderStateStr >> ','*/"extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/debug/DebugScreenEntry;display(Lnet/minecraft/client/gui/components/debug/DebugScreenDisplayer;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/chunk/LevelChunk;Lnet/minecraft/world/level/chunk/LevelChunk;)V"))
    private void setSide(GuiGraphicsExtractor guiGraphics, CallbackInfo ci, @Local Identifier identifier, @Local DebugScreenDisplayer displayer) {
        ((SidedDebugScreenDisplayer) displayer).bettervanillaf3$setSide(DebugScreenEntriesSides.getSide(identifier));
    }

    @Redirect(method = /*$ extractRenderStateStr >> ','*/"extractRenderState", at = @At(value = "NEW", target = "(Ljava/util/Collection;)Ljava/util/ArrayList;"))
    private ArrayList<Collection<String>> fakeEmptyList4(Collection<String> c) {
        return new ArrayList<>();
    }

    @Inject(method = /*$ extractRenderStateStr >> ','*/"extractRenderState", at = @At(value = "NEW", target = "(Ljava/util/Collection;)Ljava/util/ArrayList;"))
    private void groupsGoToTheRight(GuiGraphicsExtractor guiGraphics, CallbackInfo ci, @Local Map<Identifier, Collection<String>> map, @Local(ordinal = 1) List<String> right) {
        map.forEach((ignore, strings) -> {
            right.addAll(strings);
            right.add("");
        });
    }
}
