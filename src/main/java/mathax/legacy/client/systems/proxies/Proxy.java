package mathax.legacy.client.systems.proxies;

import mathax.legacy.client.utils.misc.ISerializable;
import net.minecraft.nbt.NbtCompound;

import java.util.regex.Pattern;

public class Proxy implements ISerializable<Proxy> {
    private static final Pattern IP_PATTERN = Pattern.compile("\\b(?:(?:2(?:[0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9])\\.){3}(?:(?:2([0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9]))\\b");

    public ProxyType type = ProxyType.Socks5;
    public String ip = "";
    public int port = 0;

    public String name = "";
    public String username = "";
    public String password = "";

    public boolean enabled = false;

    public boolean isValid() {
        return IP_PATTERN.matcher(ip).matches() && port >= 0 && port <= 65535 && !name.isEmpty();
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound tag = new NbtCompound();

        tag.putString("type", type.name());
        tag.putString("ip", ip);
        tag.putInt("port", port);

        tag.putString("name", name);
        tag.putString("username", username);
        tag.putString("password", password);

        tag.putBoolean("enabled", enabled);

        return tag;
    }

    @Override
    public Proxy fromTag(NbtCompound tag) {
        type = ProxyType.valueOf(tag.getString("type"));
        ip = tag.getString("ip");
        port = tag.getInt("port");

        name = tag.getString("name");
        username = tag.getString("username");
        password = tag.getString("password");

        enabled = tag.getBoolean("enabled");

        return this;
    }
}
