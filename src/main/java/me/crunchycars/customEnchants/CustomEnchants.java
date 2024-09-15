package me.crunchycars.customEnchants;

import me.crunchycars.customEnchants.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
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
        getCommand("party").setExecutor(new PartyCommand(partyManager));
        getCommand("p").setExecutor(new PartyCommand(partyManager));  // Alias for the /party command

        // Register the event listener and pass the partyManager instance
        getServer().getPluginManager().registerEvents(new PartyChatListener(partyManager), this);
        getServer().getPluginManager().registerEvents(new CustomEnchantmentListener(partyManager), this);


        CustomEnchantmentManager.initialize(partyManager);

        // Start the particle display task
        startParticleTask();

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
        this.getCommand("erudition2").setExecutor(new EruditionEnchantmentCommand(CustomEnchantmentManager.ERUDITION_TIER_2));
        this.getCommand("erudition1").setExecutor(new EruditionEnchantmentCommand(CustomEnchantmentManager.ERUDITION_TIER_1));
        this.getCommand("antigank1").setExecutor(new AntiGankEnchantmentCommand(CustomEnchantmentManager.ANTI_GANK_TIER_1));
        this.getCommand("antigank2").setExecutor(new AntiGankEnchantmentCommand(CustomEnchantmentManager.ANTI_GANK_TIER_2));
        this.getCommand("antigank3").setExecutor(new AntiGankEnchantmentCommand(CustomEnchantmentManager.ANTI_GANK_TIER_3));
        this.getCommand("lifesteal").setExecutor(new LifestealEnchantmentCommand());
        this.getCommand("obliterate1").setExecutor(new ObliterateEnchantmentCommand(CustomEnchantmentManager.OBLITERATE_TIER_1));
        this.getCommand("obliterate2").setExecutor(new ObliterateEnchantmentCommand(CustomEnchantmentManager.OBLITERATE_TIER_2));
        this.getCommand("obliterate3").setExecutor(new ObliterateEnchantmentCommand(CustomEnchantmentManager.OBLITERATE_TIER_3));
        this.getCommand("hemorrhage1").setExecutor(new HemorrhageEnchantmentCommand(CustomEnchantmentManager.HEMORRHAGE_TIER_1));
        this.getCommand("hemorrhage2").setExecutor(new HemorrhageEnchantmentCommand(CustomEnchantmentManager.HEMORRHAGE_TIER_2));
        this.getCommand("cripplingstrike1").setExecutor(new CripplingStrikeEnchantmentCommand(CustomEnchantmentManager.CRIPPLING_STRIKE_TIER_1));
        this.getCommand("cripplingstrike2").setExecutor(new CripplingStrikeEnchantmentCommand(CustomEnchantmentManager.CRIPPLING_STRIKE_TIER_2));
        this.getCommand("cripplingstrike3").setExecutor(new CripplingStrikeEnchantmentCommand(CustomEnchantmentManager.CRIPPLING_STRIKE_TIER_3));
        this.getCommand("trap1").setExecutor(new TrapEnchantmentCommand(CustomEnchantmentManager.TRAP_TIER_1));
        this.getCommand("trap2").setExecutor(new TrapEnchantmentCommand(CustomEnchantmentManager.TRAP_TIER_2));
        this.getCommand("aegis1").setExecutor(new AegisEnchantmentCommand(CustomEnchantmentManager.AEGIS_TIER_1));
        this.getCommand("aegis2").setExecutor(new AegisEnchantmentCommand(CustomEnchantmentManager.AEGIS_TIER_2));
        this.getCommand("aegis3").setExecutor(new AegisEnchantmentCommand(CustomEnchantmentManager.AEGIS_TIER_3));
        this.getCommand("frenzy1").setExecutor(new FrenzyEnchantmentCommand(CustomEnchantmentManager.FRENZY_TIER_1));
        this.getCommand("frenzy2").setExecutor(new FrenzyEnchantmentCommand(CustomEnchantmentManager.FRENZY_TIER_2));
        this.getCommand("frenzy3").setExecutor(new FrenzyEnchantmentCommand(CustomEnchantmentManager.FRENZY_TIER_3));
        this.getCommand("desperation1").setExecutor(new DesperationEnchantmentCommand(CustomEnchantmentManager.DESPERATION_TIER_1));
        this.getCommand("desperation2").setExecutor(new DesperationEnchantmentCommand(CustomEnchantmentManager.DESPERATION_TIER_2));
        this.getCommand("desperation3").setExecutor(new DesperationEnchantmentCommand(CustomEnchantmentManager.DESPERATION_TIER_3));
        this.getCommand("coupdegrace1").setExecutor(new CoupDeGraceEnchantmentCommand(CustomEnchantmentManager.COUPDEGRACE_TIER_1));
        this.getCommand("coupdegrace2").setExecutor(new CoupDeGraceEnchantmentCommand(CustomEnchantmentManager.COUPDEGRACE_TIER_2));
        this.getCommand("coupdegrace3").setExecutor(new CoupDeGraceEnchantmentCommand(CustomEnchantmentManager.COUPDEGRACE_TIER_3));
        this.getCommand("riposte1").setExecutor(new RiposteEnchantmentCommand(CustomEnchantmentManager.RIPOSTE_TIER_1));
        this.getCommand("riposte2").setExecutor(new RiposteEnchantmentCommand(CustomEnchantmentManager.RIPOSTE_TIER_2));
        this.getCommand("riposte3").setExecutor(new RiposteEnchantmentCommand(CustomEnchantmentManager.RIPOSTE_TIER_3));
        this.getCommand("armored1").setExecutor(new ArmoredEnchantmentCommand(CustomEnchantmentManager.ARMORED_TIER_1));
        this.getCommand("armored2").setExecutor(new ArmoredEnchantmentCommand(CustomEnchantmentManager.ARMORED_TIER_2));
        this.getCommand("tank1").setExecutor(new TankEnchantmentCommand(CustomEnchantmentManager.TANK_TIER_1));
        this.getCommand("tank2").setExecutor(new TankEnchantmentCommand(CustomEnchantmentManager.TANK_TIER_2));
        this.getCommand("laststand1").setExecutor(new LastStandEnchantmentCommand(CustomEnchantmentManager.LAST_STAND_TIER_1));
        this.getCommand("laststand2").setExecutor(new LastStandEnchantmentCommand(CustomEnchantmentManager.LAST_STAND_TIER_2));
        this.getCommand("drunk1").setExecutor(new DrunkEnchantmentCommand(CustomEnchantmentManager.DRUNK_TIER_1));
        this.getCommand("drunk2").setExecutor(new DrunkEnchantmentCommand(CustomEnchantmentManager.DRUNK_TIER_2));
        this.getCommand("ignite1").setExecutor(new IgniteEnchantmentCommand(CustomEnchantmentManager.IGNITE_TIER_1));
        this.getCommand("ignite2").setExecutor(new IgniteEnchantmentCommand(CustomEnchantmentManager.IGNITE_TIER_2));
        this.getCommand("ignite3").setExecutor(new IgniteEnchantmentCommand(CustomEnchantmentManager.IGNITE_TIER_3));
        this.getCommand("solitude1").setExecutor(new SolitudeEnchantmentCommand(CustomEnchantmentManager.SOLITUDE_TIER_1));
        this.getCommand("solitude2").setExecutor(new SolitudeEnchantmentCommand(CustomEnchantmentManager.SOLITUDE_TIER_2));
        this.getCommand("solitude3").setExecutor(new SolitudeEnchantmentCommand(CustomEnchantmentManager.SOLITUDE_TIER_3));
        this.getCommand("superman").setExecutor(new SupermanEnchantmentCommand(CustomEnchantmentManager.SUPERMAN));
        this.getCommand("apprehend1").setExecutor(new ApprehendEnchantmentCommand(CustomEnchantmentManager.APPREHEND_TIER_1));
        this.getCommand("apprehend2").setExecutor(new ApprehendEnchantmentCommand(CustomEnchantmentManager.APPREHEND_TIER_2));
        this.getCommand("apprehend3").setExecutor(new ApprehendEnchantmentCommand(CustomEnchantmentManager.APPREHEND_TIER_3));
        this.getCommand("famine1").setExecutor(new FamineEnchantmentCommand(CustomEnchantmentManager.FAMINE_TIER_1));
        this.getCommand("famine2").setExecutor(new FamineEnchantmentCommand(CustomEnchantmentManager.FAMINE_TIER_2));
        this.getCommand("famine3").setExecutor(new FamineEnchantmentCommand(CustomEnchantmentManager.FAMINE_TIER_3));
        this.getCommand("hex1").setExecutor(new HexEnchantmentCommand(CustomEnchantmentManager.HEX_TIER_1));
        this.getCommand("hex2").setExecutor(new HexEnchantmentCommand(CustomEnchantmentManager.HEX_TIER_2));
        this.getCommand("hex3").setExecutor(new HexEnchantmentCommand(CustomEnchantmentManager.HEX_TIER_3));
        this.getCommand("springs").setExecutor(new SpringsEnchantmentCommand(CustomEnchantmentManager.SPRINGS));
        this.getCommand("axeman1").setExecutor(new AxemanEnchantmentCommand(CustomEnchantmentManager.AXEMAN_TIER_1));
        this.getCommand("axeman2").setExecutor(new AxemanEnchantmentCommand(CustomEnchantmentManager.AXEMAN_TIER_2));
        this.getCommand("axeman3").setExecutor(new AxemanEnchantmentCommand(CustomEnchantmentManager.AXEMAN_TIER_3));
        this.getCommand("swordsman1").setExecutor(new SwordsmanEnchantmentCommand(CustomEnchantmentManager.SWORDSMAN_TIER_1));
        this.getCommand("swordsman2").setExecutor(new SwordsmanEnchantmentCommand(CustomEnchantmentManager.SWORDSMAN_TIER_2));
        this.getCommand("swordsman3").setExecutor(new SwordsmanEnchantmentCommand(CustomEnchantmentManager.SWORDSMAN_TIER_3));
        this.getCommand("houdini1").setExecutor(new HoudiniEnchantmentCommand(CustomEnchantmentManager.HOUDINI_TIER_1));
        this.getCommand("houdini2").setExecutor(new HoudiniEnchantmentCommand(CustomEnchantmentManager.HOUDINI_TIER_2));
        this.getCommand("cleave1").setExecutor(new CleaveEnchantmentCommand(CustomEnchantmentManager.CLEAVE_TIER_1));
        this.getCommand("cleave2").setExecutor(new CleaveEnchantmentCommand(CustomEnchantmentManager.CLEAVE_TIER_2));
        this.getCommand("cleave3").setExecutor(new CleaveEnchantmentCommand(CustomEnchantmentManager.CLEAVE_TIER_3));
        this.getCommand("leviathansbreath").setExecutor(new LeviathansBreathEnchantmentCommand(CustomEnchantmentManager.LEVIATHANS_BREATH));
        this.getCommand("divineLightning").setExecutor(new DivineLightningEnchantmentCommand(CustomEnchantmentManager.DIVINE_LIGHTNING));



        // Debug message
        getLogger().info("CustomEnchants plugin has been enabled.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("CustomEnchants plugin has been disabled.");
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
                        Location particleLocation = member.getLocation().add(0, 2.5, 0); // Adjust height above the head

                        // Spawn the particles for the player
                        player.spawnParticle(Particle.VILLAGER_HAPPY, particleLocation, 10, 0.3, 1, 0.3, 0.05);
                    }
                }
            }
        }
    }
}