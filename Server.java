import java.io.*;
import java.net.*;
import java.util.*;


/**
  * Name:      Miguel Angel Galan   <BR>
  * Name:      Terrence Budu-Biney  <BR.
  * Course:    4002-218-01          <BR>
  * Homework:   #13, MCT Server    <BR>
  * Date:         12/12/18              <BR>
  *<BR>
  * Class:         Server               <BR>
  * Purpose:       The purposer of this class is to create multiple threads of connections to clients
  *                in order to create chat based program. The server connects to port 12345 and 
  *                waits for a client to connect befor procceding to the next stage of the program. 
  *                It uses an inner class to handle multiple connection using threads.  
  *                                    <BR>
  *                                    <BR>
  * @author Miguel Angel Galan
  * @author Terrence Budu-Biney
  * @version 1.0
  */

public class Server {

   Vector<PrintWriter> clientMsg = new Vector<PrintWriter>();
   private final int PORT = 12345;
   
   public static void main(String [] args){
      
      new Server().execute();
   
   }
   
   
   /**
     * Execute creates a new server socket and wait till a client conects 
     * it then accepts a client when client is connected. a new server thread 
     * is created and a socket is passed in the parameter to be used in the 
     * inner class.   
     * <BR>
     *  
     */
   public void execute(){
      
      try {
      
         ServerSocket ss = new ServerSocket(PORT);
         
         while(true){
           System.out.println("Waiting for client connection......");
           Socket s = ss.accept();
           System.out.println("Client connecting");
           new ServerThread(s).start();
         
         }
         
      }catch(Exception ex){
        
            System.out.println("Unexpected error: closing server " + ex.getMessage());    
         
      }
   
   
   
   }
   
 /**
   * Name:      Miguel Angel Galan   <BR>
   * Name:      Terrence Budu-Biney  <BR.
   * Course:    4002-218-01          <BR>
   * Homework:   #13, MCT Server    <BR>
   * Date:         12/12/18              <BR>
   *<BR>
   * Class:         ServerThread               <BR>
   * Purpose:       This inner class establises the connection with the client and 
   *                proceeds with the chat communication with clients.   
   *                                    <BR>
   *                                    <BR>
   * @author Miguel Angel Galan
   * @author Terrence Budu-Biney
   * @version 1.0
   */
class ServerThread extends Thread{
      
      Socket s = null;
     
   /**
     * Constructor method that accepts a socket tha is used through the server thread 
     * <BR>  
     */ 
   public ServerThread(Socket cs){
       
       try{
         s = cs;
         System.out.println("Client connected"); 
       
       
       }catch(Exception ex){
         
        System.out.println("Error in connection message: " + ex.getMessage( ));
       
       }
     
     
     }
   /**
     * Run method is a default method that extends from thread. This method collects all connections 
     * from the socket puts it in a array list. The server will continue accepting messages from the 
     * client until the client types in quit to close the connection. When a messge is recieved 
     * a broadcast message is sent using the collection of print writers. 
     * <BR>  
     */   
   public void run(){
         
      try{
           BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
           PrintWriter pw = new PrintWriter(new BufferedOutputStream(s.getOutputStream()));
           clientMsg.add(pw);
         
         while(true){
            
            String msg = br.readLine();
            System.out.println("Server - Message received = " + msg);
            if(msg.equals("quit")){
               br.close();
               pw.close();
               break;
            }
            
            broadcastMessage(msg);
         
          
         }
         
        
        
        }catch(Exception ex){
            
            System.out.println("Client socket closing " + ex.getMessage());
        
        }
     
     
     }
   /**
     * Broadcast message traverses an array of print writers to send a message to each connection it has 
     * established.  
     * <BR> 
     * @param String the message from the client is passed in.  
     */
   private void broadcastMessage(String msg){
        
       for(PrintWriter p : clientMsg){
            
          try{
            
              p.println(msg);
              p.flush();
            
             }catch(Exception ex){
               
               System.out.println("Error message: " + ex.getMessage( ));
            
            }
         
         }
     
     }
   
 }
   
   
   

















}
