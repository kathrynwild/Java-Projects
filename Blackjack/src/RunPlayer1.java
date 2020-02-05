import javax.swing.*;

/*
 * This calls <code>Player1<code/> class constructor to begin Client program
 */
public class RunPlayer1 {
    public static void main(String[] args){
        Player1 cApp = new Player1(); // create client
        cApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cApp.runClient();
    }
}
