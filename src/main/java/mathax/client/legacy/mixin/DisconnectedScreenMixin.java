package mathax.client.legacy.mixin;

import mathax.client.legacy.systems.modules.Modules;
import mathax.client.legacy.systems.modules.misc.AutoReconnect;
import mathax.client.legacy.utils.misc.LastServerInfo;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DisconnectedScreen.class)
public abstract class DisconnectedScreenMixin extends Screen {

    @Shadow
    @Final
    private Screen parent;

    @Shadow private int reasonHeight;

    @Unique private ButtonWidget autoReconnectBtn;
    @Unique private double time = Modules.get().get(AutoReconnect.class).time.get() * 20;

    protected DisconnectedScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onRenderBackground(CallbackInfo info) {
        int x = width / 2 - 100;
        int y = Math.min((height / 2 + reasonHeight / 2) + 32, height - 30);

        addDrawableChild(new ButtonWidget(x, y, 200, 20, new LiteralText("Reconnect"), b -> {
            LastServerInfo.reconnect(parent);
        }));

        if (LastServerInfo.getLastServer() != null) {
            int x2 = width / 2 - 100;
            int y2 = Math.min((height / 2 + reasonHeight / 2) + 56, height - 30);

            if (Modules.get().isActive(AutoReconnect.class)) {
                autoReconnectBtn =
                    addDrawableChild(new ButtonWidget(x2, y2, 200, 20, new LiteralText(getText()), button -> {
                        LastServerInfo.reconnect(parent);
                    })
                );
            }
        }
    }

    @Override
    public void tick() {
        AutoReconnect autoReconnect = Modules.get().get(AutoReconnect.class);
        if (!autoReconnect.isActive() || autoReconnect.lastServerInfo == null) return;

        if (time <= 0) {
            LastServerInfo.reconnect(parent);
        } else {
            time--;
            if (autoReconnectBtn != null) ((AbstractButtonWidgetAccessor) autoReconnectBtn).setText(new LiteralText(getText()));
        }
    }

    private String getText() {
        String autoReconnectText = "Reconnect in " + String.format("%.1f" + "s", time / 20);
        if (Modules.get().isActive(AutoReconnect.class)) autoReconnectText = "Reconnecting in " + String.format("%.1f" + "s...", time / 20);
        return autoReconnectText;
    }
}