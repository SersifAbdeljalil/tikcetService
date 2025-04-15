package com.ticketservice.service;
import com.ticketservice.model.Billet;
import com.ticketservice.repository.DatabaseConnection;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@WebService(serviceName = "BilletService")
public class BilletService {
    @WebMethod
    public List<Billet> getBilletsDisponibles() {
        List<Billet> billets = new ArrayList<>();
        String sql = "SELECT * FROM billets WHERE quantite_disponible > 0";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Billet billet = new Billet();
                billet.setId(rs.getLong("id"));
                billet.setTypeBillet(rs.getString("type_billet"));
                billet.setPrix(rs.getDouble("prix"));
                billet.setQuantiteDisponible(rs.getInt("quantite_disponible"));
                // événement non chargé ici (relation ManyToOne)
                billets.add(billet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return billets;
    }
    @WebMethod
    public Billet getBilletById(@WebParam(name = "id") Long id) {
        String sql = "SELECT * FROM billets WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Billet billet = new Billet();
                billet.setId(rs.getLong("id"));
                billet.setTypeBillet(rs.getString("type_billet"));
                billet.setPrix(rs.getDouble("prix"));
                billet.setQuantiteDisponible(rs.getInt("quantite_disponible"));
                return billet;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}