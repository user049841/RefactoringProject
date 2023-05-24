package dungeonmania.battles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.ResponseBuilder;
import dungeonmania.util.NameConverter;

public class BattleFacade implements Serializable {
    private ArrayList<BattleResponse> battleResponses = new ArrayList<>();

    public void battle(Game game, Player player, Enemy enemy) {
        double initialPlayerHealth = player.getHealth();
        double initialEnemyHealth = enemy.getHealth();
        String enemyString = NameConverter.toSnakeCase(enemy);

        List<BattleItem> battleItems = player.getItems(BattleItem.class);
        BattleStatistics playerBuff = new BattleStatistics(0, 0, 0, 1, 1);
        playerBuff = calculateAllBuffs(game, player, battleItems, playerBuff);

        List<BattleRound> rounds = battleStatValues(player, enemy, playerBuff);
        if (rounds == null)
            return;

        decreaseItemDurability(player, battleItems);
        logBattleResponse(enemyString, rounds, battleItems, player.getEffectivePotion(), initialPlayerHealth,
                initialEnemyHealth);
    }

    private BattleStatistics calculateAllBuffs(Game game, Player player, List<BattleItem> battleItems,
            BattleStatistics playerBuff) {
        Potion effectivePotion = player.getEffectivePotion();
        if (effectivePotion != null) {
            playerBuff = player.applyBuff(playerBuff);
        } else {
            for (BattleItem item : player.getItems(BattleItem.class)) {
                playerBuff = item.applyBuff(playerBuff);
            }
            for (Mercenary mercenary : game.getEntities(Mercenary.class)) {
                if (mercenary.isAllied())
                    playerBuff = mercenary.applyBuff(playerBuff);
            }
        }

        return playerBuff;
    }

    private List<BattleRound> battleStatValues(Player player, Enemy enemy, BattleStatistics playerBuff) {
        BattleStatistics playerBaseStatistics = player.getBattleStatistics();
        BattleStatistics enemyBaseStatistics = enemy.getBattleStatistics();
        BattleStatistics playerBattleStatistics = BattleStatistics.applyBuff(playerBaseStatistics, playerBuff);
        if (!playerBattleStatistics.isEnabled() || !enemyBaseStatistics.isEnabled()) {
            return null;
        }

        List<BattleRound> rounds = BattleStatistics.battle(playerBattleStatistics, enemyBaseStatistics);

        player.setHealth(playerBattleStatistics.getHealth());
        enemy.setHealth(enemyBaseStatistics.getHealth());
        return rounds;
    }

    private void decreaseItemDurability(Player player, List<BattleItem> battleItems) {
        battleItems.stream().filter(InventoryItem.class::isInstance).forEach(item -> item.use(player));
    }

    private void logBattleResponse(String enemyString, List<BattleRound> rounds, List<BattleItem> battleItems,
            Potion effectivePotion, double initialPlayerHealth, double initialEnemyHealth) {
        List<ItemResponse> itemResponses = battleItems.stream()
                .map(Entity.class::cast)
                .map(ResponseBuilder::getItemResponse)
                .collect(Collectors.toList());

        if (effectivePotion != null)
            itemResponses.add(ResponseBuilder.getItemResponse(effectivePotion));

        battleResponses.add(new BattleResponse(
                enemyString,
                rounds.stream()
                        .map(ResponseBuilder::getRoundResponse)
                        .collect(Collectors.toList()),
                itemResponses,
                initialPlayerHealth,
                initialEnemyHealth));
    }

    public List<BattleResponse> getBattleResponses() {
        return battleResponses;
    }
}
