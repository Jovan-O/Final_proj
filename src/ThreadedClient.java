/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

 import java.io.DataInputStream;
 import java.io.DataOutputStream;
 import java.io.IOException;
 import java.io.ObjectInputStream;
 import java.io.ObjectOutputStream;
 import java.net.Socket;
 import java.util.Scanner;
 import java.util.logging.Level;
 import java.util.logging.Logger;
import javafx.application.Platform;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.*;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button; 
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.effect.Reflection;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Font;
import javafx.scene.shape.Circle;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.Animation; 
import javafx.animation.KeyFrame; 
import javafx.util.Duration; 
import javafx.animation.KeyValue;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
 /**
  *
  * @author btowle
  */
 public class ThreadedClient  implements Runnable
 {
     public Socket con;
     public Boolean iswriter;
     Scanner jin;
     static SharedMemoryObject intCounter;
     static int myId;
     private Threading1 threading1;
     public ThreadedClient(Socket con, Boolean iswriter, Threading1 threading1, SharedMemoryObject sharedMemoryObject)
     {
         this.con = con;
         this.iswriter = iswriter;
         this.threading1 = threading1;
         this.intCounter = new SharedMemoryObject(threading1);
         myId = -1;
     }
     
     @Override
     public void run () 
     {
         System.out.println("Inside Run - Thread started");
         Platform.runLater(() -> {
            threading1.addMarker();
        });

          try{
             jin = new Scanner(System.in);
 
             if(iswriter)
             {
                 ObjectOutputStream out = new ObjectOutputStream(con.getOutputStream());
      
                 while(con.isConnected())
                 {
                     boolean isMyTurn = myId == intCounter.getTurn();
                     int pos = intCounter.getMypos();
                     int monsterpos = intCounter.getMonsterpos();
                     if(isMyTurn && intCounter.gameStarted){
                         if(pos>monsterpos&& pos <100)
                         {
                         System.out.println("YOUR TURN");
                         System.out.println("my turn is" + intCounter.getTurn());
                         System.out.println("Enter a guess between 0 and 9: ");
                         //int rand = (int)(Math.random()*requestedNumber);
                         out.writeInt(jin.nextInt());
                         out.flush();
                         //Can use Out
 
                         try{
                             Thread.sleep(500);
                         }catch(InterruptedException e){
                             System.out.println("Problem with sleep on player turn: " + e);
                         }
                     }
                     else if (monsterpos>=pos){
                         System.out.println("You died booo");
                         intCounter.updateTurn();
 
                     }
                     if(pos>=100)
                     {
                         System.out.println("You won pookie");
                     }
                    
                     }
                     else{
                         if(!intCounter.gameStarted){
                             System.out.println("Game hasn't started still waiting on players!!");  
                         } else {
                             System.out.println("Its clients(" + intCounter.getTurn() + ") turn, you have to wait");
                         }
                         try{
                             //stupid logic...
                             while (!isMyTurn || !intCounter.gameStarted){
                                 isMyTurn = myId == intCounter.getTurn();
                                 //System.out.println(isMyTurn + ", " + intCounter.gameStarted);
                                 Thread.sleep(100);
                             }
                         } catch (InterruptedException e){
                             System.out.println("Thread sleep error on wait " + e);
                         }
                     }
                 }
             }
             else
             {
                 ObjectInputStream in = new ObjectInputStream(con.getInputStream());
                 myId = in.readInt();
                 System.out.println("My User ID is: "+myId);
                 while(con.isConnected())
                 {
                     try 
                     {
                         intCounter = (SharedMemoryObject)in.readObject();
                         //Can use In -
                         System.out.println(intCounter);
                     } 
                     catch (ClassNotFoundException ex) 
                     {
                         Logger.getLogger(ThreadedClient.class.getName()).log(Level.SEVERE, null, ex);
                     }
                 }
             }                     
             try
             {
                 System.out.println("Inside Sleeping");
                 Thread.sleep(500);
             }
             catch(InterruptedException ex)
             {
                 //Something really went wrong.
             }
         }
         catch(IOException e)
         {
              System.out.println("ERROR with Data Writer/Reader: "+e.toString());
         }
         System.out.println("Done with while connected"); 

     }
 
     //
 }