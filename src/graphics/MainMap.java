package graphics;

import core.BlockRefs;
import entities.Player;
import maps.MapGenerator;
import maps.Map;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

class MainMap extends JComponent {

    private final Image[][] image;
    private boolean worldMap;
    private Image room;
    private Image start;
    private Image connectionNS;
    private Image connectionEW;
    private Image playerIcon;
    private Image blank;
    private Image mapBorder;
    private Image treasure;

    public MainMap(Map source) {
        worldMap = false;
        image = new Image[BlockRefs.size][BlockRefs.size];
        try {
            room = ImageIO.read(new File("images/Room.png"));
            connectionEW = ImageIO.read(new File("images/EWCon.png"));
            connectionNS = ImageIO.read(new File("images/NSCon.png"));
            playerIcon = ImageIO.read(new File("images/PlayerIcon.png"));
            start = ImageIO.read(new File("images/Start.png"));
            blank = ImageIO.read(new File("images/Blank.png"));
            mapBorder = ImageIO.read(new File("images/MapBorder.png"));
            treasure = ImageIO.read(new File("images/TreasureRoom.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateMap(source);
    }

    public void updateMap(Map source) {
        char[][] map = source.getArray();

        File floor = new File("images/Rect.png");
        File wall = new File("images/Wall.png");
        File bush = new File("images/Bush.png");
        File player = new File("images/Player.png");
        File chest = new File("images/Chest.png");

        try {
            for (int i = 0; i < BlockRefs.size; i++) {
                for (int j = 0; j < BlockRefs.size; j++) {
                    switch (map[i][j]) {
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
                        case 'c':
                            image[i][j] = ImageIO.read(chest);
                            break;
                    }
                }
            }
            image[Player.getPos().getY()][Player.getPos().getX()] = ImageIO.read(player);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g) {

        char[][] miniMap = MapGenerator.buildWorldMap();
        int playerX = 0;
        int playerY = 0;
        int offsetX;
        int offsetY;

        if (!worldMap) {
            for (int i = 0; i < BlockRefs.size; i++) {
                for (int j = 0; j < BlockRefs.size; j++) {
                    g.drawImage(image[j][i], i * 46 + 46, j * 46 + 46, this);
                }
            }

            for (int i = 0; i < miniMap.length; i++) {
                for (int j = 0; j < miniMap[0].length; j++) {
                    if (miniMap[i][j] == 'p') {
                        playerX = j;
                        playerY = i;
                        break;
                    }
                }
            }

            offsetX = 550 + 77 - 7 * playerX;
            offsetY = 50 + 77 - 7 * playerY;

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
                    g.drawImage(getMiniMapImage(miniMap[i][j]), j * 7 + offsetX, i * 7 + offsetY, this);
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
        } else {
            drawWorldMap(g);
        }
    }

    public void displayWorldMap() {
        worldMap = true;
    }

    public boolean clearWorldMap() {
        if (!worldMap) {
            return true;
        }
        worldMap = false;
        return false;
    }

    private void drawWorldMap(Graphics g) {

        char[][] miniMap = MapGenerator.buildWorldMap();

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

        int offsetX = GameWindow.DEFAULT_WIDTH / 2 - 7 * playerX;
        int offsetY = GameWindow.DEFAULT_HEIGHT / 2 - 7 * playerY;

        for (int i = 0; i < miniMap.length; i++) {
            for (int j = 0; j < miniMap[0].length; j++) {
                g.drawImage(getMiniMapImage(miniMap[i][j]), j * 7 + offsetX, i * 7 + offsetY, this);
            }
        }
    }

    private Image getMiniMapImage(char icon) {
        switch (icon) {
            case 't':
                return treasure;
            case 'x':
                return room;
            case '-':
                return connectionEW;
            case '|':
                return connectionNS;
            case 'p':
                return playerIcon;
            case 's':
                return start;
            default:
                return blank;
        }
    }
}
