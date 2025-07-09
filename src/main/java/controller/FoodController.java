package controller;

import com.google.gson.Gson;
import model.FoodItem;
import services.FoodItemService;
import utils.SimpleJwtUtil;

import static spark.Spark.*;

public class FoodController {
    private static final FoodItemService service = new FoodItemService();
    private static final Gson gson = new Gson();

    public static void initRoutes() {
        path("/foods", () -> {
            get("/restaurant/:restaurantId", (req, res) -> {
                int restaurantId = Integer.parseInt(req.params("restaurantId"));
                res.type("application/json");
                return gson.toJson(service.getMenuByRestaurant(restaurantId));
            });

            before("/*", (req, res) -> {
                String authHeader = req.headers("Authorization");
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    halt(401, gson.toJson(new ErrorResponse("Missing or invalid token")));
                }
                String token = authHeader.substring(7);
                if (!SimpleJwtUtil.validateToken(token)) {
                    halt(401, gson.toJson(new ErrorResponse("Invalid or expired token")));
                }
                String role = SimpleJwtUtil.getRoleFromToken(token);
                if (!role.equalsIgnoreCase("seller")) {
                    halt(403, gson.toJson(new ErrorResponse("Access denied, only sellers allowed")));
                }
            });

            post("", (req, res) -> {
                FoodItem item = gson.fromJson(req.body(), FoodItem.class);
                String token = req.headers("Authorization").substring(7);
                int sellerId = SimpleJwtUtil.getUserIdFromToken(token);

                FoodItem added = service.addFoodItem(item);
                if (added == null) {
                    res.status(500);
                    return gson.toJson(new ErrorResponse("Failed to add food item"));
                }
                res.status(201);
                return gson.toJson(added);
            });

            put("/:id", (req, res) -> {
                int foodId = Integer.parseInt(req.params("id"));
                FoodItem update = gson.fromJson(req.body(), FoodItem.class);
                String token = req.headers("Authorization").substring(7);
                int sellerId = SimpleJwtUtil.getUserIdFromToken(token);

                FoodItem existing = service.getFoodItem(foodId);
                if (existing == null || existing.getRestaurantId() != update.getRestaurantId()) {
                    res.status(404);
                    return gson.toJson(new ErrorResponse("Food item not found or mismatched restaurant"));
                }

                boolean ok = service.updateFoodItem(update);
                if (!ok) {
                    res.status(500);
                    return gson.toJson(new ErrorResponse("Failed to update food item"));
                }
                return gson.toJson(update);
            });

            delete("/:id", (req, res) -> {
                int foodId = Integer.parseInt(req.params("id"));
                String token = req.headers("Authorization").substring(7);
                int sellerId = SimpleJwtUtil.getUserIdFromToken(token);

                FoodItem existing = service.getFoodItem(foodId);
                if (existing == null) {
                    res.status(404);
                    return gson.toJson(new ErrorResponse("Food item not found"));
                }

                boolean ok = service.deleteFoodItem(foodId, existing.getRestaurantId());
                if (!ok) {
                    res.status(500);
                    return gson.toJson(new ErrorResponse("Failed to delete food item"));
                }
                return gson.toJson(new SuccessResponse("Food item deleted"));
            });
        });
    }

    private static class ErrorResponse {
        String error;
        public ErrorResponse(String error) { this.error = error; }
    }
    private static class SuccessResponse {
        String message;
        public SuccessResponse(String message) { this.message = message; }
    }
}

