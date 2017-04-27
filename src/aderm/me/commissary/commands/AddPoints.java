package aderm.me.commissary.commands;

import aderm.me.commissary.Main;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class AddPoints implements CommandExecutor {

    private Main main;

    public AddPoints(Main pl) {
        main = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        String prefix = main.getConfig().getString("Points-Prefix");

        if(sender instanceof Player) {
            sender.sendMessage(col(main.getConfig().getString("AddPoints-Player").replace("{prefix}", prefix)));
            return true;
        }

        if(args.length == 0) {
            sender.sendMessage(col(main.getConfig().getString("AddPoints-Help").replace("{prefix}", prefix)));
            return true;
        }

        if(args.length == 1) {
            Player p = Bukkit.getPlayer(args[0]);

            if(p == null) {
                sender.sendMessage(col(main.getConfig().getString("AddPoints-Offline").replace("{prefix}", prefix).replace("{player}", args[0])));
                return true;
            }

            sender.sendMessage(col(main.getConfig().getString("AddPoints-Points").replace("{prefix}", prefix).replace("{player}", p.getName())));

        }

        if(args.length == 2) {
            Player p = Bukkit.getPlayer(args[0]);
            String points = args[1];

            if(p == null) {
                sender.sendMessage(col(main.getConfig().getString("AddPoints-Offline").replace("{prefix}", prefix).replace("{player}", args[0])));
                return true;
            }

            if(Integer.valueOf(points) <= 0) {
                sender.sendMessage(col(main.getConfig().getString("AddPoints-Less-Than-0").replace("{prefix}", prefix)));
                return true;
            }

            if(isNumber(points)) {
                sender.sendMessage(col(main.getConfig().getString("AddPoints-Added").replace("{prefix}", prefix).replace("{player}", p.getName()).replace("{points}", points)));
                int currentPoints = main.players.getInt(p.getUniqueId().toString() + ".Points");
                int finalPoints = currentPoints + Integer.valueOf(points);
                main.players.set(p.getUniqueId().toString() + ".Points", finalPoints);
                p.sendMessage(col(main.getConfig().getString("AddPoints-Given").replace("{prefix}", prefix).replace("{points}", points)));
                savePlayers();
            } else {
                sender.sendMessage(col(main.getConfig().getString("Not-A-Valid-Number")));
            }

        }

        if(args.length >= 3) {
            sender.sendMessage(col("&cUsage: /addpoints <player> <points>"));
            return true;
        }

        return false;
    }

    private String col(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    private boolean isNumber(String number) {
        if(NumberUtils.isNumber(number)) {
            return true;
        } else {
            return false;
        }
    }

    private void savePlayers(){
        try {
            main.players.save(main.playersyml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}