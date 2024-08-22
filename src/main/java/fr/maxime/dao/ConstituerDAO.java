package fr.maxime.dao;

import fr.maxime.entity.Constituer;
import fr.maxime.entity.Nourriture;
import fr.maxime.entity.Menu;
import fr.maxime.entity.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConstituerDAO {
    private Connection connection;
    private NourritureDAO nourritureDAO;
    private MenuDAO menuDAO;

    public ConstituerDAO() {
        this.connection = ConnectionManager.getConnection();
        this.nourritureDAO = new NourritureDAO();
        this.menuDAO = new MenuDAO();
    }

    public Constituer save(Constituer constituer) {
        try {
            String request = "INSERT INTO constituer (Id_nourriture, Id_menu, Quantite) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(request, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, constituer.getNourriture().getIdNourriture());
            statement.setInt(2, constituer.getMenu().getIdMenu());
            statement.setFloat(3, constituer.getQuantite());

            int nbrRows = statement.executeUpdate();
            if (nbrRows != 1) {
                connection.rollback();
                return null;
            }

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                constituer.setIdConstituer(resultSet.getInt(1));
            }

            connection.commit();
            return constituer;

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

    public Constituer findById(int idConstituer) {
        try {
            String request = "SELECT * FROM constituer WHERE Id_constituer = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setInt(1, idConstituer);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Nourriture nourriture = nourritureDAO.findById(resultSet.getInt("Id_nourriture"));
                Menu menu = menuDAO.findById(resultSet.getInt("Id_menu"));
                return Constituer.builder()
                        .idConstituer(resultSet.getInt("Id_constituer"))
                        .nourriture(nourriture)
                        .menu(menu)
                        .quantite(resultSet.getFloat("Quantite"))
                        .build();
            }

            return null;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Constituer> findAll() {
        try {
            List<Constituer> constituers = new ArrayList<>();
            String request = "SELECT * FROM constituer";
            PreparedStatement statement = connection.prepareStatement(request);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Nourriture nourriture = nourritureDAO.findById(resultSet.getInt("Id_nourriture"));
                Menu menu = menuDAO.findById(resultSet.getInt("Id_menu"));
                constituers.add(Constituer.builder()
                        .idConstituer(resultSet.getInt("Id_constituer"))
                        .nourriture(nourriture)
                        .menu(menu)
                        .quantite(resultSet.getFloat("Quantite"))
                        .build());
            }

            return constituers;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean update(Constituer constituer) {
        try {
            String request = "UPDATE constituer SET Id_nourriture = ?, Id_menu = ?, Quantite = ? WHERE Id_constituer = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setInt(1, constituer.getNourriture().getIdNourriture());
            statement.setInt(2, constituer.getMenu().getIdMenu());
            statement.setFloat(3, constituer.getQuantite());
            statement.setInt(4, constituer.getIdConstituer());

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

    public boolean delete(int idConstituer) {
        try {
            String request = "DELETE FROM constituer WHERE Id_constituer = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setInt(1, idConstituer);

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
