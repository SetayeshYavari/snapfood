package services;

import DAO.FoodItemDAO;
import model.FoodItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, Object> searchFoodItems(String keyword, Double minPrice, Double maxPrice, String category, Double minRating) {
        List<FoodItem> matched = dao.searchFoodItems(keyword, minPrice, maxPrice, category, minRating);
        List<FoodItem> related = dao.findRelatedFoodItems(category != null ? category : "", keyword);
        Map<String, Object> result = new HashMap<>();
        result.put("results", matched);
        result.put("related", related);
        return result;
    }
}


