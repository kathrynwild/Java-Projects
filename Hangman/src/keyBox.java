import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class keyBox extends JFrame implements ActionListener{

    // instance variables to get keyword
    static JTextField t;
    static JFrame f;
    static JButton b;
    static JLabel l;


    keyBox(){ };

    //method to create textfield to get keyword
    public static void createKey(){
        f = new JFrame("Guess word");
        l = new JLabel("Please enter a word to be guessed");
        b = new JButton("submit");
        keyBox te = new keyBox();
        b.addActionListener(te);
        t = new JTextField(16);
        JPanel panel = new JPanel();

        panel.add(t);
        panel.add(b);
        panel.add(l);
        f.add(panel);
        f.setSize(300, 100);
        f.setVisible(true);
    }

    // create frame to create in keyword
    public void actionPerformed(ActionEvent e)
    {
        String s = e.getActionCommand();
        if (s.equals("submit")) {
            Hangman.setKey(t.getText());
            f.setVisible(false);
            Hangman.letters();
        }
    }

}
