package core;
import tileengine.TETile;
import tileengine.Tileset;
import java.util.*;
import static utils.RandomUtils.uniform;


public class World {
    public int size = 50;
    TETile[][] world = new TETile[size][size];
    Random r;
    List<Room> rooms;


    public World(long seed) {
        r = new Random(seed);
        rooms = new ArrayList<>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        int roomAmount = uniform(r, 8,12);
        generateRooms(roomAmount);
        generateBridges();
        fillHoles();
        generateWalls();
        background();

    }

    public boolean isPlaceableRoom(int width, int height, int startX, int startY) {
        for (int x = startX; x < startX + width; x++) {
            for (int y = startY; y < startY + height; y++) {
                if (x >= size || y >= size || world [x][y] == Tileset.FLOOR) {
                    return false;
                }
            }
        }
        return true;
    }

    public void generateRooms(int roomAmount) {
        while (roomAmount > 0) {
            int startX = randomXY();
            int startY = randomXY();
            int height = randomHeightWidth();
            int width = randomHeightWidth();
            if (isPlaceableRoom(width, height,startX, startY)) {
                rooms.add(new Room(width, height, startX, startY));
                generateRoomsHelper(width, height, startX, startY);
                roomAmount--;
            }
        }
        sortRooms(rooms);
    }

    public int randomXY() {
        return uniform(r,1, size - 1);
    }

    public int randomHeightWidth() {
        return uniform(r, 4,size / 5);
    }

    public int[] generateRoomsHelper(int height, int width, int startX, int startY) {
        int endX = Math.min(startX + width, size - 1);
        int endY = Math.min(startY + height, size - 1);

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                world[x][y] = Tileset.FLOOR;
            }
        }
        return randomSideCords(startX, startY, endX, endY);
    }

    public int[] randomSideCords(int startX, int startY, int endX, int endY) {
        int[] result = new int[2];
        //0 top, 1 right, 2 down, 3 left
        int side = uniform(r, 4);

        if (side == 0) {
            result[0] = uniform(r, startX,endX + 1);
            result[1] = endY;
        } else if (side == 1) {
            result[0] = endX;
            result[1] = uniform(r, startY,endY + 1);
        } else if (side == 2) {
            result[0] = uniform(r, startX, endX + 1);
            result[1] = startY;
        } else {
            result[0] = startX;
            result[1] = uniform(r, startY, endY + 1);
        }
        return result;
    }
    public void generateBridges() {
        for (int i = 0; i < rooms.size() - 1; i++) {
            Room room1 = rooms.get(i);
            Room room2 = rooms.get(i + 1);
            generateBridgesHelper(room1.startX, room1.startY, room2.startX, room2.startY);
        }
    }
    public void generateBridgesHelper(int startX, int startY, int endX, int endY) {
        if (endX > startX) {
            generateRoomsHelper(1, endX - startX, startX, startY);
        } else {
            generateRoomsHelper(1, startX - endX, endX, startY);
        }

        if (endY > startY) {
            generateRoomsHelper(endY - startY, 1, endX, startY);
        } else {
            generateRoomsHelper(startY - endY, 1, endX, endY + 1);

        }
    }

    public void generateWalls() {
        TETile tile;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                tile = world[x][y];
                if (tile == Tileset.NOTHING) {
                    if (y < size - 1 && world[x][y + 1] == Tileset.FLOOR) {
                        world[x][y] = Tileset.WALL;
                    } else if (y > 0 && world[x][y - 1] == Tileset.FLOOR) {
                        world[x][y] = Tileset.WALL;
                    }

                    if (x < size - 1 && world[x + 1][y] == Tileset.FLOOR) {
                        world[x][y] = Tileset.WALL;
                    } else if (x > 0 && world[x -1][y] == Tileset.FLOOR) {
                        world[x][y] = Tileset.WALL;
                    }
                }
            }
        }
    }
    public void fillHoles() {
        int sides;
        for (int x = 1; x < size - 1; x++) {
            for (int y = 1; y < size - 1; y++) {
                if (world[x][y] == Tileset.NOTHING) {
                    sides = 0;
                    if (world[x + 1][y] == Tileset.FLOOR) {
                        sides++;
                    } if (world[x][y + 1] == Tileset.FLOOR) {
                        sides++;
                    } if (world[x - 1][y] == Tileset.FLOOR) {
                        sides++;
                    } if (world[x][y - 1] == Tileset.FLOOR) {
                        sides++;
                    } if (sides >= 3) {
                        world[x][y] = Tileset.FLOOR;
                    }
                }
            }
        }
    }
    public class Room {
        int startX, startY, width, height;

        public Room(int width, int height, int startX, int startY) {
            this.width = width;
            this.height = height;
            this.startX = startX;
            this.startY = startY;
        }

        public int[] getConnectingPoint() {
            int side = uniform(r, 4); // 0 = north, 1 = east, 2 = south, 3 = west
            int connectX = 0;
            int connectY = 0;
            int[] result = new int[2];

            if (side == 0) {
                result[0] = uniform(r, startX, startX + width + 1);
                result[1] = startY + height;
            } else if (side == 1) {
                result[0] = startX + width;
                result[1] = uniform(r, startY, startY + height + 1);
            } else if (side == 2) {
                result[0] = uniform(r, startX, startX + width + 1);
                result[1] = startY;
            } else {
                result[0] = startX;
                result[1] = uniform(r, startY, startY + height + 1);
            }
            return result;
        }

    }

    public void sortRooms(List<Room> roomList) {
        Collections.sort(roomList, new Comparator<Room>() {
            @Override
            public int compare(Room room1, Room room2) {
                return Double.compare(distance(room1, roomList.get(0)), distance(room1, roomList.get(0)));
            }
            private double distance(Room room1, Room room2) {
                int x1 = room1.startX;
                int y1 = room1.startY;
                int x2 = room2.startX;
                int y2 = room2.startY;
                return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
            }
        });
    }

    public void background() {
        int backgroundNum = uniform(r, 4);
        TETile backgroundTile;
        if (backgroundNum == 0) { //grass
            backgroundTile = Tileset.GRASS;
        } else if (backgroundNum == 1) {
            backgroundTile = Tileset.WATER;
        } else if (backgroundNum == 2) {
            backgroundTile = Tileset.MOUNTAIN;
        } else {
            backgroundTile = Tileset.SAND;
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (world[i][j] == Tileset.NOTHING) {
                    world[i][j] = backgroundTile;
                }
            }
        }
    }


}