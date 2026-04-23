plugins {
    id("dev.kikugie.stonecutter")
    id("net.fabricmc.fabric-loom") apply false
	id("net.fabricmc.fabric-loom-remap") apply false
}

stonecutter parameters {
    dependencies["java"] = node.project.property("java_version") as String
}

stonecutter active "26.1"