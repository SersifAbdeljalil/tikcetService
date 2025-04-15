package com.ticketservice.server;
import javax.xml.ws.Endpoint;
import com.ticketservice.service.UtilisateurService;
import com.ticketservice.service.ReservationService;
import com.ticketservice.service.BilletService;
import com.ticketservice.service.EvenementService;

public class Server {
    public static void main(String[] args) {
        String baseURL = "http://localhost:8181/";
        Endpoint.publish(baseURL + "UtilisateurService", new UtilisateurService());
        Endpoint.publish(baseURL + "ReservationService", new ReservationService());
        Endpoint.publish(baseURL + "BilletService", new BilletService());
        Endpoint.publish(baseURL + "EvenementService", new EvenementService());

        System.out.println("Services publiés à :");
        System.out.println(baseURL + "UtilisateurService?wsdl");
        System.out.println(baseURL + "ReservationService?wsdl");
        System.out.println(baseURL + "BilletService?wsdl");
        System.out.println(baseURL + "EvenementService?wsdl");
    }
}
