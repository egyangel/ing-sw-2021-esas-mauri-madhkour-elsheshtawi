package it.polimi.ingsw.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.model.DevCard;
import it.polimi.ingsw.model.LeaderCard;
import it.polimi.ingsw.model.Requirement;
import it.polimi.ingsw.model.SpecialAbility;
import it.polimi.ingsw.utility.messages.Event;
import it.polimi.ingsw.utility.messages.Message;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.reflect.Type;
/**
 * Class that converts game objects to JSON representation by google GSON
 * and creates development and leader cards from .json texts
 * */
public class JsonConverter {

    private static final Gson gson = new Gson();

    public static List<DevCard> deserializeDevCards(){
        TextReader textReader= new TextReader();

        List<DevCard> cards;
        String fileAsString = textReader.readMyText("DevCards.json");
        cards = Arrays.asList(gson.fromJson( fileAsString, DevCard[].class));
        return cards;

    }

    public static List<LeaderCard> deserializeLeaderCards(){
        TextReader textReader= new TextReader();
        List<LeaderCard> cards;

        String fileAsString = textReader.readMyText("LeaderCards.json");

        cards = Arrays.asList(gson.fromJson( fileAsString, LeaderCard[].class));

        return cards;

    }

    public static String toJson(Object object){
        return gson.toJson(object);
    }

    // use Message.getObject content in the game
    public static Object fromMsgToObject(Message msg, Class clazz) {
        return gson.fromJson(msg.getJsonContent(), clazz);
    }

    public static Object fromMsgToObject(Message msg, Type type) {
        return gson.fromJson(msg.getJsonContent(), type);
    }

    public static Object fromEventToObject(Event event, Class clazz){
        return gson.fromJson(event.getJsonContent(), clazz);
    }

    public static Object fromEventToObject(Event event, Type type){
        return gson.fromJson(event.getJsonContent(), type);
    }

}
