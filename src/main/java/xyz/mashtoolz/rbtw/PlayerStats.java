package xyz.mashtoolz.rbtw;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import xyz.mashtoolz.RBTW;
import xyz.mashtoolz.config.PBTW;
import xyz.mashtoolz.config.TBTW;
import xyz.mashtoolz.mixins.PlayerListHudAccessor;
import xyz.mashtoolz.utils.ItemUtils;
import xyz.mashtoolz.utils.NumberUtils;

import java.util.*;
import java.util.regex.Matcher;

public class PlayerStats {

    private static final MinecraftClient client = RBTW.getClient();
    private static final PlayerListHudAccessor playerListHud = (PlayerListHudAccessor) client.inGameHud.getPlayerListHud();

    private Swimsuit swimsuit = Swimsuit.TIER_0;

    private double Money = 0.0;
    private double ItemsSinceToe = 0;

    private double LootMoney = 0.0;
    private double LootFishPoints = 0.0;
    private double LootAncientMarks = 0.0;

    private int Research = 0;
    private int Heureka = 0;
    private int Toe = 0;
    private int AD = 0;

    private final Upgrades upgrades = new Upgrades();
    private final Shop archeologist = new Shop();
    private final Shop librarian = new Shop();
    private final Shop alberto = new Shop();
    private final Shop museum = new Shop();
    private final Shop fisher = new Shop();
    private final Shop computer = new Shop();
    private final Clicker clicker = new Clicker(new int[]{0, 0, 0, 0});
    private final Shop raft = new Shop();
    private final Shop alternateDimension = new Shop();
    private final MrMoney mrMoney = new MrMoney();

    private final Map<String, Double> moneyAdditions = new LinkedHashMap<>();
    private final Map<String, Double> moneyMultipliers = new LinkedHashMap<>();
    private final Map<String, Double> fishPointAdditions = new LinkedHashMap<>();
    private final Map<String, Double> fishPointMultipliers = new LinkedHashMap<>();

    public static final int[] EMPTY_INT_ARRAY = new int[0];

    private static final Set<String> MONEY_SHOPS = Set.of(
            "Upgrades", "The Tinkerer", "The Prof", "The Librarian", "The Fisher",
            "Potato Computer"
    );

    private static final Set<String> SHOP_PREFIXES = Set.of(
            "Archeologist Shop", "Museum Shop", "The Fisher", "Click Shop",
            "Rebirth Shop", "Apotheosis Shop", "🌧", "AD Shop"
    );

    public static PlayerStats from(String message) {

        PlayerStats playerStats = new PlayerStats();

        String[] entries = message.split("[\\n;]");
        for (String entry : entries) {
            if (entry.startsWith("@ms")) {
                String[] keyValue = entry.substring(4).split("=", 2);

                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();

                    switch (key) {

                        case "modtoggle" -> {
                            if (Integer.parseInt(value) == 0)
                                Objects.requireNonNull(client.getNetworkHandler()).sendChatMessage("@mod on");
                        }

                        case "swimsuit_level" -> playerStats.setSwimsuit(Integer.parseInt(value));

                        case "money" -> playerStats.setMoney(Double.parseDouble(value));
                        case "fish_points" -> playerStats.getFisher().setCurrency(Double.parseDouble(value));
                        case "ancient_marks" -> playerStats.getArcheologist().setCurrency(Double.parseDouble(value));
                        case "museum_currency" -> playerStats.getMuseum().setCurrency(Double.parseDouble(value));
                        case "raft_tokens" -> playerStats.getRaft().setCurrency(Integer.parseInt(value));

                        case "research" -> playerStats.setResearch(Integer.parseInt(value));
                        case "heureka" -> playerStats.setHeureka(Integer.parseInt(value));
                        case "toe" -> playerStats.setToe(Integer.parseInt(value));
                        case "ad" -> playerStats.setAD(Integer.parseInt(value));

                        case "upgrades" -> {
                            int[] upgrades = parseIntArray(value);
                            playerStats.getUpgrades().set(upgrades);
                        }

                        case "archeologist_upgrades" -> {
                            int[] archeologistUpgrades = parseIntArray(value);
                            playerStats.getArcheologist().getUpgrades().set(archeologistUpgrades);
                        }

                        case "librarian" -> {
                            int[] librarianUpgrades = parseIntArray(value);
                            playerStats.getLibrarian().getUpgrades().set(librarianUpgrades);
                        }

                        case "alberto_upgrades" -> {
                            int[] albertoUpgrades = parseIntArray(value);
                            playerStats.getAlberto().getUpgrades().set(albertoUpgrades);
                        }

                        case "museum_upgrades" -> {
                            int[] museumUpgrades = parseIntArray(value);
                            playerStats.getMuseum().getUpgrades().set(museumUpgrades);
                        }

                        case "fisher_upgrades" -> {
                            int[] fisherUpgrades = parseIntArray(value);
                            playerStats.getFisher().getUpgrades().set(fisherUpgrades);
                        }

                        case "computer_upgrades" -> {
                            int[] computerUpgrades = parseIntArray(value);
                            playerStats.getComputer().getUpgrades().set(computerUpgrades);
                        }

                        case "clicker_upgrades" -> {
                            int[] clickerUpgrades = parseIntArray(value);
                            playerStats.getClicker().getUpgrades().set(clickerUpgrades);
                        }

                        case "clicker.prestige" -> playerStats.getClicker().getLevels().setPrestige(Integer.parseInt(value));
                        case "clicker.superprestige" -> playerStats.getClicker().getLevels().setSuperprestige(Integer.parseInt(value));
                        case "clicker.rebirth" -> playerStats.getClicker().getLevels().setRebirth(Integer.parseInt(value));
                        case "clicker.apotheosis" -> playerStats.getClicker().getLevels().setApotheosis(Integer.parseInt(value));

                        case "clicker.reb_upgrades" -> playerStats.getClicker().getRebirthUpgrades().set(parseIntArray(value));
                        case "clicker.apoth_upgrades" -> playerStats.getClicker().getApotheosisUpgrades().set(parseIntArray(value));

                        case "raft_upgrades" -> {
                            int[] raftUpgrades = parseIntArray(value);
                            playerStats.getRaft().getUpgrades().set(raftUpgrades);
                        }

                        case "ad_upgrades" -> {
                            int[] adUpgrades = parseIntArray(value);
                            playerStats.getAlternateDimension().getUpgrades().set(adUpgrades);
                        }

                        case "mrmoney_level" -> playerStats.getMrMoney().setLevel(Integer.parseInt(value));
                        case "mrmoney_xp" -> playerStats.getMrMoney().setXP(Double.parseDouble(value));

                        default -> RBTW.log("Unknown key: " + key);
                    }
                }
            }
        }

        return playerStats;
    }

    public static void update(String message) {

        PlayerStats playerStats = RBTW.getPlayerStats();
        if (playerStats == null) return;

        String[] keyValue = message.substring(4).split("=", 2);
        if (keyValue.length == 2) {
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

            Upgrades alternateUpgrades = playerStats.getAlternateDimension().getUpgrades();

            switch (key) {

                case "research" -> {
                    int Research = Integer.parseInt(value);
                    playerStats.setResearch(Research);
                }

                case "heureka" -> {
                    int Heureka = Integer.parseInt(value);
                    playerStats.setHeureka(Heureka);
                    playerStats.setResearch(0);
                    if (!alternateUpgrades.has(19))
                        playerStats.getUpgrades().reset();
                    playerStats.getAlberto().getUpgrades().reset(0, 2);
                }

                case "toe" -> {
                    int Toe = Integer.parseInt(value);
                    playerStats.setToe(Toe);
                    playerStats.setSwimsuit(Toe >= 25 ? Swimsuit.TIER_2 : Toe >= 11 ? Swimsuit.TIER_1 : Swimsuit.TIER_0);
                    playerStats.setHeureka(alternateUpgrades.has(4) ? 1 : 0);
                    playerStats.setResearch(0);
                    playerStats.getUpgrades().reset();
                    playerStats.getAlberto().getUpgrades().reset(0, 6);
                }

                case "ad" -> {
                    int AD = Integer.parseInt(value);
                    playerStats.setAD(AD);
                    playerStats.setToe(alternateUpgrades.has(18) ? 4 : 0);
                    playerStats.setHeureka(0);
                    playerStats.setResearch(0);
                    playerStats.getUpgrades().reset();
                    playerStats.getAlberto().getUpgrades().reset(0, 9);
                    if (!alternateUpgrades.has(6))
                        playerStats.getMuseum().getUpgrades().reset();
                }
            }
        }

    }

    private static int[] parseIntArray(String arrayString) {
        arrayString = arrayString.replaceAll("[\\[\\]]", ""); // Remove brackets
        if (arrayString.isEmpty()) {
            return new int[0];
        }
        String[] parts = arrayString.split(",");
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Integer.parseInt(parts[i].trim());
        }
        return result;
    }

    public void setSwimsuit(int tier) {
        this.swimsuit = Swimsuit.from(tier);
    }

    public void setSwimsuit(Swimsuit swimsuit) {
        this.swimsuit = swimsuit;
    }

    public Swimsuit getSwimsuit() {
        return swimsuit;
    }

    public void setMoney(double money) {
        this.Money = money;
    }

    public double getMoney() {
        return Money;
    }

    public void setItemsSinceToe(double itemsSinceToe) {
        this.ItemsSinceToe = itemsSinceToe;
    }

    public void setLootMoney(double lootMoney) {
        this.LootMoney = lootMoney;
    }

    public double getLootMoney() {
        return LootMoney;
    }

    public void setLootFishPoints(double lootFishPoints) {
        this.LootFishPoints = lootFishPoints;
    }

    public double getLootFishPoints() {
        return LootFishPoints;
    }

    public void setLootAncientMarks(double lootAncientMarks) {
        this.LootAncientMarks = lootAncientMarks;
    }

    public double getLootAncientMarks() {
        return LootAncientMarks;
    }

    public void setResearch(int research) {
        this.Research = research;
    }

    public int getResearch() {
        return Research;
    }

    public void setHeureka(int heureka) {
        this.Heureka = heureka;
    }

    public int getHeureka() {
        return Heureka;
    }

    public void setToe(int toe) {
        this.Toe = toe;
    }

    public int getToe() {
        return Toe;
    }

    public void setAD(int ad) {
        this.AD = ad;
    }

    public int getAD() {
        return AD;
    }

    public Upgrades getUpgrades() {
        return upgrades;
    }

    public Shop getLibrarian() {
        return librarian;
    }

    public Shop getAlberto() {
        return alberto;
    }

    public Shop getMuseum() {
        return museum;
    }

    public Shop getArcheologist() {
        return archeologist;
    }

    public Shop getFisher() {
        return fisher;
    }

    public Shop getComputer() {
        return computer;
    }

    public Clicker getClicker() {
        return clicker;
    }

    public Shop getRaft() {
        return raft;
    }

    public Shop getAlternateDimension() {
        return alternateDimension;
    }

    public MrMoney getMrMoney() {
        return mrMoney;
    }

    public Map<String, Double> getMoneyMultipliers() {
        return moneyMultipliers;
    }

    public void updateMoneyAdditions() {
        moneyAdditions.clear();

        if (upgrades.get(11) > 0)
            moneyAdditions.put("Plus Ten", upgrades.get(11) * 10.0);
    }

    public void updateMoneyMultipliers() {
        moneyMultipliers.clear();

        assert client.player != null;

        Upgrades ALBERTO = alberto.getUpgrades();

        if (upgrades.get(0) > 0)
            moneyMultipliers.put("Money Maker", 1 + (upgrades.get(0) * 0.2));

        if (Heureka > 0)
            moneyMultipliers.put("Breakthroughs", 1 + (Heureka * 0.5));

        if (Toe >= 2)
            moneyMultipliers.put("Milestone Multiplier", Toe >= 100 ? 6.0 : Toe >= 70 ? 5.0 : Toe >= 35 ? 4.0 : Toe >= 6 ? 3.0 : 2.0);

        if (fisher.getUpgrades().has(12))
            moneyMultipliers.put("Odd Toe", Toe % 2 == 0 ? 1 : 1.5);

        if (fisher.getUpgrades().has(13))
            moneyMultipliers.put("Loaded Logarithm", Math.round((1 + NumberUtils.logN(fisher.getUpgrades().has(15) ? 8 : 10, Math.max(1, fisher.getCurrency())) * 0.1) * 100.0) / 100.0);

        if (Toe >= 4)
            moneyMultipliers.put("Milestone IV", Math.round((1 + Math.sqrt(Toe) * 0.25) * 100) / 100.0);

        if (computer.getUpgrades().has(4))
            moneyMultipliers.put("Knowledge Gain", 1 + (Arrays.stream(librarian.getUpgrades().get()).filter(upgrade -> upgrade == 1).count() * 0.2));

        if (clicker.getUpgrades().has(0))
            moneyMultipliers.put("Crazy Clicks", 2.0);

        if (clicker.getUpgrades().has(1))
            moneyMultipliers.put("Prosperous Prestiges", 1 + clicker.getLevels().getPrestige() * 0.025);

        if (AD > 0)
            moneyMultipliers.put("Alternate Dimensions", Toe >= 50 ? 2.0 : Toe >= 30 ? 1.75 : Toe >= 15 ? 1.5 : Toe >= 1 ? 1.25 : 1.0);

        if (archeologist.getUpgrades().has(0))
            moneyMultipliers.put("Even More Money", 1.25);

        if (alternateDimension.getUpgrades().has(0))
            moneyMultipliers.put("Morer Money", 2.0);

        if (alternateDimension.getUpgrades().has(1))
            moneyMultipliers.put("Stacks of Money", 1 + Math.floor(ItemsSinceToe * 0.0025 * 1000) / 1000);

        if (raft.getUpgrades().has(3))
            moneyMultipliers.put("More Money??", 2.0);

        if (museum.getUpgrades().has(3))
            moneyMultipliers.put("Freedom", 1.2);

        // EVENT MORE MONEY

        if (alternateDimension.getUpgrades().has(5) && Heureka >= 2)
            moneyMultipliers.put("Awesome Heureka", 1.3);

        if (archeologist.getUpgrades().has(6))
            moneyMultipliers.put("Amazing Money", 1.75);

        if (museum.getUpgrades().has(6))
            moneyMultipliers.put("Sadness", 1.33);

        if (clicker.getUpgrades().has(7))
            moneyMultipliers.put("Amazing Apotheosis", 1 + (Math.min(100, clicker.getLevels().getApotheosis()) / 100.0));

        if (alternateDimension.getUpgrades().has(10) && client.player.clientWorld.isNight())
            moneyMultipliers.put("Diurnal Rhythms", 1.25);

        if (alternateDimension.getUpgrades().has(12))
            moneyMultipliers.put("Toetastic", 1 + (Toe * 0.02));

        if (alternateDimension.getUpgrades().has(16))
            moneyMultipliers.put("Super Statistic", 1 + (4 * 0.15));

        if (ALBERTO.has(0) || ALBERTO.has(3) || ALBERTO.has(7) || ALBERTO.has(10))
            moneyMultipliers.put("Alberto Money", 1 + (ALBERTO.get(0) * 0.15 + ALBERTO.get(3) * 0.1 + ALBERTO.get(7) * 0.15 + ALBERTO.get(10) * 0.1));

        if (mrMoney.getLevel() >= 1)
            moneyMultipliers.put("Mr. Money", mrMoney.getLevel() < 7 ? 1.25 : 2.0);

        if (ALBERTO.has(12))
            moneyMultipliers.put("Tradeoff", Toe % 2 == 0 ? 1 - (ALBERTO.get(12) * 0.05) : 1 + (ALBERTO.get(12) * 0.2));
    }

    public void updateFishPointAdditions() {
        fishPointAdditions.clear();

        if (mrMoney.getLevel() >= 6)
            fishPointAdditions.put("Mr. Money", 1.0);
    }

    public void updateFishPointMultipliers() {
        fishPointMultipliers.clear();

        if (fisher.getUpgrades().has(9))
            fishPointMultipliers.put("Levels", 2.0);

        if (fisher.getUpgrades().has(15))
            fishPointMultipliers.put("Full Fish", 2.0);
    }

    public double getMoneyAdditions() {
        double value = 0;
        for (Map.Entry<String, Double> entry : moneyAdditions.entrySet())
            value = value + entry.getValue();
        return value;
    }

    public double getMoneyMultiplier() {
        double multiplier = 1;
        for (Map.Entry<String, Double> entry : moneyMultipliers.entrySet())
            multiplier = multiplier * entry.getValue();
        return multiplier;
    }

    public double getFishPointAdditions() {
        double value = 0;
        for (Map.Entry<String, Double> entry : fishPointAdditions.entrySet())
            value = value + entry.getValue();
        return value;
    }

    public double getFishPointsMultiplier() {
        double multiplier = 1;
        for (Map.Entry<String, Double> entry : fishPointMultipliers.entrySet())
            multiplier = multiplier * entry.getValue();
        return multiplier;
    }

    public void update(String title, ScreenHandler handler) {

        assert client.player != null;

        TBTW.lastMenu = title;
        TBTW.Upgrades.clear();

        Map<Integer, Integer> tempUpgrades = TBTW.Upgrades;
        Matcher matcher = PBTW.NPC_CURRENCY.matcher(title);
        double currency = 0;
        if (matcher.find())
            currency = Double.parseDouble(matcher.group(1));

        switch (title) {
            case String s when s.equals("Diving Gear") && handler.slots.size() == 90 -> {
                List<String> armorItems = new ArrayList<>();
                for (int i = 11; i <= 38; i += 9) {
                    ItemStack stack = handler.getSlot(i).getStack();
                    String name = stack.getName().getString().toUpperCase();
                    armorItems.add(name.length() < 2 ? "AIR" : name);
                }
                this.setSwimsuit(Swimsuit.from(armorItems));
            }
            case String s when s.startsWith("Archeologist") -> archeologist.setCurrency(currency);
            case String s when s.startsWith("The Fisher") -> fisher.setCurrency(currency);
            case String s when s.startsWith("Museum Shop") -> museum.setCurrency(currency);
            case String s when s.startsWith("🌧") -> raft.setCurrency(currency);
            case String s when s.startsWith("AD Shop") -> alternateDimension.setCurrency(currency);
            case String s when s.startsWith("Mr. Money") -> {
                ItemStack stack = handler.getSlot(13).getStack();
                if (stack.isEmpty()) return;
                String name = stack.getName().getString().toUpperCase();
                if (name.length() < 2) return;
                Matcher mrMoneyLevel = PBTW.MRMONEY_LEVEL.matcher(name);
                if (!mrMoneyLevel.find()) return;
                mrMoney.setLevel(Integer.parseInt(mrMoneyLevel.group(1)));
                LoreComponent lore = stack.getComponents().get(DataComponentTypes.LORE);
                if (lore == null) return;
                for (Text line : lore.lines()) {
                    Matcher mrMoneyXP = PBTW.MRMONEY_XP.matcher(line.getString());
                    if (!mrMoneyXP.find()) continue;
                    mrMoney.setXP(Double.parseDouble(mrMoneyXP.group(1)));
                }
            }
            default -> {
            }
        }

        Upgrades upgrades = switch (title) {
            case "Upgrades" -> getUpgrades();
            case String s when s.startsWith("Archeologist Shop") -> archeologist.getUpgrades();
            case "The Librarian" -> librarian.getUpgrades();
            case String s when s.startsWith("Museum Shop") -> museum.getUpgrades();
            case String s when s.startsWith("The Fisher") -> fisher.getUpgrades();
            case "Potato Computer" -> computer.getUpgrades();
            case String s when s.startsWith("Click Shop") -> clicker.getUpgrades();
            case String s when s.startsWith("Rebirth Shop") -> clicker.getRebirthUpgrades();
            case String s when s.startsWith("Apotheosis Shop") -> clicker.getApotheosisUpgrades();
            case String s when s.startsWith("🌧") -> raft.getUpgrades();
            case String s when s.startsWith("AD Shop") -> alternateDimension.getUpgrades();
            case String s when s.startsWith("Alberto") -> alberto.getUpgrades();
            default -> null;
        };

        if (upgrades == null) return;

        for (Slot slot : handler.slots) {
            if (slot.id >= handler.slots.size() - 36) continue;

            ItemStack stack = slot.getStack();
            if (stack == null || stack.isEmpty()) continue;

            String name = stack.getName().getString();
            if (name.length() <= 2) continue;

            if (title.equals("The Librarian") && name.contains("Information")) {
                int id = slot.id / 9;
                if (id < 0) continue;
                tempUpgrades.put(id, name.contains("PURCHASED") ? 1 : 0);
                continue;
            }

            NbtCompound pbv = ItemUtils.getPBV(stack);
            if (pbv == null) continue;

            LoreComponent lore = stack.getComponents().get(DataComponentTypes.LORE);
            if (lore == null) continue;

            int isMaxed = pbv.get("hypercube:maxed") != null ? 1 : 0;

            switch (title) {

                case "Upgrades" -> {
                    for (Text line : lore.lines()) {
                        Matcher upgradeLevel = PBTW.UPGRADE_LEVEL.matcher(line.getString());
                        if (upgradeLevel.find()) {
                            int id = pbv.getInt("hypercube:id") - 1;
                            int level = Integer.parseInt(upgradeLevel.group(1));
                            tempUpgrades.put(id, level);
                        }
                    }
                }

                case "Alberto" -> {
                    Matcher upgradeLevel = PBTW.ALBERTO_LEVEL.matcher(name);
                    if (upgradeLevel.find()) {
                        int id = pbv.getInt("hypercube:id") - 1;
                        int level = Integer.parseInt(upgradeLevel.group(1));
                        tempUpgrades.put(id, level);
                    }
                }

                case String s when SHOP_PREFIXES.stream().anyMatch(s::startsWith) -> {
                    int id = pbv.getInt("hypercube:id") - 1;
                    if (id < 0) continue;
                    tempUpgrades.put(id, isMaxed);
                }

                case "Potato Computer" -> {
                    if (!slot.inventory.getStack(6).getItem().toString().equals("minecraft:lime_dye")) continue;
                    int id = pbv.getInt("hypercube:id") - 1;
                    if (id < 0) continue;
                    tempUpgrades.put(id, isMaxed);
                }

                default -> {
//                    RBTW.log(name + " -> " + pbv);
                }
            }
        }

        int maxId = tempUpgrades.keySet().stream().max(Integer::compareTo).orElse(0);
        if (maxId != 0) {
            int[] levels = new int[maxId + 1];
            for (Map.Entry<Integer, Integer> entry : tempUpgrades.entrySet())
                levels[entry.getKey()] = entry.getValue();

            switch (title) {
                case String s when s.startsWith("AD Shop") -> {
                    if (!upgrades.has(9) && levels[9] == 1)
                        museum.getUpgrades().reset();
                }

                default -> {
                }
            }

            upgrades.set(levels);
//            RBTW.log("Upgrades: " + Arrays.toString(levels));
        }
    }


    public void update() {

        ClientPlayerEntity player = RBTW.getPlayer();
        assert player != null;

        // Update Money from TabList Footer
        Text footer = playerListHud.getFooter();
        if (footer == null) return;
        Matcher matcher = PBTW.TABLIST_FOOTER.matcher(footer.getString());
        if (matcher.find()) {
            this.setMoney(NumberUtils.parseCompact(matcher.group(1)));
            this.setItemsSinceToe(Double.parseDouble(matcher.group(2)));
        }

        // Update LootMoney, LootFishPoints, LootAncientMarks
        double lootMoney = 0;
        double lootFishPoints = 0;
        double lootAncientMarks = 0;

        PlayerInventory inventory = player.getInventory();
        for (int i = -1; i < inventory.size(); i++) {
            ItemStack stack = i == -1 ? player.currentScreenHandler.getCursorStack() : inventory.getStack(i);
            if (stack == null || stack.isEmpty()) continue;
            NbtComponent component = stack.getComponents().get(DataComponentTypes.CUSTOM_DATA);
            if (component == null) continue;
            NbtCompound nbt = component.copyNbt();
            NbtCompound pbv = nbt.getCompound("PublicBukkitValues");
            Set<String> keys = pbv.getKeys();

            if (keys.contains("hypercube:value"))
                lootMoney += (pbv.getInt("hypercube:value") + getMoneyAdditions()) * stack.getCount() * getMoneyMultiplier();
            if (keys.contains("hypercube:fish"))
                lootFishPoints += (pbv.getInt("hypercube:fish") + getFishPointAdditions()) * stack.getCount() * getFishPointsMultiplier();
            if (keys.contains("hypercube:archeologist"))
                lootAncientMarks += pbv.getInt("hypercube:archeologist") * stack.getCount();
        }


        this.setLootMoney(lootMoney);
        this.setLootFishPoints(lootFishPoints);
        this.setLootAncientMarks(lootAncientMarks);

        this.updateMoneyAdditions();
        this.updateMoneyMultipliers();
        this.updateFishPointAdditions();
        this.updateFishPointMultipliers();
//        this.updateOxygenBaseValues();
//        this.updateOxygenMultipliers();
//        this.updateOxygenUsageMultipliers();
    }
}
