package de.jaylawl.tickmonitor;

import de.jaylawl.tickmonitor.monitor.TickCounter;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class TickPlaceholder extends PlaceholderExpansion {
    private TickMonitor plugin;


    public TickPlaceholder(TickMonitor plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "tickmonitor";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }


    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        TickCounter tickCounter = plugin.getTickCounter();
        double[] averages = tickCounter.getAverages();
        if (identifier.equalsIgnoreCase("mspt_1s")) {       //would be %tickmonitor_mspt_1s% when using it
            return String.format("%.2f",averages[0]);
        }
        if (identifier.equalsIgnoreCase("mspt_10s")) {       //would be %tickmonitor_mspt_10s% when using it
            return String.format("%.2f",averages[1]);
        }
        if (identifier.equalsIgnoreCase("mspt_1min")) {       //would be %tickmonitor_mspt_1min% when using it
            return String.format("%.2f",averages[2]);
        }
        return null;
    }
}
