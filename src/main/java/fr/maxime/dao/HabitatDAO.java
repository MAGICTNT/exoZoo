package fr.maxime.dao;

import fr.maxime.entity.Habitat;
import fr.maxime.entity.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HabitatDAO {
    private Connection connection;

    public HabitatDAO() {
        this.connection = ConnectionManager.getConnection();
    }

    public Habitat save(Habitat habitat) {
        try {
            String request = "INSERT INTO habitat (Label_habitat) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(request, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, habitat.getLabelHabitat());

            int nbrRows = statement.executeUpdate();
            if (nbrRows != 1) {
                connection.rollback();
                return null;
            }

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                habitat.setIdHabitat(resultSet.getInt(1));
            }

            connection.commit();
            return habitat;

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

    public Habitat findById(int idHabitat) {
        try {
            String request = "SELECT * FROM habitat WHERE Id_habitat = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setInt(1, idHabitat);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Habitat.builder()
                        .idHabitat(resultSet.getInt("Id_habitat"))
                        .labelHabitat(resultSet.getString("Label_habitat"))
                        .build();
            }

            return null;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Habitat> findAll() {
        try {
            List<Habitat> habitats = new ArrayList<>();
            String request = "SELECT * FROM habitat";
            PreparedStatement statement = connection.prepareStatement(request);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                habitats.add(Habitat.builder()
                        .idHabitat(resultSet.getInt("Id_habitat"))
                        .labelHabitat(resultSet.getString("Label_habitat"))
                        .build());
            }

            return habitats;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean delete(int idHabitat) {
        try {
            String request = "DELETE FROM habitat WHERE Id_habitat = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setInt(1, idHabitat);

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
