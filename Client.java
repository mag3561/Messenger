import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.border.*;


/**
  * Name:      Miguel Angel Galan   <BR>
  * Name:      Terrence Budu-Biney  <BR.
  * Course:    4002-218-01          <BR>
  * Homework:   #13, MCT Server    <BR>
  * Date:         12/12/18              <BR>
  *<BR>
  * Class:         Client               <BR>
  * Purpose:       The purpose of this class is create a client graphical user interface that 
  *                connects to a server to communicate with other users. Some of the features include 
  *                having a scrolling text message area. This program also error handles for input 
  *                from the user. If they do not specify a ip addres or port number it does 
  *                not let them connect as well as not send messages. If the user chooses to quit 
  *                at any time he can do so by clicking the window exit button or file -> exit 
  *                in the menu bar.
  *                                    <BR>
  *                                    <BR>
  * @author Miguel Angel Galan
  * @author Terrence Budu-Biney
  * @version 1.0
  */
   

public class Client extends JFrame implements ActionListener{
   private JPanel topPanel, middlePanel, bottomPanel;
   private JLabel inputIPlabel, inputPortLabel;
   private JTextField ipText, sendText, portText;
   private JTextArea messageArea;
   private JButton connectServer, sendMessage;
   private JMenuBar menuBar = new JMenuBar( );
   private JMenu fileMenu = new JMenu("File");
   private JMenuItem exitMenuItem = new JMenuItem("Exit");
   private BufferedReader serverIn = null;
   private PrintWriter serverOut = null;
   private Socket s = null;

   public static void main(String [ ] args){
      
      new Client().begin();
   }
   
   /**
     * Begin is used to start the program. It creates the graphical user 
     * interface with panels, buttons, scrolling, action listeners, 
     * grid layout and border layout.  
     * <BR>
     *  
     */
   public void begin(){
      
      
      this.setTitle("Miguels and Terrence Client messenger");
      fileMenu.add(exitMenuItem);
      menuBar.add(fileMenu);
      topPanel = new JPanel(new GridLayout(1,4) );
      middlePanel = new JPanel(new BorderLayout( ) );
      bottomPanel = new JPanel(new GridLayout(0,2) );
      
      inputIPlabel = new JLabel("Enter ip addres: ", JLabel.LEFT);
      inputPortLabel = new JLabel("Enter port number: ", JLabel.LEFT);
      
      connectServer = new JButton("Connect");
      connectServer.addActionListener(this);
      
      ipText = new JTextField( );
      portText = new JTextField( );
      messageArea = new JTextArea();
      messageArea.setBorder(new EtchedBorder());
      JScrollPane receivedMessagePane = new JScrollPane(messageArea);
      
      sendText = new JTextField( );
      sendMessage = new JButton("Send");
      sendMessage.addActionListener(this);
      exitMenuItem.addActionListener(this);
      
      
      topPanel.add(inputIPlabel);
      topPanel.add(ipText);
      topPanel.add(inputPortLabel);
      topPanel.add(portText);
      topPanel.add(connectServer);
      
      middlePanel.add(receivedMessagePane);
      
      bottomPanel.add(sendText);
      bottomPanel.add(sendMessage);
      
      
      
      
      this.pack( );
      this.setLocationRelativeTo(this);
      this.setResizable(true);
      this.setJMenuBar(menuBar);
      this.setSize(500,400);
      this.add(topPanel, BorderLayout.NORTH);
      this.add(middlePanel, BorderLayout.CENTER);
      this.add(bottomPanel, BorderLayout.SOUTH);
      this.setVisible(true);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
   }
   
   
   /**
     * action performed is a default constructor for 
     * interface action listener. It takes in one 
     * parameter.  
     * <BR>
     * @param ActionEvent is used to listen for button clicks on the frame.
     * <BR>  
     */
   public void actionPerformed(ActionEvent ae){
   
      switch(ae.getActionCommand( )){
         
         case "Connect": connectServer( );
                         break;
         case "Send"   : sendReceiveMessage( );
                         break;
         case "Exit"   : exitMessenger( );
                         break;
         default : ;
      }
    
   }   
   
   /**
     * Send receive message is used to communicate with the server 
     * it writes out information that is typed into the empty text field 
     * then receives any messages from the server. 
     * <BR>
     */
   public void sendReceiveMessage( ){
      String messageToSend = sendText.getText( );
      
      if(messageToSend.equals(null)){
         String textAreaEmpty = "Enter someing in the send text field";
         JOptionPane.showMessageDialog(this, textAreaEmpty, "Input miss match", 2);
         return;
      }
      
      
      
      try{ 
           
             serverOut.println(messageToSend);
             serverOut.flush( );
             
             String received = serverIn.readLine( );
             messageArea.append("\n"+received);
             
          }
         
          catch(Exception e){
           messageArea.append("\n"+"Connection lost with server: " + e.getMessage() );
          }    
   
   }
   
   
   /**
     * Exit messenger is used to close out the program without erros 
     * if there is no connections when closing the program it prompts the 
     * user there are none. 
     * <BR>
     */
   public void exitMessenger( ){
      try{   
            serverOut.println("Quit");
            serverOut.flush( );
            serverOut.close( );
          } catch(Exception e){ System.out.println("No connections: " + e.getMessage( )); }
     System.exit(0);
     
   }
   
   
   /**
     * Connect server attempts to connect with the specified server and prompts the user there are 
     * servers to connect if the server is not up yet. This also validates the information that is typed 
     * in the jtext field if the ip address does not havae 4 octects it prompts the user to fix the format.
     * It also checks the range of the port if its greater than 65535 it prompts the user to keep it within 
     * range. 
     * <BR>
     */
   public void connectServer( ){
      String ipAddress = ipText.getText( );
      String holder = portText.getText( );
      int port = 0;
      
      
      String [] octets = ipAddress.split("\\.");
      try{
          port = Integer.parseInt(holder);
                   
         }catch(Exception e){
          String inputMissmatch = "Enter a number";
          JOptionPane.showMessageDialog(this, inputMissmatch, "Input miss match", 2);
          return;
         }
         
      if(octets.length != 4){ 
        String octetRange = "Enter an IP address with 4 octects ex: 192.168.2.3";
        JOptionPane.showMessageDialog(this, octetRange, "Input miss match", 2);
        return;
      }
      
      if(port <= 1 || port >= 65535){
            String outRange = "Number is out of range enter from 1-65535";
            JOptionPane.showMessageDialog(this, outRange, "Input miss match", 2);
            return;
      }
      
      if(ipAddress.isEmpty( )){
        String outRange = "Enter an IP address";
        JOptionPane.showMessageDialog(this, outRange, "Input miss match", 2);
        return;
      }    
              
      try{
          s = new Socket(ipAddress, port);
          
          serverIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
          serverOut = new PrintWriter(new BufferedOutputStream(s.getOutputStream()));
          
          messageArea.setText("Establishing connection");
          
          }catch(UnknownHostException u){
            messageArea.setText("Unable to connect to host");
            return;
          }catch(IOException i){
            messageArea.setText("IOException communicating with host");
            return;
          }catch(Exception e){
            messageArea.setText("Exception with host");
            e.printStackTrace( );
            return;
          }
          
      
      
   }
   
   
   
   
}