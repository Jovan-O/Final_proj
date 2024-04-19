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
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author btowle
 */
public class ServerThreaded implements Runnable
{
    Socket con;
    boolean writer;
    int requestedNumber;
    SharedMemoryObject intCounter;
    int myId;
    
    
    public ServerThreaded(Socket c, boolean w, SharedMemoryObject so, int conId)
    {
        myId = conId;

        con = c;
        writer = w;
        intCounter = so;
    }

    @Override
    public void run()
    {           
        try{
            if(writer)
                {      
                //Any initialization I need to do can be sent here
                //before the while loop
                //The client just needs to 'read' the correct
                //data.
                ObjectOutputStream out = new ObjectOutputStream(con.getOutputStream());
                out.writeInt(myId);
                out.flush();
                while(con.isConnected())
                    {
                         //System.out.println("Are we in the while? as a writer");
                        if(intCounter.getIsDirty())
                        {
                            
                            System.out.println("Sending "+intCounter);
                            out.reset();
                            out.writeObject(intCounter);
                            out.flush();
                            //We have to wait For is Dirty to be set to False 
                            //So other clients can get the update!
                            try
                            {
                                Thread.sleep(500);
                            }
                            catch(InterruptedException ex)
                            {
                                //Something really went wrong.
                            }
                            intCounter.setIsDirty(false);
                        }
                        try
                        {
                            Thread.sleep(500);
                        }
                        catch(InterruptedException ex)
                        {
                            //Something really went wrong.
                        }
                    }
                }
            else
            {
                ObjectInputStream in = new ObjectInputStream(con.getInputStream());
                while(con.isConnected())
                {                               
                    //System.out.println("Are we in the while? as a reader");
                    int temp = in.readInt();
                    System.out.println("We recieved an "+temp);
                    intCounter.addNumber(temp);
                    try
                    {
                        Thread.sleep(500);
                    }
                    catch(InterruptedException ex)
                    {
                        //Something really went wrong.
                    }
                }
            }
        }
        catch(IOException e)
        {
             System.out.println("ERROR with Data Writer/Reader: "+e.toString());
        }
    }
    
}
