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
public class TicketInspector {
    
    String role;
    
    public TicketInspector(String role)
    {
        this.role = role;
    }
    
    synchronized public boolean inspectTicket(Passenger p)
    {
        System.out.println(p.role + "-" + p.num + " is having the ticket inspected by " + role);
        try{Thread.sleep(6000);}catch(Exception e){} 
        System.out.println(role + " has inspected " + p.role + "-" + p.num + "'s ticket");
        return true;
    }
    
}
