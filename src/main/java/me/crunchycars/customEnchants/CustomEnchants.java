package me.crunchycars.customEnchants;

import org.bukkit.plugin.java.JavaPlugin;

public final class CustomEnchants extends JavaPlugin {

    @Override
    public void onEnable() {
        // Register the custom enchantments
        CustomEnchantmentManager.registerEnchantments();

        // Register commands for the Swiftness and Vitality enchantments
        this.getCommand("swiftnessEnchant").setExecutor(new SwiftnessEnchantmentCommand(CustomEnchantmentManager.SWIFTNESS_ENCHANTMENT));
        this.getCommand("vitality1").setExecutor(new VitalityEnchantmentCommand(1));
        this.getCommand("vitality2").setExecutor(new VitalityEnchantmentCommand(2));
        this.getCommand("vitality3").setExecutor(new VitalityEnchantmentCommand(3));
        this.getCommand("bleed1").setExecutor(new BleedEnchantmentCommand(1));
        this.getCommand("bleed2").setExecutor(new BleedEnchantmentCommand(2));
        this.getCommand("bleed3").setExecutor(new BleedEnchantmentCommand(3));
        this.getCommand("silence1").setExecutor(new SilenceEnchantmentCommand(1)); // Update this line as well for clarity
        this.getCommand("silence2").setExecutor(new SilenceEnchantmentCommand(2)); // Update this line as well for clarity
        this.getCommand("silence3").setExecutor(new SilenceEnchantmentCommand(3)); // Update this line as well for clarity

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