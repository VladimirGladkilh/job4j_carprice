package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;

/**
 * Класс для преобразования перечислений в мапы для работы в аяксах страницы
 */
public class EnumDataHelper {
    private static final Logger log = LoggerFactory.getLogger(EnumDataHelper.class);

    private static final class Lazy {
        private static final EnumDataHelper INST = new EnumDataHelper();
    }

    public static EnumDataHelper instOf() {
        return EnumDataHelper.Lazy.INST;
    }


    public String getBodyMap() {
        ArrayList<Map<String, String>> maps = new ArrayList<>();
        for (Body value : Body.values()) {
            Map<String, String> map = Map.of("id", value.name(), "name", value.name());
            maps.add(map);
        }
        return createJson(maps);
    }

    public String getEngineTypeMap() {
        ArrayList<Map<String, String>> maps = new ArrayList<>();
        for (EngineType value : EngineType.values()) {
            Map<String, String> map = Map.of("id", value.name(), "name", value.name());
            maps.add(map);
        }
        return createJson(maps);
    }

    public String getGearMap() {
        ArrayList<Map<String, String>> maps = new ArrayList<>();
        for (Gear value : Gear.values()) {
            Map<String, String> map = Map.of("id", value.name(), "name", value.name());
            maps.add(map);
        }
        return createJson(maps);
    }

    public String getPrivodMap() {
        ArrayList<Map<String, String>> maps = new ArrayList<>();
        for (Privod value : Privod.values()) {
            Map<String, String> map = Map.of("id", value.name(), "name", value.name());
            maps.add(map);
        }
        return createJson(maps);
    }

    public String createJson(ArrayList<Map<String, String>> maps) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(maps);
        } catch (JsonProcessingException e) {
            log.error("Ошибка оборачивания перечисления ", e);
            return "";
        }
    }
}
