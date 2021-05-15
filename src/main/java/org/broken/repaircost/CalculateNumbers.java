package org.broken.repaircost;

import lombok.SneakyThrows;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
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
		
		try {
			if (helmet != null && HashMap().get(String.valueOf(helmet.getType())) != null) {
				short helmetDurability = helmet.getDurability();
				helm = (helmetDurability * Double.parseDouble(HashMap().get(String.valueOf(helmet.getType()))));
			}

			if (chest != null && HashMap().get(String.valueOf(chest.getType())) != null) {
				short chestDurability = chest.getDurability();
				ChestPlate = (chestDurability * Double.parseDouble(HashMap().get(String.valueOf(chest.getType()))));
			}

			if (leggings != null && HashMap().get(String.valueOf(leggings.getType())) != null) {
				short leggingsDurability = leggings.getDurability();
				legging = (leggingsDurability * Double.parseDouble(HashMap().get(String.valueOf(leggings.getType()))));
			}

			if (boots != null && HashMap().get(String.valueOf(boots.getType())) != null) {
				short bootsDurability = boots.getDurability();
				boot = (bootsDurability * Double.parseDouble(HashMap().get(String.valueOf(boots.getType()))));
			}


		} catch (Throwable ex) {
			System.out.println("[CalcuateNumbers] You has an error in your yml config file");
			ex.printStackTrace();
		}

		return (helm + ChestPlate + legging + boot);
	}

	private double listOfTools(Player player) {
		String itemInHand = String.valueOf(player.getItemInHand().getType());
		short curentDurability = player.getItemInHand().getDurability();

		if (HashMap().get(itemInHand) == null) {
			System.out.println("[CalcuateNumbers] You miss something in the config, value return: " + HashMap().get(itemInHand));
			return 0;
		}
		double itemFromConfig = Double.parseDouble(HashMap().get(itemInHand));

		if (itemFromConfig > 0) {
			return (curentDurability * itemFromConfig);
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
				double rankvalue = Double.parseDouble(HashMap().get(output));

				return rankvalue;


			}
		}
		return 1;
	}

	@SneakyThrows
	public HashMap<String, String> HashMap() {
		HashMap<String, String> map = new HashMap<>();

		ConfigurationSection getConfigs = config.getCustomConfig().getConfigurationSection("Values");

		if (getConfigs != null) {
			for (String keys : getConfigs.getKeys(false)) {
				ConfigurationSection configs = config.getCustomConfig().getConfigurationSection("Values." + keys);

				if (configs != null)
					for (String s : configs.getKeys(true)) {
						String value = configs.getString(s);
						System.out.println(s);
						map.putIfAbsent(s, value);
					}

			}
		}
		return map;
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
