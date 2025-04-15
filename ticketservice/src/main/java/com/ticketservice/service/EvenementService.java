package com.ticketservice.service;
import com.ticketservice.model.Evenement;
import com.ticketservice.repository.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
@WebService(serviceName = "EvenementService")
public class EvenementService {
    @WebMethod
    public Evenement getEvenementById(@WebParam(name = "id") Long id) {
        Evenement evenement = null;
        String query = "SELECT * FROM evenements WHERE id = ?";
       
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
           
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            // Si un événement est trouvé, on le crée et le retourne
            if (rs.next()) {
                evenement = new Evenement();
                evenement.setId(rs.getLong("id"));
                evenement.setNom(rs.getString("nom"));
                evenement.setDescription(rs.getString("description"));
                evenement.setDateDebut(rs.getTimestamp("date_debut"));
                evenement.setDateFin(rs.getTimestamp("date_fin"));
                evenement.setLieu(rs.getString("lieu"));
                // Ajouter ici le code pour récupérer les billets si nécessaire
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'événement : " + e.getMessage());
        }
        if (evenement == null) {
            throw new IllegalArgumentException("Événement non trouvé pour l'ID : " + id);
        }
       
        return evenement;
    }
    @WebMethod
    public List<Evenement> getEvenementsDisponibles() {
        List<Evenement> evenements = new ArrayList<>();
        String query = "SELECT * FROM evenements";
       
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
           
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Evenement evenement = new Evenement();
                evenement.setId(rs.getLong("id"));
                evenement.setNom(rs.getString("nom"));
                evenement.setDescription(rs.getString("description"));
                evenement.setDateDebut(rs.getTimestamp("date_debut"));
                evenement.setDateFin(rs.getTimestamp("date_fin"));
                evenement.setLieu(rs.getString("lieu"));
                evenements.add(evenement);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des événements : " + e.getMessage());
        }
       
        return evenements;
    }
}