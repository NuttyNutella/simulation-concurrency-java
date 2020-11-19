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
public class BusTerminal {

	static boolean simulation_end = false; 
	
    public static void main(String[] args) {
        
        Passenger p;
        Terminal terminal = new Terminal();
        Guard g1 = new Guard(1,"Guard","West",terminal);
        Guard g2 = new Guard(2,"Guard","East",terminal);
        TicketScanner scan1 = new TicketScanner("TicketScanner","Cheras");
        TicketScanner scan2 = new TicketScanner("TicketScanner","Titiwangsa");
        TicketScanner scan3 = new TicketScanner("TicketScanner","Kepong");
        Bus b1 = new Bus("Bus","Cheras",terminal);
        Bus b2 = new Bus("Bus","Titiwangsa",terminal);
        Bus b3 = new Bus("Bus","Kepong",terminal);
        WaitingArea wa1 = new WaitingArea("WaitingArea","Cheras",scan1,b1,terminal);
        WaitingArea wa2 = new WaitingArea("WaitingArea","Titiwangsa",scan2,b2,terminal);
        WaitingArea wa3 = new WaitingArea("WaitingArea","Kepong",scan3,b3,terminal);
        TicketInspector ti = new TicketInspector("TicketInspector");
        TicketBooth machine = new TicketBooth("TicketMachine",0,terminal);
        TicketBooth staff1 = new TicketBooth("Staff-1",1,terminal);
        TicketBooth staff2 = new TicketBooth("Staff-2",2,terminal);
        
        b1.start();
        b2.start();
        b3.start();
        
        g1.start();
        g2.start();
        
        machine.start();
        staff1.start();
        staff2.start();
        
        // Create new passenger thread every 1, 2, 3 or 4 seconds
        for (int i = 1; i <= 20; i++) // Can set number of passengers entering for simulation
        {
            Random rand = new Random();
            int entrance = rand.nextInt(2);
            if(entrance == 0) // entering from West entrance
            {
                try{Thread.sleep(((new Random().nextInt(4))+1 )*1000);}catch(Exception e){} 
                p = new Passenger(i,"Passenger",g1,terminal,machine,staff1,staff2,wa1,wa2,wa3,ti);
                p.start();
            }
            else // Entering from east entrance
            {
                try{Thread.sleep(((new Random().nextInt(4))+1 )*1000);}catch(Exception e){} 
                p = new Passenger(i,"Passenger",g2,terminal,machine,staff1,staff2,wa1,wa2,wa3,ti);
                p.start();
            }
        }
                
        terminal.no_passenger = true; // used to terminate bus and ticketbooth thread naturally and peacefully along with semaphore
        
    }
    
}
