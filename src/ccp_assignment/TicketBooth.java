
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccp_assignment;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Asus
 */
public class TicketBooth extends Thread{
    
	Lock l = new ReentrantLock(true);
	
    String role; int type;
    boolean onBreak = false;
    boolean give_ticket = false;
    int bladder_volume = 0; // used for toilet break
    boolean request = false;
    boolean broke; // for ticket machine
    Terminal t;
    
    public TicketBooth(String role, int type, Terminal t)
    {
        this.role = role;
        this.type = type;
        this.t = t;
    }
    
    private void broken() // 50% chance of breaking
    {
        Random rand = new Random();
        int chance = rand.nextInt(9);
        if (chance > 4)
        {
            broke = false;
        }
        else
        {
            broke = true;
        }
    }
    
    public void getTicket(int num) // called by passenger
    {
    	l.lock();
        try{Thread.sleep(1000);}catch(Exception e){}
        l.unlock();
        if (type == 0 && broke == false) // Ticket machine not broken
        {
            broken();
            if (broke == false)
            {
                giveTicket(num);
            }
            else
            {
                System.out.println(role + " decides to malfunction for Passenger-" + num);
                notAvailable();
            }
        }
        else if (type == 1)// Staff-1 operation
        {
            if(onBreak == false) // Not on toilet break
            {
                giveTicket(num);
            }
            else // is inside the toilet
            {
                notAvailable();
            }

        }
        else if (type == 2 )// Staff-2 operation
        {

            if(onBreak == false) // Not on toilet break
            {
                giveTicket(num);
            }
            else // is inside the toilet
            {
                notAvailable();
            }

        }
        else // TicketMachine is currently broken
        {
            notAvailable();
        }
        request = false;
    }
    
    public void giveTicket(int num)
    {
        give_ticket = true;
        
        if (type == 1 || type == 2) // Staff-1 and Staff-2
        {
            System.out.println(role + " is giving the ticket to Passenger-" + num + "!");
            
            bladder_volume+=2; // every time staff gives ticket his/her bladder volume will increase by two
            if(bladder_volume == 10) // go to washroom when volume reach ten
            {
                if (t.washroom == 0) // other staff not in the washroom
                {
                    System.out.println(role + " can't hold it anymore and rushes to the washroom!");
                    t.go_staffWashroom();
                    onBreak = true;
                }
                else // other staff is in the washroom
                {
                    System.out.println(role + " wants to rush to the washroom but his colleague has occupied it! " + role + " must hold!");
                    bladder_volume-=2;
                }
            }
        }
        else if (type == 0)// Machine
        {
            System.out.println(role + " is printing the ticket to Passenger-" + num + "!");
        }
        
    }
    
    public void notAvailable()
    {
        if (type == 1 || type == 2) // Staff-1 and Staff-2
        {
            System.out.println(role + " is having a toilet break");
        }
        else if (type == 0)// Machine
        {
            System.out.println(role + " is currently fixing itself!");
        }
    }
    
    public void run()
    {  
        while(true)
        {   
        	if (t.pop.availablePermits() == 100 && t.no_passenger == true) // end simulation after no passenger left in the terminal
        	{
        		System.out.println("No more passenger, " + role + " is off!");
        		break;
        	}
        	else
        	{
	            try{Thread.sleep(1000);}catch(Exception e){}
	            if (type == 0 && broke == true) // ticket machine fixes itself over time if broke
	            {    
	                try{Thread.sleep(10000);}catch(Exception e){}
	                System.out.println(role + " is working again");
	                broke = false;
	            }
	            else if ((type == 1 || type == 2) && onBreak == true) // staff goes for a toilet break for 4 seconds
	            {
	                while (bladder_volume != 0)
	                {
	                    try{Thread.sleep(2000);}catch(Exception e){}
	                    bladder_volume-=5;
	                }
	                System.out.println(role + " returns to the booth");
	                t.exit_staffWashroom();
	                onBreak = false;
	            }  
        	}
        }   
    }
}
