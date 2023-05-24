package dungeonmania.map;

import org.json.JSONObject;

import dungeonmania.entities.EntityFactory;

public class GraphNodeFactory {
    public static GraphNode createEntity(JSONObject jsonEntity, EntityFactory factory) {
        return new GraphNode(factory.createEntity(jsonEntity));
    }
}
