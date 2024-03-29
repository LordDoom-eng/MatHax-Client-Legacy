package mathax.legacy.client.utils.render;

import mathax.legacy.client.renderer.GL;
import mathax.legacy.client.renderer.PostProcessRenderer;
import mathax.legacy.client.renderer.Shader;
import mathax.legacy.client.systems.modules.render.ESP;
import mathax.legacy.client.mixin.WorldRendererAccessor;
import mathax.legacy.client.systems.modules.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;

import static mathax.legacy.client.utils.Utils.mc;

public class Outlines {
    public static boolean renderingOutlines;

    public static Framebuffer outlinesFbo;
    public static OutlineVertexConsumerProvider vertexConsumerProvider;
    private static Shader outlinesShader;

    public static void init() {
        outlinesShader = new Shader("outline.vert", "outline.frag");
        outlinesFbo = new SimpleFramebuffer(mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight(), false, false);
        vertexConsumerProvider = new OutlineVertexConsumerProvider(mc.getBufferBuilders().getEntityVertexConsumers());
    }

    public static void beginRender() {
        outlinesFbo.clear(MinecraftClient.IS_SYSTEM_MAC);
        mc.getFramebuffer().beginWrite(false);
    }

    public static void endRender() {
        WorldRenderer worldRenderer = mc.worldRenderer;
        WorldRendererAccessor wra = (WorldRendererAccessor) worldRenderer;

        Framebuffer fbo = worldRenderer.getEntityOutlinesFramebuffer();
        wra.setEntityOutlinesFramebuffer(outlinesFbo);
        vertexConsumerProvider.draw();
        wra.setEntityOutlinesFramebuffer(fbo);

        ESP esp = Modules.get().get(ESP.class);

        mc.getFramebuffer().beginWrite(false);

        GL.bindTexture(outlinesFbo.getColorAttachment());

        outlinesShader.bind();
        outlinesShader.set("u_Size", mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight());
        outlinesShader.set("u_Texture", 0);
        outlinesShader.set("u_Width", esp.outlineWidth.get());
        outlinesShader.set("u_FillOpacity", esp.fillOpacity.get().floatValue() / 255.0);
        outlinesShader.set("u_ShapeMode", esp.shapeMode.get().ordinal());
        PostProcessRenderer.render();
    }

    public static void onResized(int width, int height) {
        if (outlinesFbo != null) outlinesFbo.resize(width, height, false);
    }
}
