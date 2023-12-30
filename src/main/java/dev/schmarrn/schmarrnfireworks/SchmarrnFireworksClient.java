package dev.schmarrn.schmarrnfireworks;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.FireworkEntityRenderer;

public class SchmarrnFireworksClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(SchmarrnFireworks.ENTITY_TYPE, FireworkEntityRenderer::new);
    }
}
