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

/**
 *
 * @author btowle
 */
public class ThreadedClient  implements Runnable
{
    public Socket con;
    public Boolean writer;
    Scanner jin;
    //
    static SharedMemoryObject intCounter;
    static int myId;
    public ThreadedClient(Socket c, Boolean w)
    {
        con = c;
        writer = w;
        intCounter = new SharedMemoryObject();
        myId = -1;
    }
    @Override
    public void run() 
    {
        System.out.println("Inside Run - Thread started");
         try{
            jin = new Scanner(System.in);

            if(writer)
            {
                ObjectOutputStream out = new ObjectOutputStream(con.getOutputStream());
                while(con.isConnected())
                {
                    boolean isMyTurn = myId == intCounter.getTurn();
                    if(isMyTurn && intCounter.gameStarted){
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
