package cl.ucn.disc.dsm.fcaimanque.terremotos.adapters;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

//Obtenido de: https://stackoverflow.com/questions/23070298/get-nested-json-object-with-gson-using-retrofit

/**
 * Obtiene la lista de terremotos desde el JSON.
 * @param <T>
 */
public class Deserializer<T> implements JsonDeserializer<T> {
    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement earthquakesData = json.getAsJsonObject().get("features");
        return new Gson().fromJson(earthquakesData, typeOfT);
    }
}
