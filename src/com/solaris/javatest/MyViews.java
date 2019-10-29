package com.solaris.javatest;


import javax.smartcardio.Card;
import java.util.*;

public class MyViews {
    public static void main(String[] args) {
        Card[] cardDeck = new Card[52];
        List<Card> cardList = Arrays.asList(cardDeck);
        System.out.println("List<Card> type:" + cardList.getClass().getName());
        List<String> settings = Collections.nCopies(100, "DEFAULT");
        Set<String> deepThoughts = Collections.emptySet();
        LinkedList<Integer> intList=new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            intList.add(i);
        }
        Collections.binarySearch(intList,4);//二分查找
    }
}
