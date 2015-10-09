package DIC.Test;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 5/22/15
 * Time: 10:38 PM
 */
public class GraphicsMemory implements MouseListener {
    private Card[] pack;
    private int storeCountArr[] = new int[8];
    JButton resetButton;
    JTextField turnsField, scoreField;
    JPanel cardsPanel, turnsPanel, scorePanel;
    JFrame frame;
    int win;
    int turns;
    int storePrevElement;
    int storePrevElementPos;

    public GraphicsMemory() {
        frame = new JFrame("MEMORY GAME");
    }

    public void initialize() {
        pack = new Card[16];
        cardsPanel = new JPanel();
        turnsPanel = new JPanel();
        scorePanel = new JPanel();
        win = 0;
        turns = 0;
        storePrevElement = 9;
        storePrevElementPos = 17;
    }

    public void newGame() {
        /*for (int i = 0; i < 16; i++) {
            int cardNum = (int) (Math.random() * 8);
            if (storeCountArr[cardNum] <= 1) {
                storeCountArr[cardNum] = storeCountArr[cardNum] + 1;
                Card card = new Card(cardNum, false);
                card.getBackLabel().addMouseListener(this);
                pack[i] = card;
            } else
                i--;
        }*/

        //make pack
        ArrayList<Card> cardArrayList = new ArrayList<Card>();
        for (int i = 0; i < 8; i++) {
            Card card1 = new Card(i + 1, false);
            cardArrayList.add(card1);

            Card card2 = new Card(i + 1, false);
            cardArrayList.add(card2);
        }
        Collections.shuffle(cardArrayList);
        for (int i = 0; i < 16; i++) {
            pack[i] = cardArrayList.get(i);
            pack[i].getBackLabel().addMouseListener(this);
        }

        frame.setSize(1600, 250);
        turnsPanel.setLayout(new BoxLayout((turnsPanel), BoxLayout.Y_AXIS));
        resetButton = new JButton("RESET");
        resetButton.addMouseListener(this);
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.X_AXIS));
        drawCardPanel();
        cardsPanel.updateUI();

        scoreField = new JTextField(7);
        scoreField.setText("SCORE = 0");
        scoreField.setEditable(false);

        turnsField = new JTextField(7);
        turnsField.setText("TURNS = 0");
        turnsField.setEditable(false);

        scorePanel.add(scoreField);
        turnsPanel.add(turnsField);
        turnsPanel.add(Box.createVerticalStrut(5));
        turnsPanel.add(resetButton);

        cardsPanel.setSize(1600, 100);
        turnsPanel.setSize(100, 100);
        scorePanel.setSize(100, 200);

        Border border = BorderFactory.createLineBorder(Color.ORANGE, 3);
        // set the border of this component
        turnsField.setBorder(border);
        scoreField.setBorder(border);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        frame.getContentPane().add(BorderLayout.NORTH, cardsPanel);
        frame.getContentPane().add(BorderLayout.EAST, turnsPanel);
        frame.getContentPane().add(BorderLayout.CENTER, scorePanel);

        frame.setVisible(true);
        frame.setResizable(false);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    private void drawCardPanel() {
        cardsPanel.removeAll();
        Border border = BorderFactory.createLineBorder(Color.WHITE, 1, true);
        for (int i = 0; i < 16; i++) {
            if (!pack[i].isExposed()) {
                cardsPanel.add(pack[i].getBackLabel());
                pack[i].getBackLabel().setBorder(border);
                cardsPanel.add(Box.createHorizontalStrut(1));
            } else {
                pack[i].getFrontLabel().setBorder(border);
                cardsPanel.add(pack[i].getFrontLabel());
                cardsPanel.add(Box.createHorizontalStrut(1));
            }
        }
    }

    public static void main(String[] args) {
        GraphicsMemory graphicsMemory = new GraphicsMemory();
        graphicsMemory.initialize();
        graphicsMemory.newGame();
        System.out.print("\f");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == resetButton) {
            frame.getContentPane().removeAll();
//            frame = new JFrame();
            initialize();
            newGame();
            frame.getContentPane().revalidate();
            frame.getContentPane().repaint();
//            System.out.print("\f");
        } else {

            int n = getIndexOfLabel((JLabel) e.getSource());
            /** if the previous element matches with the one which the user has given as input
             * and that input element in the exposed list if false then the element is made true
             * the win as well as set_text increases by 1
             */
            System.out.println("card " + n + " clicked");
            if (storePrevElement == pack[n].getNumber() && !pack[n].isExposed()) {
                win = win + 1;
                pack[storePrevElementPos].setExposed(true);
                pack[n].setExposed(true);
            } else
                pack[n].setExposed(true);
            turns = turns + 1;
            scoreField.setText("SCORE = " + String.valueOf(win));
            turnsField.setText("TURNS = " + String.valueOf(turns));
            drawCardPanel();
            cardsPanel.updateUI();
            if (storePrevElementPos == n)
                turns = turns - 1;
            //this checks whether the number of turns is even or odd.
            if (turns % 2 == 0 && (storePrevElement != pack[n].getNumber())) {
                pack[storePrevElementPos].setExposed(false);
                pack[n].setExposed(false);
                storePrevElement = 9;
                storePrevElementPos = 17;
            }
            if ((pack[n].getNumber() != storePrevElement) && (turns % 2 != 0)) {
                storePrevElement = pack[n].getNumber();
                storePrevElementPos = n;
                pack[storePrevElementPos].setExposed(true);
            }
            if (win == 8) {
                seeWinPanel();
            }
        }
    }

    public void seeWinPanel() {
        frame.setVisible(false);

        System.out.println("you win!");
        JFrame winFrame = new JFrame();
        JPanel winPanel = new JPanel();

        winPanel.setBackground(Color.BLACK);

        ImageIcon imgThisImg = new ImageIcon("D:\\Documents\\Documents\\Project 2011\\Project 2011\\Dropbox\\Arnab_personal\\Guria Assignment\\Assignment 5\\animatedfireworks.gif");

        JLabel winLabel = new JLabel(imgThisImg);
        winLabel.setSize(1000, 1000);
        winLabel.setText("GAME OVER");
        winLabel.setHorizontalTextPosition(JLabel.CENTER);
        winLabel.setVerticalTextPosition(JLabel.CENTER);
        winLabel.setForeground(Color.WHITE);

        JLabel scoreLabel = new JLabel(imgThisImg);
        scoreLabel.setSize(1000, 1000);
        scoreLabel.setText("SCORE = " + String.valueOf(turns));
        scoreLabel.setHorizontalTextPosition(JLabel.CENTER);
        scoreLabel.setVerticalTextPosition(JLabel.TOP);
        scoreLabel.setForeground(Color.WHITE);

        winPanel.add(winLabel);
        winPanel.add(scoreLabel);

        winLabel.setIcon(imgThisImg);
        winFrame.getContentPane().add(BorderLayout.CENTER, winPanel);

        winFrame.setSize(500, 500);

        winFrame.setVisible(true);
        winFrame.setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        winFrame.setLocation(dim.width / 2 - winFrame.getSize().width / 2, dim.height / 2 - winFrame.getSize().height / 2);
        winFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public int getIndexOfLabel(JLabel label) {
        int index = 0;
        for (int i = 0; i < pack.length; i++) {
            if (label == pack[i].getBackLabel()) {
                index = i;
            }
        }
        return index;
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}

class Card {
    private int number;
    private boolean isExposed;
    JLabel backLabel;
    JLabel frontLabel;

    Card(int number, boolean isExposed) {

        this.number = number;
        this.isExposed = isExposed;

        //create bacl label.
        this.backLabel = new JLabel();
        backLabel.setSize(83, 150);
        ImageIcon imageIcon1 = new ImageIcon("D:\\Documents\\Documents\\Project 2011\\Project 2011\\Dropbox\\Arnab_personal\\Guria Assignment\\Assignment 5\\card.jpg"); // load the image to a imageIcon
        Image image1 = imageIcon1.getImage(); // transform it
        Image newimg1 = image1.getScaledInstance(83, 150, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon1 = new ImageIcon(newimg1);  // transform it back
        backLabel.setIcon(imageIcon1);

        //create front label.
        frontLabel = new JLabel();
        frontLabel.setSize(83, 150);
        ImageIcon imageIcon2 = new ImageIcon("D:\\Documents\\Documents\\Project 2011\\Project 2011\\Dropbox\\Arnab_personal\\Guria Assignment\\Assignment 5\\" + String.valueOf(number + 1) + ".png"); // load the image to a imageIcon
        Image image2 = imageIcon2.getImage(); // transform it
        Image newimg2 = image2.getScaledInstance(83, 150, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon2 = new ImageIcon(newimg2);  // transform it back
        frontLabel.setIcon(imageIcon2);

    }

    int getNumber() {
        return number;
    }

    void setNumber(int num) {
        number = num;
    }

    boolean isExposed() {
        return isExposed;
    }

    void setExposed(boolean exposed) {
        isExposed = exposed;
    }

    JLabel getBackLabel() {
        return backLabel;
    }

    JLabel getFrontLabel() {
        return frontLabel;
    }
}