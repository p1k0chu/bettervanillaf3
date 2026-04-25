package io.github.p1k0chu.bettervanillaf3.client;

import net.fabricmc.api.ClientModInitializer;

public class BetterVanillaF3Client implements ClientModInitializer {
    public static final String MOD_ID = "bettervanillaf3";

    @Override
    public void onInitializeClient() {
        BetterVanillaF3Config.HANDLER.load();
    }
}
