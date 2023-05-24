package dungeonmania;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ResponseBuilder;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

public class DungeonManiaController {
    private Game game = null;

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    /**
     * /dungeons
     */
    public static List<String> dungeons() {
        return FileLoader.listFileNamesInResourceDirectory("dungeons");
    }

    /**
     * /configs
     */
    public static List<String> configs() {
        return FileLoader.listFileNamesInResourceDirectory("configs");
    }

    /**
     * /game/new
     */
    public DungeonResponse newGame(String dungeonName, String configName) throws IllegalArgumentException {
        if (!dungeons().contains(dungeonName)) {
            throw new IllegalArgumentException(dungeonName + " is not a dungeon that exists");
        }

        if (!configs().contains(configName)) {
            throw new IllegalArgumentException(configName + " is not a configuration that exists");
        }

        try {
            GameBuilder builder = new GameBuilder();
            game = builder.setConfigName(configName).setDungeonName(dungeonName).buildGame();
            return ResponseBuilder.getDungeonResponse(game);
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * /game/dungeonResponseModel
     */
    public DungeonResponse getDungeonResponseModel() {
        return null;
    }

    /**
     * /game/tick/item
     */
    public DungeonResponse tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
        return ResponseBuilder.getDungeonResponse(game.tick(itemUsedId));
    }

    /**
     * /game/tick/movement
     */
    public DungeonResponse tick(Direction movementDirection) {
        return ResponseBuilder.getDungeonResponse(game.tick(movementDirection));
    }

    /**
     * /game/build
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        List<String> validBuildables = List.of("bow", "shield", "midnight_armour", "sceptre");
        if (!validBuildables.contains(buildable)) {
            throw new IllegalArgumentException("Only bow, shield, midnight_armour and sceptre can be built");
        }

        return ResponseBuilder.getDungeonResponse(game.build(buildable));
    }

    /**
     * /game/interact
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        return ResponseBuilder.getDungeonResponse(game.interact(entityId));
    }

    /**
     * /game/save
     */
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        URL gamesPath = DungeonManiaController.class.getResource("/games");
        String fileName = Base64.getUrlEncoder().encodeToString(name.getBytes());
        File savedGame;
        if (gamesPath == null) {
            String path = DungeonManiaController.class.getResource("/").getPath();
            File gameDirectory = new File(path, "games");
            gameDirectory.mkdir();
            savedGame = new File(gameDirectory, fileName);
        } else {
            savedGame = new File(gamesPath.getPath(), fileName);
        }

        try {
            FileOutputStream out = new FileOutputStream(savedGame);
            ObjectOutputStream gameStream = new ObjectOutputStream(out);
            gameStream.writeObject(game);
            gameStream.close();
            out.close();
        } catch (IOException e) {
            return ResponseBuilder.getDungeonResponse(game);
        }

        return ResponseBuilder.getDungeonResponse(game);
    }

    /**
     * /game/load
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        String fileName = Base64.getUrlEncoder().encodeToString(name.getBytes());
        InputStream in = DungeonManiaController.class.getResourceAsStream("/games/" + fileName);
        if (in == null) {
            throw new IllegalArgumentException(name + " is not a game that exists");
        }

        try {
            ObjectInputStream gameStream = new ObjectInputStream(in);
            this.game = (Game) gameStream.readObject();
            gameStream.close();
            in.close();
            return ResponseBuilder.getDungeonResponse(game);
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalArgumentException(name + " is not a game that exists");
        }
    }

    /**
     * /games/all
     */
    public List<String> allGames() {
        URL gamesPath = DungeonManiaController.class.getResource("/games");
        if (gamesPath == null) {
            return new ArrayList<>();
        }
        String gamesPathString = gamesPath.getPath();
        if (gamesPathString.equals("")) {
            return new ArrayList<>();
        }

        File gamesDirectory = new File(gamesPathString);
        return Arrays.asList(gamesDirectory.listFiles())
            .stream()
            .map(File::getName)
            .map(name -> new String(Base64.getUrlDecoder().decode(name)))
            .collect(Collectors.toList());
    }

    /**
     * /game/new/generate
     */
    public DungeonResponse generateDungeon(
            int xStart, int yStart, int xEnd, int yEnd, String configName) throws IllegalArgumentException {
        if (!configs().contains(configName)) {
            throw new IllegalArgumentException(configName + " is not a configuration that exists");
        }

        try {
            GameBuilder builder = new GameBuilder();
            JSONObject dungeon = DungeonGenerator.generate(xStart, yStart, xEnd, yEnd);
            game = builder.setConfigName(configName).setDungeonName("generated").setDungeon(dungeon).buildGame();
            return ResponseBuilder.getDungeonResponse(game);
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * /game/rewind
     */
    public DungeonResponse rewind(int ticks) throws IllegalArgumentException {
        if (ticks <= 0 || ticks > game.getTick()) {
            throw new IllegalArgumentException("cannot rewind " + ticks + " ticks");
        }
        return ResponseBuilder.getDungeonResponse(game.timeTravel(ticks));
    }
}
