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
    public ServerListener()
    { 
        intCounter = new SharedMemoryObject();
 
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
                System.out.println("Listening for new clients.");
                con = serve.accept();
                ex.execute(new ServerThreaded(con,true,intCounter,connectionCount));
                ex.execute(new ServerThreaded(con,false,intCounter,connectionCount));
                connectionCount ++;
            }
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
    }
    
    
    
}
