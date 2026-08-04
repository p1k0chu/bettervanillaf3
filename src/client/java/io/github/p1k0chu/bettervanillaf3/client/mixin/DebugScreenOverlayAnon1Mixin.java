package io.github.p1k0chu.bettervanillaf3.client.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.p1k0chu.bettervanillaf3.client.SidedDebugScreenDisplayer;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.*;

//$ NullableImport
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
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
    @Unique
    @Nullable
    private Capture capture = null;
    @Unique
    private final List<String> performanceImpactors = new ArrayList<>();
    @Unique
    private final List<String> gpuUtilization = new ArrayList<>();

    @WrapMethod(method = "addPriorityLine")
    private void addPriorityLine(String string, Operation<Void> original) {
        if (capture(string)) return;
        switch (this.side) {
            case LEFT -> val$leftLines.add(string);
            case RIGHT -> val$rightLines.add(string);
            case null, default -> original.call(string);
        }
    }

    @WrapMethod(method = "addLine")
    private void addLine(String string, Operation<Void> original) {
        if (capture(string)) return;
        switch (this.side) {
            case LEFT -> val$leftLines.add(string);
            case RIGHT -> val$rightLines.add(string);
            case null, default -> original.call(string);
        }
    }

    @WrapMethod(method = "addToGroup(Lnet/minecraft/resources/Identifier;Ljava/lang/String;)V")
    private void addToGroup(Identifier identifier, String string, Operation<Void> original) {
        if (capture(string)) return;
        switch (this.side) {
            case LEFT -> val$leftLines.add(string);
            case RIGHT -> val$rightLines.add(string);
            case null, default -> original.call(identifier, string);
        }
    }

    @WrapMethod(method = "addToGroup(Lnet/minecraft/resources/Identifier;Ljava/util/Collection;)V")
    private void addToGroup(Identifier identifier, Collection<String> collection, Operation<Void> original) {
        if (capture != null) {
            collection.forEach(this::capture);
            return;
        }
        switch (this.side) {
            case LEFT -> val$leftLines.addAll(collection);
            case RIGHT -> val$rightLines.addAll(collection);
            case null, default -> original.call(identifier, collection);
        }
    }

    @Override
    public void bettervanillaf3$setSide(@Nullable Side side) {
        this.side = side;
    }

    @Override
    public void bettervanillaf3$beginCapture(Capture capture) {
        this.capture = capture;
        switch (capture) {
            case PERFORMANCE_IMPACTORS -> this.performanceImpactors.clear();
            case GPU_UTILIZATION -> this.gpuUtilization.clear();
        }
    }

    @Override
    public void bettervanillaf3$endCapture() {
        this.capture = null;
    }

    @Override
    public void bettervanillaf3$appendPerformanceToLastLine() {
        if (val$leftLines.isEmpty()) return;

        List<String> additions = new ArrayList<>();
        additions.addAll(this.performanceImpactors);
        additions.addAll(this.gpuUtilization);
        if (additions.isEmpty()) return;

        int last = val$leftLines.size() - 1;
        val$leftLines.set(last, val$leftLines.get(last) + " " + String.join(" ", additions));
    }

    @Unique
    private boolean capture(String string) {
        if (this.capture == null) return false;
        switch (this.capture) {
            case PERFORMANCE_IMPACTORS -> this.performanceImpactors.add(
                    this.performanceImpactors.isEmpty() ? string.stripLeading() : string
            );
            case GPU_UTILIZATION -> this.gpuUtilization.add(string);
        }
        return true;
    }
}
