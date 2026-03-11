package io.github.p1k0chu.nostalgic_f3.client;

import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import org.jspecify.annotations.Nullable;

public interface SidedDebugScreenDisplayer extends DebugScreenDisplayer {
    /// sets the side for next invocations of add-* methods, until next setSide
    void nostalgic_f3$setSide(@Nullable Side side);

    enum Side {
        LEFT,
        RIGHT;
    }
}
