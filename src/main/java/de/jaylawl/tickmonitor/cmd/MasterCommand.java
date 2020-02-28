package de.jaylawl.tickmonitor.cmd;

import de.jaylawl.tickmonitor.TickMonitor;
import de.jaylawl.tickmonitor.monitor.TickCounter;
import de.jaylawl.tickmonitor.util.TabHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MasterCommand implements CommandExecutor, TabCompleter {
    TickMonitor plugin;
    public MasterCommand(TickMonitor plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        int argNumber = TabHelper.getArgNumber(args);
        List<String> completions;

        if (argNumber == 1) {
            completions = new ArrayList<>(Arrays.asList(
                    "enable",
                    "disable",
                    "getlatest",
                    "monitor",
                    "reset"
            ));
        } else {
            return Collections.emptyList();
        }

        return TabHelper.sortedCompletions(args[argNumber - 1], completions);

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        String mainArg;

        if (args.length == 0) {
            mainArg = "getlatest";
        } else {
            mainArg = args[0].toLowerCase();
        }

        TickCounter tickCounter = plugin.getTickCounter();

        if (!tickCounter.isEnabled() && Arrays.asList("getlatest", "get", "monitor", "reset").contains(mainArg)) {
            sender.sendMessage("§cTickMonitor is currently disabled; cancelling command");
            return true;
        }

        switch (mainArg) {

            case "enable":
            case "on":
                if (tickCounter.isEnabled()) {
                    sender.sendMessage("TickMonitor is already enabled");
                } else {
                    tickCounter.setEnabled(true);
                    sender.sendMessage("Successfully enabled & reset TickMonitor");
                }
                break;

            case "disable":
            case "off":
                if (!tickCounter.isEnabled()) {
                    sender.sendMessage("TickMonitor is already disabled");
                } else {
                    tickCounter.setEnabled(false);
                    sender.sendMessage("Successfully disabled TickMonitor");
                }
                break;

            case "getlatest":
            case "get":
                double[] averages = tickCounter.getAverages();
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                sender.sendMessage(
                        "Latest MSPT" +
                                " | 1s: " + decimalFormat.format(averages[0]) + " ms" +
                                " | 10s: " + decimalFormat.format(averages[1]) + " ms" +
                                " | 1m: " + decimalFormat.format(averages[2]) + " ms"
                );
                break;

            case "monitor":
                if (!(sender instanceof Player)) {
                    sender.sendMessage("§cIngame exclusive command");
                    return true;
                }
                Player player = ((Player) sender);
                if (tickCounter.isMonitoring(player)) {
                    tickCounter.removeMonitoringPlayer(player);
                    player.sendActionBar("§r ");
                } else {
                    tickCounter.addMonitoringPlayer(player);
                }
                break;

            case "reset":
                tickCounter.reset();
                sender.sendMessage("Successfully reset MSPT timings");
                break;
        }


        return true;
    }


}
