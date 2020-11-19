/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccp_assignment;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Asus
 */
public class Bus extends Thread{
    
    Semaphore seat = new Semaphore(0); // Bus will come after the entrance is open
    String role; String name; Terminal t;
    boolean busArrived = false;
    
    public Bus(String role, String name, Terminal t)
    {
        this.role = role;
        this.name = name;
        this.t = t;
    }
    
    synchronized public void enterBus(Passenger p)
    {
        try{seat.acquire(1);p.onBus = true;}catch(Exception e){}
    }
    
    public void arrive()
    {
        seat.release(12);
        busArrived = true;
    }
    
    public void depart()
    {
        System.out.println(role + "-" + name + " is full! Departing...");
        busArrived = false;
    }
    
    public void run()
    {
        while(true)
        {
            if (t.pop.availablePermits() == 100 && t.no_passenger == true) // end simulation after no passenger left in the terminal
            {
                System.out.println("No more passenger in terminal, " + role + "-" + name + " will not arrive at the terminal anymore");
                break;
            }
            else
            {
                System.out.println(role + "-" + name + " will arrive in 60 seconds");
                try{Thread.sleep(30000);}catch(Exception e){}
                if (new Random().nextInt(2) == 0)
                {
                    System.out.println(role + "-" + name + " will arrive in 30 seconds");
                    try{Thread.sleep(30000);}catch(Exception e){}
                }
                else // delay in arrival
                {
                    System.out.println(role + "-" + name + " will arrive in 60 seconds, 30 seconds late than initial ETA because of traffic jam!");
                    try{Thread.sleep(30000);}catch(Exception e){}
                    System.out.println(role + "-" + name + " will arrive in 30 seconds, no more traffic jam!");
                    try{Thread.sleep(30000);}catch(Exception e){}
                }
                arrive();
                System.out.println(role + "-" + name + " has arrived and will depart in 60 seconds (only when full)");
                try{Thread.sleep(30000);}catch(Exception e){}
                System.out.println(role + "-" + name + " will depart in 30 seconds (only when full)");
                try{Thread.sleep(30000);}catch(Exception e){}
                if (seat.availablePermits() == 0)
                {
                    depart();
                }
                else
                {
                    while (seat.availablePermits() != 0)
                    {
                        if (t.pop.availablePermits() == 100 && t.no_passenger == true) // end simulation after no passenger left in the terminal
                        {
                                System.out.println("No more passenger in the terminal! " + role + "-" + name + " departs!") ;
                                busArrived = false;
                                break;
                        }
                        else
                        {
                            System.out.println(role + "-" + name + " is not full yet! Waiting for 30 seconds more. Seats left: " + seat.availablePermits());
                            try{Thread.sleep(30000);}catch(Exception e){}
                            if (seat.availablePermits() == 0)
                            {
                                depart();
                            }
                        }
                    }
                }
            }
        }
    }
    
}
