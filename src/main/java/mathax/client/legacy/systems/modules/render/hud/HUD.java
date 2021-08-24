package mathax.client.legacy.systems.modules.render.hud;

import mathax.client.legacy.MatHaxClientLegacy;
import mathax.client.legacy.events.render.Render2DEvent;
import mathax.client.legacy.gui.GuiTheme;
import mathax.client.legacy.gui.screens.HudElementScreen;
import mathax.client.legacy.gui.tabs.builtin.HudTab;
import mathax.client.legacy.gui.widgets.WWidget;
import mathax.client.legacy.gui.widgets.containers.WHorizontalList;
import mathax.client.legacy.gui.widgets.pressable.WButton;
import mathax.client.legacy.settings.*;
import mathax.client.legacy.systems.modules.render.hud.modules.*;
import mathax.client.legacy.systems.modules.Categories;
import mathax.client.legacy.systems.modules.Module;
import mathax.client.legacy.utils.render.AlignmentX;
import mathax.client.legacy.utils.render.AlignmentY;
import mathax.client.legacy.utils.render.color.SettingColor;
import mathax.client.legacy.bus.EventHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;

import java.util.ArrayList;
import java.util.List;

import static mathax.client.legacy.utils.Utils.mc;

public class HUD extends Module {
    private static final HudRenderer RENDERER = new HudRenderer();

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgEditor = settings.createGroup("Editor");

    // General

    public final Setting<Double> scale = sgGeneral.add(new DoubleSetting.Builder()
        .name("scale")
        .description("Scale of the HUD.")
        .defaultValue(1)
        .min(0.75)
        .sliderMin(0.75)
        .sliderMax(4)
        .build()
    );

    public final Setting<SettingColor> primaryColor = sgGeneral.add(new ColorSetting.Builder()
        .name("primary-color")
        .description("Primary color of text.")
        .defaultValue(new SettingColor(225, 75, 100))
        .build()
    );

    public final Setting<SettingColor> secondaryColor = sgGeneral.add(new ColorSetting.Builder()
        .name("secondary-color")
        .description("Secondary color of text.")
        .defaultValue(new SettingColor(175, 175, 175))
        .build()
    );

    // Editor

    public final Setting<Integer> snappingRange = sgEditor.add(new IntSetting.Builder()
        .name("snapping-range")
        .description("Snapping range in editor.")
        .defaultValue(6)
        .build()
    );

    // Improve vanilla HUD

    public final Setting<Boolean> mountHud = sgGeneral.add(new BoolSetting.Builder()
        .name("mount-hud")
        .description("Display xp bar and hunger when riding.")
        .defaultValue(true)
        .build()
    );

    public final List<HudElement> elements = new ArrayList<>();

    private final HudElementLayer topLeft, topCenter, topRight, bottomLeft, bottomCenter, bottomRight;

    public final Runnable reset = () -> {
        align();
        elements.forEach(element -> {
            element.active = element.defaultActive;
            element.settings.forEach(group -> group.forEach(Setting::reset));
        });
    };

    public HudElement get(RegistryKey<HudElement> key) {
        return null;
    }

    public HudElement get(Identifier id) {
        return null;
    }

    public HUD() {
        super(Categories.Render, "HUD", "In game overlay.");

        // Top Left
        topLeft = new HudElementLayer(RENDERER, elements, AlignmentX.Left, AlignmentY.Top, 2, 2);
        topLeft.add(new WatermarkHud(this));
        topLeft.add(new WelcomeHud(this));
        topLeft.add(new FpsHud(this));
        topLeft.add(new PingHud(this));
        topLeft.add(new TpsHud(this));
        topLeft.add(new SpeedHud(this));
        topLeft.add(new ServerHud(this));
        topLeft.add(new ServerBrandHud(this));
        topLeft.add(new BiomeHud(this));
        topLeft.add(new DurabilityHud(this));
        topLeft.add(new BreakingBlockHud(this));
        topLeft.add(new LookingAtHud(this));
        topLeft.add(new ModuleInfoHud(this));
        topLeft.add(new TextRadarHud(this));

        // Top Center
        topCenter = new HudElementLayer(RENDERER, elements, AlignmentX.Center, AlignmentY.Top, 0, 2);
        topCenter.add(new LagNotifierHud(this));

        // Top Right
        topRight = new HudElementLayer(RENDERER, elements, AlignmentX.Right, AlignmentY.Top, 2, 2);
        topRight.add(new InventoryViewerHud(this));
        topRight.add(new PotionTimersHud(this));
        topRight.add(new HoleHud(this));
        topRight.add(new CombatHud(this));


        // Bottom Left
        bottomLeft = new HudElementLayer(RENDERER, elements, AlignmentX.Left, AlignmentY.Bottom, 2, 2);
        bottomLeft.add(new RotationHud(this));
        bottomLeft.add(new PositionHud(this));

        // Bottom Center
        bottomCenter = new HudElementLayer(RENDERER, elements, AlignmentX.Center, AlignmentY.Bottom, 48, 64);
        bottomCenter.add(new DateHud(this));
        bottomCenter.add(new RealTimeHud(this));
        bottomCenter.add(new InGameTimeHud(this));
        bottomCenter.add(new PlayerModelHud(this));
        bottomCenter.add(new ArmorHud(this));
        bottomCenter.add(new CompassHud(this));
        bottomCenter.add(new ContainerViewerHud(this));
        bottomCenter.add(new TotemHud(this));

        // Bottom Right
        bottomRight = new HudElementLayer(RENDERER, elements, AlignmentX.Right, AlignmentY.Bottom, 2, 2);
        bottomRight.add(new ActiveModulesHud(this));

        align();
    }

    private void align() {
        RENDERER.begin(scale.get(), 0, true);

        topLeft.align();
        topCenter.align();
        topRight.align();
        bottomLeft.align();
        bottomCenter.align();
        bottomRight.align();

        RENDERER.end();
    }

    @EventHandler
    public void onRender(Render2DEvent event) {
        if (mc.options.debugEnabled || mc.options.hudHidden) return;

        RENDERER.begin(scale.get(), event.tickDelta, false);

        for (HudElement element : elements) {
            if (element.active || HudTab.INSTANCE.isScreen(mc.currentScreen) || mc.currentScreen instanceof HudElementScreen) {
                element.update(RENDERER);
                element.render(RENDERER);
            }
        }

        RENDERER.end();
    }

    @Override
    public WWidget getWidget(GuiTheme theme) {
        WHorizontalList list = theme.horizontalList();

        WButton reset = list.add(theme.button("Reset")).widget();
        reset.action = this.reset;
        list.add(theme.label("Resets positions (do this after changing scale)."));

        /*WButton editor = list.add(theme.button("Editor")).widget();
        editor.action =
        list.add(theme.label("Opens the editor of HUD modules."));*/

        return list;
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound tag = super.toTag();

        NbtList modulesTag = new NbtList();
        for (HudElement module : elements) modulesTag.add(module.toTag());
        tag.put("modules", modulesTag);

        return tag;
    }

    @Override
    public Module fromTag(NbtCompound tag) {
        if (tag.contains("modules")) {
            NbtList modulesTag = tag.getList("modules", 10);

            for (NbtElement t : modulesTag) {
                NbtCompound moduleTag = (NbtCompound) t;

                HudElement module = getModule(moduleTag.getString("name"));
                if (module != null) module.fromTag(moduleTag);
            }
        }

        return super.fromTag(tag);
    }

    private HudElement getModule(String name) {
        for (HudElement module : elements) {
            if (module.name.equals(name)) return module;
        }

        return null;
    }

    public boolean mountHud() {
        return isActive() && mountHud.get();
    }
}