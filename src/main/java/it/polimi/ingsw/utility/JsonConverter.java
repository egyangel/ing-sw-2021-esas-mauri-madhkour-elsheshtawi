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

public class JsonConverter {
    private static final Gson gson = new Gson();




    public static List<DevCard> deserializeDevCards(){
        TextReader textReader= new TextReader();
        System.out.println("deserializeDevCards");
        List<DevCard> cards;

        String fileAsString = textReader.readMyText("DevCards.json");
       // System.out.println(fileAsString);

        cards = Arrays.asList(gson.fromJson( fileAsString, DevCard[].class));

        System.out.println("DevCards length" + cards.size());
        return cards;
       /*
        try(JsonReader reader = new JsonReader(new FileReader("src/main/resources/DevCards.json"))) {
            cards = Arrays.asList(gson.fromJson(reader, DevCard[].class));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cards;*/
    }

    public static List<LeaderCard> deserializeLeaderCards(){
        TextReader textReader= new TextReader();
        System.out.println("deserializeLeaderCards");
        List<LeaderCard> cards;

        String fileAsString = textReader.readMyText("LeaderCards.json");
        //System.out.println(fileAsString);
        cards = Arrays.asList(gson.fromJson( fileAsString, LeaderCard[].class));

        System.out.println("Leader length" + cards.size());
        return cards;
        /*
       // List<LeaderCard> cards = null;
        try(JsonReader reader = new JsonReader(new FileReader("src/main/resources/LeaderCards.json"))) {
            cards = Arrays.asList(gson.fromJson(reader, LeaderCard[].class));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cards;*/
    }

    private static String serializeDevCard(DevCard devcard) {
        return gson.toJson(devcard);
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

    // DEBUG METHODS

    /*
    public static List<Requirement> deserializeRequirements(){
        List<Requirement> reqs = new ArrayList<>();
        try(JsonReader reader = new JsonReader(new FileReader("src/main/resources/RequirementList.json"))) {
            reqs = Arrays.asList(gson.fromJson(reader, Requirement[].class));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reqs;
    }

    public static List<SpecialAbility> deserializeSpecialAbilities() {
        List<SpecialAbility> abis = new ArrayList<>();
        try(JsonReader reader = new JsonReader(new FileReader("src/main/resources/AbilityList.json"))) {
            abis = Arrays.asList(gson.fromJson(reader, SpecialAbility[].class));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return abis;
    }
     */

    /*
    public static List<LeaderCard> deserializeLeaderCardsOldVersion() {
        List<LeaderCard> listOfCards = new ArrayList<>();
        LeaderCard[] extractedJson;

        try (FileReader reader = new FileReader("src/main/resources/LeaderCards.json")) {
            Gson g = (new GsonBuilder()).registerTypeAdapterFactory(
                    RuntimeTypeAdapterFactory
                            .of(SpecialAbility.class, "type")
                            .registerSubtype(Discount.class, "Discount")
                            .registerSubtype(AdditionalProduction.class, "AdditionalProduction")
                            .registerSubtype(ConvertWhiteMarble.class, "ConvertWhiteMarble")
                            .registerSubtype(ExstraSlot.class, "ExstraSlot")
            ).create();

            extractedJson = g.fromJson(reader, LeaderCard[].class);

            for (int i = 0; i < extractedJson.length; i++){
                // System.out.println(extractedJson[i].getAbility().getType());
                listOfCards.add(new LeaderCard(extractedJson[i].getRequirement(), extractedJson[i].getVictoryPoints(), extractedJson[i].getAbility()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return listOfCards;
    }
    */
}
