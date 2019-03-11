package ru.example;

import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class BullsAndCows {

    private static String timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
    private static final Logger LOGGER = Logger.getLogger(BullsAndCows.class.getName());
    private static FileHandler fh;

    public static void main(String[] args) {

        try {
            boolean flag = true;

            fh = new FileHandler("logfile_" + timeStamp + ".log");
            LOGGER.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            System.out.println("Welcome to Bulls and Cows game!");
            LOGGER.info("Logger started!");

            do {
                flag = playGame();
            }
            while (flag);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static boolean playGame() {

        Scanner in = new Scanner(System.in);
        int[] bullsAndCows;
        String userSting = "";

        int countOfAttempt = 10;

        //String word = "java";     //для тестирования
        String word = getWordFromFile("dictionary.txt");

        //System.out.println("I offered a " + word.length() + "-letter word, your guess?");
        LOGGER.info("I offered a " + word.length() + "-letter word, your guess?");

        for (int i = 0; i < countOfAttempt; i++) {
            userSting = in.nextLine();

            if (userSting.length() != word.length()) {
                //System.out.println("Wrong word length! Launch a new game!");
                LOGGER.info("Wrong word length! Launch a new game!");
                return true;
            }

            bullsAndCows = checkBullsAndCows(userSting, word);

            if (bullsAndCows[0] == word.length()) {
                //System.out.println("You won!");
                LOGGER.info("You won!");
                i = countOfAttempt - 1;
            } else {
                //System.out.println("Bulls: " + bullsAndCows[0] + ";  Cows: " + bullsAndCows[1]);
                LOGGER.info("Bulls: " + bullsAndCows[0] + ";  Cows: " + bullsAndCows[1]);
                if (i == countOfAttempt - 1) {
                    //System.out.println("You lose! Word is: " + word);
                    LOGGER.info("You lose! Word is: " + word);
                }
            }

            if (i == countOfAttempt - 1) {
                System.out.println("Wanna play again? yes/no");
                //LOGGER.info("Wanna play again? yes/no" );
                if (in.nextLine().equals("yes")) {
                    return true;
                }
            }
        }

        return false;
    }

    public static String getWordFromFile(String fileName) {

        Random Random = new Random();
        ArrayList<String> list = new ArrayList<>();
        String str = "";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            while ((str = reader.readLine()) != null) {
                if (!str.isEmpty()) {
                    list.add(str);
                }
            }
        } catch (IOException ex) {
            System.out.println("Error reading file!");
        }

        str = list.get(Random.nextInt(list.size()));
        return str;
    }

    public static int[] checkBullsAndCows(String userString, String word) {

        int[] bullsAndCows = new int[2];
        for (int i = 0; i < word.length(); i++) {
            if (userString.charAt(i) == word.charAt(i)) {
                bullsAndCows[0]++;
            } else {
                if (word.contains(Character.toString(userString.charAt(i))))
                    bullsAndCows[1]++;
            }
        }
        return bullsAndCows;
    }
}
