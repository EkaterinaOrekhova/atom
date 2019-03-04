package ru_example;
import java.io.BufferedReader;
import java.util.Random;
import java.util.ArrayList;
import java.io.*;

public class MyClass {
    public static void main(String[] args) {

        Random Random = new Random();
        int rand = Random.nextInt(52976);

        ArrayList<String> list = new ArrayList<>();
        String str;

        try(BufferedReader reader = new BufferedReader(new FileReader("dictionary.txt")))
        {
            while((str = reader.readLine()) != null ){
                if(!str.isEmpty()){
                    list.add(str);
                }}
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }

        str = list.get(rand);

        System.out.println("Hello my friend! I offered a " + str.length() + "-letter word.");

    }
}
