package graphics;

import core.BlockRefs;
import entities.Player;
import maps.MapGenerator;
import maps.StandardMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainMap extends JComponent {

    private final Image[][] image;
    private Image room;
    private Image start;
    private Image connectionNS;
    private Image connectionEW;
    private Image playerIcon;
    private Image blank;
    private Image mapBorder;

    public MainMap(StandardMap source) {

        image = new Image[BlockRefs.xSize][BlockRefs.ySize];
        try {
            room = ImageIO.read(new File("images/Room.png"));
            connectionEW = ImageIO.read(new File("images/EWCon.png"));
            connectionNS = ImageIO.read(new File("images/NSCon.png"));
            playerIcon = ImageIO.read(new File("images/PlayerIcon.png"));
            start = ImageIO.read(new File("images/Start.png"));
            blank = ImageIO.read(new File("images/Blank.png"));
            mapBorder = ImageIO.read(new File("images/MapBorder.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateMap(source);
    }

    public void updateMap(StandardMap source) {
        ArrayList<String> map = source.getArray();

        File floor = new File("images/Rect.png");
        File wall = new File("images/Wall.png");
        File bush = new File("images/Bush.png");
        File player = new File("images/Player.png");

        try {
            for (int i = 0; i < BlockRefs.xSize; i++) {
                for (int j = 0; j < BlockRefs.ySize; j++) {
                    switch (map.get(j).charAt(i)) {
                        case '/':
                        case '\\':
                        case '-':
                        case '|':
                            image[i][j] = ImageIO.read(wall);
                            break;
                        case '@':
                            image[i][j] = ImageIO.read(bush);
                            break;
                        case ' ':
                            image[i][j] = ImageIO.read(floor);
                            break;
                    }
                }
            }
            image[Player.getPos().getX()][Player.getPos().getY()] = ImageIO.read(player);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g) {
        for (int i = 0; i < BlockRefs.xSize; i++) {
            for (int j = 0; j < BlockRefs.ySize; j++) {
                g.drawImage(image[i][j], i * 46 + 46, j * 46 + 46, this);
            }
        }

       char[][] miniMap = MapGenerator.displayWorldMap();

        int playerX = 0;
        int playerY = 0;

        for (int i = 0; i < miniMap.length; i++) {
            for (int j = 0; j < miniMap[0].length; j++) {
                if (miniMap[i][j] == 'p') {
                    playerX = j;
                    playerY = i;
                    break;
                }
            }
        }

        int offsetX = 550 + 77 - 7 * playerX;
        int offsetY = 50 + 77 - 7 * playerY;


        int lowX = playerX - 10;
        int highX = playerX + 10;

        int lowY = playerY - 10;
        int highY = playerY + 10;


        lowX = Math.max(lowX, 0);
        highX = Math.min(highX, miniMap[0].length);
        lowY = Math.max(lowY, 0);
        highY = Math.min(highY, miniMap.length);

        for (int i = lowY; i < highY; i++) {
            for (int j = lowX; j < highX; j++) {
                switch (miniMap[i][j]) {
                    case 'x':
                        g.drawImage(room, j * 7 + offsetX, i * 7 + offsetY, this);
                        break;
                    case '-':
                        g.drawImage(connectionEW, j * 7 + offsetX, i * 7 + offsetY, this);
                        break;
                    case '|':
                        g.drawImage(connectionNS, j * 7 + offsetX, i * 7 + offsetY, this);
                        break;
                    case 'p':
                        g.drawImage(playerIcon, j * 7 + offsetX, i * 7 + offsetY, this);
                        break;
                    case 's':
                        g.drawImage(start, j * 7 + offsetX, i * 7 + offsetY, this);
                        break;
                    default:
                        g.drawImage(blank, j * 7 + offsetX, i * 7 + offsetY, this);
                        break;

                }
            }
        }
        for (int i = 0; i < 20; i++) {
            g.drawImage(mapBorder, 557, (i + 4) * 7 + 29, this);
            g.drawImage(mapBorder, 550 + 21 * 7, (i + 4) * 7 + 29, this);
        }
        for (int i = 0; i < 21; i++) {
            g.drawImage(mapBorder, 557 + i * 7, 57, this);
            g.drawImage(mapBorder, 557 + i * 7, 19 * 7 + 64, this);
        }
    }
}
