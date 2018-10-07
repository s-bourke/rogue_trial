package core;

import graphics.GameWindow;
import maps.MapGenerator;

import java.awt.*;


public class RogueTrial {

    public static void main(String[] args) {

        MapGenerator.wipeMaps();

        GameWindow game = new GameWindow();
        game.getContentPane().setBackground( Color.BLACK );
        game.setVisible(true);


    }

}