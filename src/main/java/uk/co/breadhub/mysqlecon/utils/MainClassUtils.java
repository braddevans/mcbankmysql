package uk.co.breadhub.mysqlecon.utils;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import uk.co.breadhub.mysqlecon.Main;

public class MainClassUtils {

    public static boolean setupEconomy() {
        if (Main.getInstance().getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Main.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        Main.setEcon(rsp.getProvider());
        return Main.getEconomy() != null;
    }

    public static boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = Main.getInstance().getServer().getServicesManager().getRegistration(Chat.class);
        Main.setChat(rsp.getProvider());
        return Main.getChat() != null;
    }

    public static boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = Main.getInstance().getServer().getServicesManager().getRegistration(Permission.class);
        Main.setPerms(rsp.getProvider());
        return Main.getPermissions() != null;
    }
}
