package fr.maxime.dao;

import fr.maxime.entity.Alimenter;
import fr.maxime.entity.Animal;
import fr.maxime.entity.Menu;
import fr.maxime.entity.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlimenterDAO {
    private Connection connection;
    private AnimalDAO animalDAO;
    private MenuDAO menuDAO;

    public AlimenterDAO() {
        this.connection = ConnectionManager.getConnection();
        this.animalDAO = new AnimalDAO();
        this.menuDAO = new MenuDAO();
    }

    public Alimenter save(Alimenter alimenter) {
        try {
            String request = "INSERT INTO alimenter (Id_animal, Id_menu, Moment_alimentation) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(request, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, alimenter.getAnimal().getIdAnimal());
            statement.setInt(2, alimenter.getMenu().getIdMenu());
            statement.setDate(3, alimenter.getMommentAlimentation());

            int nbrRows = statement.executeUpdate();
            if (nbrRows != 1) {
                connection.rollback();
                return null;
            }

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                alimenter.setIdAlimenter(resultSet.getInt(1));
            }

            connection.commit();
            return alimenter;

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

    public Alimenter findById(int idAnimal, int idMenu) {
        try {
            String request = "SELECT * FROM alimenter WHERE Id_animal = ? AND Id_menu = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setInt(1, idAnimal);
            statement.setInt(2, idMenu);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Alimenter.builder()
                        .idAlimenter(resultSet.getInt("Id_animal")) // Use correct column name for ID
                        .animal(animalDAO.findById(resultSet.getInt("Id_animal")))
                        .menu(menuDAO.findById(resultSet.getInt("Id_menu")))
                        .mommentAlimentation(resultSet.getDate("Moment_alimentation"))
                        .build();
            }

            return null;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Alimenter> findAll() {
        try {
            List<Alimenter> alimenters = new ArrayList<>();
            String request = "SELECT * FROM alimenter";
            PreparedStatement statement = connection.prepareStatement(request);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                alimenters.add(Alimenter.builder()
                        .idAlimenter(resultSet.getInt("Id_animal")) // Use correct column name for ID
                        .animal(animalDAO.findById(resultSet.getInt("Id_animal")))
                        .menu(menuDAO.findById(resultSet.getInt("Id_menu")))
                        .mommentAlimentation(resultSet.getDate("Moment_alimentation"))
                        .build());
            }

            return alimenters;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean update(Alimenter alimenter) {
        try {
            String request = "UPDATE alimenter SET Moment_alimentation = ? WHERE Id_animal = ? AND Id_menu = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setDate(1, alimenter.getMommentAlimentation());
            statement.setInt(2, alimenter.getAnimal().getIdAnimal());
            statement.setInt(3, alimenter.getMenu().getIdMenu());

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

    public boolean delete(int idAnimal, int idMenu) {
        try {
            String request = "DELETE FROM alimenter WHERE Id_animal = ? AND Id_menu = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setInt(1, idAnimal);
            statement.setInt(2, idMenu);

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
