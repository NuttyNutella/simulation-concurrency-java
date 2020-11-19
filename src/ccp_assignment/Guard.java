/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccp_assignment;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asus
 */
public class Guard extends Thread{
    
    int num;
    String role;
    String name;
    Terminal t;
    
    public Guard(int num, String role, String name, Terminal t)
    {
        this.num = num;
        this.name = name;
        this.role = role;
        this.t = t;
    }
    
    public void run()
    {
        while (true)
        {
            if (t.pop.availablePermits() == 100 && t.no_passenger == true) // end simulation after no passenger left in the terminal
            {
                System.out.println("No more passengers in sight! " + role + "-" + name + " closes " + name + " Entrance!");
                break;
            }
            else
            {
                try{Thread.sleep(1000);}catch(Exception e){} 
                synchronized (t)
                {
                    if (t.pop.availablePermits() == 0 && t.block_mode == false)
                    {
                        System.out.println("Terminal max capacity reached! Guards are starting to block passengers");
                        t.block_mode = true;
                    }
                    else if(t.pop.availablePermits() >= 31 && t.block_mode == true)
                    {   
                        System.out.println("Terminal max capacity reached below 70%! Guards are once again allowing the passengers to enter");
                        t.block_mode = false;
                    }
                }
            }
        }
    }
}
