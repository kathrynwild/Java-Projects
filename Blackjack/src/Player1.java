import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/*
 * This creates the Client for the BlackJack game to send data to the Server through buttons and diplay data for the
 * client
 */
public class Player1 extends JFrame{
    private JTextArea displayArea = new JTextArea(); // display information to user
    private ObjectOutputStream output; // output stream to server
    private ObjectInputStream input; // input stream from server
    private String message = ""; // message from server
    private String chatServer; // host server for this application
    private Socket client; // socket to communicate with server
    private JButton hit = new JButton("Hit"); // button for user to 'hit'
    private JButton stay = new JButton("Stay"); // button for user to 'stay'
    private JPanel buttons = new JPanel(); // panel to hold buttons

    /*
     * This constructor holds the <code>'hit'</code> and <code>'stay'</code> buttons to send the server if they would
     * like another card or not
     */
    public Player1() {
        super("Player1");

        hit.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        try // send object to server
                        {
                            output.flush(); // flush data to output
                            sendData("hit");
                            //displayMessage("\n\nanother card\n\n");

                        } // end try
                        catch ( IOException ioException )
                        {
                            displayArea.append( "\nError writing object" );
                        } // end catch
                    }
                }
        );
        stay.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        try // send object to server
                        {
                            output.flush(); // flush data to output
                            sendData("stay");
                            hit.setEnabled(false);
                            stay.setEnabled(false);
                        } // end try
                        catch ( IOException ioException )
                        {
                            displayArea.append( "\nError writing object" );
                        } // end catch
                    }
                }
        );

        buttons.setLayout(new GridLayout(1,2));
        buttons.add(hit, BorderLayout.SOUTH);
        buttons.add(stay, BorderLayout.SOUTH);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        setSize(400, 300); // set window size
        setVisible(true); // show window
    }

    // connect to server and process messages from server
    /*
     * This method starts the client and controls all connections and streams with the server
     */
    public void runClient() {
        try {
            connectToServer(); // create a Socket to make connection
            getStreams(); // get the input and output streams
            processConnection();
        } catch (EOFException eofException) {
            displayMessage("\nPlayer1 terminated connection");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /*
     * This method connects to a Server with a specific socket
     */
    private void connectToServer() throws IOException {
        client = new Socket(InetAddress.getByName(chatServer), 12345);
    }

    /*
     * This method sets up <code>input</code> and <code>output</code> streams
     */
    private void getStreams() throws IOException {
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush(); // flush output buffer to send header information

        input = new ObjectInputStream(client.getInputStream());
    }

    /*
     * This method processes messages from the server about if the turn is finished
     * <p>
     * if turn is over, <code>'hit'</code> and <code>'stay'</code> buttons are disabled
     */
    private void processConnection() throws IOException {
        do {
            try {
                message = (String) input.readObject();
                if(message.contains("finish")){
                    hit.setEnabled(false);
                    stay.setEnabled(false);
                }
                displayMessage("\n" + message); // display message
            } catch (ClassNotFoundException classNotFoundException) {
                displayMessage("\nUnknown object type received");
            }

        } while (!message.equals("SERVER>>> TERMINATE"));
    }

    /*
     * closes streams and sockets
     */
    private void closeConnection() {
        displayMessage("\nClosing connection");

        try {
            output.close(); // close output stream
            input.close(); // close input stream
            client.close(); // close socket
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /*
     * This method sends messages to the Server with the output
     */
    private void sendData(String message) {
        try // send object to server
        {
            output.writeObject(message);
            output.flush(); // flush data to output
            //displayMessage("\nCLIENT>>> " + message);
        } catch (IOException ioException) {
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
                        displayArea.append(messageToDisplay);
                    }
                }
        );
    }
}