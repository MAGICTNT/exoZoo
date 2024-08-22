package fr.maxime.dao;

import fr.maxime.entity.Vivre;
import fr.maxime.entity.ConnectionManager;
import fr.maxime.entity.Animal;
import fr.maxime.entity.Habitat;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VivreDAO {
    private Connection connection;
    private AnimalDAO animalDAO;
    private HabitatDAO habitatDAO;

    public VivreDAO() {
        this.connection = ConnectionManager.getConnection();
        this.animalDAO = new AnimalDAO();
        this.habitatDAO = new HabitatDAO();
    }

    public Vivre save(Vivre vivre) {
        try {
            String request = "INSERT INTO vivre (Id_animal, Id_habitat) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(request, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, vivre.getAnimal().getIdAnimal());
            statement.setInt(2, vivre.getHabitat().getIdHabitat());

            int nbrRows = statement.executeUpdate();
            if (nbrRows != 1) {
                connection.rollback();
                return null;
            }

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                vivre.setIdVivre(resultSet.getInt(1));
            }

            connection.commit();
            return vivre;

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

    public Vivre findById(int idVivre) {
        try {
            String request = "SELECT * FROM vivre WHERE Id_vivre = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setInt(1, idVivre);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Animal animal = animalDAO.findById(resultSet.getInt("Id_animal"));
                Habitat habitat = habitatDAO.findById(resultSet.getInt("Id_habitat"));

                return Vivre.builder()
                        .idVivre(resultSet.getInt("Id_vivre"))
                        .animal(animal)
                        .habitat(habitat)
                        .build();
            }

            return null;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Vivre> findAll() {
        try {
            List<Vivre> vivres = new ArrayList<>();
            String request = "SELECT * FROM vivre";
            PreparedStatement statement = connection.prepareStatement(request);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Animal animal = animalDAO.findById(resultSet.getInt("Id_animal"));
                Habitat habitat = habitatDAO.findById(resultSet.getInt("Id_habitat"));

                vivres.add(Vivre.builder()
                        .idVivre(resultSet.getInt("Id_vivre"))
                        .animal(animal)
                        .habitat(habitat)
                        .build());
            }

            return vivres;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean delete(int idVivre) {
        try {
            String request = "DELETE FROM vivre WHERE Id_vivre = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setInt(1, idVivre);

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
