package dev.luan.vs.kits;

import dev.luan.vs.VS;
import dev.luan.vs.utilities.ItemHelper;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;

@Getter
public class KitManager {

    private final VS plugin;
    private final HashMap<String, VSKit> VSKitDatas;

    public KitManager(final VS plugin) {
        this.plugin = plugin;
        this.VSKitDatas = new HashMap<>();

        final VSKit uhc = new VSKit("uhc", "§eUHC",
                new ItemStack(Material.GOLDEN_APPLE),
                new ItemStack[]{
                        ItemHelper.getItem(Material.DIAMOND_SWORD, "", 1, Arrays.asList(Enchantment.DAMAGE_ALL), Arrays.asList(3)),
                        new ItemStack(Material.FISHING_ROD),
                        ItemHelper.getItem(Material.BOW, "", 1, Arrays.asList(Enchantment.ARROW_KNOCKBACK), Arrays.asList(3)),
                        new ItemStack(Material.LAVA_BUCKET),
                        new ItemStack(Material.WATER_BUCKET),
                        new ItemStack(Material.GOLDEN_APPLE, 6),
                        new ItemStack(Material.ENCHANTED_GOLDEN_APPLE),
                        new ItemStack(Material.COOKED_BEEF, 64),
                        new ItemStack(Material.COBBLESTONE, 64),
                        new ItemStack(Material.ARROW, 16),
                        new ItemStack(Material.OAK_WOOD, 64),
                        new ItemStack(Material.LAVA_BUCKET),
                        new ItemStack(Material.WATER_BUCKET),
                        ItemHelper.getItem(Material.DIAMOND_PICKAXE, "", 1, Arrays.asList(Enchantment.DIG_SPEED), Arrays.asList(3)),
                        ItemHelper.getItem(Material.DIAMOND_AXE, "", 1, Arrays.asList(Enchantment.DIG_SPEED), Arrays.asList(3))
                },
                new ItemStack[]{
                        ItemHelper.getItem(Material.DIAMOND_BOOTS, "", 1, Arrays.asList(Enchantment.PROTECTION_ENVIRONMENTAL), Arrays.asList(1)),
                        ItemHelper.getItem(Material.DIAMOND_LEGGINGS, "", 1, Arrays.asList(Enchantment.PROTECTION_ENVIRONMENTAL), Arrays.asList(1)),
                        ItemHelper.getItem(Material.DIAMOND_CHESTPLATE, "", 1, Arrays.asList(Enchantment.PROTECTION_ENVIRONMENTAL), Arrays.asList(1)),
                        ItemHelper.getItem(Material.DIAMOND_HELMET, "", 1, Arrays.asList(Enchantment.PROTECTION_ENVIRONMENTAL), Arrays.asList(1))
                });
        this.VSKitDatas.put("uhc", uhc);

        final VSKit only_sword = new VSKit("only_sword", "§bOnly Sword",
                new ItemStack(Material.STONE_SWORD),
                new ItemStack[]{
                        new ItemStack(Material.STONE_SWORD),
                },
                new ItemStack[]{
                        new ItemStack(Material.IRON_LEGGINGS),
                        new ItemStack(Material.IRON_CHESTPLATE),
                        new ItemStack(Material.AIR)
                });
        this.VSKitDatas.put("only_sword", only_sword);

        final VSKit soup = new VSKit("soup", "§cSoup",
                new ItemStack(Material.MUSHROOM_STEW),
                new ItemStack[]{
                        ItemHelper.getItem(Material.IRON_SWORD, "", 1, Arrays.asList(Enchantment.DAMAGE_ALL), Arrays.asList(1)),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.MUSHROOM_STEW),
                        new ItemStack(Material.BOWL, 16),
                        new ItemStack(Material.RED_MUSHROOM, 16),
                        new ItemStack(Material.BROWN_MUSHROOM, 16)
                },
                new ItemStack[]{
                        new ItemStack(Material.IRON_BOOTS),
                        new ItemStack(Material.IRON_LEGGINGS),
                        new ItemStack(Material.IRON_CHESTPLATE),
                        new ItemStack(Material.IRON_HELMET),
                });
        this.VSKitDatas.put("soup", soup);

        final VSKit sg = new VSKit("sg", "§aSG",
                new ItemStack(Material.FISHING_ROD),
                new ItemStack[]{
                        new ItemStack(Material.IRON_SWORD),
                        new ItemStack(Material.FISHING_ROD),
                        new ItemStack(Material.BOW),
                        new ItemStack(Material.GOLDEN_APPLE),
                        new ItemStack(Material.COOKED_BEEF, 5),
                        new ItemStack(Material.COBWEB, 2),
                        new ItemStack(Material.TNT, 2),
                        new ItemStack(Material.FLINT_AND_STEEL),
                        new ItemStack(Material.ARROW, 3)
                },
                new ItemStack[]{
                        new ItemStack(Material.IRON_BOOTS),
                        new ItemStack(Material.IRON_LEGGINGS),
                        new ItemStack(Material.IRON_CHESTPLATE),
                        new ItemStack(Material.IRON_HELMET),
                });
        this.VSKitDatas.put("sg", sg);

        final VSKit sw = new VSKit("sw", "§4SW",
                new ItemStack(Material.DIAMOND_SWORD),
                new ItemStack[]{
                        ItemHelper.getItem(Material.DIAMOND_SWORD, "", 1, Arrays.asList(Enchantment.DAMAGE_ALL), Arrays.asList(1)),
                        new ItemStack(Material.FISHING_ROD),
                        new ItemStack(Material.WATER_BUCKET),
                        new ItemStack(Material.WATER_BUCKET),
                        new ItemStack(Material.LAVA_BUCKET),
                        new ItemStack(Material.FLINT_AND_STEEL),
                        new ItemStack(Material.END_STONE, 64),
                        new ItemStack(Material.COBWEB, 3),
                        new ItemStack(Material.DIAMOND_PICKAXE)
                },
                new ItemStack[]{
                        ItemHelper.getItem(Material.DIAMOND_BOOTS, "", 1, Arrays.asList(Enchantment.PROTECTION_ENVIRONMENTAL), Arrays.asList(1)),
                        ItemHelper.getItem(Material.DIAMOND_LEGGINGS, "", 1, Arrays.asList(Enchantment.PROTECTION_ENVIRONMENTAL), Arrays.asList(1)),
                        ItemHelper.getItem(Material.IRON_CHESTPLATE, "", 1, Arrays.asList(Enchantment.PROTECTION_ENVIRONMENTAL), Arrays.asList(1)),
                        ItemHelper.getItem(Material.DIAMOND_HELMET, "", 1, Arrays.asList(Enchantment.PROTECTION_ENVIRONMENTAL), Arrays.asList(1)),
                });
        this.VSKitDatas.put("sw", sw);
    }

    public Inventory getInventory(final String player_name) {
        final Inventory inventory = Bukkit.createInventory(null, 9, "Kit wählen | " + player_name);

        for(final VSKit vsKit : this.VSKitDatas.values()) {
            final ItemStack itemStack = vsKit.itemStack.clone();
            final ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(vsKit.displayName);
            if(player_name.contains("1vs1-Warteschlange")) {
                itemMeta.setLore(Arrays.asList("§7" + this.plugin.getQueueManager().getQueuePlayerDatas().get(vsKit).size() + "§8/§72"));
            }
            itemStack.setItemMeta(itemMeta);
            inventory.addItem(itemStack);
        }

        return inventory;
    }

    public VSKit getVSKitByItemStack(final ItemStack itemStack) {
        for(final VSKit vsKit : this.VSKitDatas.values()) {
            if(!vsKit.itemStack.equals(itemStack)) continue;
            return vsKit;
        }
        return null;
    }

    public VSKit getVSKitByMaterial(final Material material) {
        for(final VSKit vsKit : this.VSKitDatas.values()) {
            if(!vsKit.itemStack.getType().equals(material)) continue;
            return vsKit;
        }
        return null;
    }

    @Getter
    public static class VSKit {

        private final ItemStack itemStack;
        private final String key, displayName;
        private final ItemStack[] itemStacks, armorContents;

        public VSKit(final String key, final String displayName, final ItemStack itemStack, final ItemStack[] itemStacks, final ItemStack[] armorContents) {
            this.key = key;
            this.displayName = displayName;
            this.itemStack = itemStack;
            this.itemStacks = itemStacks;
            this.armorContents = armorContents;
        }
    }
}

