/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccp_assignment;

import java.util.Random;

/**
 *
 * @author Asus
 */
public class Passenger extends Thread{
    
    int num;
    String role;
    Guard g; Terminal terminal;
    TicketBooth m, s1, s2;
    boolean can_enter = false;
    boolean has_ticket = false;
    boolean onBus = false;
    WaitingArea wa1; WaitingArea wa2; WaitingArea wa3; TicketInspector ti;
    String destination;
            
    public Passenger(int num, String role, Guard g, Terminal terminal, TicketBooth m, TicketBooth s1, TicketBooth s2, WaitingArea wa1, WaitingArea wa2, WaitingArea wa3, TicketInspector ti)
    {
        this.num = num;
        this.role = role;
        this.g = g;
        this.terminal = terminal;
        this.m = m; // Machine
        this.s1 = s1; // Staff-1
        this.s2 = s2; // Staff-2
        this.wa1 = wa1;
        this.wa2 = wa2;
        this.wa3 = wa3;
        this.ti = ti;
    }

    public void run()
    {
        if (new Random().nextInt(3) == 0)
        {
            destination = "Cheras";
        }
        else if (new Random().nextInt(3) == 1)
        {
            destination = "Titiwangsa";
        }
        else
        {
            destination = "Kepong";
        }
        
        //try{Thread.sleep(((new Random().nextInt(4))+1 )*1000);}catch(Exception e){} 
        while(can_enter == false)
        {      
            synchronized(g)
            {
                if (g.num == 1)
                {
                    System.out.println(role + "-" + num + " is trying to enter from the West Entrance");
                }
                else
                {
                    System.out.println(role + "-" + num + " is trying to enter from the East Entrance");
                }
                
                terminal.enterTerminal(this,g);

                if ((terminal.pop.availablePermits()>0) && (terminal.pop.availablePermits()%10 == 0))
                {
                    System.out.println("Current population: " + (100 - terminal.pop.availablePermits()));
                }
            }
        }

        System.out.println(role + "-" + num + " has entered the terminal");

        try{Thread.sleep(1000);}catch(Exception e){}
        while (has_ticket == false)
        {  
            Random rand = new Random();
            int choice = rand.nextInt(3);
            if (choice == 0 && m.broke == false)
            {
                System.out.println(role + "-" + num + " moves to ticketing machine and is trying to purchase a ticket from it");
                
                m.request = true; // begin request of buying ticket

                try{Thread.sleep(1000);}catch(Exception e){}
                m.getTicket(num);

                if (m.request == false && m.give_ticket == true) // ticket machine finish the request of buying ticket and has given the ticket
                {
                    System.out.println(role + "-" + num + " has purchased a " + destination + " ticket from " + m.role + "!");
                    has_ticket = true;
                    m.give_ticket = false;

                }
                else // ticketmachine malfunction
                {
                    System.out.println(role + "-" + num + " moves away from malfuctioning " + m.role + " !");
                }

                try{Thread.sleep(3000);}catch(Exception e){}
                
            }
            else if (choice == 1 && s1.onBreak == false)
            {
                System.out.println(role + "-" + num + " moves to Booth 1 to purchase a ticket");
                
                s1.request = true; // begin request of buying ticket

                try{Thread.sleep(1000);}catch(Exception e){}
                s1.getTicket(num);

                if (s1.request == false && s1.give_ticket == true) // staff finish the request of buying ticket and has given the ticket
                {
                    System.out.println(role + "-" + num + " has purchased a " + destination + " ticket from " + s1.role + "!");
                    has_ticket = true;
                    s1.give_ticket = false;
                }  
                else if (s1.onBreak == true) // staff is having toilet break
                {
                    System.out.println(role + "-" + num + " moves away from Booth 1 because " + s1.role + " is on toilet break!");
                }
                else // for debug purpose and prevent deadlock
                {
                    System.out.println(role + "-" + num + " moves away from Booth 1 because " + s1.role + " is taking too long!");
                }


                try{Thread.sleep(3000);}catch(Exception e){}
            }
            else if (choice == 2 && s2.onBreak == false)
            {
                System.out.println(role + "-" + num + " moves to Booth 2 to purchase a ticket");

                s2.request = true; // begin request of buying ticket
                try{Thread.sleep(1000);}catch(Exception e){}
                s2.getTicket(num);

                if (s2.request == false && s2.give_ticket == true) // staff finish the request of buying ticket and has given the ticket
                {
                    System.out.println(role + "-" + num + " has purchased a " + destination + " ticket from " + s2.role + "!");
                    has_ticket = true;
                    s2.give_ticket = false;
                }
                else if (s2.onBreak == true) // staff is having toilet break
                {
                    System.out.println(role + "-" + num + " moves away from Booth 2 because " + s2.role + " is on toilet break!");
                }
                else // for debug purpose and prevent deadlock
                {
                    System.out.println(role + "-" + num + " moves away from Booth 2 because " + s2.role + " is taking too long!");
                }

                try{Thread.sleep(3000);}catch(Exception e){}    
            }
            else
            {
                // try again
                try{Thread.sleep(((new Random().nextInt(4))+1 )*1000);}catch(Exception e){} 
            }
        }
       
        boolean isWaiting = false;
        boolean canBoard = false;
        
        if (destination == "Cheras")
        {
            while (isWaiting == false)
            {
                isWaiting = wa1.enterWaitingArea(this);
                if (isWaiting == true) // succeed in entering waiting area
                {
                    boolean scan = false; boolean inspect = false;
                    if (new Random().nextInt(2) == 0)
                    {
                        scan = wa1.s.scanTicket(this);
                        inspect = ti.inspectTicket(this);
                    }
                    else
                    {
                        inspect = ti.inspectTicket(this);
                        scan = wa2.s.scanTicket(this);
                    }
                    
                    if (scan == true && inspect == true)
                    {
                        System.out.println(role + "-" + num + " is eligible to board the bus");
                        canBoard = true;
                    }
                    else
                        System.out.println("Error"); // Probably should not happen
                    
                }
                else // waiting area is full and passenger can't enter
                {
                    System.out.println(role + "-" + num + " is waiting in the foyer");
                    try{Thread.sleep(30000);}catch(Exception e){}   
                }
            }
        }
        else if (destination == "Titiwangsa")
        {
            while (isWaiting == false)
            {
                isWaiting = wa2.enterWaitingArea(this);
                if (isWaiting == true) // succeed in entering waiting area
                {
                    boolean scan = false; boolean inspect = false;
                    if (new Random().nextInt(2) == 0)
                    {
                        scan = wa2.s.scanTicket(this);
                        inspect = ti.inspectTicket(this);
                    }
                    else
                    {
                        inspect = ti.inspectTicket(this);
                        scan = wa2.s.scanTicket(this);
                    }
                    
                    if (scan == true && inspect == true) // waiting area is full and passenger can't enter
                    {
                        System.out.println(role + "-" + num + " is eligible to board the bus");
                        canBoard = true;
                    }
                    else
                        System.out.println("Error");
                }
                else
                {
                    System.out.println(role + "-" + num + " is waiting in the foyer");
                    try{Thread.sleep(30000);}catch(Exception e){}   
                }
            }
        }
        else
        {
            while (isWaiting == false)
            {
                isWaiting = wa3.enterWaitingArea(this);
                if (isWaiting == true)// succeed in entering waiting area
                {
                    boolean scan = false; boolean inspect = false;
                    if (new Random().nextInt(2) == 0) 
                    {
                        scan = wa3.s.scanTicket(this);
                        inspect = ti.inspectTicket(this);
                    }
                    else
                    {
                        inspect = ti.inspectTicket(this);
                        scan = wa3.s.scanTicket(this);
                    }
                    
                    if (scan == true && inspect == true)
                    {
                        System.out.println(role + "-" + num + " is eligible to board the bus");
                        canBoard = true;
                    }
                    else
                        System.out.println("Error");
                }
                else // waiting area is full and passenger can't enter
                {
                    System.out.println(role + "-" + num + " is waiting in the foyer");
                    try{Thread.sleep(30000);}catch(Exception e){}   
                }
            }
        }
        
        if (canBoard = true)
        {
            if (destination == "Cheras")
            {
                boolean end = false;
                while (end == false)
                {
                    end = wa1.exitWaitingArea(this);
                    if (end == false)
                    {
                        try{Thread.sleep(20000);}catch(Exception e){}
                    }
                }
            }
            else if (destination == "Titiwangsa")
            {
                boolean end = false;
                while (end == false)
                {
                    end = wa2.exitWaitingArea(this);
                    if (end == false)
                    {
                        try{Thread.sleep(15000);}catch(Exception e){}
                    }
                }
            }
            else
            {
                boolean end = false;
                while (end == false)
                {
                    end = wa3.exitWaitingArea(this);
                    if (end == false)
                    {
                        try{Thread.sleep(15000);}catch(Exception e){}
                    }
                }
            }
        }
    }
    
}
