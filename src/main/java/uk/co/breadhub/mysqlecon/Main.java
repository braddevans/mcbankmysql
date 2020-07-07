package uk.co.breadhub.mysqlecon;

import com.earth2me.essentials.api.Economy;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.breadhub.mysqlecon.database.DatabaseImpl;
import uk.co.breadhub.mysqlecon.listeners.CommandListener;

import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    private static DatabaseImpl database;
    private static Economy economy = new Economy();
    private static Main instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        saveDefaultConfig();

        //Register Commands
        getCommand("bank").setExecutor(new CommandListener());

        if (!getConfig().getString("Database.Username").equals("testacc")) {
            // register Database
            database = new DatabaseImpl();
            database.connect();
            database.init();
        } else {
            log.info("Plugin Disabled Cannot have account named testacc");
            onDisable();
        }
    }

    @Override
    public void onDisable() {
        if (!getConfig().getString("Database.Username").equals("testacc")) {
            database.disconnect();
        }
    }

    public static Economy getEconomy() {
        return economy;
    }

    public static Main getInstance() {
        return instance;
    }

    public static DatabaseImpl getDatabase() {
        return database;
    }
}
