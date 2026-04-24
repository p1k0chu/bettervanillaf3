package io.github.p1k0chu.nostalgic_f3.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class NostalgicF3ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> NostalgicF3Config.getInstance().makeScreen(parent);
    }
}
