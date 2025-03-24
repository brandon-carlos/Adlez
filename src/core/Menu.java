package core;


import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;


import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

import static core.EncounterWorld.moveLeftEncounter;
import static core.GamePlay.*;


public class Menu {
    private int seedHolder;
    public static int seed;
    public static boolean loadGame = false;
    public static boolean newGame = false;

    public Menu() {
    }

    public int getSeed() {
        return seed;
    }


    public static void renderMainMenu() {
        StdDraw.setPenColor(Color.WHITE);
        Font font = new Font("MonoSpaced", Font.BOLD, 35);
        StdDraw.setFont(font);
        StdDraw.text(25, 40, "CS61B: BYOW");
        font = new Font("Monaco", Font.PLAIN, 25);
        StdDraw.setFont(font);
        StdDraw.text(25, 30, "(N) NEW GAME");
        StdDraw.text(25, 25, "(L) LOAD GAME");
        StdDraw.text(25, 20, "(Q) Quit Game");


        StdDraw.show();
    }

    public static void renderEncounter(TERenderer t) {
        StdDraw.clear(Color.BLACK);
        StdDraw.show();
        StdDraw.setPenColor(Color.WHITE);
        Font font = new Font("Monaco", Font.PLAIN, 25);
        StdDraw.setFont(font);

        StdDraw.text(25, 25, "Collect all the coins in 10 seconds");
        StdDraw.show();
        StdDraw.pause(2000);
        t.resetFont();

        EncounterWorld eWorld = new EncounterWorld(t);
        GamePlay.placeAvatarNewWorld(eWorld);
        t.renderFrame(EncounterWorld.encounterWorld);

        long startTime = System.currentTimeMillis();
        while (true) {
            if (EncounterWorld.coins.isEmpty()) {
                isCell = false;
                break;
            }
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                c = Character.toLowerCase(c);

                if (c == 'w') {
                    EncounterWorld.moveUpEncounter();
                } else if (c == 'a') {
                    EncounterWorld.moveLeftEncounter();
                } else if (c == 's') {
                    EncounterWorld.moveDownEncounter();
                } else if (c == 'd') {
                    EncounterWorld.moveRightEncounter();
                }
                t.renderFrame(EncounterWorld.encounterWorld);

            }

            if (System.currentTimeMillis() - startTime >= 10000) {
                EncounterWorld newWorld = new EncounterWorld(t);
                GamePlay.placeAvatarNewWorld(newWorld);
                t.renderFrame(EncounterWorld.encounterWorld);
                StdDraw.show();
                startTime = System.currentTimeMillis();
            }


        }
    }




    public static void readMainInput() throws IOException {


        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (String.valueOf(c).equals("n") || String.valueOf(c).equals("N")) {
                    newGame = true;
                    renderSeedMenu();
                    moveKeeper.setLength(0);
                    break;
                } else if (String.valueOf(c).equals("l") || String.valueOf(c).equals("L")) {
                    newGame = false;
                    loadGame = true;
                    break;
                } else if (String.valueOf(c).equals("q") || String.valueOf(c).equals("Q")) {
                    for (char ch : moveKeeper.toString().toCharArray()) {
                        saveKeys(ch);
                    }
                    saveSeed(Menu.seed);
                    System.exit(0);
                }
            }
        }
    }


    public static void renderSeedMenu() {
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.clear(Color.BLUE);
        Font font = new Font("MonoSpaced", Font.BOLD, 35);
        StdDraw.setFont(font);
        StdDraw.text(25, 40, "CS61B: BYOW");


        font = new Font("Monaco", Font.PLAIN, 25);
        StdDraw.setFont(font);
        StdDraw.text(25, 30, "Enter seed and press S to start");
        StdDraw.show();


        StringBuilder userInput = new StringBuilder();
        boolean notDone = true;


        while (notDone) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if ((String.valueOf(c).equals("s") || String.valueOf(c).equals("S")) && !userInput.isEmpty()) {
                    notDone = false;
                } else if (String.valueOf(c).equals("\b")) {
                    if (userInput.length() > 0) {
                        userInput.deleteCharAt(userInput.length() - 1);
                    }
                } else if (Character.isDigit(c)){
                    userInput.append(c);
                }


                StdDraw.clear(Color.BLUE);
                StdDraw.text(25, 30, "Enter seed and press S to start");
                StdDraw.text(25, 20, userInput.toString());
                StdDraw.show();
            }
        }
        seed = Integer.parseInt(userInput.toString());
    }


    public static List<String> getMoves(String f) throws FileNotFoundException {
        File file = new File(f);
        List<String> moves = new ArrayList<>();


        if (file.exists() && file.canRead()) {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                for (int i = 0; i < line.length(); i++) {
                    moves.add(String.valueOf(line.charAt(i)));
                }
            }
        }
        return moves;
    }


}



