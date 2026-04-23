package io.github.p1k0chu.nostalgic_f3.client;

import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.List;

public final class DebugScreenEntriesSides {
    private DebugScreenEntriesSides() {
    }

    private static final List<Identifier> leftSide = List.of(
            Identifier.withDefaultNamespace("game_version"),
            Identifier.withDefaultNamespace("fps"),
            Identifier.withDefaultNamespace("tps"), // integrated server...
            Identifier.withDefaultNamespace("chunk_render_stats"), // c counter
            Identifier.withDefaultNamespace("entity_render_stats"), // e counter
            Identifier.withDefaultNamespace("particle_render_stats"), // p counter
            Identifier.withDefaultNamespace("chunk_source_stats"), // chunks[c] and chunks[s]

            Identifier.withDefaultNamespace("player_position"),
            Identifier.withDefaultNamespace("light_levels"),
            Identifier.withDefaultNamespace("heightmap"), // CH + SH
            Identifier.withDefaultNamespace("biome"),
            Identifier.withDefaultNamespace("local_difficulty"),
            Identifier.withDefaultNamespace("day_count"),
            Identifier.withDefaultNamespace("chunk_generation_stats"), // noise router + biome builder
            Identifier.withDefaultNamespace("entity_spawn_counts"), // SC
            Identifier.withDefaultNamespace("sound_mood"),
            Identifier.withDefaultNamespace("post_effect"),
            Identifier.withDefaultNamespace("player_section_position")
    );

    private static final List<Identifier> rightSide = List.of(
            Identifier.withDefaultNamespace("memory"),
            Identifier.withDefaultNamespace("system_specs"),
            Identifier.withDefaultNamespace("gpu_utilization"),
            Identifier.withDefaultNamespace("simple_performance_impactors"),

            Identifier.withDefaultNamespace("looking_at_block"),
            Identifier.withDefaultNamespace("looking_at_fluid"),
            Identifier.withDefaultNamespace("looking_at_entity")
    );

    public static int compare(Identifier left, Identifier right) {
        int res = leftSide.indexOf(left) - leftSide.indexOf(right);
        if (res != 0) return res;

        return rightSide.indexOf(left) - rightSide.indexOf(right);
    }

    public static SidedDebugScreenDisplayer.@Nullable Side getSide(Identifier id) {
        if (leftSide.contains(id)) return SidedDebugScreenDisplayer.Side.LEFT;
        if (rightSide.contains(id)) return SidedDebugScreenDisplayer.Side.RIGHT;
        return null;
    }
}
