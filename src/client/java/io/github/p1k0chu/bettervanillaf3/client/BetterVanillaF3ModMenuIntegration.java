package io.github.p1k0chu.bettervanillaf3.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class BetterVanillaF3ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> BetterVanillaF3Config.getInstance().makeScreen(parent);
    }
}
