package mathax.legacy.client.systems.accounts.types;

import mathax.legacy.client.systems.accounts.Account;
import mathax.legacy.client.systems.accounts.AccountType;
import mathax.legacy.client.systems.accounts.MicrosoftLogin;
import net.minecraft.client.util.Session;

public class MicrosoftAccount extends Account<MicrosoftAccount> {
    public MicrosoftAccount(String refreshToken) {
        super(AccountType.Microsoft, refreshToken);
    }

    @Override
    public boolean fetchInfo() {
        return auth() != null;
    }

    @Override
    public boolean fetchHead() {
        try {
            return cache.makeHead("https://www.mc-heads.net/avatar/" + cache.uuid + "/8");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean login() {
        super.login();

        String token = auth();
        if (token == null) return false;

        setSession(new Session(cache.username, cache.uuid, token, "mojang"));
        return true;
    }

    private String auth() {
        MicrosoftLogin.LoginData data = MicrosoftLogin.login(name);
        if (!data.isGood()) return null;

        name = data.newRefreshToken;
        cache.username = data.username;
        cache.uuid = data.uuid;

        return data.mcToken;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MicrosoftAccount)) return false;
        return ((MicrosoftAccount) o).name.equals(this.name);
    }
}
