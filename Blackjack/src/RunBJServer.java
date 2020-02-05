import javax.swing.*;

/*
 * This calls <code>BJServer<code/> class constructor to begin Server program
 */
public class RunBJServer {
    public static void main(String[] args){
        BJServer sApp = new BJServer(); // create server
        sApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sApp.runServer();
    }

}