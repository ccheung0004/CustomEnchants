package me.crunchycars.customEnchants;

import me.crunchycars.customEnchants.commands.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomEnchants extends JavaPlugin {

    @Override
    public void onEnable() {
        // Register the custom enchantments
        CustomEnchantmentManager.getAllEnchantments();

        // Register commands
        this.getCommand("swiftnessEnchant").setExecutor(new SwiftnessEnchantmentCommand((SwiftnessEnchantment) CustomEnchantmentManager.SWIFTNESS_ENCHANTMENT));
        this.getCommand("vitality1").setExecutor(new VitalityEnchantmentCommand((VitalityEnchantment) CustomEnchantmentManager.VITALITY_TIER_1));
        this.getCommand("vitality2").setExecutor(new VitalityEnchantmentCommand((VitalityEnchantment) CustomEnchantmentManager.VITALITY_TIER_2));
        this.getCommand("vitality3").setExecutor(new VitalityEnchantmentCommand((VitalityEnchantment) CustomEnchantmentManager.VITALITY_TIER_3));
        this.getCommand("bleed1").setExecutor(new BleedEnchantmentCommand((BleedEnchantment) CustomEnchantmentManager.BLEED_TIER_1));
        this.getCommand("bleed2").setExecutor(new BleedEnchantmentCommand((BleedEnchantment) CustomEnchantmentManager.BLEED_TIER_2));
        this.getCommand("bleed3").setExecutor(new BleedEnchantmentCommand((BleedEnchantment) CustomEnchantmentManager.BLEED_TIER_3));
        this.getCommand("silence1").setExecutor(new SilenceEnchantmentCommand((SilenceEnchantment) CustomEnchantmentManager.SILENCE_TIER_1));
        this.getCommand("silence2").setExecutor(new SilenceEnchantmentCommand((SilenceEnchantment) CustomEnchantmentManager.SILENCE_TIER_2));
        this.getCommand("silence3").setExecutor(new SilenceEnchantmentCommand((SilenceEnchantment) CustomEnchantmentManager.SILENCE_TIER_3));
        this.getCommand("divineLightning").setExecutor(new DivineLightningEnchantmentCommand((DivineLightningEnchantment) CustomEnchantmentManager.DIVINE_LIGHTNING));

        // Register the event listener
        getServer().getPluginManager().registerEvents(new CustomEnchantmentListener(), this);

        // Debug message
        getLogger().info("CustomEnchants plugin has been enabled.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("CustomEnchants plugin has been disabled.");
    }
}