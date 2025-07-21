package controller;

import com.google.gson.Gson;
import model.Restaurant;
import services.RestaurantService;
import utils.SimpleJwtUtil;
import Messages.ErrorResponse;
import Messages.SuccessResponse;

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
            int id = SimpleJwtUtil.getUserIdFromToken(token);

            if (!role.equals("seller")) {
                res.status(403);
                return gson.toJson(new ErrorResponse("Only sellers can register restaurants"));
            }

            Restaurant restaurant = gson.fromJson(req.body(), Restaurant.class);
            restaurant.setId(id);

            restaurantService.registerRestaurant(restaurant);
            return gson.toJson(new SuccessResponse("Restaurant registered. Awaiting approval."));
        });

        get("/restaurants", (req, res) -> {
            return gson.toJson(restaurantService.getAllApprovedRestaurants());
        });

        put("/admin/restaurants/:id/approve", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            restaurantService.approveRestaurant(id);
            return gson.toJson(new SuccessResponse("Restaurant approved."));
        });
    }
}
