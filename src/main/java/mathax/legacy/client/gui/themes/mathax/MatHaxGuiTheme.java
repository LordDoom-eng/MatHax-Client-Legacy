package mathax.legacy.client.gui.themes.mathax;

import mathax.legacy.client.gui.DefaultSettingsWidgetFactory;
import mathax.legacy.client.gui.GuiTheme;
import mathax.legacy.client.gui.WidgetScreen;
import mathax.legacy.client.gui.renderer.packer.GuiTexture;
import mathax.legacy.client.gui.themes.mathax.widgets.input.WMatHaxDropdown;
import mathax.legacy.client.gui.themes.mathax.widgets.input.WMatHaxSlider;
import mathax.legacy.client.gui.themes.mathax.widgets.input.WMatHaxTextBox;
import mathax.legacy.client.gui.utils.AlignmentX;
import mathax.legacy.client.gui.utils.CharFilter;
import mathax.legacy.client.gui.widgets.containers.WSection;
import mathax.legacy.client.gui.widgets.containers.WView;
import mathax.legacy.client.gui.widgets.containers.WWindow;
import mathax.legacy.client.gui.widgets.input.WDropdown;
import mathax.legacy.client.gui.widgets.input.WSlider;
import mathax.legacy.client.gui.widgets.input.WTextBox;
import mathax.legacy.client.renderer.text.TextRenderer;
import mathax.legacy.client.systems.accounts.Account;
import mathax.legacy.client.systems.modules.Module;
import mathax.legacy.client.utils.render.color.Color;
import mathax.legacy.client.utils.render.color.SettingColor;
import mathax.legacy.client.utils.Utils;
import mathax.legacy.client.gui.themes.mathax.widgets.*;
import mathax.legacy.client.gui.themes.mathax.widgets.pressable.*;
import mathax.legacy.client.gui.widgets.*;
import mathax.legacy.client.gui.widgets.pressable.*;
import mathax.legacy.client.settings.*;

public class MatHaxGuiTheme extends GuiTheme {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgColors = settings.createGroup("Colors");
    private final SettingGroup sgTextColors = settings.createGroup("Text");
    private final SettingGroup sgBackgroundColors = settings.createGroup("Background");
    private final SettingGroup sgOutline = settings.createGroup("Outline");
    private final SettingGroup sgSeparator = settings.createGroup("Separator");
    private final SettingGroup sgScrollbar = settings.createGroup("Scrollbar");
    private final SettingGroup sgSlider = settings.createGroup("Slider");

    // General

    public final Setting<Double> scale = sgGeneral.add(new DoubleSetting.Builder()
            .name("scale")
            .description("Scale of the GUI.")
            .defaultValue(1)
            .min(0.75)
            .sliderMin(0.75)
            .sliderMax(4)
            .onSliderRelease()
            .onChanged(aDouble -> {
                if (Utils.mc.currentScreen instanceof WidgetScreen) ((WidgetScreen) Utils.mc.currentScreen).invalidate();
            })
            .build()
    );

    public final Setting<AlignmentX> moduleAlignment = sgGeneral.add(new EnumSetting.Builder<AlignmentX>()
            .name("module-alignment")
            .description("How module titles are aligned.")
            .defaultValue(AlignmentX.Center)
            .build()
    );

    public final Setting<Boolean> categoryIcons = sgGeneral.add(new BoolSetting.Builder()
            .name("category-icons")
            .description("Adds item icons to module categories.")
            .defaultValue(true)
            .build()
    );

    public final Setting<Boolean> hideHUD = sgGeneral.add(new BoolSetting.Builder()
            .name("hide-HUD")
            .description("Hide HUD when in GUI.")
            .defaultValue(false)
            .onChanged(v -> {
                if (Utils.mc.currentScreen instanceof WidgetScreen) Utils.mc.options.hudHidden = v;
            })
            .build()
    );

    // Colors

    public final Setting<SettingColor> accentColor = color("accent", "Installer color of the GUI.", new SettingColor(230, 75, 100));
    public final Setting<SettingColor> checkboxColor = color("checkbox", "Color of checkbox.", new SettingColor(230, 75, 100));
    public final Setting<SettingColor> plusColor = color("plus", "Color of plus button.", new SettingColor(255, 255, 255));
    public final Setting<SettingColor> minusColor = color("minus", "Color of minus button.", new SettingColor(255, 255, 255));

    // Text

    public final Setting<SettingColor> textColor = color(sgTextColors, "text", "Color of text.", new SettingColor(255, 255, 255));
    public final Setting<SettingColor> textSecondaryColor = color(sgTextColors, "text-secondary-text", "Color of secondary text.", new SettingColor(150, 150, 150));
    public final Setting<SettingColor> titleTextColor = color(sgTextColors, "title-text", "Color of title text.", new SettingColor(255, 255, 255));
    public final Setting<SettingColor> loggedInColor = color(sgTextColors, "logged-in-text", "Color of logged in account name.", new SettingColor(45, 225, 45));

    // Background

    public final ThreeStateColorSetting backgroundColor = new ThreeStateColorSetting(
            sgBackgroundColors,
            "background",
            new SettingColor(35, 35, 35, 100),
            new SettingColor(55, 55, 55, 100),
            new SettingColor(75, 75, 75, 100)
    );

    public final Setting<SettingColor> moduleBackground = color(sgBackgroundColors, "module-background", "Color of module background when active.", new SettingColor(50, 50, 50));

    // Outline

    public final ThreeStateColorSetting outlineColor = new ThreeStateColorSetting(
            sgOutline,
            "outline",
            new SettingColor(125, 125, 125),
            new SettingColor(150, 150, 150),
            new SettingColor(175, 175, 175)
    );

    // Separator

    public final Setting<SettingColor> separatorText = color(sgSeparator, "separator-text", "Color of separator text", new SettingColor(255, 255, 255));
    public final Setting<SettingColor> separatorCenter = color(sgSeparator, "separator-center", "Center color of separators.", new SettingColor(255, 255, 255));
    public final Setting<SettingColor> separatorEdges = color(sgSeparator, "separator-edges", "Color of separator edges.", new SettingColor(225, 225, 225, 150));

    // Scrollbar

    public final ThreeStateColorSetting scrollbarColor = new ThreeStateColorSetting(
            sgScrollbar,
            "Scrollbar",
            new SettingColor(55, 55, 55, 200),
            new SettingColor(85, 85, 85, 200),
            new SettingColor(150, 150, 150, 200)
    );

    // Slider

    public final ThreeStateColorSetting sliderHandle = new ThreeStateColorSetting(
            sgSlider,
            "slider-handle",
            new SettingColor(0, 255, 180),
            new SettingColor(0, 240, 165),
            new SettingColor(0, 225, 150)
    );

    public final Setting<SettingColor> sliderLeft = color(sgSlider, "slider-left", "Color of slider left part.", new SettingColor(0, 150, 80));
    public final Setting<SettingColor> sliderRight = color(sgSlider, "slider-right", "Color of slider right part.", new SettingColor(50, 50, 50));

    public MatHaxGuiTheme() {
        super("MatHax");

        settingsFactory = new DefaultSettingsWidgetFactory(this);
    }

    private Setting<SettingColor> color(SettingGroup group, String name, String description, SettingColor color) {
        return group.add(new ColorSetting.Builder()
                .name(name + "-color")
                .description(description)
                .defaultValue(color)
                .build());
    }

    private Setting<SettingColor> color(String name, String description, SettingColor color) {
        return color(sgColors, name, description, color);
    }

    // Widgets

    @Override
    public WWindow window(String title) {
        return w(new WMatHaxWindow(title));
    }

    @Override
    public WLabel label(String text, boolean title, double maxWidth) {
        if (maxWidth == 0) return w(new WMatHaxLabel(text, title));
        return w(new WMatHaxMultiLabel(text, title, maxWidth));
    }

    @Override
    public WHorizontalSeparator horizontalSeparator(String text) {
        return w(new WMatHaxHorizontalSeparator(text));
    }

    @Override
    public WVerticalSeparator verticalSeparator() {
        return w(new WMatHaxVerticalSeparator());
    }

    @Override
    protected WButton button(String text, GuiTexture texture) {
        return w(new WMatHaxButton(text, texture));
    }

    @Override
    public WMinus minus() {
        return w(new WMatHaxMinus());
    }

    @Override
    public WPlus plus() {
        return w(new WMatHaxPlus());
    }

    @Override
    public WCheckbox checkbox(boolean checked) {
        return w(new WMatHaxCheckbox(checked));
    }

    @Override
    public WSlider slider(double value, double min, double max) {
        return w(new WMatHaxSlider(value, min, max));
    }

    @Override
    public WTextBox textBox(String text, CharFilter filter) {
        return w(new WMatHaxTextBox(text, filter));
    }

    @Override
    public <T> WDropdown<T> dropdown(T[] values, T value) {
        return w(new WMatHaxDropdown<>(values, value));
    }

    @Override
    public WTriangle triangle() {
        return w(new WMatHaxTriangle());
    }

    @Override
    public WTooltip tooltip(String text) {
        return w(new WMatHaxTooltip(text));
    }

    @Override
    public WView view() {
        return w(new WMatHaxView());
    }

    @Override
    public WSection section(String title, boolean expanded, WWidget headerWidget) {
        return w(new WMatHaxSection(title, expanded, headerWidget));
    }

    @Override
    public WAccount account(WidgetScreen screen, Account<?> account) {
        return w(new WMatHaxAccount(screen, account));
    }

    @Override
    public WWidget module(Module module) {
        return w(new WMatHaxModule(module));
    }

    @Override
    public WQuad quad(Color color) {
        return w(new WMatHaxQuad(color));
    }

    @Override
    public WTopBar topBar() {
        return w(new WMatHaxTopBar());
    }

    // Colors

    @Override
    public Color textColor() {
        return textColor.get();
    }

    @Override
    public Color textSecondaryColor() {
        return textSecondaryColor.get();
    }

    // Other

    @Override
    public TextRenderer textRenderer() {
        return TextRenderer.get();
    }

    @Override
    public double scale(double value) {
        return value * scale.get();
    }

    @Override
    public boolean categoryIcons() {
        return categoryIcons.get();
    }

    @Override
    public boolean hideHUD() {
        return hideHUD.get();
    }

    public class ThreeStateColorSetting {
        private final Setting<SettingColor> normal, hovered, pressed;

        public ThreeStateColorSetting(SettingGroup group, String name, SettingColor c1, SettingColor c2, SettingColor c3) {
            normal = color(group, name, "Color of " + name + ".", c1);
            hovered = color(group, "hovered-" + name, "Color of " + name + " when hovered.", c2);
            pressed = color(group, "pressed-" + name, "Color of " + name + " when pressed.", c3);
        }

        public SettingColor get() {
            return normal.get();
        }

        public SettingColor get(boolean pressed, boolean hovered, boolean bypassDisableHoverColor) {
            if (pressed) return this.pressed.get();
            return (hovered && (bypassDisableHoverColor || !disableHoverColor)) ? this.hovered.get() : this.normal.get();
        }

        public SettingColor get(boolean pressed, boolean hovered) {
            return get(pressed, hovered, false);
        }
    }
}
