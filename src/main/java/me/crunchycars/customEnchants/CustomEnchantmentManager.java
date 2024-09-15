package me.crunchycars.customEnchants;

import java.util.ArrayList;
import java.util.List;

public class CustomEnchantmentManager {
    public static final CustomEnchantment SWIFTNESS_ENCHANTMENT = new SwiftnessEnchantment();
    public static final CustomEnchantment VITALITY_TIER_1 = new VitalityEnchantment(1);
    public static final CustomEnchantment VITALITY_TIER_2 = new VitalityEnchantment(2);
    public static final CustomEnchantment VITALITY_TIER_3 = new VitalityEnchantment(3);
    public static final CustomEnchantment BLEED_TIER_1 = new BleedEnchantment(1);
    public static final CustomEnchantment BLEED_TIER_2 = new BleedEnchantment(2);
    public static final CustomEnchantment BLEED_TIER_3 = new BleedEnchantment(3);
    public static final CustomEnchantment SILENCE_TIER_1 = new SilenceEnchantment(1);
    public static final CustomEnchantment SILENCE_TIER_2 = new SilenceEnchantment(2);
    public static final CustomEnchantment SILENCE_TIER_3 = new SilenceEnchantment(3);
    public static final EruditionEnchantment ERUDITION_TIER_1 = new EruditionEnchantment(1);
    public static final EruditionEnchantment ERUDITION_TIER_2 = new EruditionEnchantment(2);
    public static final AntiGankEnchantment ANTI_GANK_TIER_1 = new AntiGankEnchantment(1);
    public static final AntiGankEnchantment ANTI_GANK_TIER_2 = new AntiGankEnchantment(2);
    public static final AntiGankEnchantment ANTI_GANK_TIER_3 = new AntiGankEnchantment(3);
    public static final LifestealEnchantment LIFESTEAL_TIER_1 = new LifestealEnchantment(1);
    public static final LifestealEnchantment LIFESTEAL_TIER_2 = new LifestealEnchantment(2);
    public static final LifestealEnchantment LIFESTEAL_TIER_3 = new LifestealEnchantment(3);
    public static final ObliterateEnchantment OBLITERATE_TIER_1 = new ObliterateEnchantment(1);
    public static final ObliterateEnchantment OBLITERATE_TIER_2 = new ObliterateEnchantment(2);
    public static final ObliterateEnchantment OBLITERATE_TIER_3 = new ObliterateEnchantment(3);
    public static final HemorrhageEnchantment HEMORRHAGE_TIER_1 = new HemorrhageEnchantment((1));
    public static final HemorrhageEnchantment HEMORRHAGE_TIER_2 = new HemorrhageEnchantment((2));
    public static final CripplingStrikeEnchantment CRIPPLING_STRIKE_TIER_1 = new CripplingStrikeEnchantment((1));
    public static final CripplingStrikeEnchantment CRIPPLING_STRIKE_TIER_2 = new CripplingStrikeEnchantment((1));
    public static final CripplingStrikeEnchantment CRIPPLING_STRIKE_TIER_3 = new CripplingStrikeEnchantment((3));
    public static final TrapEnchantment TRAP_TIER_1 = new TrapEnchantment((1));
    public static final TrapEnchantment TRAP_TIER_2 = new TrapEnchantment((2));
    public static final AegisEnchantment AEGIS_TIER_1 = new AegisEnchantment(1);
    public static final AegisEnchantment AEGIS_TIER_2 = new AegisEnchantment(2);
    public static final AegisEnchantment AEGIS_TIER_3 = new AegisEnchantment(3);
    public static final FrenzyEnchantment FRENZY_TIER_1 = new FrenzyEnchantment(1);
    public static final FrenzyEnchantment FRENZY_TIER_2 = new FrenzyEnchantment(2);
    public static final FrenzyEnchantment FRENZY_TIER_3 = new FrenzyEnchantment(3);
    public static final DesperationEnchantment DESPERATION_TIER_1 = new DesperationEnchantment(1);
    public static final DesperationEnchantment DESPERATION_TIER_2 = new DesperationEnchantment(2);
    public static final DesperationEnchantment DESPERATION_TIER_3 = new DesperationEnchantment(3);
    public static final CoupDeGraceEnchantment COUPDEGRACE_TIER_1 = new CoupDeGraceEnchantment(1);
    public static final CoupDeGraceEnchantment COUPDEGRACE_TIER_2 = new CoupDeGraceEnchantment(2);
    public static final CoupDeGraceEnchantment COUPDEGRACE_TIER_3 = new CoupDeGraceEnchantment(3);
    public static final RiposteEnchantment RIPOSTE_TIER_1 = new RiposteEnchantment(1);
    public static final RiposteEnchantment RIPOSTE_TIER_2 = new RiposteEnchantment(2);
    public static final RiposteEnchantment RIPOSTE_TIER_3 = new RiposteEnchantment(3);
    public static final ArmoredEnchantment ARMORED_TIER_1 = new ArmoredEnchantment(1);
    public static final ArmoredEnchantment ARMORED_TIER_2 = new ArmoredEnchantment(2);
    public static final TankEnchantment TANK_TIER_1 = new TankEnchantment(1);
    public static final TankEnchantment TANK_TIER_2 = new TankEnchantment(2);
    public static final LastStandEnchantment LAST_STAND_TIER_1 = new LastStandEnchantment(1);
    public static final LastStandEnchantment LAST_STAND_TIER_2 = new LastStandEnchantment(2);
    public static final DrunkEnchantment DRUNK_TIER_1 = new DrunkEnchantment(1);
    public static final DrunkEnchantment DRUNK_TIER_2 = new DrunkEnchantment(2);
    public static final IgniteEnchantment IGNITE_TIER_1 = new IgniteEnchantment(1);
    public static final IgniteEnchantment IGNITE_TIER_2 = new IgniteEnchantment(2);
    public static final IgniteEnchantment IGNITE_TIER_3 = new IgniteEnchantment(3);
    public static final SolitudeEnchantment SOLITUDE_TIER_1 = new SolitudeEnchantment(1);
    public static final SolitudeEnchantment SOLITUDE_TIER_2 = new SolitudeEnchantment(2);
    public static final SolitudeEnchantment SOLITUDE_TIER_3 = new SolitudeEnchantment(3);
    public static final SupermanEnchantment SUPERMAN = new SupermanEnchantment(1);
    public static final ApprehendEnchantment APPREHEND_TIER_1 = new ApprehendEnchantment(1);
    public static final ApprehendEnchantment APPREHEND_TIER_2 = new ApprehendEnchantment(2);
    public static final ApprehendEnchantment APPREHEND_TIER_3 = new ApprehendEnchantment(3);
    public static final FamineEnchantment FAMINE_TIER_1 = new FamineEnchantment(1);
    public static final FamineEnchantment FAMINE_TIER_2 = new FamineEnchantment(2);
    public static final FamineEnchantment FAMINE_TIER_3 = new FamineEnchantment(3);
    public static final HexEnchantment HEX_TIER_1 = new HexEnchantment(1);
    public static final HexEnchantment HEX_TIER_2 = new HexEnchantment(2);
    public static final HexEnchantment HEX_TIER_3 = new HexEnchantment(3);
    public static final SpringsEnchantment SPRINGS = new SpringsEnchantment();
    public static final SwordsmanEnchantment SWORDSMAN_TIER_1 = new SwordsmanEnchantment(1);
    public static final SwordsmanEnchantment SWORDSMAN_TIER_2 = new SwordsmanEnchantment(2);
    public static final SwordsmanEnchantment SWORDSMAN_TIER_3 = new SwordsmanEnchantment(3);
    public static final AxemanEnchantment AXEMAN_TIER_1 = new AxemanEnchantment(1);
    public static final AxemanEnchantment AXEMAN_TIER_2 = new AxemanEnchantment(2);
    public static final AxemanEnchantment AXEMAN_TIER_3 = new AxemanEnchantment(3);
    public static final HoudiniEnchantment HOUDINI_TIER_1 = new HoudiniEnchantment(1);
    public static final HoudiniEnchantment HOUDINI_TIER_2 = new HoudiniEnchantment(2);

    public static DivineLightningEnchantment DIVINE_LIGHTNING;
    public static LeviathansBreathEnchantment LEVIATHANS_BREATH;
    public static CleaveEnchantment CLEAVE_TIER_1;
    public static CleaveEnchantment CLEAVE_TIER_2;
    public static CleaveEnchantment CLEAVE_TIER_3;

    private static final List<CustomEnchantment> ENCHANTMENTS = new ArrayList<>();

    // Initialize method to setup enchantments that require PartyManager
    public static void initialize(PartyManager partyManager) {
        DIVINE_LIGHTNING = new DivineLightningEnchantment(partyManager);
        LEVIATHANS_BREATH = new LeviathansBreathEnchantment(1, partyManager);
        CLEAVE_TIER_1 = new CleaveEnchantment(1, partyManager);
        CLEAVE_TIER_2 = new CleaveEnchantment(2, partyManager);
        CLEAVE_TIER_3 = new CleaveEnchantment(3, partyManager);

        // Add the newly initialized enchantments to the list
        ENCHANTMENTS.add(DIVINE_LIGHTNING);
        ENCHANTMENTS.add(LEVIATHANS_BREATH);
        ENCHANTMENTS.add(CLEAVE_TIER_1);
        ENCHANTMENTS.add(CLEAVE_TIER_2);
        ENCHANTMENTS.add(CLEAVE_TIER_3);
    }

    static {
        ENCHANTMENTS.add(SWIFTNESS_ENCHANTMENT);
        ENCHANTMENTS.add(VITALITY_TIER_1);
        ENCHANTMENTS.add(VITALITY_TIER_2);
        ENCHANTMENTS.add(VITALITY_TIER_3);
        ENCHANTMENTS.add(BLEED_TIER_1);
        ENCHANTMENTS.add(BLEED_TIER_2);
        ENCHANTMENTS.add(BLEED_TIER_3);
        ENCHANTMENTS.add(SILENCE_TIER_1);
        ENCHANTMENTS.add(SILENCE_TIER_2);
        ENCHANTMENTS.add(SILENCE_TIER_3);
        ENCHANTMENTS.add(ERUDITION_TIER_1);
        ENCHANTMENTS.add(ERUDITION_TIER_2);
        ENCHANTMENTS.add(ANTI_GANK_TIER_1);
        ENCHANTMENTS.add(ANTI_GANK_TIER_2);
        ENCHANTMENTS.add(ANTI_GANK_TIER_3);
        ENCHANTMENTS.add(LIFESTEAL_TIER_1);
        ENCHANTMENTS.add(LIFESTEAL_TIER_2);
        ENCHANTMENTS.add(LIFESTEAL_TIER_3);
        ENCHANTMENTS.add(OBLITERATE_TIER_1);
        ENCHANTMENTS.add(OBLITERATE_TIER_2);
        ENCHANTMENTS.add(OBLITERATE_TIER_3);
        ENCHANTMENTS.add(HEMORRHAGE_TIER_1);
        ENCHANTMENTS.add(HEMORRHAGE_TIER_2);
        ENCHANTMENTS.add(CRIPPLING_STRIKE_TIER_1);
        ENCHANTMENTS.add(CRIPPLING_STRIKE_TIER_2);
        ENCHANTMENTS.add(CRIPPLING_STRIKE_TIER_3);
        ENCHANTMENTS.add(TRAP_TIER_1);
        ENCHANTMENTS.add(TRAP_TIER_2);
        ENCHANTMENTS.add(AEGIS_TIER_1);
        ENCHANTMENTS.add(AEGIS_TIER_2);
        ENCHANTMENTS.add(AEGIS_TIER_3);
        ENCHANTMENTS.add(FRENZY_TIER_1);
        ENCHANTMENTS.add(FRENZY_TIER_2);
        ENCHANTMENTS.add(FRENZY_TIER_3);
        ENCHANTMENTS.add(DESPERATION_TIER_3);
        ENCHANTMENTS.add(DESPERATION_TIER_2);
        ENCHANTMENTS.add(DESPERATION_TIER_1);
        ENCHANTMENTS.add(COUPDEGRACE_TIER_1);
        ENCHANTMENTS.add(COUPDEGRACE_TIER_2);
        ENCHANTMENTS.add(COUPDEGRACE_TIER_3);
        ENCHANTMENTS.add(RIPOSTE_TIER_1);
        ENCHANTMENTS.add(RIPOSTE_TIER_2);
        ENCHANTMENTS.add(RIPOSTE_TIER_3);
        ENCHANTMENTS.add(ARMORED_TIER_1);
        ENCHANTMENTS.add(ARMORED_TIER_2);
        ENCHANTMENTS.add(TANK_TIER_1);
        ENCHANTMENTS.add(TANK_TIER_2);
        ENCHANTMENTS.add(LAST_STAND_TIER_1);
        ENCHANTMENTS.add(LAST_STAND_TIER_2);
        ENCHANTMENTS.add(DRUNK_TIER_1);
        ENCHANTMENTS.add(DRUNK_TIER_2);
        ENCHANTMENTS.add(IGNITE_TIER_1);
        ENCHANTMENTS.add(IGNITE_TIER_2);
        ENCHANTMENTS.add(IGNITE_TIER_3);
        ENCHANTMENTS.add(SOLITUDE_TIER_1);
        ENCHANTMENTS.add(SOLITUDE_TIER_2);
        ENCHANTMENTS.add(SOLITUDE_TIER_3);
        ENCHANTMENTS.add(SUPERMAN);
        ENCHANTMENTS.add(APPREHEND_TIER_1);
        ENCHANTMENTS.add(APPREHEND_TIER_2);
        ENCHANTMENTS.add(APPREHEND_TIER_3);
        ENCHANTMENTS.add(FAMINE_TIER_1);
        ENCHANTMENTS.add(FAMINE_TIER_2);
        ENCHANTMENTS.add(FAMINE_TIER_3);
        ENCHANTMENTS.add(HEX_TIER_1);
        ENCHANTMENTS.add(HEX_TIER_2);
        ENCHANTMENTS.add(HEX_TIER_3);
        ENCHANTMENTS.add(SPRINGS);
        ENCHANTMENTS.add(AXEMAN_TIER_1);
        ENCHANTMENTS.add(AXEMAN_TIER_2);
        ENCHANTMENTS.add(AXEMAN_TIER_3);
        ENCHANTMENTS.add(SWORDSMAN_TIER_1);
        ENCHANTMENTS.add(SWORDSMAN_TIER_2);
        ENCHANTMENTS.add(SWORDSMAN_TIER_3);
        ENCHANTMENTS.add(HOUDINI_TIER_1);
        ENCHANTMENTS.add(HOUDINI_TIER_2);
    }

    public static List<CustomEnchantment> getAllEnchantments() {
        return ENCHANTMENTS;
    }
}