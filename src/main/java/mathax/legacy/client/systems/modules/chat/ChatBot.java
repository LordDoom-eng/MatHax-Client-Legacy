package mathax.legacy.client.systems.modules.chat;

import mathax.legacy.client.bus.EventHandler;
import mathax.legacy.client.events.game.ReceiveMessageEvent;
import mathax.legacy.client.settings.BoolSetting;
import mathax.legacy.client.settings.Setting;
import mathax.legacy.client.settings.SettingGroup;
import mathax.legacy.client.settings.StringSetting;
import mathax.legacy.client.systems.modules.Categories;
import mathax.legacy.client.systems.modules.Module;
import mathax.legacy.client.utils.world.TickRate;
import net.minecraft.item.Items;

public class ChatBot extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgMatHax = settings.createGroup("MatHax");
    private final SettingGroup sgTPS = settings.createGroup("TPS");

    // General

    private final Setting<String> prefix = sgGeneral.add(new StringSetting.Builder()
        .name("prefix")
        .description("Command prefix for the bot.")
        .defaultValue("!")
        .build()
    );

    private final Setting<String> helpMsg = sgGeneral.add(new StringSetting.Builder()
        .name("message")
        .description("The specified message to get sent. Placeholder for commands: %commandlist%")
        .defaultValue("MatHax Legacy Chat Bot Commands -> %commandlist%")
        .build()
    );

    // MatHax

    private final Setting<Boolean> mathax = sgMatHax.add(new BoolSetting.Builder()
        .name("MatHax")
        .description("Sends MatHax website.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> mathaxAntiLinkBlock = sgMatHax.add(new BoolSetting.Builder()
        .name("anti-link-block")
        .description("Adds spaces between dots in the link to get trough anti link.")
        .defaultValue(true)
        .build()
    );

    // TPS

    private final Setting<Boolean> tps = sgTPS.add(new BoolSetting.Builder()
        .name("TPS")
        .description("Sends the current server TPS.")
        .defaultValue(true)
        .build()
    );

    private final Setting<String> tpsMsg = sgTPS.add(new StringSetting.Builder()
        .name("message")
        .description("The specified message to get sent. Placeholder for TPS: %tps%")
        .defaultValue("Current server TPS is %tps%!")
        .build()
    );

    public ChatBot() {
        super(Categories.Chat, Items.OBSERVER, "chat-bot", "Bot which automatically responds to commands.");
    }

    @EventHandler
    private void onMessageRecieve(ReceiveMessageEvent event) {
        String msg = event.message.getString();
        if (msg.contains(helpMsg.get().replace("%commandlist%", ""))) return;
        else if (msg.contains(prefix.get() + "help") || msg.contains(">" + prefix.get() + "help") || msg.contains("> " + prefix.get() + "help")) {
            sendMessage(helpMsg.get().replace("%commandlist%", getActiveCommands().replace(", removeme", "")));
        }
        else if ((msg.contains(prefix.get() + "mathax") || msg.contains(prefix.get() + "mathaxlegacy") || msg.contains(">" + prefix.get() + "mathax") || msg.contains(">" + prefix.get() + "mathaxlegacy") || msg.contains("> " + prefix.get() + "mathax") || msg.contains("> " + prefix.get() + "mathaxlegacy")) && mathax.get()) {
            sendMessage(getMatHaxMsg());
        }
        else if ((msg.contains(prefix.get() + "tps") || msg.contains(">" + prefix.get() + "tps") || msg.contains("> " + prefix.get() + "tps")) && tps.get()) {
            sendMessage(tpsMsg.get().replace("%tps%", String.format("%.1f", TickRate.INSTANCE.getTickRate())));
        }
    }

    private String getActiveCommands() {
        StringBuilder commandList = new StringBuilder().append(prefix.get() + "help, ");
        if (mathax.get()) {
            commandList.append(prefix.get() + "mathax, mathaxlegacy, ");
        }
        if (tps.get()) {
            commandList.append(prefix.get() + "tps, ");
        }
        commandList.append("removeme");
        return commandList.toString();
    }

    private String getMatHaxMsg() {
        if (mathaxAntiLinkBlock.get()) {
            return "Download MatHax now on mathaxclient . xyz to get better!";
        } else {
            return "Download MatHax now on https://mathaxclient.xyz to get better!";
        }
    }

    private void sendMessage(String message) {
        mc.player.sendChatMessage(message);
    }
}
