/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccp_assignment;

/**
 *
 * @author Asus
 */
public class TicketScanner {
    
    String role; String name;
    
    public TicketScanner(String role, String name)
    {
        this.role = role;
        this.name = name;
    }
    
    synchronized public boolean scanTicket(Passenger p)
    {
        System.out.println(p.role + "-" + p.num + " is scanning the ticket at " + role + "-" + name);
        try{Thread.sleep(2000);}catch(Exception e){} 
        System.out.println(role + "-" + name + " has scanned " + p.role + "-" + p.num + "'s ticket");
        return true;
    }
    
    
}
