package core;

import core.World.Room;
import static utils.RandomUtils.uniform;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

public class GamePlay {
    public static int xValue;
    public static int yValue;
    private static BufferedWriter load;
    public static StringBuilder moveKeeper = new StringBuilder();
    public static boolean isCell = false;
    public static ArrayList<int[]> cellTracker = new ArrayList<>();

    public static void makeFile(String file) throws IOException {
        load = new BufferedWriter(new FileWriter(file, true));
    }

    public static void saveKeys(char c) throws IOException {
        if (load != null) {
            load.write(c);
            load.flush();
        }
    }

    public static void saveSeed(int seed) throws IOException {
        if (load != null) {
            load.newLine();
            load.write(Integer.toString(seed));
            load.flush();
        }
    }

    public static void placeAvatar(World w) {
        List<Room> rooms = w.rooms;
        Random r = w.r;
        int random = uniform(r, rooms.size());
        Room startingRoom = rooms.get(random);
        rooms.remove(startingRoom);
        while (true) {
            int width = uniform(r, startingRoom.startX, startingRoom.startX + startingRoom.width + 1);
            int height = uniform(r, startingRoom.startY, startingRoom.startY + startingRoom.height + 1);
            if (w.world[width][height] == Tileset.FLOOR) {
                w.world[width][height] = Tileset.AVATAR;
                xValue = width;
                yValue = height;
                break;
            }
        }
        for (Room room : rooms) {
            int width = uniform(r, room.startX, room.startX + room.width);
            int height = uniform(r, room.startY, room.startY + room.height);
            if (w.world[width][height] == Tileset.FLOOR) {
                w.world[width][height] = Tileset.CELL;
                cellTracker.add(new int[]{width, height});
            }
        }

    }

    public static void placeAvatarNewWorld(EncounterWorld e) {
        Random r = e.r;
        while (true) {
            int width = uniform(r, 15, 35);
            int height = uniform(r, 15, 35);
            if (EncounterWorld.encounterWorld[width][height] == Tileset.FLOOR) {
                EncounterWorld.encounterWorld[width][height] = Tileset.AVATAR;
                EncounterWorld.x = width;
                EncounterWorld.y = height;
                break;
            }
        }
    }

    public static void moveAvatar(World w, TERenderer t, Menu m, File f) throws IOException {
        boolean notDone = true;

        while (notDone) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                c = Character.toLowerCase(c);
                if (String.valueOf(c).equals(":")) {
                    while (!StdDraw.hasNextKeyTyped()) { }
                        char d = StdDraw.nextKeyTyped();

                        if (String.valueOf(d).equals("q") || String.valueOf(d).equals("Q")) {
                            WorldLoad.clearFile(f);
                            for (char ch : moveKeeper.toString().toCharArray()) {
                                saveKeys(ch);
                            }
                            saveSeed(Menu.seed);
                            saveCellTracker(new File("cellTracker.txt"));
                            GamePlay.cellTracker.clear();
                            System.exit(0);
                        }

                } else if (String.valueOf(c).equals("w")) {
                    moveUp(w, m, t);
                } else if (String.valueOf(c).equals("a")) {
                    moveLeft(w, m, t);
                } else if (String.valueOf(c).equals("s")) {
                    moveDown(w, m, t);
                } else if (String.valueOf(c).equals("d")) {
                    moveRight(w, m, t);
                }

                if (!isCell) {
                    t.renderFrame(w.world);
                } else {
                    Menu.renderEncounter(t);
                }
            }
        }
    }


    public static boolean moveUp(World w, Menu m, TERenderer t) throws IOException {
        return handleMove(w, xValue, yValue + 1, 'w');
    }

    public static boolean moveLeft(World w, Menu m, TERenderer t) throws IOException {
        return handleMove(w, xValue - 1, yValue, 'a');
    }

    public static boolean moveDown(World w, Menu m, TERenderer t) throws IOException {
        return handleMove(w, xValue, yValue - 1, 's');
    }

    public static boolean moveRight(World w, Menu m, TERenderer t) throws IOException {
        return handleMove(w, xValue + 1, yValue, 'd');
    }

    // Helper method to handle movement logic
    private static boolean handleMove(World w, int newX, int newY, char move) throws IOException {
        if (w.world[newX][newY] == Tileset.FLOOR || w.world[newX][newY] == Tileset.CELL) {
            // Update the avatar's position
            if (w.world[newX][newY] == Tileset.CELL) {
                isCell = true; // Mark as stepping on a CELL
                WorldLoad.cellRemover(w, newX, newY); // Remove CELL after stepping
            } else {
                isCell = false; // Not a CELL tile
            }

            // Move the avatar
            w.world[newX][newY] = Tileset.AVATAR;
            w.world[xValue][yValue] = Tileset.FLOOR;

            // Update coordinates
            xValue = newX;
            yValue = newY;

            // Save the move
            saveKeys(move);
            moveKeeper.append(move);

            return true;
        }
        return false; // Movement not possible
    }


    public static void saveCellTracker(File file) throws IOException {
        clearFile(file);

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (int[] cell : cellTracker) {
            writer.write(cell[0] + "," + cell[1]);
            writer.newLine();
        }
        writer.close();
        System.out.println("CellTracker has been saved.");
    }

    public static void loadCellTracker(File file) throws IOException {
        if (!file.exists()) {
            return;
        }
        cellTracker.clear();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            cellTracker.add(new int[]{x, y});
        }
        reader.close();
    }

    public static void clearFile(File file) throws IOException {
        if (file.exists() && file.canWrite()) {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.close();
        }
    }

}
