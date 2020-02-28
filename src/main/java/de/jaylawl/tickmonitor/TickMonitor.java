package de.jaylawl.tickmonitor;

import de.jaylawl.tickmonitor.cmd.MasterCommand;
import de.jaylawl.tickmonitor.monitor.TickCounter;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class TickMonitor extends JavaPlugin {

    private static TickMonitor instance;
    private TickCounter tickCounter;

    @Override
    public void onEnable() {

        Logger logger = getLogger();
        PluginManager pluginManager = getServer().getPluginManager();

        boolean isPaper;
        try {
            isPaper = Class.forName("com.destroystokyo.paper.VersionHistoryManager$VersionData") != null;
        } catch (ClassNotFoundException exception) {
            isPaper = false;
        }
        if (!isPaper) {
            logger.info("§cThis plugin requires PaperSpigot to function; disabling plugin");
            pluginManager.disablePlugin(this);
            return;
        }
        tickCounter = new TickCounter(this);

        pluginManager.registerEvents(tickCounter, this);

        PluginCommand masterCommand = getCommand("tickmonitor");
        if (masterCommand != null) {
            MasterCommand cmd = new MasterCommand(this);
            masterCommand.setExecutor(cmd);
            masterCommand.setTabCompleter(cmd);
            logger.info("Successfully enabled; starting monitoring of MSPTs...");
        } else {
            logger.info("§cFailed to enable the master command; disabling plugin");
            pluginManager.disablePlugin(this);
        }
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new TickPlaceholder(this).register();

        }

    }

    public TickCounter getTickCounter() {
        return this.tickCounter;
    }

}
