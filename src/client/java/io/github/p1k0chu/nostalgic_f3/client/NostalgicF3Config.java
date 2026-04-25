package io.github.p1k0chu.nostalgic_f3.client;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
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

    @SerialEntry
    private boolean hideOverlayWhenF1 = false;

    @SerialEntry
    private float pieChartScale = 1.0f;

    @SerialEntry
    private boolean disabledPieChartBg = false;

    //? >=26.1 {
    @SerialEntry
    private boolean addDayCounterToLocalDifficulty = false;
    //? }

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
                                .option(
                                        Option.<Boolean>createBuilder()
                                                .name(Component.literal("Hide overlay when F1"))
                                                .description(OptionDescription.of(Component.literal("When HUD is hidden and set to True, hides the overlay even when a menu is open.")))
                                                .binding(
                                                        false,
                                                        this::isHideOverlayWhenF1,
                                                        this::setHideOverlayWhenF1
                                                )
                                                .controller(TickBoxControllerBuilder::create)
                                                .build()
                                )
                                //? >=26.1 {
                                .option(
                                        Option.<Boolean>createBuilder()
                                                .name(Component.literal("Day counter"))
                                                .description(OptionDescription.of(Component.literal("Add day counter to local difficulty line")))
                                                .binding(
                                                        false,
                                                        this::isAddDayCounterToLocalDifficulty,
                                                        this::setAddDayCounterToLocalDifficulty
                                                )
                                                .controller(TickBoxControllerBuilder::create)
                                                .build()
                                )
                                //? }
                                .group(
                                        OptionGroup.createBuilder()
                                                .name(Component.literal("Pie Chart"))
                                                .option(
                                                        Option.<Float>createBuilder()
                                                                .name(Component.literal("Debug Pie Chart scale"))
                                                                .description(OptionDescription.of(Component.literal("Change the scale of the Pie Chart in Debug Overlay")))
                                                                .binding(
                                                                        1.0f,
                                                                        this::getPieChartScale,
                                                                        this::setPieChartScale
                                                                )
                                                                .controller(
                                                                        option -> FloatSliderControllerBuilder.create(option)
                                                                                .range(0.1f, 2.0f)
                                                                                .step(0.1f)
                                                                )
                                                                .build()
                                                )
                                                .option(
                                                        Option.<Boolean>createBuilder()
                                                                .name(Component.literal("Disable Pie Chart background"))
                                                                .description(OptionDescription.of(Component.literal("Disable the semi-transparent gray background of the profiler pie chart")))
                                                                .binding(
                                                                        false,
                                                                        this::isDisabledPieChartBg,
                                                                        this::setDisabledPieChartBg
                                                                )
                                                                .controller(TickBoxControllerBuilder::create)
                                                                .build()
                                                )
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

    public boolean isHideOverlayWhenF1() {
        return hideOverlayWhenF1;
    }

    public void setHideOverlayWhenF1(boolean hideOverlayWhenF1) {
        this.hideOverlayWhenF1 = hideOverlayWhenF1;
    }

    public float getPieChartScale() {
        return pieChartScale;
    }

    public void setPieChartScale(float pieChartScale) {
        this.pieChartScale = pieChartScale;
    }

    public boolean isDisabledPieChartBg() {
        return disabledPieChartBg;
    }

    public void setDisabledPieChartBg(boolean disabledPieChartBg) {
        this.disabledPieChartBg = disabledPieChartBg;
    }

    //? >=26.1 {
    public boolean isAddDayCounterToLocalDifficulty() {
        return addDayCounterToLocalDifficulty;
    }

    public void setAddDayCounterToLocalDifficulty(boolean addDayCounterToLocalDifficulty) {
        this.addDayCounterToLocalDifficulty = addDayCounterToLocalDifficulty;
    }
    //? }
}
