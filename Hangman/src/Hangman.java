import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class Hangman extends JFrame {
    // instance variables
    static char[] keyword;
    static char[] guessed;
    static int guessNum;
    static int count = 0;
    static int ct = 0;
    static private String key;
    static String guesses = Integer.toString(guessNum);

    //instance variables to play game
    static JFrame frame = new JFrame("Hangman");
    static JLabel label = new JLabel("Guess a letter:");
    static JLabel wordl;
    static JLabel numGuesses = new JLabel("Guesses: " + guesses);
    static JPanel p = new JPanel();
    static JPanel p2 = new JPanel();
    static JPanel guessPanel = new JPanel();
    static JPanel keyPanel = new JPanel();
    static JPanel manPanel = new JPanel();

    // alphabet buttons for game
    static JButton ab, bb, cb, db, eb, fb, gb, hb, ib, jb, kb, lb, mb, nb, ob, pb, qb, rb, sb, tb, ub, vb, wb, xb, yb, zb;
    static JButton[] buttons = {ab = new JButton("a"), bb = new JButton("b"), cb = new JButton("c"),
            db = new JButton("d"), eb = new JButton("e"), fb = new JButton("f"), gb = new JButton("g"),
            hb = new JButton("h"), ib = new JButton("i"), jb = new JButton("j"), kb = new JButton("k"),
            lb = new JButton("l"), mb = new JButton("m"), nb = new JButton("n"), ob = new JButton("o"),
            pb = new JButton("p"), qb = new JButton("q"), rb = new JButton("r"), sb = new JButton("s"),
            tb = new JButton("t"), ub = new JButton("u"), vb = new JButton("v"), wb = new JButton("w"),
            xb = new JButton("x"), yb = new JButton("y"), zb = new JButton("z")};

    //constructor
    Hangman(){ }

    // method to play game
    public static void letters(){
        // give value to instance variable after getting the keyword
        keyword = new char[key.length()];
        guessed = new char[keyword.length];
        wordl = new JLabel(Arrays.toString(guessed) + " ( " + keyword.length + " letters )");
        ButtonHandler handler = new ButtonHandler();
        // set panels and frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 1));
        p.setLayout(new GridLayout(2, 13));
        guessPanel.setLayout(new GridLayout(1,1));
        keyPanel.setLayout(new GridLayout(1,1));
        p2.setLayout(new GridLayout(1, 3));

        int sameLet = 0;
        // make keyword array from keyword String
        for(int i=0; i<key.length();i++){
            keyword[i] = key.charAt(i);

            //calculate number of same letters in word
            for(int j=0; j<key.length(); j++){
                if(i != j){
                    if(keyword[i] == keyword[j]){
                        sameLet++;
                    }
                }
            }
        }

        // guesses is different letters in word + 5 to draw hangman
        guessNum = keyword.length - sameLet + 5;

        // set action to alphabet buttons and add to top panel
        for(int i=0; i<buttons.length; i++){
            buttons[i].addActionListener(handler);
        }
        p.add(label); // "guess letter" before alphabet buttons
        for(int i =0; i<buttons.length; i++){
            p.add(buttons[i]);
        }

        // create subpanels for keyword and number of guess and add to middle panel
        keyPanel.add(wordl);
        guessPanel.add(numGuesses);
        p2.add(guessPanel, BorderLayout.EAST);
        p2.add(keyPanel, BorderLayout.CENTER);

        // add panels to frame and show frame
        frame.add(p);
        frame.add(p2);
        frame.setSize(1000, 1000);
        frame.setVisible(true);
    }

    private static class ButtonHandler implements ActionListener {
        // handle button event
        @Override
        public void actionPerformed(ActionEvent event) {
            //decrease number of guesses after each button press
            guessNum--;
            // reload number of guesses
            guessPanel.remove(numGuesses);
            guesses = Integer.toString(guessNum);
            numGuesses = new JLabel("Guesses: " + guesses);
            guessPanel.add(numGuesses, BorderLayout.EAST);
            frame.revalidate();
            frame.repaint();

            //if the alphabet button pressed is in the keyword, add it to the guessed word
            for(int i =0; i<keyword.length;i++){
                if(Character.toString(keyword[i]).equals(event.getActionCommand())){
                    count++;

                    guessed[i] = keyword[i];
                    // reload guessed word
                    keyPanel.remove(wordl);
                    wordl = new JLabel(Arrays.toString(guessed));
                    keyPanel.add(wordl, BorderLayout.CENTER);
                    frame.revalidate();
                    frame.repaint();
                }
            }

            // remove alphabet button after it is pressed
            if(event.getActionCommand() == "a"){ ab.setVisible(false); }
            else if(event.getActionCommand() == "b"){ bb.setVisible(false); }
            else if(event.getActionCommand() == "c"){ cb.setVisible(false); }
            else if(event.getActionCommand() == "d"){ db.setVisible(false); }
            else if(event.getActionCommand() == "e"){ eb.setVisible(false); }
            else if(event.getActionCommand() == "f"){ fb.setVisible(false); }
            else if(event.getActionCommand() == "g"){ gb.setVisible(false); }
            else if(event.getActionCommand() == "h"){ hb.setVisible(false); }
            else if(event.getActionCommand() == "i"){ ib.setVisible(false); }
            else if(event.getActionCommand() == "j"){ jb.setVisible(false); }
            else if(event.getActionCommand() == "k"){ kb.setVisible(false); }
            else if(event.getActionCommand() == "l"){ lb.setVisible(false); }
            else if(event.getActionCommand() == "m"){ mb.setVisible(false); }
            else if(event.getActionCommand() == "n"){ nb.setVisible(false); }
            else if(event.getActionCommand() == "o"){ ob.setVisible(false); }
            else if(event.getActionCommand() == "p"){ pb.setVisible(false); }
            else if(event.getActionCommand() == "q"){ qb.setVisible(false); }
            else if(event.getActionCommand() == "r"){ rb.setVisible(false); }
            else if(event.getActionCommand() == "s"){ sb.setVisible(false); }
            else if(event.getActionCommand() == "t"){ tb.setVisible(false); }
            else if(event.getActionCommand() == "u"){ ub.setVisible(false); }
            else if(event.getActionCommand() == "v"){ vb.setVisible(false); }
            else if(event.getActionCommand() == "w"){ wb.setVisible(false); }
            else if(event.getActionCommand() == "x"){ xb.setVisible(false); }
            else if(event.getActionCommand() == "y"){ yb.setVisible(false); }
            else if(event.getActionCommand() == "z"){ zb.setVisible(false); }

            // if button pressed is not in keyword
            if(Arrays.toString(keyword).contains(event.getActionCommand()) == false){
                //create hangman panel
                DrawMan head = new DrawMan();

                //update hangman panel for each wrong letter
                if(ct == 0){
                    frame.add(head);
                }
                if(ct > 0){
                    head.revalidate();
                    head.repaint();
                }
                ct++;
            }

            // if all letters are guessed, show game is won
            if(count == keyword.length){
                frame.setVisible(false);

                JLabel win = new JLabel("GAME WON");
                win.setFont(new Font("TimesRoman",Font.BOLD, 150));

                JFrame winFrame = new JFrame();
                winFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                winFrame.add(win);
                winFrame.setSize(1000, 300);
                winFrame.setVisible(true);
            }
            // if player runs out of guesses, show game is over
            else if(guessNum == 0){
                frame.setVisible(false);
                JLabel lose = new JLabel("GAME LOST, word was: " + key);
                lose.setFont(new Font("TimesRoman",Font.BOLD, 50));

                JFrame loseFrame = new JFrame();
                loseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loseFrame.add(lose);
                loseFrame.setSize(700, 300);
                loseFrame.setVisible(true);
            }
        }
    }

    // set the keyword
    public static void setKey(String s){ key = s; }

    public static int getCt(){
        return ct;
    }

}
