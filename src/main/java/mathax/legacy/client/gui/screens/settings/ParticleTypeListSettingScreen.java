package mathax.legacy.client.gui.screens.settings;

import mathax.legacy.client.gui.GuiTheme;
import mathax.legacy.client.gui.widgets.WWidget;
import mathax.legacy.client.settings.Setting;
import mathax.legacy.client.utils.misc.Names;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class ParticleTypeListSettingScreen extends LeftRightListSettingScreen<ParticleType<?>> {
    public ParticleTypeListSettingScreen(GuiTheme theme, Setting<List<ParticleType<?>>> setting) {
        super(theme, "Select Particles", setting, setting.get(), Registry.PARTICLE_TYPE);
    }

    @Override
    protected WWidget getValueWidget(ParticleType<?> value) {
        return theme.label(getValueName(value));
    }

    @Override
    protected String getValueName(ParticleType<?> value) {
        return Names.get(value);
    }

    @Override
    protected boolean skipValue(ParticleType<?> value) {
        return !(value instanceof ParticleEffect);
    }
}
