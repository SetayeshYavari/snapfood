package controller;

import com.google.gson.Gson;
import model.Order;
import model.User;
import services.OrderService;
import services.UserService;
import spark.Request;
import spark.Response;

import java.util.List;

import static spark.Spark.*;

public class AdminController {
    private static final Gson gson = new Gson();
    private static final UserService userService = new UserService();
    static final OrderService orderService = new OrderService();

    public static void initRoutes() {
        path("/admin", () -> {

            // Get all users
            get("/users", (req, res) -> gson.toJson(userService.getAllUsers()));

            // Delete user
            delete("/users/:id", (req, res) -> {
                int userId = Integer.parseInt(req.params(":id"));
                boolean deleted = userService.deleteUser(userId);
                if (!deleted) {
                    res.status(404);
                    return gson.toJson(new ErrorResponse("User not found"));
                }
                return gson.toJson(new SuccessResponse("User deleted"));
            });

            // Get ongoing orders
            get("/orders/ongoing", (req, res) -> {
                List<Order> ongoing = orderService.getOngoingOrders();
                return gson.toJson(ongoing);
            });

            // Get system stats
            get("/stats", (req, res) -> {
                AdminStats stats = new AdminStats();
                stats.totalUsers = userService.getAllUsers().size();
                stats.totalOrders = orderService.getAllOrders().size();
                stats.totalOngoingOrders = orderService.getOngoingOrders().size();
                stats.totalDeliveredOrders = orderService.getDeliveredOrders().size();
                return gson.toJson(stats);
            });
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

    static class AdminStats {
        int totalUsers;
        int totalOrders;
        int totalOngoingOrders;
        int totalDeliveredOrders;
    }
}

