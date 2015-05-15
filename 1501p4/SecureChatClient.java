/* CS 1501
–   It opens a connection to the server via a Socket at the server's IP address and 
port. Use 8765 for the port.  Have your client prompt the user for the server name. 
More than likely you will always be using "localhost" for the server name, since you 
will be running the server on your own machine.
–   It creates an ObjectOutputStream on the socket (for writing) and immediately calls 
the flush() method (this technicality prevents deadlock)
–   It creates on ObjectInputStream on the socket (be sure you create this AFTER 
creating the ObjectOutputStream)
–   It receives the server's public key, E, as a BigInteger object
–   It receives the server's public mod value, N, as a BigInteger object
–   It receives the server's preferred symmetric cipher (either "Sub" or "Add"), as a 
String object
–   Based on the value of the cipher preference, it creates either a Substitute object 
or an Add128 object, storing the resulting object in a SymCipher variable. See details 
below on the requirements for the Substitute and Add128 classes.
–   It gets the key from its cipher object using the getKey() method, and then 
converts the result into a BigInteger object. To ensure that the BigInteger is 
positive (a requirement for RSA), use the BigInteger constructor that takes a 
sign-magnitude representation of a BigInteger – see the API for details.
–   It RSA-encrypts the BigInteger version of the key using E and N, and sends the 
resulting BigInteger to the server (so the server can also determine the key – the 
server already knows which cipher will be used)
–   It prompts the user for his/her name, then encrypts it using the cipher and sends 
it to the server. The encryption will be done using the encode() method of the 
SymCipher interface, and the resulting array of bytes will be sent to the server as 
a single object using the ObjectOutputStream.

-      At this point the "handshaking" is complete and the client begins its regular 
execution. The client should have a nice user interface and should allow for 
deadlock-free reading of messages from the user and posting of messages received 
from the server. All messages typed in by the user should be sent to the server 
and should only be posted after they are received back from the server.
-      All messages typed in from the user must be encrypted using the encode() 
method of chosen cipher object, then sent to the server for distribution using 
the ObjectOutputStream.
-      All messages received by the client should be read from the ObjectInputStream 
as byte [] objects. They should then be decrypted using the decode() method of 
the chosen cipher and posted to the client's window / panel. When the client quits 
(either through some functionality in the program or by simply closing the window) 
the message "CLIENT CLOSING" should be sent to the server. This message should be 
encrypted like all other messages, but should not have any prefix (ex: no client 
name). The server will use this special message as a sentinel to close the connection
 with the client.
*/
import java.util.*;
import java.io.*;
import java.math.BigInteger;
import java.net.*;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;

public class SecureChatClient extends JFrame implements Runnable, ActionListener {

    public static final int PORT = 8765;

    ObjectInputStream myReader;
    ObjectOutputStream myWriter;
    JTextArea outputArea;
    JLabel prompt;
    JTextField inputField;
    String myName, serverName;
	Socket connection;
	BigInteger E, N;
	String encType;
	SymCipher cipher = null;

    public SecureChatClient ()
    {
        try {

        serverName = JOptionPane.showInputDialog(this, "Enter the server name: ");  ///localhost
        InetAddress addr =
                InetAddress.getByName(serverName);  
        connection = new Socket(addr, PORT);   // Connect to server with new
                                               // Socket
        myWriter = new ObjectOutputStream(connection.getOutputStream());
        myWriter.flush();	//prevents deadlock
        
        myReader =
             new ObjectInputStream(connection.getInputStream());   // Get Reader and Writer

        E = (BigInteger) myReader.readObject();
        N = (BigInteger) myReader.readObject();
        encType = (String) myReader.readObject();
        
        if (encType.equals("Sub")){
        	cipher = new Substitute();
        	System.out.println("Substitute");
        }
        else {
        	cipher = new Add128();
        	System.out.println("Add128");
        }
        
        BigInteger cipherkey = new BigInteger(1, cipher.getKey());
        System.out.println("symmetric key: "+ cipherkey);
        
        BigInteger encryptedKey = cipherkey.modPow(E,N);
        myWriter.writeObject(encryptedKey);
        myWriter.flush();
        
        
        myName = JOptionPane.showInputDialog(this, "Enter your user name: ");
        byte[] enmyName = cipher.encode(myName);
        myWriter.writeObject(enmyName);   // Send name to Server.  Server will need
        myWriter.flush();                // this to announce sign-on and sign-off
                                    // of clients

        this.setTitle(myName);      // Set title to identify chatter

        Box b = Box.createHorizontalBox();  // Set up graphical environment for
        outputArea = new JTextArea(8, 30);  // user
        outputArea.setEditable(false);
        b.add(new JScrollPane(outputArea));

        outputArea.append("Welcome to the Chat Group, " + myName + "\n");

        inputField = new JTextField("");  // This is where user will type input
        inputField.addActionListener(this);

        prompt = new JLabel("Type your messages below:");
        Container c = getContentPane();

        c.add(b, BorderLayout.NORTH);
        c.add(prompt, BorderLayout.CENTER);
        c.add(inputField, BorderLayout.SOUTH);

        Thread outputThread = new Thread(this);  // Thread is to receive strings
        outputThread.start();                    // from Server

		addWindowListener(
                new WindowAdapter()
                {
                    public void windowClosing(WindowEvent e)
                    { 
                    	try {
                    		String end = "CLIENT CLOSING";
                    		byte[] enend = cipher.encode(end);
							myWriter.writeObject(enend);
							myWriter.flush();
						} 
                    	catch (IOException e1) {
							e1.printStackTrace();
						}
                      System.exit(0);
                     }
                }
            );

        setSize(500, 200);
        setVisible(true);

        }
        catch (Exception e)
        {
            System.out.println("Problem starting client!");
        }
    }

    public void run()
    {
        while (true)
        {
			try {
				byte[] currMsg = (byte[]) myReader.readObject();
				System.out.println("bytes[] received: "+currMsg);
				String decurrMsg = cipher.decode(currMsg);
				outputArea.append(decurrMsg+"\n");

				//System.out.println("decrypted byte[]: ");
				System.out.println("corresponding string: "+decurrMsg);
			}
			catch (Exception e)
			{
				System.out.println(e +  ", closing client!");
				e.printStackTrace();
				break;
			}
        }
        System.exit(0);
    }

    public void actionPerformed(ActionEvent e)
    {
        String currMsg = e.getActionCommand();      // Get input value
        inputField.setText("");
        try {
			System.out.println("original String msg: "+ currMsg);
        	byte[] encurrMsg = cipher.encode(myName + ": " + currMsg);
			myWriter.writeObject(encurrMsg); // Add name and send it
			myWriter.flush();
			
			//System.out.println("corresponding byte[]: ");
			System.out.println("encrypted bytes[]: "+ encurrMsg);
		} catch (IOException e1) {
			e1.printStackTrace();
		}   
    }                                               // to Server

    public static void main(String [] args)
    {
        SecureChatClient JR = new SecureChatClient();
        JR.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
}


