/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.captain.pack;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;


/**
 *
 * @author andre
 */
public class PackCmd implements CommandExecutor {

    public CaptainPacks plugin;
    
    public PackCmd(CaptainPacks instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String label, String[] args) {
        if ((cs instanceof Player)) {
            Player player = (Player) cs;
            switch (args.length) {
                case 0:
                    if (player.hasPermission("captainpacks.pack")  || player.hasPermission("captainpacks.*")) {
                        if (plugin.getPack(player) != null) {
                            player.openInventory(plugin.getPack(player));
                        } else {
                            player.sendMessage(ChatColor.GRAY + "Create a backpack with /pack new");
                        }
                    }
                    break;
                case 1:
                    if(player.hasPermission("captainpacks.pack") || player.hasPermission("captainpacks.*")) {
                        if(args[0].equals("new")) {
                            if (plugin.getPack(player) != null) {
                                player.sendMessage(ChatColor.GRAY + "You already have a pack. Open with /pack");
                                return true;
                            }
                            plugin.addPack(player);
                            player.sendMessage(ChatColor.GRAY + "Open your backpack with /pack");
                        }
                    }
                case 2:
                    if(player.hasPermission("captainpacks.*")) {
                        String arg = args[0];
                        for(String s : plugin.getPacks().keySet()) {
                            if(s.equals(arg)) {
                                player.openInventory(plugin.getPacks().get(s));
                                return true;
                            }
                        }
                    }
            }
        }
        return false;
    }
    
}
