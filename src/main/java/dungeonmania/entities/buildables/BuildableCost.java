package dungeonmania.entities.buildables;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.enemies.ZombieToast;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.entities.inventory.cost.ItemCost;
import dungeonmania.map.GameMap;

public class BuildableCost {
    private static final Map<String, ItemCost> COSTS = Map.of(
            "bow", Bow.COST,
            "shield", Shield.COST,
            "sceptre", Sceptre.COST,
            "midnight_armour", MidnightArmour.COST);

    public static ItemCost getCost(String buildable) {
        return COSTS.get(buildable);
    }

    public static List<String> getBuildables(GameMap map, Inventory inventory) {
        return COSTS
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue().isSatisfied(inventory))
            .map(Map.Entry::getKey)
            .filter(name -> !(name.equals("midnight_armour") && map.countEntities(ZombieToast.class) > 0))
            .collect(Collectors.toList());
    }

    public static Class<? extends InventoryItem> getAlternativeIfExists(Class<? extends InventoryItem> itemType) {
        Map<Class<? extends InventoryItem>, Class<? extends InventoryItem>> alternativeCosts = Map.of(
        Treasure.class, SunStone.class,
        Key.class, SunStone.class);

        return alternativeCosts.get(itemType);
    }
}
