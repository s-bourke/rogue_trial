package graphics;

import core.BlockRefs;
import entities.Player;
import maps.MapGenerator;
import maps.StandardMap;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static core.Direction.*;
import static java.awt.event.KeyEvent.*;
import static java.lang.System.exit;

public class GameWindow extends JFrame {

    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 800;
    private MainMap component;
    private StandardMap currentMap;
    private Player player;

    public GameWindow() {
        setTitle("Rogue Trial");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        KeyListener listener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case VK_W:
                    case VK_UP:
                        currentMap = player.move(currentMap,N);
                        break;
                    case VK_S:
                    case VK_DOWN:
                        currentMap = player.move(currentMap,S);
                        break;
                    case VK_D:
                    case VK_RIGHT:
                        currentMap = player.move(currentMap,E);
                        break;
                    case VK_A:
                    case VK_LEFT:
                        currentMap = player.move(currentMap,W);
                        break;
                    case VK_Q:
                        exit(0);

                }
                component.updateMap(currentMap);
                repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        };

        player = new Player(BlockRefs.xSize / 2, BlockRefs.ySize / 2);

        currentMap = MapGenerator.genStartingMap();

        component = new MainMap(currentMap);
        add(component);
        addKeyListener(listener);

    }

}

