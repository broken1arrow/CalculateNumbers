package org.broken.repaircost;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CalculateNumbers extends PlaceholderExpansion implements Configurable {
	private final ConfigSettings config = new ConfigSettings(this);

	boolean TwoDecimal = false;

	public void CalculateNumber() {
		CalculateNumbers instance = this;
		config.reload();

	}


	@Override
	public String onRequest(OfflinePlayer player, String identifier) {

		if (identifier.startsWith("numbervalue")) {
			try {
				String[] StringInput;
				int length = (StringInput = identifier.split("(?<!\\\\),")).length;
				for (int i = 0; i < length; i++) {
					String output = StringInput[i].replaceAll("\\\\,", ",");

					if (output.startsWith("decimal"))
						TwoDecimal = true;
					if (output.startsWith("tool")) {
						double outputNumber = listOfStringValues((Player) player, output);

						double toolListNumber = listOfTools((Player) player);
						//short curentDurability = ((Player) player).getItemInHand().getDurability();

						double numberOutput = (mathFormula(toolListNumber, outputNumber));

						double roundOff = Math.round(numberOutput * 100.0) / 100.0;
						if (TwoDecimal && roundOff > 0) {
							TwoDecimal = false;
							return String.valueOf(roundOff);
						} else return String.valueOf(Integer.valueOf((int) numberOutput));
					}

					if (output.startsWith("armor")) {
						double outputNumber = listOfStringValues((Player) player, output);
						double listOfArmorValue = listOfArmor((Player) player);

						double outputarmorDmgNumber = (listOfArmorValue / outputNumber);
						double roundOff = Math.round(outputarmorDmgNumber * 100.0) / 100.0;
						if (TwoDecimal && roundOff > 0) {
							TwoDecimal = false;
							return String.valueOf(roundOff);
						} else return String.valueOf(Integer.valueOf((int) outputarmorDmgNumber));
					}
				}
			} catch (Throwable ex) {
				ex.printStackTrace();
			}
		}
		boolean emptyPlaceholder = config.getCustomConfig().getBoolean("EmptyPlaceholder");
		if (!emptyPlaceholder)
			return "[CalcuateNumbers] You has type invalid placeholder, if you think is a bug contact the author.";

		return "";
	}

	private double mathFormula(double toolDurability, double outputNumber) {
		String stringToolDurability = String.valueOf(toolDurability);
		String stringoutputNumber = String.valueOf(outputNumber);


		try {
			String formulaString = config.getCustomConfig().getString("Math");
			String total = formulaString.replace("{toolDurability}", stringToolDurability).replace("{valueNumber}", stringoutputNumber);
			String[] splite = total.split(" ");


			String secondInput = splite[1];
			double firstInput = Double.parseDouble(splite[0]);
			double threeInput = Double.parseDouble(splite[2]);

			if (secondInput.equals("+") || secondInput.equals("-") || secondInput.equals("/") || secondInput.equals("*")) {
				switch (secondInput) {
					case ("+"):
						return (firstInput + threeInput);
					case ("-"):
						return (firstInput - threeInput);
					case ("/"):
						return (firstInput / threeInput);

					case ("*"):
						return (firstInput * threeInput);
				}

			}
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		return 0;

	}

	private double listOfArmor(Player player) {
		PlayerInventory inv = player.getInventory();
		ItemStack helmet = inv.getHelmet();
		ItemStack chest = inv.getChestplate();
		ItemStack boots = inv.getBoots();
		ItemStack leggings = inv.getLeggings();
		double helm = 0.0;
		double ChestPlate = 0.0;
		double legging = 0.0;
		double boot = 0.0;

		ConfigurationSection congest = config.getCustomConfig();
		ConfigurationSection configSection = congest.getConfigurationSection("Armor");
		try {
			if (configSection != null) {
				for (String key : configSection.getKeys(true)) {

					if (helmet != null) {
						short helmetDurability = helmet.getDurability();
						if (helmet.getType() == Material.LEATHER_HELMET && key.equals("Helmet.Leather"))
							helm = (helmetDurability * Double.parseDouble(configSection.getString(key, String.valueOf(0.0))));
						else if (helmet.getType() == Material.LEGACY_GOLD_HELMET && key.equals("Helmet.Gold"))
							helm = (helmetDurability * Double.parseDouble(configSection.getString(key, String.valueOf(0.0))));
						else if (helmet.getType() == Material.CHAINMAIL_HELMET && key.equals("Helmet.Chainmail"))
							helm = (helmetDurability * Double.parseDouble(configSection.getString(key, String.valueOf(0.0))));
						else if (helmet.getType() == Material.IRON_HELMET && key.equals("Helmet.Iron"))
							helm = (helmetDurability * Double.parseDouble(configSection.getString(key, String.valueOf(0.0))));
						else if (helmet.getType() == Material.DIAMOND_HELMET && key.equals("Helmet.Dimond"))
							helm = (helmetDurability * Double.parseDouble(configSection.getString(key, String.valueOf(0.0))));
						else if (helmet.getType() == Material.NETHERITE_HELMET && key.equals("Helmet.Netherite"))
							helm = (helmetDurability * Double.parseDouble(configSection.getString(key, String.valueOf(0.0))));
					}

					if (chest != null) {
						short chestDurability = chest.getDurability();
						if (chest.getType() == Material.LEATHER_CHESTPLATE && key.equals("ChestPlate.Leather"))
							ChestPlate = (chestDurability * Double.parseDouble(configSection.getString(key, String.valueOf(0.0))));
						else if (chest.getType() == Material.LEGACY_GOLD_CHESTPLATE && key.equals("ChestPlate.Gold"))
							ChestPlate = (chestDurability * Double.parseDouble(configSection.getString(key, String.valueOf(0.0))));
						else if (chest.getType() == Material.CHAINMAIL_CHESTPLATE && key.equals("ChestPlate.Chainmail"))
							ChestPlate = (chestDurability * Double.parseDouble(configSection.getString(key, String.valueOf(0.0))));
						else if (chest.getType() == Material.IRON_CHESTPLATE && key.equals("ChestPlate.Iron"))
							ChestPlate = (chestDurability * Double.parseDouble(configSection.getString(key, String.valueOf(0.0))));
						else if (chest.getType() == Material.DIAMOND_CHESTPLATE && key.equals("ChestPlate.Dimond"))
							ChestPlate = (chestDurability * Double.parseDouble(configSection.getString(key, String.valueOf(0.0))));
						else if (chest.getType() == Material.NETHERITE_CHESTPLATE && key.equals("ChestPlate.Netherite"))
							ChestPlate = (chestDurability * Double.parseDouble(configSection.getString(key, String.valueOf(0.0))));
					}

					if (leggings != null) {
						short leggingsDurability = leggings.getDurability();
						if (leggings.getType() == Material.LEATHER_LEGGINGS && key.equals("Leggings.Leather"))
							legging = (leggingsDurability * Double.parseDouble(configSection.getString(key, String.valueOf(0.0))));
						else if (leggings.getType() == Material.LEGACY_GOLD_LEGGINGS && key.equals("Leggings.Gold"))
							legging = (leggingsDurability * Double.parseDouble(configSection.getString(key, String.valueOf(0.0))));
						else if (leggings.getType() == Material.CHAINMAIL_LEGGINGS && key.equals("Leggings.Chainmail"))
							legging = (leggingsDurability * Double.parseDouble(configSection.getString(key, String.valueOf(0.0))));
						else if (leggings.getType() == Material.IRON_LEGGINGS && key.equals("Leggings.Iron"))
							legging = (leggingsDurability * Double.parseDouble(configSection.getString(key, String.valueOf(0.0))));
						else if (leggings.getType() == Material.DIAMOND_LEGGINGS && key.equals("Leggings.Dimond"))
							legging = (leggingsDurability * Double.parseDouble(configSection.getString(key, String.valueOf(0.0))));
						else if (leggings.getType() == Material.NETHERITE_LEGGINGS && key.equals("Leggings.Netherite"))
							legging = (leggingsDurability * Double.parseDouble(configSection.getString(key, String.valueOf(0.0))));
					}

					if (boots != null) {
						short bootsDurability = boots.getDurability();
						if (boots.getType() == Material.LEATHER_BOOTS && key.equals("Boots.Leather"))
							boot = (bootsDurability * Double.parseDouble(configSection.getString(key, String.valueOf(0.0))));
						else if (boots.getType() == Material.LEGACY_GOLD_BOOTS && key.equals("Boots.Leather"))
							boot = (bootsDurability * Double.parseDouble(configSection.getString(key, String.valueOf(0.0))));
						else if (boots.getType() == Material.CHAINMAIL_BOOTS && key.equals("Boots.Leather"))
							boot = (bootsDurability * Double.parseDouble(configSection.getString(key, String.valueOf(0.0))));
						else if (boots.getType() == Material.IRON_BOOTS && key.equals("Boots.Leather"))
							boot = (bootsDurability * Double.parseDouble(configSection.getString(key, String.valueOf(0.0))));
						else if (boots.getType() == Material.DIAMOND_BOOTS && key.equals("Boots.Leather"))
							boot = (bootsDurability * Double.parseDouble(configSection.getString(key, String.valueOf(0.0))));
						else if (boots.getType() == Material.NETHERITE_BOOTS && key.equals("Boots.Leather"))
							boot = (bootsDurability * Double.parseDouble(configSection.getString(key, String.valueOf(0.0))));
					}
				}
			}
		} catch (Throwable ex) {
			System.out.println("[CalcuateNumbers] You has an error in your yml config file");
			ex.printStackTrace();
		}

		return (helm + ChestPlate + legging + boot);
	}

	private double listOfTools(Player player) {
		String inv = String.valueOf(player.getItemInHand().getType());
		short curentDurability = player.getItemInHand().getDurability();

		ConfigurationSection congest = config.getCustomConfig();
		ConfigurationSection configSection = congest.getConfigurationSection("Tools.");
		if (configSection != null) {
			for (String key : configSection.getKeys(true)) {
				if (key.equals(inv))
					return (curentDurability * Double.parseDouble(configSection.getString(key, String.valueOf(0.0))));
				
			}
		}
		return 0;
	}

	private double listOfToolsInventory(Player player) {
		return 0;
	}

	private double listOfStringValues(Player players, String input) {
		String[] inputSplited;

		int length = (inputSplited = input.split(":")).length;
		for (int i = 0; i < length; i++) {
			String inputs = inputSplited[i];

			if (i == 1) {
				String output = PlaceholderAPI.setBracketPlaceholders(players, inputs);

				for (String list : listning()) {
					String[] splited = list.split(": ");
					String outputvalue = splited[0];

					if (output.equals(outputvalue)) {
						return Double.parseDouble(splited[1]);
					}
				}
			}
		}
		return 1;
	}

	public List<String> listning() {
		List<String> values = config.getCustomConfig().getStringList("Values");
		if (values.isEmpty())
			return Collections.singletonList("Warn!!! Values missing or Values are emty");
		return values;
	}

	@Override
	public Map<String, Object> getDefaults() {
		return null;
	}

	@Override
	public @NotNull String getIdentifier() {
		return "number";
	}

	@Override
	public @NotNull String getAuthor() {
		return "broken_arrow";
	}

	@Override
	public boolean canRegister() {
		return true;
	}

	@Override
	public @NotNull String getVersion() {
		return "0.60";
	}

}
