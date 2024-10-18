package dev.luan.vs.utilities;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ItemBuilder {

    private ItemStack itemStack;
    private int amount = 1, customModelData = 0, potionDuration = 0, potionAmplifier = 0;
    private String name, skullOwner;
    private UUID uuid;
    private List<String> lore = new ArrayList<>();
    private HashMap<Enchantment, Integer> enchantments = new HashMap<>();
    private Color color;
    private PotionEffectType potionEffectType;
    private PotionType potionType;
    private boolean unbreakable = false, hideFlags = false, glow = false;

    public ItemBuilder(final Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemBuilder(final ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder setCustomModelData(final int customModelData) {
        this.customModelData = customModelData;
        return this;
    }

    public ItemBuilder setName(final String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder setSkullOwner(final String skullOwner) {
        this.skullOwner = skullOwner;
        return this;
    }

    public ItemBuilder setSkullOwner(final UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public ItemBuilder setLore(final List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder addEnchantment(final Enchantment enchantment, final int level) {
        this.enchantments.put(enchantment, level);
        return this;
    }

    public ItemBuilder setColor(final Color color) {
        this.color = color;
        return this;
    }

    public ItemBuilder setPotionEffectType(final PotionEffectType potionEffectType, final int duration, final int amplifier) {
        this.potionEffectType = potionEffectType;
        this.potionDuration = duration;
        this.potionAmplifier = amplifier;
        return this;
    }

    public ItemBuilder setPotionType(final PotionType potionType) {
        this.potionType = potionType;
        return this;
    }

    public ItemBuilder setPotionDuration(final int duration) {
        this.potionDuration = duration;
        return this;
    }

    public ItemBuilder setPotionAmplifier(final int amplifier) {
        this.potionAmplifier = amplifier;
        return this;
    }

    public ItemBuilder setUnbreakable(final boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public ItemBuilder setHideFlags(final boolean hideFlags) {
        this.hideFlags = hideFlags;
        return this;
    }

    public ItemBuilder setGlow(final boolean glow) {
        this.glow = glow;
        return this;
    }

    public ItemStack build() {
        final ItemStack itemStack = this.itemStack;
        itemStack.setAmount(this.amount);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setUnbreakable(this.unbreakable);

        if(this.name != null) {
            itemMeta.setDisplayName(this.name);
        }
        if(!this.lore.isEmpty()) {
            itemMeta.setLore(this.lore);
        }

        if(!this.enchantments.isEmpty()) {
            for(final Enchantment enchantment : this.enchantments.keySet()) {
                itemMeta.addEnchant(enchantment, this.enchantments.get(enchantment), true);
            }
        }

        if(this.hideFlags) {
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
            itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
            itemMeta.addItemFlags(ItemFlag.HIDE_DYE);
            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        if(this.glow) {
            itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        if(this.customModelData > 0) {
            itemMeta.setCustomModelData(this.customModelData);
        }

        itemStack.setItemMeta(itemMeta);

        if(this.color != null) {
            if(itemStack.getType().equals(Material.FIREWORK_STAR)) {
                final FireworkEffectMeta fireworkEffectMeta = (FireworkEffectMeta) itemStack.getItemMeta();
                final FireworkEffect fireworkEffect = FireworkEffect.builder().withColor(this.color).build();
                fireworkEffectMeta.setEffect(fireworkEffect);
                itemStack.setItemMeta(fireworkEffectMeta);
            } else {
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
                leatherArmorMeta.setColor(this.color);
                itemStack.setItemMeta(leatherArmorMeta);
            }
        }

        if(this.uuid != null) {
            final SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
            PlayerProfile playerProfile = Bukkit.createPlayerProfile(this.uuid);
            PlayerTextures playerTextures = playerProfile.getTextures();
            playerProfile.setTextures(playerTextures);
            skullMeta.setOwnerProfile(playerProfile);
            itemStack.setItemMeta(skullMeta);
        }

        if(this.skullOwner != null) {
            final SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
            PlayerProfile playerProfile = Bukkit.createPlayerProfile(UUID.fromString("4fbecd49-c7d4-4c18-8410-adf7a7348728"));
            PlayerTextures playerTextures = playerProfile.getTextures();
            try {
                playerTextures.setSkin(new URL("http://textures.minecraft.net/texture/" + this.skullOwner.toLowerCase()));
            } catch (MalformedURLException e) {e.printStackTrace();}
            playerProfile.setTextures(playerTextures);
            skullMeta.setOwnerProfile(playerProfile);
            itemStack.setItemMeta(skullMeta);
        }

        if(this.potionEffectType != null) {
            final PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
            for(final PotionEffect potionEffect : potionMeta.getCustomEffects()) { potionMeta.removeCustomEffect(potionEffect.getType()); }
            potionMeta.setMainEffect(this.potionEffectType);
            potionMeta.addCustomEffect(new PotionEffect(this.potionEffectType, this.potionDuration, this.potionAmplifier), true);
            itemStack.setItemMeta(potionMeta);
        }

        if(this.potionType != null) {
            final PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
            potionMeta.setBasePotionType(this.potionType);
            itemStack.setItemMeta(potionMeta);
        }

        return itemStack;
    }
}