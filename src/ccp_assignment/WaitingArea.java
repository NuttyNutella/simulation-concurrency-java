/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccp_assignment;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Asus
 */
public class WaitingArea{
    
	Lock l = new ReentrantLock(true);
	
    String role; String name; TicketScanner s; Bus b; Terminal t;
    int count = 0;

    public WaitingArea(String role, String name, TicketScanner s, Bus b, Terminal t)
    {
        this.role = role;
        this.name = name;
        this.s = s;
        this.b = b;
        this.t = t;
    }  
    
    public boolean enterWaitingArea(Passenger p)
    {
    	l.lock();
    	try {Thread.sleep(1000);}catch(Exception e) {}
    	l.unlock();
        if (count < 10)
        {
            count++;
            System.out.println(p.role + "-" + p.num + " has entered " + role + "-" + name + ". " + role + "-" + name + " population: " + count);
            return true;
        }
        else // is full
        {
            System.out.println(role + "-" + name + " is full");
            return false;
        }
    } 
    
    synchronized public boolean exitWaitingArea(Passenger p)
    {
        if (b.busArrived == true)
        {   
            b.enterBus(p);
            if (p.onBus == true) // passenger succeeds in boarding the bus
            {
                count--;
                System.out.println(p.role + "-" + p.num + " has boarded " + b.role + "-" + b.name + " Seat left: " +
                        b.seat.availablePermits() + "\n" + role + "-" + name + " population: " + count);
                t.pop.release(1);
                return true;
            }
            else // bus is full and passenger does not succeed in boarding the bus
            {
                System.out.println(p.role + "-" + p.num + " cannot board " + b.role + "-" + b.name + ", it's full!");
                return false;
            }
        }
        else // bus is not arrived yet
        {
            System.out.println(p.role + "-" + p.num + " is waiting for " + b.role + "-" + b.name);
            return false;
        }
    } 
    
}
