package it.polimi.ingsw.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.model.DevCard;
import it.polimi.ingsw.model.specialability.*;
import it.polimi.ingsw.utility.messages.CVEvent;
import it.polimi.ingsw.utility.messages.MVEvent;
import it.polimi.ingsw.utility.messages.Message;
import it.polimi.ingsw.utility.messages.VCEvent;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.lang.reflect.Type;

public class JsonConverter {
    private static final Gson gsonForLeaderCard = (new GsonBuilder()).registerTypeAdapterFactory(
            RuntimeTypeAdapterFactory
                    .of(SpecialAbility.class, "type")
                                .registerSubtype(Discount .class, "Discount")
                                .registerSubtype(AdditionalProduction .class, "AdditionalProduction")
                                .registerSubtype(ConvertWhiteMarble .class, "ConvertWhiteMarble")
                                .registerSubtype(ExstraSlot .class, "ExstraSlot")
                ).create();

    private static final Gson gson = new Gson();

    public static List<DevCard> deserializeDevCards(){
        List<DevCard> cards = null;
        try(JsonReader reader = new JsonReader(new FileReader("src/main/resources/DevCards.json"))) {
            cards = Arrays.asList(gson.fromJson(reader, DevCard[].class));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cards;
    }

    private static String serializeDevCard(DevCard devcard) {
        return gson.toJson(devcard);
    }

    public static String toJson(Object object){
        return gson.toJson(object);
    }

    public static Object fromMsgToObject(Message msg, Class clazz) {
        return gson.fromJson(msg.getJsonContent(), clazz);
    }

    public static Object fromMsgToObject(Message msg, Type type) {
        return gson.fromJson(msg.getJsonContent(), type);
    }
}
