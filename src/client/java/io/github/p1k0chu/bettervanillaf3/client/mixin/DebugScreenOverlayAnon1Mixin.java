package io.github.p1k0chu.bettervanillaf3.client.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.p1k0chu.bettervanillaf3.client.SidedDebugScreenDisplayer;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.*;

//$ NullableImport
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;

@Mixin(targets = "net.minecraft.client.gui.components.DebugScreenOverlay$1")
abstract class DebugScreenOverlayAnon1Mixin implements SidedDebugScreenDisplayer {
    @Shadow
    @Final
    List<String> val$leftLines;
    @Shadow
    @Final
    List<String> val$rightLines;
    @Unique
    @Nullable
    private Side side = null;

    @WrapMethod(method = "addPriorityLine")
    private void addPriorityLine(String string, Operation<Void> original) {
        switch (this.side) {
            case LEFT -> val$leftLines.add(string);
            case RIGHT -> val$rightLines.add(string);
            case null, default -> original.call(string);
        }
    }

    @WrapMethod(method = "addLine")
    private void addLine(String string, Operation<Void> original) {
        switch (this.side) {
            case LEFT -> val$leftLines.add(string);
            case RIGHT -> val$rightLines.add(string);
            case null, default -> original.call(string);
        }
    }

    @WrapMethod(method = "addToGroup(Lnet/minecraft/resources/Identifier;Ljava/lang/String;)V")
    private void addToGroup(Identifier identifier, String string, Operation<Void> original) {
        switch (this.side) {
            case LEFT -> val$leftLines.add(string);
            case null, default -> original.call(identifier, string);
        }
    }

    @WrapMethod(method = "addToGroup(Lnet/minecraft/resources/Identifier;Ljava/util/Collection;)V")
    private void addToGroup(Identifier identifier, Collection<String> collection, Operation<Void> original) {
        switch (this.side) {
            case LEFT -> val$leftLines.addAll(collection);
            case null, default -> original.call(identifier, collection);
        }
    }

    @Override
    public void bettervanillaf3$setSide(@Nullable Side side) {
        this.side = side;
    }
}
