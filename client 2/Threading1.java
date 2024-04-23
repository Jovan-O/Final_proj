/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.*;


/**
 *
 * @author btowle
 */
public class Threading1 {

    public static void main(String[] args)
    {
        
        Scanner input = new Scanner(System.in);
        System.out.println("Enter S for server, C for Client");
        String choice = input.next();
        if(choice.equals("S"))
        {
            ServerListener server = new ServerListener();
            server.BuisnessLogic();
        }
        else
        {
            try {
                Socket con = new Socket("127.0.0.1",8888);
                ThreadedClient writerS = new ThreadedClient(con,true);
                ThreadedClient readerS = new ThreadedClient(con,false);
                System.out.println("Initiating Writer");
                Thread t1 = new Thread(writerS);
                System.out.println("Initiating Reader");
                Thread t2 = new Thread(readerS);
                System.out.println("Starting thread 1");
                t1.start();
                System.out.println("Startin thread 2");
                t2.start();
                try {
                    t1.join();
                    t2.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Threading1.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
            } catch (IOException ex) 
            {
                Logger.getLogger(Threading1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
      
        /*PC_Queue pcq = new PC_Queue();
        MyFirstThread z = new MyFirstThread("B1",10);
        MyFirstThread x = new MyFirstThread("A1",10);

        
        Thread t1 = new Thread(x);
        Thread t2 = new Thread(z);
    
        t1.start();
       t2.start();

        
        try
        {
            t1.join();
            t2.join();
 
            
        }
        catch(InterruptedException e)
        {
            System.out.println("ERROR: Thread never joined!");
        }
       
        
        
        /*
       MyFirstThread x = new MyFirstThread("Hello World!",500);
       MyFirstThread y = new MyFirstThread("The quick red fox jumped over the lazy brown dog!",200);
       
       Thread t1 = new Thread(x);
       Thread t2 = new Thread(y);
       t1.start();
       t2.start();
       try
       {
        t1.join();
        t2.join();
       }
       catch (InterruptedException e)
       {
           System.out.println(e.toString());
       }
       System.out.println("Will be printed last!");
    */
     //tpools();
    }
    
public static void tpools()
    {
        ExecutorService ex = Executors.newFixedThreadPool(3);
        /*ex.execute(new MyFirstThread("123456789",100));
        ex.execute(new MyFirstThread("ABCDEFGHIJKLMNOPQRSTUVWXYZ",100));
        ex.execute(new MyFirstThread("!@#$%^&*()+-_=",100));*/
        ex.shutdown();
    }
}

