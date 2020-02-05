import javax.swing.*;
import java.awt.*;

public class DrawMan extends JPanel {
    int count = Hangman.getCt();

    //draw the head
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.WHITE);
        g.setColor(Color.BLACK);

        //head
        if(Hangman.getCt() >0){
            g.drawOval(350, 10, 50, 50);
        }

        //body
        if(Hangman.getCt() > 1){
            g.drawLine(375, 60, 375, 100);
        }

        //left arm
        if(Hangman.getCt() > 2){
            g.drawLine(375, 80, 325, 60);
        }

        //right arm
        if(Hangman.getCt() > 3){
            g.drawLine(375, 80, 425, 60);
        }

        //left leg
        if(Hangman.getCt() > 4){
            g.drawLine(375, 100, 325, 140);
        }

        //right leg
        if(Hangman.getCt() > 5){
            g.drawLine(375, 100, 425, 140);
        }
    }

}


