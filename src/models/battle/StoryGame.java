package models.battle;

import defentions.Defentions;
import models.deck.Deck;

import java.util.Random;

public class StoryGame {
    public static int endStory=4;
    public Battle story(Player player0, int storyLvl){
        Player player1=null;
        int numberOfFlags=0;
        switch (storyLvl){
            case 1:{
                player1=lvl1();
                numberOfFlags=0;
                break;
            }
            case 2:{
             player1=lvl2();
             numberOfFlags=9;
             break;
            }
            case 3:{
                player1=lvl3();
                numberOfFlags=1;
                break;
            }
        }
        return new Battle(player0,player1,MatchType.convert(storyLvl),numberOfFlags);
    }
    private Player lvl1(){
        int[] cardCodes={201,209,211,211,213,217,218,221,222,226,238,236,240,101,107,110,111,112,118,120};
        Deck deck= Defentions.maker(cardCodes,401,301);
        return new Player(1,"PC",deck,false);
    }
    private Player lvl2(){
        int[] cardCodes={202,203,205,208,212,215,215,219,223,227,230,233,239,102,103,105,109,108,113,119};
        Deck deck= Defentions.maker(cardCodes,418,305);
        return new Player(1,"PC",deck,false);
    }
    private Player lvl3(){
        int[] cardCodes={206,207,210,214,216,216,220,224,225,228,229,231,234,106,110,112,114,115,116,101};
        Deck deck= Defentions.maker(cardCodes,412,307);
        return new Player(1,"PC",deck,false);
    }

    public Battle getRandomBattle(){
        Player player0=null;
        int numberOfFlags=0;
        MatchType matchType = MatchType.kill;
        switch ((new Random().nextInt(3))+1){
            case 1:{
                player0=lvl1();
                matchType = MatchType.kill;
                numberOfFlags=0;
                break;
            }
            case 2:{
                player0=lvl2();
                matchType = MatchType.collectFlag;
                numberOfFlags=9;
                break;
            }
            case 3:{
                player0=lvl3();
                matchType = MatchType.keepFlag;
                numberOfFlags=1;
                break;
            }
        }
        Player player1=null;
        switch ((new Random().nextInt(3))+1){
            case 1:{
                player1=lvl1();
                break;
            }
            case 2:{
                player1=lvl2();
                break;
            }
            case 3:{
                player1=lvl3();
                break;
            }
        }
        player0.setName("PC0");
        player1.setName("PC1");
        return new Battle(player0,player1,matchType,numberOfFlags);
    }

}
