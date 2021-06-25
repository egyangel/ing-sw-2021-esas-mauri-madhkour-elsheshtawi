package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.DevCard;
import it.polimi.ingsw.model.LeaderCard;
import it.polimi.ingsw.model.PersonalBoard;
import it.polimi.ingsw.model.Requirement;

import java.util.*;

public class RequirementChecker {
    private static DevCard.CardColor colorOne;
    private static DevCard.CardColor colorTwo;
    private static Map<DevCard.CardColor, Boolean> colorToResult;
    private static List<DevCard.CardColor> colorList;
    private static List<DevCard> devCardList;

    public static boolean check(PersonalBoard board, LeaderCard card){
        Requirement requirement = card.getRequirement();
        boolean result = false;
        switch (requirement.getType()){
            case TWOCARD:
                setColorsOfReq(requirement);
                setDevCardList(board);
                setDevCardColorList();
                result = checkForTwoColors();
                break;
            case THREECARD:
                break;
            case LEVELTWOCARD:
                break;
            case RESOURCES:
                break;
        }
        return result;
    }

    private static void setColorsOfReq(Requirement req){
        colorOne = req.getColor(0);
        colorTwo = req.getColor(1);
    }

    private static void setDevCardList(PersonalBoard board){
        devCardList = new ArrayList<>();
        devCardList.addAll(board.getOwnedCards());
    }

    private static void setDevCardColorList(){
        colorList = new ArrayList<>();
        for(DevCard card: devCardList){
            colorList.add(card.getColor());
        }
    }

    private static boolean checkForTwoColors(){
        colorToResult = new HashMap<>();
        colorToResult.put(colorOne, false);
        colorToResult.put(colorTwo, false);
        for(DevCard.CardColor color: colorList){
            if (color == colorOne){
                colorToResult.replace(colorOne, true);
            }
            if (color == colorTwo){
                colorToResult.replace(colorTwo, true);
            }
        }
        Set<Boolean> resultSet = new HashSet<>(colorToResult.values());
        if(resultSet.contains(false)) return false;
        else return true;
    }
}
