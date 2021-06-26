package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

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
                setColorsOfReq(requirement);
                setDevCardList(board);
                setDevCardColorList();
                result = checkForThreeColors();
                break;
            case LEVELTWOCARD:
                setDevCardList(board);
                result = checkForLevelTwo(requirement);
                break;
            case RESOURCES:
                result = checkForResources(requirement, board);
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

    private static boolean checkForThreeColors(){
        colorToResult = new HashMap<>();
        colorToResult.put(colorOne, false);
        colorToResult.put(colorTwo, false);
        int calls = 0;
        for(DevCard.CardColor color: colorList){
            if (color == colorOne){
                calls++;
                if(calls == 2) {
                    colorToResult.replace(colorOne, true);
                }
            }
            if (color == colorTwo){
                colorToResult.replace(colorTwo, true);
            }
        }
        Set<Boolean> resultSet = new HashSet<>(colorToResult.values());
        if(resultSet.contains(false)) return false;
        else return true;
    }

    private static boolean checkForLevelTwo(Requirement req){
        for(DevCard card: devCardList){
            if((card.getColor() == req.getColor(0)) && (card.getLevel() == 2)){
                return true;
            }
        }
        return false;
    }

    private static boolean checkForResources(Requirement req, PersonalBoard board){
        Resources reqres = req.getResource();
        Resources totalRes = board.getTotalResources();
        if(totalRes.includes(reqres))
            return true;
        return false;
    }
}
