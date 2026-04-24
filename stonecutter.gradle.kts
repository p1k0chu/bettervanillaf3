plugins {
    id("dev.kikugie.stonecutter")
    id("net.fabricmc.fabric-loom") apply false
	id("net.fabricmc.fabric-loom-remap") apply false
}

stonecutter tasks {
    order("publishModrinth")
}

stonecutter parameters {
    dependencies["java"] = node.project.property("java_version") as String
    swaps["extractRenderStateStr"] = when {
        current.parsed < "26.1" -> "\"render\""
        else -> "\"extractRenderState\""
    }
    swaps["extractGuiStr"] = when {
        current.parsed < "26.1" -> "\"render\""
        else -> "\"extractGui\""
    }
    swaps["extractDebugOverlayStr"] = when {
        current.parsed < "26.1" -> "\"renderDebugOverlay\""
        else -> "\"extractDebugOverlay\""
    }
    swaps["NullableImport"] = when {
        current.parsed < "1.21.11" -> "import org.jetbrains.annotations.Nullable;"
        else -> "import org.jspecify.annotations.Nullable;"
    }

    replacements {
        string(current.parsed < "26.1") {
            replace("GuiGraphicsExtractor", "GuiGraphics")
        }
        string(current.parsed < "1.21.11") {
            replace("Identifier", "ResourceLocation")
        }
    }
}

stonecutter active "26.1"
