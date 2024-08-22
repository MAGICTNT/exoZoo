package fr.maxime.dao;

import fr.maxime.entity.Race;
import fr.maxime.entity.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RaceDAO {
    private Connection connection;

    public RaceDAO() {
        this.connection = ConnectionManager.getConnection();
    }

    public Race save(Race race) {
        try {
            String request = "INSERT INTO race (Label_race) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(request, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, race.getLabelRace());

            int nbrRows = statement.executeUpdate();
            if (nbrRows != 1) {
                connection.rollback();
                return null;
            }

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                race.setIdRace(resultSet.getInt(1));
            }

            connection.commit();
            return race;

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

    public Race findById(int idRace) {
        try {
            String request = "SELECT * FROM race WHERE Id_race = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setInt(1, idRace);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Race.builder()
                        .idRace(resultSet.getInt("Id_race"))
                        .labelRace(resultSet.getString("Label_race"))
                        .build();
            }

            return null;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Race> findAll() {
        try {
            List<Race> races = new ArrayList<>();
            String request = "SELECT * FROM race";
            PreparedStatement statement = connection.prepareStatement(request);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                races.add(Race.builder()
                        .idRace(resultSet.getInt("Id_race"))
                        .labelRace(resultSet.getString("Label_race"))
                        .build());
            }

            return races;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean delete(int idRace) {
        try {
            String request = "DELETE FROM race WHERE Id_race = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setInt(1, idRace);

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
