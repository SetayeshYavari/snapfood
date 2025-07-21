package controller;
//----don't mind the errors :)-----NOT COMPLETED----------
import com.google.gson.Gson;
import model.Order;
import services.OrderService;
import utils.SimpleJwtUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import services.OrderService;

import static controller.AdminController.orderService;
import static spark.Spark.*;

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

            post("", (req, res) -> {
                String token = req.headers("Authorization").substring(7);
                int userId = SimpleJwtUtil.getUserIdFromToken(token);

                Order order = gson.fromJson(req.body(), Order.class);
                order.setUserId(userId);
                Order saved = service.placeOrder(order);

                if (saved == null) {
                    res.status(500);
                    return gson.toJson(new ErrorResponse("Failed to place order"));
                }
                res.status(201);
                return gson.toJson(saved);
            });

            get("", (req, res) -> {
                String token = req.headers("Authorization").substring(7);
                int userId = SimpleJwtUtil.getUserIdFromToken(token);
                List<Order> orders = service.getOrdersByUser(userId);
                res.type("application/json");
                return gson.toJson(orders);
            });

            post("/cancel/:id", (req, res) -> {
                String token = req.headers("Authorization").substring(7);
                int userId = SimpleJwtUtil.getUserIdFromToken(token);
                int orderId = Integer.parseInt(req.params("id"));

                boolean cancelled = service.cancelOrder(orderId, userId);
                if (!cancelled) {
                    res.status(400);
                    return gson.toJson(new ErrorResponse("Cannot cancel order (maybe already shipped or doesn't belong to you)"));
                }
                return gson.toJson(new SuccessResponse("Order cancelled successfully"));
            });

            // Seller views all orders for their restaurant
            get("/restaurant", (req, res) -> {
                String token = req.headers("Authorization").substring(7);
                int restaurantId = SimpleJwtUtil.getRestaurantIdFromToken(token);
                List<Order> orders = service.getOrdersByRestaurant(restaurantId);
                res.type("application/json");
                return gson.toJson(orders);
            });

            // Seller confirms order
            put("/:id/confirm", (req, res) -> {
                String token = req.headers("Authorization").substring(7);
                int restaurantId = SimpleJwtUtil.getRestaurantIdFromToken(token);
                int orderId = Integer.parseInt(req.params("id"));
                boolean confirmed = service.confirmOrder(orderId, restaurantId);
                if (!confirmed) {
                    res.status(400);
                    return gson.toJson(new ErrorResponse("Cannot confirm order"));
                }
                return gson.toJson(new SuccessResponse("Order confirmed"));
            });

            put("/:id/assign", (req, res) -> {
                String token = req.headers("Authorization").substring(7);
                String role = SimpleJwtUtil.getRoleFromToken(token);
                if (!"courier".equals(role)) {
                    halt(403, gson.toJson(new ErrorResponse("Forbidden")));
                }
                int courierId = SimpleJwtUtil.getUserIdFromToken(token);
                int orderId = Integer.parseInt(req.params("id"));

                boolean assigned = service.assignCourier(orderId, courierId);
                if (!assigned) {
                    res.status(400);
                    return gson.toJson(new ErrorResponse("Failed to assign courier"));
                }
                return gson.toJson(new SuccessResponse("Courier assigned successfully"));
            });

            put("/:id/status", (req, res) -> {
                String token = req.headers("Authorization").substring(7);
                int courierId = SimpleJwtUtil.getUserIdFromToken(token);
                String newStatus = gson.fromJson(req.body(), StatusRequest.class).status;
                int orderId = Integer.parseInt(req.params("id"));

                boolean updated = service.updateDeliveryStatus(orderId, courierId, newStatus);
                if (!updated) {
                    res.status(400);
                    return gson.toJson(new ErrorResponse("Failed to update delivery status"));
                }
                return gson.toJson(new SuccessResponse("Delivery status updated"));
            });

            // GET /orders/history?status=DELIVERED&from=2024-01-01&to=2024-12-31
            get("/history", (req, res) -> {
                String token = req.headers("Authorization").substring(7);
                int userId = SimpleJwtUtil.getUserIdFromToken(token);

                String status = req.queryParams("status");
                String fromStr = req.queryParams("from");
                String toStr = req.queryParams("to");

                LocalDate from = (fromStr != null) ? LocalDate.parse(fromStr) : null;
                LocalDate to = (toStr != null) ? LocalDate.parse(toStr) : null;

                List<Order> orders = service.getUserOrderHistory(userId, status, from, to);
                res.type("application/json");
                return gson.toJson(orders);
            });

            // Seller updates order status
            put("/orders/:id/status", (req, res) -> {
                int orderId = Integer.parseInt(req.params(":id"));
                String newStatus = new Gson().fromJson(req.body(), Map.class).get("status").toString();

                String userRole = SimpleJwtUtil.getRoleFromRequest(req);
                if (!"seller".equals(userRole)) {
                    res.status(403);
                    return gson.toJson(new ErrorResponse("Only sellers can update status"));
                }

                boolean result = orderService.updateStatusBySeller(orderId, newStatus);
                if (result) return gson.toJson(Map.of("message", "Status updated to " + newStatus));
                res.status(404);
                return gson.toJson(new ErrorResponse("Order not found or update failed"));
            });

// Get order status history
            get("/orders/:id/status-history", (req, res) -> {
                int orderId = Integer.parseInt(req.params(":id"));
                List<String> history = orderService.getStatusHistory(orderId);
                return gson.toJson(history);
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

    private static class StatusRequest {
        String status;
    }

}

