package services;

import DAO.FoodItemDAO;
import model.FoodItem;

import java.util.List;

public class FoodItemService {

    private final FoodItemDAO dao = new FoodItemDAO();

    public List<FoodItem> getMenuByRestaurant(int restaurantId) {
        return dao.getFoodItemsByRestaurant(restaurantId);
    }

    public FoodItem getFoodItem(int id) {
        return dao.getFoodItem(id);
    }

    public FoodItem addFoodItem(FoodItem item) {
        return dao.addFoodItem(item);
    }

    public boolean updateFoodItem(FoodItem item) {
        return dao.updateFoodItem(item);
    }

    public boolean deleteFoodItem(int id, int restaurantId) {
        return dao.deleteFoodItem(id, restaurantId);
    }
}

