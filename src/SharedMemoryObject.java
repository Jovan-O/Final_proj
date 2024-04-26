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
 
     //players array and player count are interconnected
     //player count tracks how many new connections are formed while the player array keeps track of ID's
     private int [] players = new int[4];
     private int playerCount = 0;
     private boolean isDirty;
     private Threading1 threadingInstance;
     public boolean gameStarted;
     
     int [] positions={10,10,10,10};
     int turn=-1;
     int mypos=10;
     int monsterpos=0;
     String[] t = new String[4];

     public SharedMemoryObject(Threading1 threadingInstance) {
        this.threadingInstance = threadingInstance;
        myLock = new ReentrantLock();
        turn += 1;    
        gameStarted = false;
    }
    
     public void updateTurn()
     {
         turn = (turn + 1) % playerCount;
     }
     public int getMypos()
     {
         return mypos;
     }
     public int getMonsterpos()
     {
         return monsterpos;
     }
     public void gameOver()
     {
         for(int i =0;i<3;i++)
         {
             if(positions[i]==monsterpos)
             {
                 System.out.println("Game over Player "+ i + " got eaten");
             }
         
         else if(positions[i]==100)
         {
             System.out.println("Game over Player "+ i +" Won");
 
         }
       
     }
     }
 
     public void addPlayer(int connectionID){
         myLock.lock();
         // grabbing player info
         // players[connection count]
         players[playerCount] = connectionID;
         playerCount++;
         myLock.unlock();
     }
 
     public void gameStart(){
         myLock.lock();
         gameStarted = true;
         System.out.println("We got all of our players!!!");
         isDirty = true;
         myLock.unlock();
     }
 
     public void clientInput(int n)
     {
         if(mypos <100 && mypos>monsterpos)
         {   
             System.out.println("My turn is" + turn);
             System.out.println("BEFORE adding "+n+": "+mypos);
             myLock.lock();
             mypos+=(10-Math.abs((Math.random()*10)-n));
             System.out.println("The moce is "+ mypos);
             positions[turn] = mypos; // Update the position in the array
             System.out.println("the number of steps is "+ mypos);
             myLock.unlock();
             // Update progress bars in Threading1
        threadingInstance.updateProgressBars(this);
             System.out.println("After adding "+n+": "+mypos);
             System.out.println(toString());
             if(turn==2)
             {
                 monsterRun();
                 gameOver();
                 
             }
             turn = (turn + 1) % playerCount; // Ensure turn is always between 0 and player count (2)
             isDirty = true;
         }
        
 
        if(mypos>=100)
         {
             System.out.println("Player "+ turn+ "won");
         
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
     public void monsterRun()
     {
         monsterpos+=(int) (Math.abs(Math.random()*10)+1);
         for(int i =0;i<3;i++)
         {
         if(positions[i]<=monsterpos)
         {
             System.out.print("You dead dead");
             turn = (turn + 1) % playerCount; 
         }
     }
     }
     public String update()
     {
       
         
         String all = "";
     
         for(int j = 0; j < playerCount; j++)
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
         System.out.println("The turn is "+ turn);
        
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