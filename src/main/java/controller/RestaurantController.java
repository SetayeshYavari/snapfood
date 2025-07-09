package controller;

import com.google.gson.Gson;
import model.Restaurant;
import services.RestaurantService;
import utils.SimpleJwtUtil;

import static spark.Spark.*;

public class RestaurantController {
    private static final RestaurantService restaurantService = new RestaurantService();
    private static final Gson gson = new Gson();

    public static void initRoutes() {

        post("/restaurants", (req, res) -> {
            String token = req.headers("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                res.status(401);
                return gson.toJson(new ErrorResponse("Missing token"));
            }

            token = token.substring(7);
            if (!SimpleJwtUtil.validateToken(token)) {
                res.status(401);
                return gson.toJson(new ErrorResponse("Invalid or expired token"));
            }

            String role = SimpleJwtUtil.getRoleFromToken(token);
            int sellerId = SimpleJwtUtil.getUserIdFromToken(token);

            if (!role.equals("seller")) {
                res.status(403);
                return gson.toJson(new ErrorResponse("Only sellers can register restaurants"));
            }

            Restaurant restaurant = gson.fromJson(req.body(), Restaurant.class);
            restaurant.setSellerId(sellerId);

            restaurantService.registerRestaurant(restaurant);
            return gson.toJson(new SuccessResponse("Restaurant registered. Awaiting approval."));
        });

        get("/restaurants", (req, res) -> {
            return gson.toJson(restaurantService.getApprovedRestaurants());
        });

        // Admin approve route (optional)
        put("/admin/restaurants/:id/approve", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            restaurantService.approveRestaurant(id);
            return gson.toJson(new SuccessResponse("Restaurant approved."));
        });
    }

    static class ErrorResponse {
        String error;
        public ErrorResponse(String error) { this.error = error; }
    }

    static class SuccessResponse {
        String message;
        public SuccessResponse(String message) { this.message = message; }
    }
}
