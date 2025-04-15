package com.ticketservice.service;
import com.ticketservice.repository.DatabaseConnection;
import com.ticketservice.model.Utilisateur;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.sql.*;
@WebService(serviceName = "UtilisateurService")
public class UtilisateurService {
    @WebMethod
    public boolean inscrireUtilisateur(
            @WebParam(name = "nom") String nom,
            @WebParam(name = "prenom") String prenom,
            @WebParam(name = "email") String email,
            @WebParam(name = "password") String password) {
        String sql = "INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    @WebMethod
    public Utilisateur connecterUtilisateur(
            @WebParam(name = "email") String email,
            @WebParam(name = "password") String password) {
        String sql = "SELECT * FROM utilisateurs WHERE email = ? AND mot_de_passe = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Utilisateur user = new Utilisateur(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe")
                );
                user.setId(rs.getLong("id"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}