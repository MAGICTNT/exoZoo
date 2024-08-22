package fr.maxime.dao;

import fr.maxime.entity.Nourriture;
import fr.maxime.entity.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NourritureDAO {
    private Connection connection;

    public NourritureDAO() {
        this.connection = ConnectionManager.getConnection();
    }

    public Nourriture save(Nourriture nourriture) {
        try {
            String request = "INSERT INTO nourriture (Label_nourriture) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(request, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, nourriture.getLabelNourriture());

            int nbrRows = statement.executeUpdate();
            if (nbrRows != 1) {
                connection.rollback();
                return null;
            }

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                nourriture.setIdNourriture(resultSet.getInt(1));
            }

            connection.commit();
            return nourriture;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }

    public Nourriture findById(int idNourriture) {
        try {
            String request = "SELECT * FROM nourriture WHERE Id_nourriture = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setInt(1, idNourriture);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Nourriture.builder()
                        .idNourriture(resultSet.getInt("Id_nourriture"))
                        .labelNourriture(resultSet.getString("Label_nourriture"))
                        .build();
            }

            return null;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Nourriture> findAll() {
        try {
            List<Nourriture> nourritures = new ArrayList<>();
            String request = "SELECT * FROM nourriture";
            PreparedStatement statement = connection.prepareStatement(request);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                nourritures.add(Nourriture.builder()
                        .idNourriture(resultSet.getInt("Id_nourriture"))
                        .labelNourriture(resultSet.getString("Label_nourriture"))
                        .build());
            }

            return nourritures;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean update(Nourriture nourriture) {
        try {
            String request = "UPDATE nourriture SET Label_nourriture = ? WHERE Id_nourriture = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setString(1, nourriture.getLabelNourriture());
            statement.setInt(2, nourriture.getIdNourriture());

            int rowsUpdated = statement.executeUpdate();
            connection.commit();
            return rowsUpdated == 1;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }

    public boolean delete(int idNourriture) {
        try {
            String request = "DELETE FROM nourriture WHERE Id_nourriture = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setInt(1, idNourriture);

            int rowsDeleted = statement.executeUpdate();
            connection.commit();
            return rowsDeleted == 1;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }
}
