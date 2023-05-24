package dungeonmania;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;

public class GameState implements Serializable {
    private ArrayList<Entity> entities;
    private Player player;

    private GameState(List<Entity> entities, Player player) {
        this.entities = new ArrayList<>(entities);
        this.player = player;
    }

    public static GameState saveState(GameMap map) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream gameOut = new ObjectOutputStream(out);
            gameOut.writeObject(map);

            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            ObjectInputStream gameIn = new ObjectInputStream(in);
            GameMap mapCopy = (GameMap) gameIn.readObject();

            gameOut.close();
            gameIn.close();
            out.close();
            in.close();

            List<Entity> entities = mapCopy.getEntities();
            entities.forEach(entity -> entity.setId(UUID.randomUUID().toString()));

            return new GameState(entities, mapCopy.getPlayer());
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Player getPlayer() {
        return player;
    }
}
