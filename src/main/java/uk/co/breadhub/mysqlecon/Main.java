package uk.co.breadhub.mysqlecon;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.breadhub.mysqlecon.database.DatabaseApi;
import uk.co.breadhub.mysqlecon.database.DatabaseImpl;
import uk.co.breadhub.mysqlecon.listeners.CommandListener;
import uk.co.breadhub.mysqlecon.utils.MainClassUtils;

import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    private static DatabaseApi database = new DatabaseImpl();
    private static Economy econ = null;
    private static Main instance;
    private static Permission perms = null;
    private static Chat chat = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        saveDefaultConfig();
        if (!MainClassUtils.setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        MainClassUtils.setupPermissions();
        MainClassUtils.setupChat();

        //Register Commands
        getCommand("bank").setExecutor(new CommandListener());

        if (!getConfig().getString("Database.Username").equals("testacc")) {
            // register Database
            database.connect();
            database.init();
        } else {
            log.info("Plugin Disabled Cannot have account named testacc");
            onDisable();
        }
    }

    @Override
    public void onDisable() {
        database.disconnect();
    }

    public static Main getInstance() {
        return instance;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static Permission getPermissions() {
        return perms;
    }

    public static Chat getChat() {
        return chat;
    }

    public static void setChat(Chat chat) {
        Main.chat = chat;
    }

    public static void setEcon(Economy econ) {
        Main.econ = econ;
    }

    public static void setPerms(Permission perms) {
        Main.perms = perms;
    }

    public static DatabaseApi getDatabase() {
        return database;
    }
}
