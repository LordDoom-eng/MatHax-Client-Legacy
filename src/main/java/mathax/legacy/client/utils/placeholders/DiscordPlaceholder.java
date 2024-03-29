package mathax.legacy.client.utils.placeholders;

import mathax.legacy.client.Version;
import mathax.legacy.client.gui.screens.*;
import mathax.legacy.client.gui.screens.TitleScreen;
import mathax.legacy.client.gui.screens.settings.ColorSettingScreen;
import mathax.legacy.client.gui.tabs.builtin.*;
import mathax.legacy.client.utils.Utils;
import mathax.legacy.client.utils.render.PeekScreen;
import mathax.legacy.client.utils.render.PromptBuilder;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.*;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.EditWorldScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static mathax.legacy.client.utils.Utils.mc;

public class DiscordPlaceholder {
    private static final Pattern pattern = Pattern.compile("(%activity%|%version%|%username%|%health%)");

    public static String apply(String string) {
        Matcher matcher = pattern.matcher(string);
        StringBuffer sb = new StringBuffer(string.length());

        while (matcher.find()) {
            matcher.appendReplacement(sb, getReplacement(matcher.group(1)));
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    private static String getReplacement(String placeholder) {
        switch (placeholder) {
            case "%activity%":
                if (mc.getOverlay() instanceof SplashOverlay) {
                    return "The game is loading...";
                } else if (mc.currentScreen instanceof PromptBuilder.PromptScreen && PromptBuilder.promptId.equals("new-update")) {
                    return "Viewing new update";
                } else if (mc.currentScreen instanceof PromptBuilder.PromptScreen && PromptBuilder.promptId.equals("new-update-button")) {
                    return "Viewing new update";
                } else if (mc.currentScreen instanceof TitleScreen) {
                    return "In main menu";
                } else if (mc.currentScreen instanceof net.minecraft.client.gui.screen.TitleScreen) {
                    return "In main menu";
                } else if (mc.currentScreen instanceof MultiplayerScreen) {
                    return "In server selection";
                } else if (mc.currentScreen instanceof ConnectScreen) {
                    return "Connecting to " + Utils.getNakedActivity();
                } else if (mc.currentScreen instanceof DisconnectedScreen) {
                    return "Got disconnected from " + Utils.getNakedActivity();
                } else if (mc.currentScreen instanceof GameMenuScreen) {
                    return "Game paused on " + Utils.getNakedActivity();
                } else if (mc.currentScreen instanceof PeekScreen) {
                    return "Using .peek on " + Utils.getNakedActivity();
                } else if (mc.currentScreen instanceof StatsScreen) {
                    return "Viewing stats";
                } else if (mc.currentScreen instanceof OptionsScreen) {
                    return "Changing Minecraft settings";
                } else if (mc.currentScreen instanceof AccessibilityOptionsScreen) {
                    return "Changing Minecraft accessibility settings";
                } else if (mc.currentScreen instanceof ChatOptionsScreen) {
                    return "Changing Minecraft chat settings";
                } else if (mc.currentScreen instanceof SoundOptionsScreen) {
                    return "Changing Minecraft sound settings";
                } else if (mc.currentScreen instanceof LanguageOptionsScreen) {
                    return "Changing Minecraft language";
                }  else if (mc.currentScreen instanceof VideoOptionsScreen) {
                    return "Changing Minecraft video settings";
                }  else if (mc.currentScreen instanceof SkinOptionsScreen) {
                    return "Changing Minecraft skin settings";
                } else if (mc.currentScreen instanceof PackScreen) {
                    return "Changing resourcepack";
                } else if (mc.currentScreen instanceof ControlsOptionsScreen) {
                    return "Changing Minecraft keybinds";
                } else if (mc.currentScreen instanceof NarratorOptionsScreen) {
                    return "Changing Narrator settings";
                } else if (mc.currentScreen instanceof SelectWorldScreen) {
                    return "In world selection";
                } else if (mc.currentScreen instanceof EditWorldScreen) {
                    return "Editing a world";
                } else if (mc.currentScreen instanceof CreateWorldScreen) {
                    return "Creating a new world";
                } else if (mc.currentScreen instanceof AddServerScreen) {
                    return "Adding a server";
                } else if (mc.currentScreen instanceof DirectConnectScreen) {
                    return "In direct connect";
                } else if (mc.currentScreen instanceof ModuleScreen) {
                    return "Editing module " + getModule();
                } else if (mc.currentScreen instanceof BaritoneTab.BaritoneScreen) {
                    return "Configuring Baritone";
                } else if (mc.currentScreen instanceof ConfigTab.ConfigScreen) {
                    return "Editing Config";
                } else if (mc.currentScreen instanceof DiscordPresenceTab.DiscordPresenceScreen) {
                    return "Configuring Discord Presence";
                } else if (mc.currentScreen instanceof EnemiesTab.EnemiesScreen) {
                    return "Editing Enemies";
                } else if (mc.currentScreen instanceof FriendsTab.FriendsScreen) {
                    return "Editing Friends";
                } else if (mc.currentScreen instanceof GuiTab.GuiScreen) {
                    return "Editing GUI";
                } else if (mc.currentScreen instanceof HudTab.HudScreen) {
                    return "Editing HUD";
                }  else if (mc.currentScreen instanceof MacrosTab.MacrosScreen) {
                    return "Configuring Macros";
                }  else if (mc.currentScreen instanceof MacrosTab.MacroEditorScreen) {
                    return "Configuring a Macro";
                }  else if (mc.currentScreen instanceof ModulesScreen) {
                    return "In click gui";
                } else if (mc.currentScreen instanceof ProfilesTab.ProfilesScreen) {
                    return "Changing profiles";
                } else if (mc.currentScreen instanceof ColorSettingScreen) {
                    return "Changing color";
                } else if (mc.currentScreen instanceof AccountsScreen) {
                    return "In account manager";
                } else if (mc.currentScreen instanceof AddAlteningAccountScreen) {
                    return "Adding the altening alt";
                } else if (mc.currentScreen instanceof AddCrackedAccountScreen) {
                    return "Adding cracked account";
                } else if (mc.currentScreen instanceof AddPremiumAccountScreen) {
                    return "Adding premium account";
                } else if (mc.currentScreen instanceof ProxiesScreen) {
                    return "Editing proxies";
                } else {
                    if (!(mc.world == null)) {
                        return Utils.getActivity();
                    } else {
                        return "In " + mc.currentScreen.getTitle().toString();
                    }
                }
            case "%version%":      return Version.getStylized();
            case "%username%":     return mc.getSession().getUsername();
            case "%health%":       return getHealth();
            default: return "In " + mc.currentScreen.getTitle().toString();
        }
    }

    private static String getHealth() {
        int health = Math.round(mc.player.getHealth() + mc.player.getAbsorptionAmount());
        return String.valueOf(health);
    }

    private static String getModule() {
        String module = ((ModuleScreen) mc.currentScreen).module.name.toLowerCase();
        String replaced = module.replace("-", " ");
        String[] split = replaced.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < split.length; i++) {
            sb.append(Character.toUpperCase(split[i].charAt(0)))
                .append(split[i].substring(1)).append(" ");
        }

        String s = sb.toString().trim();

        if (s.equals("Anti Afk")) {
            return "Anti AFK";
        } else if (s.equals("Tps Sync")) {
            return "TPS Sync";
        } else if (s.equals("Auto Ez")) {
            return "Auto EZ";
        }  else if (s.equals("Hud")) {
            return "HUD";
        } else if (s.equals("Esp")) {
            return "ESP";
        } else if (s.equals("Penis Esp")) {
            return "Penis ESP";
        }   else if (s.equals("Hole Esp")) {
            return "Hole ESP";
        }  else if (s.equals("Storage Esp")) {
            return "Storage ESP";
        }  else if (s.equals("Gui Move")) {
            return "GUI Move";
        }  else if (s.equals("Void Esp")) {
            return "Void ESP";
        }   else if (s.equals("City Esp")) {
            return "City ESP";
        }  else if (s.equals("Unfocused Cpu")) {
            return "Unfocused CPU";
        }  else if (s.equals("Echest Farmer")) {
            return "EChest Farmer";
        }  else if (s.equals("Cev Breaker")) {
            return "CEV Breaker";
        }  else if (s.equals("Click Tp")) {
            return "Click TP";
        }  else if (s.equals("Auto 32k")) {
            return "Auto 32K";
        }  else if (s.equals("Skeleton Esp")) {
            return "Skeleton ESP";
        } else {
            return s;
        }
    }
}
