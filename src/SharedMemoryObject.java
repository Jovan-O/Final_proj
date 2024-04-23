/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author btowle
 */
public class SharedMemoryObject implements java.io.Serializable
{
    ReentrantLock myLock;    
    private int [] numbers;
    private boolean isDirty;
    int [] positions=new int[4];
    int turn=-1;
    int mypos;
    int monsterpos=0;
    String[] t = new String[4];
   


    
    public SharedMemoryObject()
    {
        myLock = new ReentrantLock();
        turn+=1;
        mypos = 0;      
        positions[turn]=mypos; 
    }
    public void gameOver()
    {
        System.out.println("Game over");
    }
    public void addNumber(int n)
    {
        if(mypos <100)
        {   
            System.out.println("My turn is" + turn);
            System.out.println("BEFORE adding "+n+": "+mypos);
            myLock.lock();
            mypos+=(10-Math.abs((Math.random()*10)-n));
            positions[turn] = mypos; // Update the position in the array
            System.out.println("the number of steps is "+ mypos);
            myLock.unlock();
            System.out.println("After adding "+n+": "+mypos);
            System.out.println(toString());
            turn = (turn + 1) % 4; // Ensure turn is always between 0 and 3
            isDirty = true;
        }
        if(mypos>=100)
        {
            System.out.println("Player "+ turn+ "won");
            gameOver();
        }
    }
    public int getTurn()
    {
        return turn;
    }
    public boolean getIsDirty()
    {
        return isDirty;
    }
    
    public void setIsDirty(boolean d)
    {
        isDirty = d;
    }
    public String update()
    {
        String all = "";
        for(int j = 0; j < 4; j++)
        {
            String p = "";
            for(int i = 0; i < 100; i++)
            {
                if(i == positions[j])
                {
                    p += "P" + j;
                }
                else if(i == monsterpos)
                {
                    p += "M";
                }
                else
                {
                    p += "-";
                }
            }
            all += p + "\n";
        }
        return all;
    }
    @Override
    public String toString()
    {
       String s ;
            myLock.lock();
           
        s=update();
            
            myLock.unlock();

        
        
        return s;
              
    }
}
