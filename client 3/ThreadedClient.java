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
    SharedMemoryObject intCounter;
    //I can make this static as there will only be two threads on the client.
    static int myId;
    public ThreadedClient(Socket c, Boolean w)
    {
        con = c;
        writer = w;    
    }

    @Override
    public void run() 
    {
        System.out.print("Inside Run - Thread started");
         try{
            jin = new Scanner(System.in);

            if(writer)
            {
                ObjectOutputStream out = new ObjectOutputStream(con.getOutputStream());
                while(con.isConnected())
                { 
                    System.out.println("Inside Writer");
                    System.out.println("Enter a guess between 0 and 9: ");
                    //int rand = (int)(Math.random()*requestedNumber);
                    out.writeInt(jin.nextInt());
                    out.flush();
                    //Can use Out 
                }
            }
            else
            {
                ObjectInputStream in = new ObjectInputStream(con.getInputStream());
                myId = in.readInt();
                System.out.println("My User ID is: "+myId);
                while(con.isConnected())
                {      
                    System.out.println("Inside Reader");
                    try 
                    {
                        
                        intCounter = (SharedMemoryObject)in.readObject();
                        //Can use In.-     
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
}
