/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

 import java.io.IOException;
 import java.net.ServerSocket;
 import java.net.Socket;
 import java.util.concurrent.ExecutorService;
 import java.util.concurrent.Executors;
 
 /**
  *
  * @author btowle
  */
 public class ServerListener
 {
     SharedMemoryObject intCounter;
     ExecutorService ex; 
     ServerSocket serve;
     private Threading1 threading1;
     public ServerListener(Threading1 threading1)
     { 

         this.threading1 = threading1;
         intCounter = new SharedMemoryObject(threading1);
         
  
         try
         {
             serve = new ServerSocket(8888);
         }
         catch (IOException e )
         {
             System.out.println("ERROR Creating the server: "+e.getMessage());
         }
         ex = Executors.newFixedThreadPool(10);
     }
     
     public void BuisnessLogic() 
     {
 
         int connectionCount = 0;
         while(true)
         {
             Socket con;
             try
             {
                 //limits to 10
                 /*if(connectionCount < 10) {
                     System.out.println("Listening for new clients.");
                     con = serve.accept();
                     ex.execute(new ServerThreaded(con, true, intCounter, connectionCount));
                     ex.execute(new ServerThreaded(con, false, intCounter, connectionCount));
                     connectionCount++;
                 }*/
 
                 System.out.println("Listening for new clients.");
                 con = serve.accept();
                 ex.execute(new ServerThreaded(con, true, intCounter, connectionCount));
                 ex.execute(new ServerThreaded(con, false, intCounter, connectionCount));
                 intCounter.addPlayer(connectionCount);
                 connectionCount++;
 
                 // checks to see if number of players is 3 (game size)
                 if(connectionCount == 3){
                     //start game
                     intCounter.gameStart();
                 }
 
             }/*catch (InterruptedException e){
                 System.out.println("ERROR sleeping: "+e.getMessage());
             }*/
             catch(IOException e)
             {
                 System.out.println("ERROR accepting: "+e.getMessage());
                 break;
             }
         }
         try
         {    
            serve.close(); 
            ex.shutdown();
         }
         catch(IOException e)
         {
             System.out.println("ERROR Closing: "+e.getMessage());
 
         }
         ;}
     
     
     
 }