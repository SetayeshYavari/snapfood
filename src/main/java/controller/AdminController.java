package controller;

import DAO.CouponDAO;
import DAO.UserDAO;
import com.google.gson.Gson;
import model.Coupon;
import model.User;
import model.Order;
import services.AdminService;
import utils.SimpleJwtUtil;
import Messages.ErrorResponse;
import Messages.SuccessResponse;

import static spark.Spark.*;

import java.util.List;

public class AdminController {

    private static final Gson gson = new Gson();
    private static final AdminService adminService = new AdminService();

    public static void initRoutes() {
        before((req, res) -> {
            String authHeader = req.headers("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                halt(401, gson.toJson(new ErrorResponse("Missing or invalid token")));
            }

            String token = authHeader.substring(7);
            if (!SimpleJwtUtil.validateToken(token)) {
                halt(401, gson.toJson(new ErrorResponse("Invalid or expired token")));
            }

            int userId = SimpleJwtUtil.getUserIdFromToken(token);
            String role = SimpleJwtUtil.getRoleFromToken(token);

            User user = UserDAO.getUserById(userId);
            if (user == null) {
                halt(401, gson.toJson(new ErrorResponse("User not found")));
            }

            req.attribute("user", user);
            req.attribute("role", role);
        });

        // ------------ USERS ------------

        // List all users
        get("/admin/users", (req, res) -> {
            try {
                User user = req.attribute("user");
                if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
                    res.status(403);
                    return "Unauthorized";
                }
                List<User> users = adminService.getAllUsers();
                res.type("application/json");
                res.status(200);
                return gson.toJson(users);
            } catch (Exception e) {
                res.status(500);
                return "Internal server error";
            }
        });


        // Update user approval status
        patch("/admin/users/:id/status", (req, res) -> {
            String id = req.params(":id");
            var json = gson.fromJson(req.body(), java.util.Map.class);
            String status = (String) json.get("status");
            User user = req.attribute("user");
            if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
                res.status(403);
                return "Unauthorized";
            }
            if (!"approved".equals(status) && !"rejected".equals(status)) {
                res.status(400);
                return "Invalid status value";
            }
            boolean updated = adminService.updateUserStatus(Integer.parseInt(id), status);
            if (updated) {
                res.status(200);
                return "Status updated";
            } else {
                res.status(404);
                return "User not found";
            }
        });

        // ------------ ORDERS ------------

        get("/admin/orders", (req, res) -> {
            String search = req.queryParams("search");
            String vendor = req.queryParams("vendor");
            String courier = req.queryParams("courier");
            String customer = req.queryParams("customer");
            String status = req.queryParams("status");
            List<Order> orders = adminService.getAllOrders(search, vendor, courier, customer, status);
            res.type("application/json");
            return gson.toJson(orders);
        });

        // ------------ COUPONS ------------

        // List all coupons
        get("/admin/coupons", (req, res) -> {
            User user = req.attribute("user");
            if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
                res.status(403);
                return "Unauthorized";
            }
            List<Coupon> coupons = adminService.getAllCoupons();
            res.type("application/json");
            return gson.toJson(coupons);
        });

        // Create coupon
        post("/admin/coupons", (req, res) -> {
            try {
                User user = req.attribute("user");
                if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
                    res.status(403);
                    return "Unauthorized";
                }
                Coupon coupon = gson.fromJson(req.body(), Coupon.class);
                Coupon existing = CouponDAO.getCouponByCode(coupon.getCouponCode());
                if (existing != null) {
                    res.status(409);
                    return "Coupon code already exists";
                }
                Coupon created = adminService.createCoupon(coupon);
                res.status(201);
                res.type("application/json");
                return gson.toJson(created);
            } catch (Exception e) {
                res.status(500);
                return "Internal server error";
            }
        });


        // Delete coupon by id
        delete("/admin/coupons/:id", (req, res) -> {
            User user = req.attribute("user");
            if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
                res.status(403);
                return "Unauthorized";
            }
            int id = Integer.parseInt(req.params(":id"));
            boolean deleted = adminService.deleteCoupon(id);
            if (deleted) {
                res.status(200);
                return "Coupon deleted";
            } else {
                res.status(404);
                return "Coupon not found";
            }
        });

        // Update coupon by id
        put("/admin/coupons/:id", (req, res) -> {
            User user = req.attribute("user");
            if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
                res.status(403);
                return "Unauthorized";
            }
            int id = Integer.parseInt(req.params(":id"));
            Coupon coupon = gson.fromJson(req.body(), Coupon.class);
            Coupon updated = adminService.updateCoupon(id, coupon);
            if (updated != null) {
                res.status(200);
                res.type("application/json");
                return gson.toJson(updated);
            } else {
                res.status(404);
                return "Coupon not found";
            }
        });

        // Get coupon details by id
        get("/admin/coupons/:id", (req, res) -> {
            User user = req.attribute("user");
            if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
                res.status(403);
                return "Unauthorized";
            }
            int id = Integer.parseInt(req.params(":id"));
            Coupon coupon = adminService.getCouponById(id);
            if (coupon != null) {
                res.status(200);
                res.type("application/json");
                return gson.toJson(coupon);
            } else {
                res.status(404);
                return "Coupon not found";
            }
        });
    }
}