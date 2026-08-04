package io.github.p1k0chu.bettervanillaf3.client;

import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;

//$ NullableImport
import org.jspecify.annotations.Nullable;

public interface SidedDebugScreenDisplayer extends DebugScreenDisplayer {
    /// sets the side for next invocations of add-* methods, until next setSide
    void bettervanillaf3$setSide(@Nullable Side side);

    /// Remembers the current line count so a separator can later be inserted
    /// only when an entry actually emits lines.
    void bettervanillaf3$markLineCount();

    void bettervanillaf3$addSeparatorBeforeNewLines();

    void bettervanillaf3$beginCapture(Capture capture);

    void bettervanillaf3$endCapture();

    void bettervanillaf3$appendPerformanceToLastLine();

    enum Capture {
        PERFORMANCE_IMPACTORS,
        GPU_UTILIZATION
    }

    enum Side {
        LEFT,
        RIGHT;
    }
}
