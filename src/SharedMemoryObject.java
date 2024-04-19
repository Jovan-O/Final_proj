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
    
    public SharedMemoryObject()
    {
        myLock = new ReentrantLock();
        numbers = new int[10];       
    }
    public void addNumber(int n)
    {
        if(n <10)
        {   
            System.out.println("BEFORE adding "+n+": "+numbers[n]);
            myLock.lock();
            numbers[n] ++;
            myLock.unlock();
            System.out.println("After adding "+n+": "+numbers[n]);
            isDirty = true;
        }
    }
    
    public boolean getIsDirty()
    {
        return isDirty;
    }
    
    public void setIsDirty(boolean d)
    {
        isDirty = d;
    }
    
    @Override
    public String toString()
    {
        myLock.lock();
        String t = "";
        for(int i =0; i<10; i ++)
        {
            t += i+":"+numbers[i]+"\n";
        }
        myLock.unlock();
        return t;       
    }
}
