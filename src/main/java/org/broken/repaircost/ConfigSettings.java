package org.broken.repaircost;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class ConfigSettings {

	private final CalculateNumbers calculateNumbers;

	private final PlaceholderAPIPlugin plugin;
	private File customConfigFile;
	private FileConfiguration customConfig;


	public ConfigSettings(CalculateNumbers calculateNumbers) {
		this.calculateNumbers = calculateNumbers;
		plugin = calculateNumbers.getPlaceholderAPI();
		reload();
	}

	public void reload() {
		if (customConfigFile == null) {
			customConfigFile = new File(plugin.getDataFolder(), "repaircostConfig.yml");

			customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

			customConfig.options().header("Dynamic change numbers, config version " + calculateNumbers.getVersion()
					+ "\nThis is main config for Calulate damage on tools and wapons"
					+ "\n"
					+ "\nYou put in your rank name and a number as some standard, it will"
					+ "\ndivide your number with how much damage the tool has lost."
					+ "\nYou can for now only change the order and use <+,/,*,->,"
					+ "\nwill look on fix for this."
					+ "\n"
					+ "\nUse the placeholder like this, it need to output same name has you"
					+ "\nset up in the config"
					+ "\n%number_numbervalue,tool:{a placeholder from your ranking plugin}%"
					+ "\n"
					+ "\nIf you want to have two decimals use"
					+ "\n%number_numbervalue,decimal,tool:{a placeholder from your ranking plugin}%"
					+ "\n"
					+ "\nFor calcuate armor values use placeholder below."
					+ "\n%number_numbervalue,armor:{a placeholder from your ranking plugin}%"
					+ "\n"
					+ "\nIf you want to have two decimals use this placeholder"
					+ "\n%number_numbervalue,decimal,armor:{a placeholder from your ranking plugin}%"
					+ "\n"
					+ "\nDo like this:"
					+ "\nValues:"
					+ "\n- 'Examplerank: 5'"
					+ "\n");

			if (customConfig.getKeys(false).isEmpty()) {
				customConfig.set("EmptyPlaceholder", false);
				customConfig.set("Values", new String[]{"Examplerank: 6", "Examplerank1: 4", "Examplerank2: 2"});
				customConfig.set("Math", "{toolDurability} / {valueNumber}");
				//      Helmet
				customConfig.set("Armor.Helmet.Leather", "1.75");
				customConfig.set("Armor.Helmet.Chainmail", "1.75");
				customConfig.set("Armor.Helmet.Gold", "1.50");
				customConfig.set("Armor.Helmet.Iron", "1.75");
				customConfig.set("Armor.Helmet.Dimond", "2");
				customConfig.set("Armor.Helmet.Netherite", "3");
				//     ChestPlate
				customConfig.set("Armor.ChestPlate.Leather", "1.75");
				customConfig.set("Armor.ChestPlate.Chainmail", "1.75");
				customConfig.set("Armor.ChestPlate.Gold", "1.5");
				customConfig.set("Armor.ChestPlate.Iron", "1.75");
				customConfig.set("Armor.ChestPlate.Dimond", "2");
				customConfig.set("Armor.ChestPlate.Netherite", "3");
				//     Leggings
				customConfig.set("Armor.Leggings.Leather", "1.75");
				customConfig.set("Armor.Leggings.Chainmail", "1.75");
				customConfig.set("Armor.Leggings.Gold", "1.5");
				customConfig.set("Armor.Leggings.Iron", "1.75");
				customConfig.set("Armor.Leggings.Dimond", "2");
				customConfig.set("Armor.Leggings.Netherite", "3");
				//     Boots
				customConfig.set("Armor.Boots.Leather", "1.75");
				customConfig.set("Armor.Boots.Chainmail", "1.75");
				customConfig.set("Armor.Boots.Gold", "1.5");
				customConfig.set("Armor.Boots.Iron", "1.75");
				customConfig.set("Armor.Boots.Dimond", "2");
				customConfig.set("Armor.Boots.Netherite", "3");
				//Tools
				String[] path = {"STONE_HOE", "GOLDEN_HOE", "IRON_HOE", "DIAMOND_HOE", "NETHERITE_HOE",
						"STONE_SHOVEL", "GOLDEN_SHOVEL", "IRON_SHOVEL", "DIAMOND_SHOVEL", "NETHERITE_SHOVEL",
						"STONE_AXE", "GOLDEN_AXE", "IRON_AXE", "DIAMOND_AXE", "NETHERITE_AXE", "STONE_PICKAXE",
						"GOLDEN_PICKAXE", "IRON_PICKAXE", "DIAMOND_PICKAXE", "NETHERITE_PICKAXE"};

				for (String key : path)
					customConfig.set("Tools." + key, "3");

			}
			save();
		}
	}


	public FileConfiguration load() {
		if (customConfig == null) reload();
		return customConfig;
	}

	public void save() {
		if (customConfig == null || customConfigFile == null) {
			return;
		}

		try {
			load().save(customConfigFile);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public String getContents(File customConfigFile) {
		final StringBuilder sb = new StringBuilder();

		try {
			List<String> lines = Files.readAllLines(customConfigFile.toPath());
			lines.forEach((line) -> sb.append(line).append("\n"));
		} catch (IOException e) {
			return null;
		}

		return sb.toString();
	}

	public File getCustomConfigFile() {
		return customConfigFile;
	}

	public FileConfiguration getCustomConfig() {
		return customConfig;
	}

}



