package controller;

import com.google.gson.Gson;
import model.Order;
import services.OrderService;
import utils.SimpleJwtUtil;

import static spark.Spark.*;

import java.util.List;

public class OrderController {
    private static final OrderService service = new OrderService();
    private static final Gson gson = new Gson();

    public static void initRoutes() {
        path("/orders", () -> {
            before("/*", (req, res) -> {
                String authHeader = req.headers("Authorization");
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    halt(401, gson.toJson(new ErrorResponse("Missing or invalid token")));
                }
                String token = authHeader.substring(7);
                if (!SimpleJwtUtil.validateToken(token)) {
                    halt(401, gson.toJson(new ErrorResponse("Invalid or expired token")));
                }
            });

            // Place order endpoint
            post("", (req, res) -> {
                String token = req.headers("Authorization").substring(7);
                int userId = SimpleJwtUtil.getUserIdFromToken(token);

                Order order = gson.fromJson(req.body(), Order.class);
                order.setUserId(userId);
                Order savedOrder = service.placeOrder(order);

                if (savedOrder == null) {
                    res.status(500);
                    return gson.toJson(new ErrorResponse("Failed to place order"));
                }
                res.status(201);
                return gson.toJson(savedOrder);
            });

            // Get past orders for logged-in user
            get("/my", (req, res) -> {
                String token = req.headers("Authorization").substring(7);
                int userId = SimpleJwtUtil.getUserIdFromToken(token);

                List<Order> orders = service.getOrdersByUser(userId);
                res.type("application/json");
                return gson.toJson(orders);
            });

            // Cancel order endpoint
            put("/:id/cancel", (req, res) -> {
                String token = req.headers("Authorization").substring(7);
                int userId = SimpleJwtUtil.getUserIdFromToken(token);
                int orderId = Integer.parseInt(req.params("id"));

                boolean canceled = service.cancelOrder(orderId, userId);
                if (!canceled) {
                    res.status(400);
                    return gson.toJson(new ErrorResponse("Cannot cancel order"));
                }
                return gson.toJson(new SuccessResponse("Order canceled successfully"));
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

