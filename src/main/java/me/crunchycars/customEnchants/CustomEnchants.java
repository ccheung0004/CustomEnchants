package me.crunchycars.customEnchants;

import me.crunchycars.customEnchants.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public final class CustomEnchants extends JavaPlugin {

    private PartyManager partyManager;

    @Override
    public void onEnable() {
        // Correctly initialize and assign the partyManager to the class-level variable
        this.partyManager = new PartyManager(this);

        // Register party commands
        registerCommand("party", new PartyCommand(partyManager));
        registerCommand("p", new PartyCommand(partyManager));  // Alias for the /party command

        // Register the event listener and pass the partyManager instance
        getServer().getPluginManager().registerEvents(new PartyChatListener(partyManager), this);
        getServer().getPluginManager().registerEvents(new CustomEnchantmentListener(partyManager), this);

        CustomEnchantmentManager.initialize(partyManager);

        // Start the particle display task
        startParticleTask();

        // Register commands with debugging
        registerCommand("swiftnessEnchant", new SwiftnessEnchantmentCommand((SwiftnessEnchantment) CustomEnchantmentManager.SWIFTNESS_ENCHANTMENT));
        registerCommand("vitality1", new VitalityEnchantmentCommand((VitalityEnchantment) CustomEnchantmentManager.VITALITY_TIER_1));
        registerCommand("vitality2", new VitalityEnchantmentCommand((VitalityEnchantment) CustomEnchantmentManager.VITALITY_TIER_2));
        registerCommand("vitality3", new VitalityEnchantmentCommand((VitalityEnchantment) CustomEnchantmentManager.VITALITY_TIER_3));
        registerCommand("bleed1", new BleedEnchantmentCommand((BleedEnchantment) CustomEnchantmentManager.BLEED_TIER_1));
        registerCommand("bleed2", new BleedEnchantmentCommand((BleedEnchantment) CustomEnchantmentManager.BLEED_TIER_2));
        registerCommand("bleed3", new BleedEnchantmentCommand((BleedEnchantment) CustomEnchantmentManager.BLEED_TIER_3));
        registerCommand("silence1", new SilenceEnchantmentCommand((SilenceEnchantment) CustomEnchantmentManager.SILENCE_TIER_1));
        registerCommand("silence2", new SilenceEnchantmentCommand((SilenceEnchantment) CustomEnchantmentManager.SILENCE_TIER_2));
        registerCommand("silence3", new SilenceEnchantmentCommand((SilenceEnchantment) CustomEnchantmentManager.SILENCE_TIER_3));
        registerCommand("erudition1", new EruditionEnchantmentCommand(CustomEnchantmentManager.ERUDITION_TIER_1));
        registerCommand("erudition2", new EruditionEnchantmentCommand(CustomEnchantmentManager.ERUDITION_TIER_2));
        registerCommand("antigank1", new AntiGankEnchantmentCommand(CustomEnchantmentManager.ANTI_GANK_TIER_1));
        registerCommand("antigank2", new AntiGankEnchantmentCommand(CustomEnchantmentManager.ANTI_GANK_TIER_2));
        registerCommand("antigank3", new AntiGankEnchantmentCommand(CustomEnchantmentManager.ANTI_GANK_TIER_3));
        registerCommand("lifesteal", new LifestealEnchantmentCommand());
        registerCommand("obliterate1", new ObliterateEnchantmentCommand(CustomEnchantmentManager.OBLITERATE_TIER_1));
        registerCommand("obliterate2", new ObliterateEnchantmentCommand(CustomEnchantmentManager.OBLITERATE_TIER_2));
        registerCommand("obliterate3", new ObliterateEnchantmentCommand(CustomEnchantmentManager.OBLITERATE_TIER_3));
        registerCommand("hemorrhage1", new HemorrhageEnchantmentCommand(CustomEnchantmentManager.HEMORRHAGE_TIER_1));
        registerCommand("hemorrhage2", new HemorrhageEnchantmentCommand(CustomEnchantmentManager.HEMORRHAGE_TIER_2));
        registerCommand("cripplingstrike1", new CripplingStrikeEnchantmentCommand(CustomEnchantmentManager.CRIPPLING_STRIKE_TIER_1));
        registerCommand("cripplingstrike2", new CripplingStrikeEnchantmentCommand(CustomEnchantmentManager.CRIPPLING_STRIKE_TIER_2));
        registerCommand("cripplingstrike3", new CripplingStrikeEnchantmentCommand(CustomEnchantmentManager.CRIPPLING_STRIKE_TIER_3));
        registerCommand("trap1", new TrapEnchantmentCommand(CustomEnchantmentManager.TRAP_TIER_1));
        registerCommand("trap2", new TrapEnchantmentCommand(CustomEnchantmentManager.TRAP_TIER_2));
        registerCommand("aegis1", new AegisEnchantmentCommand(CustomEnchantmentManager.AEGIS_TIER_1));
        registerCommand("aegis2", new AegisEnchantmentCommand(CustomEnchantmentManager.AEGIS_TIER_2));
        registerCommand("aegis3", new AegisEnchantmentCommand(CustomEnchantmentManager.AEGIS_TIER_3));
        registerCommand("frenzy1", new FrenzyEnchantmentCommand(CustomEnchantmentManager.FRENZY_TIER_1));
        registerCommand("frenzy2", new FrenzyEnchantmentCommand(CustomEnchantmentManager.FRENZY_TIER_2));
        registerCommand("frenzy3", new FrenzyEnchantmentCommand(CustomEnchantmentManager.FRENZY_TIER_3));
        registerCommand("desperation1", new DesperationEnchantmentCommand(CustomEnchantmentManager.DESPERATION_TIER_1));
        registerCommand("desperation2", new DesperationEnchantmentCommand(CustomEnchantmentManager.DESPERATION_TIER_2));
        registerCommand("desperation3", new DesperationEnchantmentCommand(CustomEnchantmentManager.DESPERATION_TIER_3));
        registerCommand("coupdegrace1", new CoupDeGraceEnchantmentCommand(CustomEnchantmentManager.COUPDEGRACE_TIER_1));
        registerCommand("coupdegrace2", new CoupDeGraceEnchantmentCommand(CustomEnchantmentManager.COUPDEGRACE_TIER_2));
        registerCommand("coupdegrace3", new CoupDeGraceEnchantmentCommand(CustomEnchantmentManager.COUPDEGRACE_TIER_3));
        registerCommand("riposte1", new RiposteEnchantmentCommand(CustomEnchantmentManager.RIPOSTE_TIER_1));
        registerCommand("riposte2", new RiposteEnchantmentCommand(CustomEnchantmentManager.RIPOSTE_TIER_2));
        registerCommand("riposte3", new RiposteEnchantmentCommand(CustomEnchantmentManager.RIPOSTE_TIER_3));
        registerCommand("armored1", new ArmoredEnchantmentCommand(CustomEnchantmentManager.ARMORED_TIER_1));
        registerCommand("armored2", new ArmoredEnchantmentCommand(CustomEnchantmentManager.ARMORED_TIER_2));
        registerCommand("tank1", new TankEnchantmentCommand(CustomEnchantmentManager.TANK_TIER_1));
        registerCommand("tank2", new TankEnchantmentCommand(CustomEnchantmentManager.TANK_TIER_2));
        registerCommand("laststand1", new LastStandEnchantmentCommand(CustomEnchantmentManager.LAST_STAND_TIER_1));
        registerCommand("laststand2", new LastStandEnchantmentCommand(CustomEnchantmentManager.LAST_STAND_TIER_2));
        registerCommand("drunk1", new DrunkEnchantmentCommand(CustomEnchantmentManager.DRUNK_TIER_1));
        registerCommand("drunk2", new DrunkEnchantmentCommand(CustomEnchantmentManager.DRUNK_TIER_2));
        registerCommand("ignite1", new IgniteEnchantmentCommand(CustomEnchantmentManager.IGNITE_TIER_1));
        registerCommand("ignite2", new IgniteEnchantmentCommand(CustomEnchantmentManager.IGNITE_TIER_2));
        registerCommand("ignite3", new IgniteEnchantmentCommand(CustomEnchantmentManager.IGNITE_TIER_3));
        registerCommand("solitude1", new SolitudeEnchantmentCommand(CustomEnchantmentManager.SOLITUDE_TIER_1));
        registerCommand("solitude2", new SolitudeEnchantmentCommand(CustomEnchantmentManager.SOLITUDE_TIER_2));
        registerCommand("solitude3", new SolitudeEnchantmentCommand(CustomEnchantmentManager.SOLITUDE_TIER_3));
        registerCommand("superman", new SupermanEnchantmentCommand(CustomEnchantmentManager.SUPERMAN));
        registerCommand("apprehend1", new ApprehendEnchantmentCommand(CustomEnchantmentManager.APPREHEND_TIER_1));
        registerCommand("apprehend2", new ApprehendEnchantmentCommand(CustomEnchantmentManager.APPREHEND_TIER_2));
        registerCommand("apprehend3", new ApprehendEnchantmentCommand(CustomEnchantmentManager.APPREHEND_TIER_3));
        registerCommand("famine1", new FamineEnchantmentCommand(CustomEnchantmentManager.FAMINE_TIER_1));
        registerCommand("famine2", new FamineEnchantmentCommand(CustomEnchantmentManager.FAMINE_TIER_2));
        registerCommand("famine3", new FamineEnchantmentCommand(CustomEnchantmentManager.FAMINE_TIER_3));
        registerCommand("hex1", new HexEnchantmentCommand(CustomEnchantmentManager.HEX_TIER_1));
        registerCommand("hex2", new HexEnchantmentCommand(CustomEnchantmentManager.HEX_TIER_2));
        registerCommand("hex3", new HexEnchantmentCommand(CustomEnchantmentManager.HEX_TIER_3));
        registerCommand("springs", new SpringsEnchantmentCommand(CustomEnchantmentManager.SPRINGS));
        registerCommand("axeman1", new AxemanEnchantmentCommand(CustomEnchantmentManager.AXEMAN_TIER_1));
        registerCommand("axeman2", new AxemanEnchantmentCommand(CustomEnchantmentManager.AXEMAN_TIER_2));
        registerCommand("axeman3", new AxemanEnchantmentCommand(CustomEnchantmentManager.AXEMAN_TIER_3));
        registerCommand("swordsman1", new SwordsmanEnchantmentCommand(CustomEnchantmentManager.SWORDSMAN_TIER_1));
        registerCommand("swordsman2", new SwordsmanEnchantmentCommand(CustomEnchantmentManager.SWORDSMAN_TIER_2));
        registerCommand("swordsman3", new SwordsmanEnchantmentCommand(CustomEnchantmentManager.SWORDSMAN_TIER_3));
        registerCommand("houdini1", new HoudiniEnchantmentCommand(CustomEnchantmentManager.HOUDINI_TIER_1));
        registerCommand("houdini2", new HoudiniEnchantmentCommand(CustomEnchantmentManager.HOUDINI_TIER_2));
        registerCommand("cleave1", new CleaveEnchantmentCommand(CustomEnchantmentManager.CLEAVE_TIER_1));
        registerCommand("cleave2", new CleaveEnchantmentCommand(CustomEnchantmentManager.CLEAVE_TIER_2));
        registerCommand("cleave3", new CleaveEnchantmentCommand(CustomEnchantmentManager.CLEAVE_TIER_3));
        registerCommand("leviathansbreath", new LeviathansBreathEnchantmentCommand(CustomEnchantmentManager.LEVIATHANS_BREATH));
        registerCommand("divineLightning", new DivineLightningEnchantmentCommand(CustomEnchantmentManager.DIVINE_LIGHTNING));
        registerCommand("backstab1", new BackstabEnchantmentCommand(CustomEnchantmentManager.BACKSTAB_TIER_1));
        registerCommand("backstab2", new BackstabEnchantmentCommand(CustomEnchantmentManager.BACKSTAB_TIER_2));
        registerCommand("backstab3", new BackstabEnchantmentCommand(CustomEnchantmentManager.BACKSTAB_TIER_3));
        registerCommand("arsonist1", new ArsonistEnchantmentCommand(CustomEnchantmentManager.ARSONIST_TIER_1));
        registerCommand("arsonist2", new ArsonistEnchantmentCommand(CustomEnchantmentManager.ARSONIST_TIER_2));
        registerCommand("arsonist3", new ArsonistEnchantmentCommand(CustomEnchantmentManager.ARSONIST_TIER_3));
        registerCommand("heavy1", new HeavyEnchantmentCommand(CustomEnchantmentManager.HEAVY_TIER_1));
        registerCommand("heavy2", new HeavyEnchantmentCommand(CustomEnchantmentManager.HEAVY_TIER_2));
        registerCommand("heavy3", new HeavyEnchantmentCommand(CustomEnchantmentManager.HEAVY_TIER_3));
        registerCommand("jellylegs1", new JellyLegsEnchantmentCommand(CustomEnchantmentManager.JELLY_LEGS_TIER_1));
        registerCommand("jellylegs2", new JellyLegsEnchantmentCommand(CustomEnchantmentManager.JELLY_LEGS_TIER_2));
        registerCommand("jellylegs3", new JellyLegsEnchantmentCommand(CustomEnchantmentManager.JELLY_LEGS_TIER_3));
        registerCommand("assassin1", new AssassinEnchantmentCommand(CustomEnchantmentManager.ASSASSIN_TIER_1));
        registerCommand("assassin2", new AssassinEnchantmentCommand(CustomEnchantmentManager.ASSASSIN_TIER_2));
        registerCommand("assassin3", new AssassinEnchantmentCommand(CustomEnchantmentManager.ASSASSIN_TIER_3));
        registerCommand("poison1", new PoisonEnchantmentCommand(CustomEnchantmentManager.POISON_TIER_1));
        registerCommand("poison2", new PoisonEnchantmentCommand(CustomEnchantmentManager.POISON_TIER_2));
        registerCommand("poison3", new PoisonEnchantmentCommand(CustomEnchantmentManager.POISON_TIER_3));
        registerCommand("confusion1", new ConfusionEnchantmentCommand(CustomEnchantmentManager.CONFUSION_TIER_1));
        registerCommand("confusion2", new ConfusionEnchantmentCommand(CustomEnchantmentManager.CONFUSION_TIER_2));
        registerCommand("confusion3", new ConfusionEnchantmentCommand(CustomEnchantmentManager.CONFUSION_TIER_3));

        // Debug message
        getLogger().info("CustomEnchants plugin has been enabled.!!! woooo");
    }

    private void registerCommand(String name, CommandExecutor executor) {
        getLogger().info("Attempting to register command: " + name);
        if (this.getCommand(name) == null) {
            getLogger().severe("Command " + name + " is not defined in plugin.yml");
        } else {
            getLogger().info("Registering command: " + name);
            this.getCommand(name).setExecutor(executor);
            getLogger().info("Successfully registered command: " + name);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("CustomEnchants plugin has been disabled. nooooo");
    }

    // Method to start the particle display task
    private void startParticleTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                showPartyMemberParticles();
            }
        }.runTaskTimer(this, 0L, 20L); // Runs every second (20 ticks)
    }

    // Method to show particles above party members
    private void showPartyMemberParticles() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (partyManager != null && partyManager.isInParty(player.getUniqueId())) { // Ensure partyManager is not null
                // Get the party members
                Party party = partyManager.getParty(player.getUniqueId());

                for (UUID memberUUID : party.getMembers()) {
                    Player member = Bukkit.getPlayer(memberUUID);
                    if (member != null && member.isOnline()) {
                        Location particleLocation = member.getLocation().add(0, 4, 0); // Adjust height above the head

                        // Spawn the particles for the player
                        player.spawnParticle(Particle.VILLAGER_HAPPY, particleLocation, 10, 0.3, 1, 0.3, 0.05);
                    }
                }
            }
        }
    }
}