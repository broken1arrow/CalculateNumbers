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
					+ "\n  Rank:"
					+ "\n    Examplerank: 5"
					+ "\n");

			if (customConfig.getKeys(false).isEmpty()) {
				customConfig.set("EmptyPlaceholder", false);

				String[] rankPath = {"Examplerank,6", "Examplerank1,4", "Examplerank2,2"};

				for (String keys : rankPath) {
					String[] Split = keys.split(",");
					String key = Split[0];
					String value = Split[1];
					customConfig.set("Values.Rank." + key, value);
				}

				customConfig.set("Math", "{toolDurability} / {valueNumber}");

				String[] armorPath = {"GOLDEN_HELMET,1.75", "IRON_HELMET,1.5", "DIAMOND_HELMET,2", "NETHERITE_HELMET,3",
						"GOLDEN_CHESTPLATE,1.8", "IRON_CHESTPLATE,1.5", "DIAMOND_CHESTPLATE, 2.5", "NETHERITE_CHESTPLATE,3.5",
						"GOLDEN_LEGGINGS,1.75", "IRON_LEGGINGS,1.6", "DIAMOND_LEGGINGS,2", "NETHERITE_LEGGINGS,3.2",
						"GOLDEN_BOOT,1.75", "IRON_BOOT,1.75", "DIAMOND_BOOT,2", "NETHERITE_BOOT,3"};

				for (String keys : armorPath) {
					String[] Split = keys.split(",");
					String key = Split[0];
					String value = Split[1];
					customConfig.set("Values.Armor." + key, value);
				}

				String[] toolPath = {"WOODEN_HOE,0", "STONE_HOE,1.5", "GOLDEN_HOE,1.2", "IRON_HOE,1.5", "DIAMOND_HOE,1.7", "NETHERITE_HOE,1.8",
						"WOODEN_SHOVEL,0", "STONE_SHOVEL,1.5", "GOLDEN_SHOVEL,1.2", "IRON_SHOVEL,1.5", "DIAMOND_SHOVEL,1.8", "NETHERITE_SHOVEL,2",
						"WOODEN_PICKAXE,0", "STONE_PICKAXE,1.5", "GOLDEN_PICKAXE,1.2", "IRON_PICKAXE,1.5", "DIAMOND_PICKAXE,1.8", "NETHERITE_PICKAXE,2.1"};

				for (String keys : toolPath) {
					String[] Split = keys.split(",");
					String key = Split[0];
					String value = Split[1];
					customConfig.set("Values.Tools." + key, value);
				}
				String[] waponsPath = {"WOODEN_AXE,0", "STONE_AXE,1.2", "GOLDEN_AXE,0.8", "IRON_AXE,1.5", "DIAMOND_AXE,1.8", "NETHERITE_AXE,2",
						"WOODEN_SWORD,0", "STONE_SWORD,1.2", "GOLDEN_SWORD,0.8", "IRON_SWORD,1.5", "DIAMOND_SWORD,1.8", "NETHERITE_SWORD,2",
						"BOW,1.3", "CROSSBOW,1.3"};

				for (String keys : waponsPath) {
					String[] Split = keys.split(",");
					String key = Split[0];
					String value = Split[1];

					customConfig.set("Values.Wapons." + key, value);
				}
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
/*
	@SneakyThrows
	public HashMap<String, Integer> HashMap() {

		ConfigurationSection config = getCustomConfig().getConfigurationSection("Values.Tools");

		System.out.println(config);

		if (config != null)
			for (String s : config.getKeys(false)) {
				int value = getCustomConfig().getInt("Values.Tools.");
				maps.putIfAbsent(s, value);
			}
		return maps;
	}*/

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



