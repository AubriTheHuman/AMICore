package com.aubrithehuman.amicore.malum;

import java.awt.Color;
import java.util.ArrayList;

import com.aubrithehuman.amicore.item.ModItems;
import com.sammy.malum.core.modcontent.MalumSpiritTypes;
import com.sammy.malum.core.systems.spirits.MalumSpiritType;

@SuppressWarnings("unchecked")
public class MalumSpiritAdditons {
	
	//Malum spirit  additions
	public static ArrayList<MalumSpiritType> newSpirits = new ArrayList<MalumSpiritType>();
	
	
	public static final Color MALICIOUS_SPIRIT_COLOR = new Color(171, 17, 130);
    public static MalumSpiritType MALICIOUS_SPIRIT = MalumSpiritTypes.create("malicious", MALICIOUS_SPIRIT_COLOR, ModItems.MALICIOUS_SPIRIT);
    public static final Color MAGNETIC_SPIRIT_COLOR = new Color(235, 77, 77);
    public static MalumSpiritType MAGNETIC_SPIRIT = MalumSpiritTypes.create("magnetic", MAGNETIC_SPIRIT_COLOR, ModItems.MAGNETIC_SPIRIT);
    public static final Color RUINOUS_SPIRIT_COLOR = new Color(15, 15, 15);
    public static MalumSpiritType RUINOUS_SPIRIT = MalumSpiritTypes.create("ruinous", RUINOUS_SPIRIT_COLOR, ModItems.RUINOUS_SPIRIT);
    public static final Color TEMPESTUOUS_SPIRIT_COLOR = new Color(170, 195, 219);
    public static MalumSpiritType TEMPESTUOUS_SPIRIT = MalumSpiritTypes.create("tempestuous", TEMPESTUOUS_SPIRIT_COLOR, ModItems.TEMPESTUOUS_SPIRIT);
    public static final Color ELECTRIC_SPIRIT_COLOR = new Color(220, 236, 115);
    public static MalumSpiritType ELECTRIC_SPIRIT = MalumSpiritTypes.create("electric", ELECTRIC_SPIRIT_COLOR, ModItems.ELECTRIC_SPIRIT);
    public static final Color MECHANICAL_SPIRIT_COLOR = new Color(245, 188, 34);
    public static MalumSpiritType MECHANICAL_SPIRIT = MalumSpiritTypes.create("mechanical", MECHANICAL_SPIRIT_COLOR, ModItems.MECHANICAL_SPIRIT);
    public static final Color HERBAL_SPIRIT_COLOR = new Color(10, 124, 48);
    public static MalumSpiritType HERBAL_SPIRIT = MalumSpiritTypes.create("herbal", HERBAL_SPIRIT_COLOR, ModItems.HERBAL_SPIRIT);
    
	public static final Color VOIDIAL_SPIRIT_COLOR = new Color(57, 27, 77);
    public static MalumSpiritType VOIDIAL_SPIRIT = MalumSpiritTypes.create("voidial", VOIDIAL_SPIRIT_COLOR, ModItems.VOIDIAL_SPIRIT);
    public static final Color DEATHLY_SPIRIT_COLOR = new Color(59, 59, 82);
    public static MalumSpiritType DEATHLY_SPIRIT = MalumSpiritTypes.create("deathly", DEATHLY_SPIRIT_COLOR, ModItems.DEATHLY_SPIRIT);
    public static final Color ASTRAL_SPIRIT_COLOR = new Color(235, 240, 255);
    public static MalumSpiritType ASTRAL_SPIRIT = MalumSpiritTypes.create("astral", ASTRAL_SPIRIT_COLOR, ModItems.ASTRAL_SPIRIT);
    public static final Color CORPOREAL_SPIRIT_COLOR = new Color(237, 211, 245);
    public static MalumSpiritType CORPOREAL_SPIRIT = MalumSpiritTypes.create("corporeal", CORPOREAL_SPIRIT_COLOR, ModItems.CORPOREAL_SPIRIT);
    public static final Color CHAOTIC_SPIRIT_COLOR = new Color(111, 0, 255);
    public static MalumSpiritType CHAOTIC_SPIRIT = MalumSpiritTypes.create("chaotic", CHAOTIC_SPIRIT_COLOR, ModItems.CHAOTIC_SPIRIT);
    public static final Color FROZEN_SPIRIT_COLOR = new Color(151, 232, 226);
    public static MalumSpiritType FROZEN_SPIRIT = MalumSpiritTypes.create("frozen", FROZEN_SPIRIT_COLOR, ModItems.FROZEN_SPIRIT);
    public static final Color HUNGRY_SPIRIT_COLOR = new Color(235, 199, 56);
    public static MalumSpiritType HUNGRY_SPIRIT = MalumSpiritTypes.create("hungry", HUNGRY_SPIRIT_COLOR, ModItems.HUNGRY_SPIRIT);
    
    public static final Color RADIATIVE_SPIRIT_COLOR = new Color(42, 78, 34);
    public static MalumSpiritType RADIATIVE_SPIRIT = MalumSpiritTypes.create("radiative", RADIATIVE_SPIRIT_COLOR, ModItems.RADIATIVE_SPIRIT);
    public static final Color THOUGHTFUL_SPIRIT_COLOR = new Color(255, 107, 240);
    public static MalumSpiritType THOUGHTFUL_SPIRIT = MalumSpiritTypes.create("thoughtful", THOUGHTFUL_SPIRIT_COLOR, ModItems.THOUGHTFUL_SPIRIT);
    public static final Color ENLIGHTENED_SPIRIT_COLOR = new Color(166, 121, 16);
    public static MalumSpiritType ENLIGHTENED_SPIRIT = MalumSpiritTypes.create("enlightened", ENLIGHTENED_SPIRIT_COLOR, ModItems.ENLIGHTENED_SPIRIT);
	
    public static void register() {
    	newSpirits.clear();
    	newSpirits.add(RUINOUS_SPIRIT);
    }

}
