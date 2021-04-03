package it.polimi.ingsw;



import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.LeaderCard;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import it.polimi.ingsw.model.specialability.SpecialAbility;

public class App {



    public static void main(String[] args) {

        myFunction();
    }

    static void myFunction() {

        List<LeaderCard> listOfCards = new ArrayList<>();
        try (FileReader reader = new FileReader("src/main/java/resouces/LeaderCard.json")) {



            LeaderCard[] extractedJson = new Gson().fromJson(reader, LeaderCard[].class);
            //  System.out.println(extractedJson.length);
            for (int i = 0; i < extractedJson.length; i++) {
                // System.out.println(extractedJson[i].getAbility().getType());
                listOfCards.add(new LeaderCard(extractedJson[i].getRequirements(), extractedJson[i].getVictoryPoints(), extractedJson[i].getAbility()));

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (LeaderCard listOfCard : listOfCards) {
            // System.out.println(extractedJson[i].getAbility().getType());

            System.out.println(listOfCard.getAbility());
        }
    }
}

