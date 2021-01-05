import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Random;

/*
 * This creates the Server for the BlackJack game to deal cards to the client <code>Player1</code>, show the dealers
 * cards, and display the winner
 */
public class BJServer extends JFrame {
    private JTextArea displayArea = new JTextArea(); // display area for user
    private ObjectOutputStream output; // output stream to client
    private ObjectInputStream input; // input stream from client
    private ServerSocket server; // server socket
    private Socket connection; // connection to client
    private int counter = 1; // number of connections
    private JButton deal = new JButton("Deal Cards"); // button for user to deal cards
    String[] p1FaceCards = {" ", " " ," ", " " ," ", " " ," ", " " ," ", " " }; // array to hold the players face cards
    int[] p1Cards = {-1,-1,-1,-1,-1,-1,-1,-1}; // array to hold the players number value cards
    int[] dealerCards = {-1,-1,-1,-1,-1,-1,-1,-1}; // array to hold the dealers face cards
    String[] dealerFaceCards = {" ", " " ," ", " " ," ", " " ," ", " " ," ", " " }; // array to hold the players number value cards
    int count = 0; // keeps track of the number of times client clicks the 'hit'' button
    Random rand = new Random();

    /*
     * This constructor holds the <code>'deal'</code> button to send the client information about their cards and
     * display the dealer's cards
     */
    public BJServer() {
        super("Server");

        // create deal button and set interactive
        deal.addActionListener(
                new ActionListener() {
                    // send message to client
                    public void actionPerformed( ActionEvent event )
                    {
                        //once cards dealt, make button still
                        deal.setEnabled(false);

                        //set player card int and String arrays to keep track of cards and send them to display on
                        //player/client screen
                        sendData("Your cards: ");
                        p1Cards[0] = rand.nextInt(12);
                        p1FaceCards[0] = Deck.getFace(p1Cards[0]);
                        p1Cards[1] = rand.nextInt(12);
                        p1FaceCards[1] = Deck.getFace(p1Cards[1]);
                        sendData(p1FaceCards[0]);
                        sendData(p1FaceCards[1]);

                        //set dealer card int and String arrays to keep track of cards and display on
                        //dealer/server screen
                        dealerCards[0] = rand.nextInt(12);
                        dealerFaceCards[0] = Deck.getFace(dealerCards[0]);
                        dealerCards[1] = rand.nextInt(12);
                        dealerFaceCards[1] = Deck.getFace(dealerCards[1]);
                        displayMessage("\n" +dealerFaceCards[0]);
                        displayMessage("\n" +dealerFaceCards[1]);

                    }
                }
        );

        //add dealer button to bottom of screen
        add(deal,BorderLayout.SOUTH);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        setSize(300, 300);
        setVisible(true);

    }

    /*
     * This method starts the Server and controls all connections and streams with its clients
     */
    public void runServer() {
        try {
            //create BJserver socket
            server = new ServerSocket(12345, 100);

            //set Server to wait for a player/client to connect
            while (true) {
                try {
                    waitForConnection();
                    getStreams(); // get input & output streams
                    processConnection();
                }
                catch (EOFException eofException) {
                    displayMessage("\nServer terminated connection");
                }
                finally {
                    closeConnection();
                    ++counter; //add a player
                }
            }
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /*
     * This method waits for connections to arrive, then displays the player connection
     */
    private void waitForConnection() throws IOException {
        displayMessage("Waiting for connection\n");
        connection = server.accept(); // allow server to accept connection
        displayMessage("Player has joined");
        deal.setEnabled(true);
    }

    /*
     * This method sets up <code>input</code> and <code>output</code> streams
     */
    private void getStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush(); // flush output buffer to send header information

        input = new ObjectInputStream(connection.getInputStream());
    }

    /*
     * This method processes messages from the Client about which button was clicked
     * <p>
     * if <code>'hit'</code> button is pressed, then player card arrays are updated and data about the cards is sent
     * back to the Client
     * <p>
     * if <code>'stay'</code> button is pressed, then dealer card arrays are updated and a data about the dealer cards
     * are updated and displayed on screen
     * <p>
     * winner is then displayed along with point totals
     */
    private void processConnection() throws IOException {
        String message = "You have joined the game.";
        sendData(message); // send connection successful message

        do // process messages sent from client
        {
            try {
                message = (String) input.readObject(); // read new message

                int over = 0;
                // if hit button is pressed on client screen
                if(message.contains("hit")){
                    if(count == 0){
                        //add to player card array and display card
                        p1Cards[2] = rand.nextInt(12);
                        p1FaceCards[2] = Deck.getFace(p1Cards[2]);
                        this.sendData(p1FaceCards[2]);
                        count++;
                    }
                    else if(count == 1){
                        p1Cards[3] = rand.nextInt(12);
                        p1FaceCards[3] = Deck.getFace(p1Cards[3]);
                        sendData(p1FaceCards[3]);
                        count++;
                    }
                    else if(count == 2){
                        p1Cards[4] = rand.nextInt(12);
                        p1FaceCards[4] = Deck.getFace(p1Cards[4]);
                        sendData(p1FaceCards[4]);
                        count++;
                    }
                    else if(count == 3) {
                        p1Cards[5] = rand.nextInt(12);
                        p1FaceCards[5] = Deck.getFace(p1Cards[5]);
                        sendData(p1FaceCards[5]);
                        count++;
                    }

                    // if total goes over 21 finish the players turn
                    if(getP1Total() > 21){
                        sendData("finish \n Total exceeded 21 \n YOU LOSE");
                        over = 1;
                    }
                }

                int ct=2;
                if(message.contains("stay") || over == 1){
                    sendData("DEALERS CARDS: ");
                    sendData(dealerFaceCards[0] + "\n" + dealerFaceCards[1]);

                    //dealer plays until total is over 17
                    while(getDealerTotal() <= 17){
                        displayMessage("\n Dealer hits.");
                        Thread.sleep(1000); //slow down so player can watch dealer hit
                        dealerCards[ct] = rand.nextInt(12);
                        dealerFaceCards[ct] = Deck.getFace(dealerCards[ct]);
                        displayMessage("\n" + dealerFaceCards[ct]);
                        sendData(dealerFaceCards[ct]);
                        ct++;
                    }

                    //display winner
                    if((getP1Total() > 21 && getDealerTotal() > 21) || (getP1Total() == getDealerTotal())){
                        displayMessage("\nTie. \n player total was " + getP1Total() + " and " +
                                "dealer total was " + getDealerTotal() + ".");
                        sendData("\nTie. \n player total was " + getP1Total() + " and " +
                                "dealer total was " + getDealerTotal() + ".");
                    }
                    else if(getP1Total() > getDealerTotal()){
                        if(getP1Total() <=21){
                            displayMessage("\nPlayer wins. \n player total was " + getP1Total() + " and " +
                                    "dealer total was " + getDealerTotal() + ".");
                            sendData("\nPlayer wins. \n player total was " + getP1Total() + " and " +
                                    "dealer total was " + getDealerTotal() + ".");
                        }
                        else{
                            displayMessage("\nDealer wins. \n player total was " + getP1Total() + " and " +
                                    "dealer total was " + getDealerTotal() + ".");
                            sendData("\nDealer wins. \n player total was " + getP1Total() + " and " +
                                    "dealer total was " + getDealerTotal() + ".");
                        }
                    }
                    else{
                        if(getDealerTotal() <=21){
                            displayMessage("\nDealer wins. \n player total was " + getP1Total() + " and " +
                                    "dealer total was " + getDealerTotal() + ".");
                            sendData("\nDealer wins. \n player total was " + getP1Total() + " and " +
                                    "dealer total was " + getDealerTotal() + ".");
                        }
                        else{
                            displayMessage("\nPlayer wins. \n player total was " + getP1Total() + " and " +
                                    "dealer total was " + getDealerTotal() + ".");
                            sendData("\nPlayer wins. \n player total was " + getP1Total() + " and " +
                                    "dealer total was " + getDealerTotal() + ".");
                        }
                    }
                }

                //displayMessage("\n" + message); // display message
            }
            catch (ClassNotFoundException | InterruptedException classNotFoundException) {
                displayMessage("\nUnknown object type received");
            }
        }
        while (!message.equals("CLIENT>>> TERMINATE"));
    }

    /*
     * closes streams and sockets
     */
    private void closeConnection() {
        displayMessage("\nTerminating connection\n");

        try {
            output.close(); // close output stream
            input.close(); // close input stream
            connection.close(); // close socket
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /*
     * This method sends messages to the Client with the output
     */
    private void sendData(String message) {
        try {
            output.writeObject(message);
            output.flush(); // flush output to client
        }
        catch (IOException ioException) {
            displayArea.append("\nError writing object");
        }
    }

    /*
     * This method uses <code>Runnable</code> to continuously update messages to the display area
     */
    private void displayMessage(final String messageToDisplay) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() // updates displayArea
                    {
                        displayArea.append(messageToDisplay); // append message
                    }
                }
        );
    }

    /*
     * This method calculates the total points players hand and returns it
     *
     * @return dealerTotal      the total number of points in the player's hand
     */
    public int getP1Total() {
        int p1Total = 0;

        // go through player cards array and add the corresponding amount to the player total
        for(int i=0; i<p1Cards.length; i++){
            if(p1Cards[i] < 9 && p1Cards[i] >=0){
                p1Total += (p1Cards[i] + 2);
            }
            else if(p1Cards[i] == 9 || p1Cards[i] == 10 || p1Cards[i] == 11){
                p1Total += (10);
            }
            else if(p1Cards[i] == 12){
                p1Total += (1);
            }
        }

        return p1Total;
    }

    /*
     * This method calculates the total points of the dealers hand and returns it
     *
     * @return dealerTotal      the total number of points in the dealers hand
     */
    public int getDealerTotal() {
        int dealerTotal = 0;

        for(int i=0; i<dealerCards.length; i++){
            if(dealerCards[i] < 9 && dealerCards[i] >=0){
                dealerTotal += (dealerCards[i] + 2);
            }
            else if(dealerCards[i] == 9 || dealerCards[i] == 10 || dealerCards[i] == 11){
                dealerTotal += (10);
            }
            else if(dealerCards[i] == 12){
                dealerTotal += (1);
            }
        }

        return dealerTotal;
    }
}
