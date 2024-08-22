package fr.maxime.dao;

import fr.maxime.entity.Animal;
import fr.maxime.entity.Race;
import fr.maxime.entity.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnimalDAO {
    private Connection connection;
    private RaceDAO raceDAO;

    public AnimalDAO() {
        this.connection = ConnectionManager.getConnection();
        this.raceDAO = new RaceDAO();
    }

    public Animal save(Animal animal) {
        try {
            String request = "INSERT INTO animal (Label_animal, Description_animal, Age, Id_race) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(request, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, animal.getLabelAnimal());
            statement.setString(2, animal.getDescriptionAnimal());
            statement.setShort(3, animal.getAge());
            statement.setInt(4, animal.getRace().getIdRace());

            int nbrRows = statement.executeUpdate();
            if (nbrRows != 1) {
                connection.rollback();
                return null;
            }

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                animal.setIdAnimal(resultSet.getInt(1));
            }

            connection.commit();
            return animal;

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

    public Animal findById(int idAnimal) {
        try {
            String request = "SELECT * FROM animal WHERE Id_animal = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setInt(1, idAnimal);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Race race = raceDAO.findById(resultSet.getInt("Id_race"));
                return Animal.builder()
                        .idAnimal(resultSet.getInt("Id_animal"))
                        .labelAnimal(resultSet.getString("Label_animal"))
                        .descriptionAnimal(resultSet.getString("Description_animal"))
                        .age(resultSet.getShort("Age"))
                        .race(race)
                        .build();
            }

            return null;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Animal> findAll() {
        try {
            List<Animal> animals = new ArrayList<>();
            String request = "SELECT * FROM animal";
            PreparedStatement statement = connection.prepareStatement(request);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Race race = raceDAO.findById(resultSet.getInt("Id_race"));
                animals.add(Animal.builder()
                        .idAnimal(resultSet.getInt("Id_animal"))
                        .labelAnimal(resultSet.getString("Label_animal"))
                        .descriptionAnimal(resultSet.getString("Description_animal"))
                        .age(resultSet.getShort("Age"))
                        .race(race)
                        .build());
            }

            return animals;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean update(Animal animal) {
        try {
            String request = "UPDATE animal SET Label_animal = ?, Description_animal = ?, Age = ?, Id_race = ? WHERE Id_animal = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setString(1, animal.getLabelAnimal());
            statement.setString(2, animal.getDescriptionAnimal());
            statement.setShort(3, animal.getAge());
            statement.setInt(4, animal.getRace().getIdRace());
            statement.setInt(5, animal.getIdAnimal());

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

    public boolean delete(int idAnimal) {
        try {
            String request = "DELETE FROM animal WHERE Id_animal = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setInt(1, idAnimal);

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
