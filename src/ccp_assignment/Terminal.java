/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccp_assignment;

import java.util.concurrent.Semaphore;

/**
 *
 * @author Asus
 */
public class Terminal{
    
	boolean no_passenger = false;
    Semaphore pop = new Semaphore(100);
    int washroom = 0;
    boolean block_mode = false;
    
    synchronized public void go_staffWashroom()
    {
        washroom--;
    };
    
    synchronized public void exit_staffWashroom()
    {
        washroom++;
    };
    
    public void enterTerminal(Passenger p, Guard g)
    {
        try{Thread.sleep(1000);}catch(Exception e){}
        if (block_mode == true)
        {
            System.out.println(g.role + "-" + g.name + " blocks Passenger-" + p.num + "from entering!");
        }
        else
        {
            try {pop.acquire(1);p.can_enter = true;} catch (Exception ex) {}
        }
    }
}
