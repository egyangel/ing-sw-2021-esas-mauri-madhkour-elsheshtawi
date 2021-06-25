package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.*;

import java.util.List;
import java.util.Map;

public class ObjectPrinter {
    public static String faithTrackPrinter(Map<PersonalBoard.PopeArea, Boolean> map, int faithPoints){
        StringBuilder sb = new StringBuilder();
        String resetAnsi = "\u001B[0m";
        String redAnsi = "\u001B[35m";
        String greyAnsi = "\033[1;37m";
        String crossAnsi = "\u271e";
        sb.append(faithTrackDrawVP());
        sb.append("\n");
        sb.append(faithTrackDrawTopPart());
        for(int i=0; i<10; i++){
            sb.append("\u2502");
            if (i == faithPoints)
                sb.append(" " + redAnsi + " " + crossAnsi + resetAnsi + " ");
            else
                sb.append(" " + greyAnsi + " " + i + resetAnsi + " ");
            sb.append("\u2502");
        }
        for(int i=10; i<25; i++){
            sb.append("\u2502");
            if (i == faithPoints)
                sb.append(" " + redAnsi + " " + crossAnsi + resetAnsi + " ");
            else
                sb.append(" " + greyAnsi  + i + resetAnsi + " ");
            sb.append("\u2502");
        }
        sb.append("\n");
        sb.append(faithTrackDrawBottomPart());
        sb.append("\n");
        sb.append(faithTrackDrawVaticanReport(map));
        return sb.toString();
    }

    public static String faithTrackDrawVP(){
        StringBuilder sb = new StringBuilder();
        String empty = " ";
        for(int i=0; i<20; i++){
            sb.append(empty);
        }
        sb.append("1VP");
        for(int i=0; i<15; i++){
            sb.append(empty);
        }
        sb.append("2VP");
        for(int i=0; i<15; i++){
            sb.append(empty);
        }
        sb.append("4VP");
        for(int i=0; i<15; i++){
            sb.append(empty);
        }
        sb.append("6VP");
        for(int i=0; i<15; i++){
            sb.append(empty);
        }
        sb.append("9VP");
        for(int i=0; i<14; i++){
            sb.append(empty);
        }
        sb.append("12VP");
        for(int i=0; i<14; i++){
            sb.append(empty);
        }
        sb.append("16VP");
        for(int i=0; i<14; i++){
            sb.append(empty);
        }
        sb.append("20VP");
        return sb.toString();
    }

    public static String faithTrackDrawTopPart(){
        StringBuilder sb = new StringBuilder();
        String yellowAnsi = "\u001B[33m";
        String redAnsi = "\u001B[35m";
        String whiteAnsi = "";
        sb.append(faithTrackDrawOneTop(whiteAnsi));
        sb.append(faithTrackDrawOneTop(whiteAnsi));
        sb.append(faithTrackDrawOneTop(whiteAnsi));
        sb.append(faithTrackDrawOneTop(yellowAnsi));
        sb.append(faithTrackDrawOneTop(whiteAnsi));
        sb.append(faithTrackDrawOneTop(whiteAnsi));
        sb.append(faithTrackDrawOneTop(yellowAnsi));
        sb.append(faithTrackDrawOneTop(whiteAnsi));
        sb.append(faithTrackDrawOneTop(redAnsi));
        sb.append(faithTrackDrawOneTop(yellowAnsi));
        sb.append(faithTrackDrawOneTop(whiteAnsi));
        sb.append(faithTrackDrawOneTop(whiteAnsi));
        sb.append(faithTrackDrawOneTop(yellowAnsi));
        sb.append(faithTrackDrawOneTop(whiteAnsi));
        sb.append(faithTrackDrawOneTop(whiteAnsi));
        sb.append(faithTrackDrawOneTop(yellowAnsi));
        sb.append(faithTrackDrawOneTop(redAnsi));
        sb.append(faithTrackDrawOneTop(whiteAnsi));
        sb.append(faithTrackDrawOneTop(yellowAnsi)); //18
        sb.append(faithTrackDrawOneTop(whiteAnsi));
        sb.append(faithTrackDrawOneTop(whiteAnsi));
        sb.append(faithTrackDrawOneTop(yellowAnsi));
        sb.append(faithTrackDrawOneTop(whiteAnsi));
        sb.append(faithTrackDrawOneTop(whiteAnsi));
        sb.append(faithTrackDrawOneTop(redAnsi));
        sb.append("\n");
        return sb.toString();
    }

    public static String faithTrackDrawOneTop(String colorCode){
        StringBuilder sb = new StringBuilder();
        String resetAnsi = "\u001B[0m";
        sb.append(colorCode + "\u2552\u2550\u2550\u2550\u2550\u2555" + resetAnsi);
        return sb.toString();
    }

    public static String faithTrackDrawBottomPart(){
        StringBuilder sb = new StringBuilder();
        String yellowAnsi = "\u001B[33m";
        String redAnsi = "\u001B[35m";
        String whiteAnsi = "";
        sb.append(faithTrackDrawOneBottom(whiteAnsi));
        sb.append(faithTrackDrawOneBottom(whiteAnsi));
        sb.append(faithTrackDrawOneBottom(whiteAnsi));
        sb.append(faithTrackDrawOneBottom(yellowAnsi));
        sb.append(faithTrackDrawOneBottom(whiteAnsi));
        sb.append(faithTrackDrawOneBottom(whiteAnsi));
        sb.append(faithTrackDrawOneBottom(yellowAnsi));
        sb.append(faithTrackDrawOneBottom(whiteAnsi));
        sb.append(faithTrackDrawOneBottom(redAnsi));
        sb.append(faithTrackDrawOneBottom(yellowAnsi));
        sb.append(faithTrackDrawOneBottom(whiteAnsi));
        sb.append(faithTrackDrawOneBottom(whiteAnsi));
        sb.append(faithTrackDrawOneBottom(yellowAnsi));
        sb.append(faithTrackDrawOneBottom(whiteAnsi));
        sb.append(faithTrackDrawOneBottom(whiteAnsi));
        sb.append(faithTrackDrawOneBottom(yellowAnsi));
        sb.append(faithTrackDrawOneBottom(redAnsi));
        sb.append(faithTrackDrawOneBottom(whiteAnsi));
        sb.append(faithTrackDrawOneBottom(yellowAnsi)); //18
        sb.append(faithTrackDrawOneBottom(whiteAnsi));
        sb.append(faithTrackDrawOneBottom(whiteAnsi));
        sb.append(faithTrackDrawOneBottom(yellowAnsi));
        sb.append(faithTrackDrawOneBottom(whiteAnsi));
        sb.append(faithTrackDrawOneBottom(whiteAnsi));
        sb.append(faithTrackDrawOneBottom(redAnsi));
        return sb.toString();
    }

    public static String faithTrackDrawOneBottom(String colorCode){
        StringBuilder sb = new StringBuilder();
        String resetAnsi = "\u001B[0m";
        sb.append(colorCode + "\u2558\u2550\u2550\u2550\u2550\u255b" + resetAnsi);
        return sb.toString();
    }

    public static String faithTrackDrawVaticanReport(Map<PersonalBoard.PopeArea, Boolean> map){
        StringBuilder sb = new StringBuilder();
        String empty = " ";
        String opening = "\u250d";
        String straight = "\u2501";
        String closing = "\u2511";
        String redAnsi = "\u001B[35m";
        String resetAnsi = "\u001B[0m";
        String tick = "\u2714";
        String cross = "\u2718";
        for(int i=0; i<30; i++){
            sb.append(empty);
        }
        sb.append(redAnsi + opening);
        for(int i=0; i<22; i++){
            sb.append(straight);
        }
        sb.append(closing + resetAnsi);
        for(int i=0; i<18; i++){
            sb.append(empty);
        }
        sb.append(redAnsi + opening);
        for(int i=0; i<28; i++){
            sb.append(straight);
        }
        sb.append(closing + resetAnsi);
        for(int i=0; i<12; i++){
            sb.append(empty);
        }
        sb.append(redAnsi + opening);
        for(int i=0; i<34; i++){
            sb.append(straight);
        }
        sb.append(closing + resetAnsi);
        sb.append("\n");
        for(int i=0; i<39; i++){
            sb.append(empty);
        }
        sb.append("2VP ");
        if(map.get(PersonalBoard.PopeArea.FIRST))
            sb.append(tick);
        else
            sb.append(cross);
        for(int i=0; i<40; i++){
            sb.append(empty);
        }
        sb.append("3VP ");
        if(map.get(PersonalBoard.PopeArea.SECOND))
            sb.append(tick);
        else
            sb.append(cross);
        for(int i=0; i<40; i++){
            sb.append(empty);
        }
        sb.append("4VP ");
        if(map.get(PersonalBoard.PopeArea.THIRD))
            sb.append(tick);
        else
            sb.append(cross);

        return sb.toString();
    }

    public static String drawStrongBox(Resources res){
        Resources res1 = res.cloneThisType(Resources.ResType.COIN);
        Resources res2 = res.cloneThisType(Resources.ResType.STONE);
        Resources res3 = res.cloneThisType(Resources.ResType.SHIELD);
        Resources res4 = res.cloneThisType(Resources.ResType.SERVANT);
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        for(int i = 0; i<13; i++){
            sb.append("\u2509");
        }
        sb.append("\n\u254f " + res1.describeResource() + "  " + res2.describeResource() + " \u254f\n");
        sb.append("\u254f " + res3.describeResource() + "  " + res4.describeResource() + " \u254f\n");
        sb.append("  ");
        for(int i = 0; i<13; i++){
            sb.append("\u2509");
        }
        return sb.toString();
    }

    public static String printLeaders(List<LeaderCard> list, boolean active){
        StringBuilder sb = new StringBuilder();
        if (active) sb.append("Active leader cards: ");
        else sb.append("Inactive leader cards: ");
        if (list.isEmpty()){
            sb.append("none");
        } else {
            for (LeaderCard leader: list){
                sb.append("\n");
                sb.append(leader.describeLeaderCard());
            }
        }
        return sb.toString();
    }

    public static String printDevSlots(List<DevSlot> list){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i<3; i++){
            sb.append(list.get(i).describeDevSlot() + "\n");
        }
        return sb.toString();
    }

    public static String printWarehouse(List<Shelf> list){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i<3; i++){
            sb.append(list.get(i).describeShelfFancy());
        }
        return sb.toString();
    }

    public static String printDevCardMatrixAsList(List<DevCard> list){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i<12; i++){
            sb.append(i+1 + ") " + list.get(i).describeDevCard());
            sb.append("\n");
        }
        return sb.toString();
    }
}
