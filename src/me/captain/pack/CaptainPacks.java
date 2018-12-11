/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.captain.pack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.entity.Player;

/**
 * @author captainawesome7
 * 
 * Main plugin class
 */
public class CaptainPacks extends JavaPlugin {
    /**
     * Overrides default method, runs when enabled
     */
    @Override
    public void onEnable() {
        this.getCommand("pack").setExecutor(new PackCmd(this));
        packs = new HashMap();
        loadPacks();
    }
    /**
     * Overrides default method, runs when disabled
     */
    @Override
    public void onDisable() {
        savePacks();
    }
    
    public HashMap<String, Inventory> packs;
    /**
     * Load all backpacks
     */
    public void loadPacks() {
        packs.clear();
        try {
            File f = new File(getDataFolder(), "packs.yml");
            YamlConfiguration packf = new YamlConfiguration();
            packf.load(f);
            for (String s : packf.getKeys(false)) {
                String owner = packf.getString(s + ".owner");
                int itemAmt = packf.getInt(s + ".itemstacks");
                ArrayList<ItemStack> items = new ArrayList();
                for(int i = 0; i < itemAmt; i++) {
                    items.add(packf.getItemStack(s + "." + i));
                }
                Inventory inv = this.getServer().createInventory(null, 45);
                for(ItemStack item : items) {
                    try {
                        inv.addItem(item);
                    } catch(IllegalArgumentException e) {
                        
                    }
                }
                packs.put(owner, inv);
            }
            System.out.println("[CaptainPacks] Loaded packs.yml");
        } catch (IOException | IllegalArgumentException | InvalidConfigurationException e) {
            System.out.println("[CaptainPacks] Error while loading packs.yml");
        }
    }
    /**
     * Save backpacks
     */
    public void savePacks() {
        try {
            File f = new File(getDataFolder(), "packs.yml");
            YamlConfiguration packf = new YamlConfiguration();
            for(String s : packs.keySet()) {
                Inventory inv = packs.get(s);
                packf.set(s + ".owner", s);
                packf.set(s + ".itemstacks", inv.getSize());
                int itemAmt = inv.getSize();
                for(int i = 0; i<itemAmt; i++) {
                    packf.set(s + "." + i, inv.getItem(i));
                }
            }
            packf.save(f);
            System.out.println("[CaptainPacks] Saved packs.yml");
        } catch(IOException e) {
            System.out.println("[CaptainPacks] Error while saving packs.yml");
        }
    }
    /**
     * Get backpack for given player object
     * @param player - A Player that owns a backpack
     * @return Inventory - If the player has a backpack
     */
    public Inventory getPack(Player player) {
        if(packs.containsKey(player.getName())) {
            return packs.get(player.getName());
        } else {
            return null;
        }
    }
    
    public void addPack(Player player) {
        packs.put(player.getName(), getServer().createInventory(null, 45));
    }
    
    public HashMap<String, Inventory> getPacks() {
        return this.packs;
    }
}