package dev.luan.vs.utilities;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ItemHelper {

    /* NORMAL ITEMSTACK */
    public static ItemStack getItem(Material material, int amount) {
        ItemStack itemStack = null;
        itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getItem(Material material, int amount, boolean unbreakable) {
        ItemStack itemStack = null;
        itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setUnbreakable(unbreakable);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getItem(Material material) {
        ItemStack itemStack = null;
        itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getItem(Material material, boolean unbreakable) {
        ItemStack itemStack = null;
        itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setUnbreakable(unbreakable);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /* ITEMSTACK (DISPLAYNAME) */
    public static ItemStack getItem(Material material, String displayname, int amount) {
        ItemStack itemStack = null;
        itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayname);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getItem(Material material, String displayname, int amount, boolean unbreakable) {
        ItemStack itemStack = null;
        itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayname);
        itemMeta.setUnbreakable(unbreakable);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /* ITEMSTACK (LORE) */
    public static ItemStack getItem(Material material, String displayname, int amount, List<String> lore) {
        ItemStack itemStack = null;
        itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayname);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getItem(Material material, String displayname, int amount, List<String> lore, boolean unbreakable) {
        ItemStack itemStack = null;
        itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayname);
        itemMeta.setUnbreakable(unbreakable);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /* ITEMSTACK (ENCHANTMENT) */
    public static ItemStack getItem(Material material, String displayname, int amount, List<Enchantment> enchantments, List<Integer> level) {
        ItemStack itemStack = null;
        itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayname);
        int current = 0;
        for(Enchantment enchantment : enchantments) {
            itemMeta.addEnchant(enchantment, level.get(current), true);
            current++;
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getItem(Material material, String displayname, int amount, List<Enchantment> enchantments, List<Integer> level, boolean unbreakable) {
        ItemStack itemStack = null;
        itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayname);
        itemMeta.setUnbreakable(unbreakable);

        int current = 0;
        for(Enchantment enchantment : enchantments) {
            itemMeta.addEnchant(enchantment, level.get(current), true);
            current++;
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /* ITEMSTACK (ENCHANTMENT, LORE) */
    public static ItemStack getItem(Material material, String displayname, int amount, List<String> lore, List<Enchantment> enchantments, List<Integer> level) {
        ItemStack itemStack = null;
        itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayname);
        int current = 0;
        for(Enchantment enchantment : enchantments) {
            itemMeta.addEnchant(enchantment, level.get(current), true);
            current++;
        }
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getItem(Material material, String displayname, int amount, List<String> lore, List<Enchantment> enchantments, List<Integer> level, boolean unbreakable) {
        ItemStack itemStack = null;
        itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayname);
        int current = 0;
        for(Enchantment enchantment : enchantments) {
            itemMeta.addEnchant(enchantment, level.get(current), true);
            current++;
        }
        itemMeta.setLore(lore);
        itemMeta.setUnbreakable(unbreakable);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /* ITEMSTACK COLOR */
    public static ItemStack getItem(Material material, String displayname, Color color) {
        ItemStack itemStack = new ItemStack(material);
        LeatherArmorMeta itemMeta = (LeatherArmorMeta) itemStack.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.setColor(color);
        itemMeta.setDisplayName(displayname);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getItem(Material material, String displayname, Color color, boolean unbreakable) {
        ItemStack itemStack = new ItemStack(material);
        LeatherArmorMeta itemMeta = (LeatherArmorMeta) itemStack.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.setColor(color);
        itemMeta.setDisplayName(displayname);
        itemMeta.setUnbreakable(unbreakable);
        itemStack.setItemMeta(itemMeta);
        return itemStack;

    }

    /* ITEMSTACK GLOW */
    public static ItemStack addGlow(ItemStack itemStack) {
        ItemStack itemStack1 = itemStack.clone();
        ItemMeta itemMeta = itemStack1.getItemMeta();
        itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack1.setItemMeta(itemMeta);
        return itemStack1;
    }

    /* TIPPED ARROW */
    public static ItemStack getTippedArrow(String name, PotionEffectType potionEffectType, int amount) {
        final ItemStack itemStack = new ItemStack(Material.TIPPED_ARROW);
        itemStack.setAmount(amount);
        final PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        potionMeta.setDisplayName(name);
        potionMeta.addCustomEffect(new PotionEffect(potionEffectType, 100, 2), true);
        itemStack.setItemMeta(potionMeta);
        return itemStack;
    }

    public static ItemStack getSkull(String displayname, UUID uuid, List<String> lore) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        skullMeta.setDisplayName(displayname);
        skullMeta.setLore((lore == null ? Arrays.asList() : lore));
        PlayerProfile playerProfile = Bukkit.createPlayerProfile(uuid);
        PlayerTextures playerTextures = playerProfile.getTextures();
        playerProfile.setTextures(playerTextures);

        skullMeta.setOwnerProfile(playerProfile);
        item.setItemMeta(skullMeta);
        return item;
    }

    public static ItemStack getSkullByURL(String displayname, String URLTexture) {
        URLTexture = URLTexture.toLowerCase();
        URLTexture = URLTexture.replace("http://textures.minecraft.net/texture/", "");
        URLTexture = URLTexture.replace("https://textures.minecraft.net/texture/", "");

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        skullMeta.setDisplayName(displayname);

        PlayerProfile playerProfile = Bukkit.createPlayerProfile(UUID.fromString("4fbecd49-c7d4-4c18-8410-adf7a7348728"));
        PlayerTextures playerTextures = playerProfile.getTextures();
        try {
            playerTextures.setSkin(new URL("http://textures.minecraft.net/texture/" + URLTexture));
        } catch (MalformedURLException e) {e.printStackTrace();}
        playerProfile.setTextures(playerTextures);

        skullMeta.setOwnerProfile(playerProfile);
        item.setItemMeta(skullMeta);
        return item;
    }
}