package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static utils.RandomUtils.uniform;

public class EncounterWorld {
    static TETile[][] encounterWorld = new TETile[50][50];
    Random r;
    static List<Integer> coins;
    public static int x;
    public static int y;
    private static TERenderer ter;

    public EncounterWorld(TERenderer t) {
        ter = t;
        r = new Random();
        coins = new ArrayList<>();
        newEncounterWorld();
        placeCoins();
    }

    public void newEncounterWorld() {
        TETile tile;
        for (int x = 0; x < 50; x++) {
            for (int y = 0; y < 50; y++) {
                encounterWorld[x][y] = Tileset.NOTHING;
            }
        }
        for (int i = 15; i < 35; i++) {
            for (int j = 15; j < 35; j++) {
                encounterWorld[i][j] = Tileset.FLOOR;
            }
        }
        for (int x = 14; x < 36; x++) {
            for (int y = 14; y < 36; y++) {
                if (encounterWorld[x][y] == Tileset.NOTHING) {
                    encounterWorld[x][y] = Tileset.WALL;
                }
            }
        }
    }

    public void placeCoins() {
        int coinAmount = uniform(r, 6, 11);
        while (coinAmount > 0) {
            int width = uniform(r, 15, 35);
            int height = uniform(r, 15, 35);
            if (encounterWorld[width][height] == Tileset.FLOOR) {
                encounterWorld[width][height] = Tileset.COIN;
                coinAmount--;
                coins.add(0);
            }
        }
    }

    public static void moveUpEncounter() {
        if (encounterWorld[x][y + 1] == Tileset.FLOOR) {
            encounterWorld[x][y + 1] = Tileset.AVATAR;
            encounterWorld[x][y] = Tileset.FLOOR;
            y++;
        } else if (encounterWorld[x][y + 1] == Tileset.COIN) {
            encounterWorld[x][y + 1] = Tileset.AVATAR;
            encounterWorld[x][y] = Tileset.FLOOR;
            y++;
            coins.remove(0);
        }
    }

    public static void moveLeftEncounter() {
           if (encounterWorld[x - 1][y] == Tileset.FLOOR) {
            encounterWorld[x - 1][y] = Tileset.AVATAR;
            encounterWorld[x][y] = Tileset.FLOOR;
            x--;
        } else if (encounterWorld[x - 1][y] == Tileset.COIN) {
            encounterWorld[x - 1][y] = Tileset.AVATAR;
            encounterWorld[x][y] = Tileset.FLOOR;
            x--;
            coins.remove(0);
           }
    }

    public static void moveDownEncounter() {
        if (encounterWorld[x][y - 1] == Tileset.FLOOR) {
            encounterWorld[x][y - 1] = Tileset.AVATAR;
            encounterWorld[x][y] = Tileset.FLOOR;
            y--;
        } else if (encounterWorld[x][y - 1] == Tileset.COIN) {
            encounterWorld[x][y - 1] = Tileset.AVATAR;
            encounterWorld[x][y] = Tileset.FLOOR;
            y--;
            coins.remove(0);
        }
    }

    public static void moveRightEncounter() {
        if (encounterWorld[x + 1][y] == Tileset.FLOOR) {
            encounterWorld[x + 1][y] = Tileset.AVATAR;
            encounterWorld[x][y] = Tileset.FLOOR;
            x++;
        } else if (encounterWorld[x + 1][y] == Tileset.COIN) {
            encounterWorld[x + 1][y] = Tileset.AVATAR;
            encounterWorld[x][y] = Tileset.FLOOR;
            x++;
            coins.remove(0);
        }
    }



}
