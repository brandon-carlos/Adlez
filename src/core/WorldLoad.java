package core;

import tileengine.TERenderer;
import tileengine.Tileset;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static core.GamePlay.xValue;
import static core.GamePlay.yValue;

public class WorldLoad {

    public static World loadWorld(String f, int seed, Menu m, TERenderer t) throws IOException {
        File file = new File(f);
        World world = new World(seed);

        GamePlay.cellTracker.clear();
        if (!file.exists() || !file.canRead()) {
            return world;
        }

        List<String> moves = new ArrayList<>();
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line = bufferedReader.readLine();

        while (line != null) {
            for (char c : line.toCharArray()) {
                moves.add(String.valueOf(c));
            }
            line = bufferedReader.readLine();
        }

        fileReader.close();
        bufferedReader.close();

        GamePlay.placeAvatar(world);

        for (String move : moves) {
            replay(world, move, m, t);
        }

        return world;
    }

    public static void cellRemover(World world, int x, int y) throws IOException {
        for (int i = 0; i < GamePlay.cellTracker.size(); i++) {
            int[] cell = GamePlay.cellTracker.get(i);
            if (cell[0] == x && cell[1] == y) {
                GamePlay.cellTracker.remove(i);
                break;
            }
        }
        GamePlay.saveCellTracker(new File("cellTracker.txt"));


    }

    private static void replay(World world, String move, Menu m, TERenderer t) throws IOException {
        if (move.equals("w")) {
            if (!GamePlay.moveUp(world, m, t)) {
                if (world.world[xValue][yValue + 1] == Tileset.CELL) {
                    cellRemover(world, xValue, yValue + 1);
                    world.world[xValue][yValue + 1] = Tileset.AVATAR;
                    world.world[xValue][yValue] = Tileset.FLOOR;
                    yValue++;

                }
            }
        } else if (move.equals("a")) {
            if (!GamePlay.moveLeft(world, m, t)) {
                if (world.world[xValue - 1][yValue] == Tileset.CELL) {
                    cellRemover(world, xValue - 1, yValue);
                    world.world[xValue - 1][yValue] = Tileset.AVATAR;
                    world.world[xValue][yValue] = Tileset.FLOOR;
                    xValue--;
                }
            }
        } else if (move.equals("s")) {
            if (!GamePlay.moveDown(world, m, t)) {
                if (world.world[xValue][yValue - 1] == Tileset.CELL) {
                    cellRemover(world, xValue, yValue - 1);
                    world.world[xValue][yValue - 1] = Tileset.AVATAR;
                    world.world[xValue][yValue] = Tileset.FLOOR;
                    yValue--;
                }
            }
        } else if (move.equals("d")) {
            if (!GamePlay.moveRight(world, m, t)) {
                if (world.world[xValue + 1][yValue] == Tileset.CELL) {
                    cellRemover(world, xValue + 1, yValue);
                    world.world[xValue + 1][yValue] = Tileset.AVATAR;
                    world.world[xValue][yValue] = Tileset.FLOOR;
                    xValue++;
                }
            }
        }

        if (world.world[xValue][yValue] == Tileset.CELL) {
            world.world[xValue][yValue] = Tileset.FLOOR;
        }
    }

    public static void clearFile(File file) throws IOException {
        if (file.exists() && file.canWrite()) {
            BufferedWriter writer = null;
            writer = new BufferedWriter(new FileWriter(file));
            writer.close();
        }

    }

    public static int loadSeed(String file) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(file));
        scanner.nextLine();
        StringBuilder number = new StringBuilder();
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine();


            for (char c : line.toCharArray()) {
                if (Character.isDigit(c)) {
                    number.append(c);
                } else {
                    break;
                }
            }
        }
        if (number.length() > 0) {
            return Integer.parseInt(number.toString());
        }
        return 0;
    }


}
