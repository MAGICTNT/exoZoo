package fr.maxime.dao;

import fr.maxime.entity.Menu;
import fr.maxime.entity.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {
    private Connection connection;

    public MenuDAO() {
        this.connection = ConnectionManager.getConnection();
    }

    public Menu save(Menu menu) {
        try {
            String request = "INSERT INTO menu (label_menu, quantite_recommande) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(request, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, menu.getLabelMenu());
            statement.setFloat(2, menu.getQuantiteRecommande());

            int nbrRows = statement.executeUpdate();
            if (nbrRows != 1) {
                connection.rollback();
                return null;
            }

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                menu.setIdMenu(resultSet.getInt("Id_menu")); // La colonne Id_menu avec une majuscule
            }

            connection.commit();
            return menu;

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

    public Menu findById(int idMenu) {
        try {
            String request = "SELECT * FROM menu WHERE Id_menu = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setInt(1, idMenu);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Menu.builder()
                        .idMenu(resultSet.getInt("Id_menu")) // La colonne Id_menu avec une majuscule
                        .labelMenu(resultSet.getString("label_menu"))
                        .quantiteRecommande(resultSet.getFloat("quantite_recommande"))
                        .build();
            }

            return null;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Menu> findAll() {
        try {
            List<Menu> menus = new ArrayList<>();
            String request = "SELECT * FROM menu";
            PreparedStatement statement = connection.prepareStatement(request);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                menus.add(Menu.builder()
                        .idMenu(resultSet.getInt("Id_menu")) // La colonne Id_menu avec une majuscule
                        .labelMenu(resultSet.getString("label_menu"))
                        .quantiteRecommande(resultSet.getFloat("quantite_recommande"))
                        .build());
            }

            return menus;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean delete(int idMenu) {
        try {
            String request = "DELETE FROM menu WHERE Id_menu = ?";
            PreparedStatement statement = connection.prepareStatement(request);
            statement.setInt(1, idMenu);

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
