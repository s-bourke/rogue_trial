package core;

import graphics.GameWindow;
import maps.MapGenerator;

import java.awt.*;


class RogueTrial {

    public static void main(String[] args) {

        MapGenerator.wipeMaps();
        MapGenerator.makeMapDir();
        GameWindow game = new GameWindow();
        game.getContentPane().setBackground(Color.BLACK);
        game.setVisible(true);


    }

}