package io.github.p1k0chu.nostalgic_f3.client;

import net.fabricmc.api.ClientModInitializer;

public class NostalgicF3Client implements ClientModInitializer {
    public static final String MOD_ID = "nostalgic-f3";

    @Override
    public void onInitializeClient() {
        NostalgicF3Config.HANDLER.load();
    }
}
