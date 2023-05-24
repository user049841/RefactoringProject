package dungeonmania.util;

import java.util.Arrays;
import java.util.Iterator;

import dungeonmania.entities.DoorType;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Portal;
import dungeonmania.entities.logic.LightBulb;

public class NameConverter {
    public static String toSnakeCase(Entity entity) {
        String nameBasic = toSnakeCase(entity.getClass().getSimpleName());
        if (entity instanceof Portal) {
            String color = "_" + ((Portal) entity).getColor().toLowerCase();
            return nameBasic + color;
        }
        if (entity instanceof DoorType) {
            String open = ((DoorType) entity).isOpen() ? "_open" : "";
            return nameBasic + open;
        }
        if (entity instanceof LightBulb) {
            String active = ((LightBulb) entity).isActivated() ? "_on" : "_off";
            return nameBasic + active;
        }

        return nameBasic;
    }

    public static String toSnakeCase(String name) {
        String[] words = name.split("(?=[A-Z])");
        if (words.length == 1)
            return words[0].toLowerCase();

        StringBuilder builder = new StringBuilder();
        Iterator<String> iter = Arrays.stream(words).iterator();
        builder.append(iter.next().toLowerCase());

        while (iter.hasNext())
            builder.append("_").append(iter.next().toLowerCase());

        return builder.toString();
    }

    public static String toSnakeCase(Class<?> clazz) {
        return toSnakeCase(clazz.getSimpleName());
    }
}
