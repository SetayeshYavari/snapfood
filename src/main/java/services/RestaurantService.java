package services;

import DAO.RestaurantDAO;
import model.Restaurant;

import java.sql.SQLException;
import java.util.List;

public class RestaurantService {
    private final RestaurantDAO dao = new RestaurantDAO();

    public void registerRestaurant(Restaurant r) throws SQLException {
        dao.addRestaurant(r);
    }

    public List<Restaurant> getApprovedRestaurants() throws SQLException {
        return dao.getAllApprovedRestaurants();
    }

    public List<Restaurant> getPendingRestaurants() throws SQLException {
        return dao.getPendingRestaurants();
    }

    public void approveRestaurant(int id) throws SQLException {
        dao.approveRestaurant(id);
    }
}