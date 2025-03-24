package core;
import tileengine.TERenderer;


import java.io.File;
import java.io.IOException;


import static utils.RandomUtils.uniform;


public class Main {
    public static void main(String[] args) throws IOException {

        File file = new File("savedWorld.txt");
        GamePlay.makeFile(String.valueOf(file));

        File cellTrackerFile = new File("cellTracker.txt");
        //WorldLoad.clearFile(cellTrackerFile);


        TERenderer ter = new TERenderer();
        ter.initialize(50, 50);
        Menu menu = new Menu();


        Menu.renderMainMenu();
        Menu.readMainInput();
        ter.resetFont();


        int seed = menu.getSeed();
        World test;


        if (Menu.loadGame && !Menu.newGame) {
            GamePlay.loadCellTracker(cellTrackerFile);
            Menu.seed = WorldLoad.loadSeed(String.valueOf(file));
            GamePlay.clearFile(cellTrackerFile);
            test = WorldLoad.loadWorld(String.valueOf(file), Menu.seed, menu, ter);;
            ter.renderFrame(test.world);
            GamePlay.moveAvatar(test, ter, menu, file);
        } else if (Menu.newGame) {
            WorldLoad.clearFile(file);
            test = new World(seed);
            GamePlay.placeAvatar(test);
            ter.renderFrame(test.world);
            GamePlay.moveAvatar(test, ter, menu, file);

        }








    }










}



