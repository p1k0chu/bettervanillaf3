package io.github.p1k0chu.nostalgic_f3.client;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

//$ NullableImport
import org.jspecify.annotations.Nullable;

public class NostalgicF3Config {
    public static final ConfigClassHandler<NostalgicF3Config> HANDLER = ConfigClassHandler.createBuilder(NostalgicF3Config.class)
            .id(Identifier.fromNamespaceAndPath(NostalgicF3Client.MOD_ID, "config"))
            .serializer(
                    config -> GsonConfigSerializerBuilder.create(config)
                            .setPath(FabricLoader.getInstance().getConfigDir().resolve("nostalgic_f3_config.json5"))
                            .setJson5(true)
                            .build()
            )
            .build();

    @SerialEntry
    private boolean useOldSectionRelativePos = true;

    public Screen makeScreen(@Nullable Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Nostalgic f3 config"))
                .category(
                        ConfigCategory.createBuilder()
                                .name(Component.literal("Config"))
                                .tooltip(Component.literal("All the mod's config"))
                                .option(
                                        Option.<Boolean>createBuilder()
                                                .name(Component.literal("Old section-related position"))
                                                .description(
                                                        OptionDescription.of(
                                                                Component.literal("Adds section-related position to player's block position. e.g.:"),
                                                                Component.literal("Block: 1 2 3 [1 2 3]")
                                                                        .withStyle(style -> style.withBold(true))
                                                        )
                                                )
                                                .binding(
                                                        true,
                                                        this::isUseOldSectionRelativePos,
                                                        this::setUseOldSectionRelativePos
                                                )
                                                .controller(TickBoxControllerBuilder::create)
                                                .build()
                                )
                                .build()
                )
                .save(HANDLER::save)
                .build()
                .generateScreen(parent);
    }

    public boolean isUseOldSectionRelativePos() {
        return useOldSectionRelativePos;
    }

    public void setUseOldSectionRelativePos(boolean useOldSectionRelativePos) {
        this.useOldSectionRelativePos = useOldSectionRelativePos;
    }

    public static NostalgicF3Config getInstance() {
        return HANDLER.instance();
    }
}
