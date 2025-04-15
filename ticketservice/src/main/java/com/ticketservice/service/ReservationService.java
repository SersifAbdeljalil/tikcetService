package com.ticketservice.service;

import com.ticketservice.repository.DatabaseConnection;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.sql.*;
import java.util.Date;

@WebService(serviceName = "ReservationService")
public class ReservationService {

    @WebMethod
    public String reserverBillet(
            @WebParam(name = "userId") Long userId,
            @WebParam(name = "billetId") Long billetId,
            @WebParam(name = "quantite") int quantite) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            // Vérification de la disponibilité du billet
            String checkQuery = "SELECT quantite_disponible FROM billets WHERE id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setLong(1, billetId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        int dispo = rs.getInt("quantite_disponible");
                        if (dispo < quantite) {
                            return "Quantité insuffisante.";
                        }
                    } else {
                        return "Billet non trouvé.";
                    }
                }
            }

            // Insertion de la réservation
            String insertQuery = "INSERT INTO reservations(utilisateur_id, billet_id, date_reservation, quantite) VALUES (?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setLong(1, userId);
                insertStmt.setLong(2, billetId);
                insertStmt.setTimestamp(3, new java.sql.Timestamp(new Date().getTime()));
                insertStmt.setInt(4, quantite);
                insertStmt.executeUpdate();
            }

            // Mise à jour du stock
            String updateQuery = "UPDATE billets SET quantite_disponible = quantite_disponible - ? WHERE id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setInt(1, quantite);
                updateStmt.setLong(2, billetId);
                updateStmt.executeUpdate();
            }

            conn.commit();
            return "Réservation confirmée !";

        } catch (SQLException e) {
            e.printStackTrace();
            return "Erreur SQL : " + e.getMessage();
        }
    }

    @WebMethod
    public boolean annulerReservation(@WebParam(name = "reservationId") Long id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Recréditer les billets réservés avant suppression
            String fetchReservation = "SELECT billet_id, quantite FROM reservations WHERE id = ?";
            Long billetId = null;
            int quantite = 0;

            try (PreparedStatement fetchStmt = conn.prepareStatement(fetchReservation)) {
                fetchStmt.setLong(1, id);
                try (ResultSet rs = fetchStmt.executeQuery()) {
                    if (rs.next()) {
                        billetId = rs.getLong("billet_id");
                        quantite = rs.getInt("quantite");
                    } else {
                        return false;
                    }
                }
            }

            conn.setAutoCommit(false);

            // Supprimer la réservation
            try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM reservations WHERE id = ?")) {
                deleteStmt.setLong(1, id);
                deleteStmt.executeUpdate();
            }

            // Recréditer les billets
            try (PreparedStatement updateStock = conn.prepareStatement("UPDATE billets SET quantite_disponible = quantite_disponible + ? WHERE id = ?")) {
                updateStock.setInt(1, quantite);
                updateStock.setLong(2, billetId);
                updateStock.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}