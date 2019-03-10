package ru.example;
import java.io.BufferedReader;
import java.util.Random;
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;

public class BullsAndCows {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        int [] bullsAndCows;
        String userSting = "";
        Random Random = new Random();
        int countOfAttempt = 10;
        //String word = "java";     //для тестирования

        String word = getWordFromFile(Random.nextInt(52976));
        System.out.println("Welcome to Bulls and Cows game! I offered a " + word.length() + "-letter word, your guess?");

        for (int i = 0; i < countOfAttempt; i++)
        {
            userSting = in.nextLine();
            bullsAndCows = checkBullsAndCows(userSting, word);

            if (bullsAndCows[0] == word.length()){
                System.out.println("You won!");
                i = countOfAttempt - 1;
            }
            else {
                System.out.println("Bulls: " + bullsAndCows[0] + ";  Cows: " + bullsAndCows[1]);
                if (i == countOfAttempt - 1) {
                    System.out.println("You lose!");
                }
            }

            if (i == countOfAttempt - 1) {
                System.out.println("Wanna play again? yes/no");
                if (in.nextLine().equals("yes")) {
                    word = getWordFromFile(Random.nextInt(52976));
                    System.out.println("I offered a " + word.length() + "-letter word, your guess?");
                    i = -1;
                }
            }
        }

    }

    public static String getWordFromFile(int rand){

        ArrayList<String> list = new ArrayList<>();
        String str = "";

        try(BufferedReader reader = new BufferedReader(new FileReader("dictionary.txt")))
        {
            while((str = reader.readLine()) != null ){
                if(!str.isEmpty()){
                    list.add(str);
                }}
        }
        catch(IOException ex){
            System.out.println("Error reading file!");
        }

        str = list.get(rand);

        return str;
    }

    public static int [] checkBullsAndCows(String userString, String word){

        int [] bullsAndCows = new int [2];
        for(int i = 0; i < word.length(); i++)
        {
            if (userString.charAt(i) == word.charAt(i))
            {
                bullsAndCows[0]++;
            }
            else {
                if (word.contains(Character.toString(userString.charAt(i))))
                    bullsAndCows[1]++;
            }
        }
        return bullsAndCows;
    }
}
