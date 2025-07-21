package controller;

import Messages.ErrorResponse;
import Messages.SuccessResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.*;
import services.UserService;
import utils.SimpleJwtUtil;

import static spark.Spark.*;

public class UserController {

    private static final UserService userService = new UserService();
    private static final Gson gson = new Gson();

    public static void initRoutes() {
        before("/users/*", (req, res) -> {
            String path = req.pathInfo();
            if (req.requestMethod().equals("OPTIONS") ||
                    path.equals("/users/register") ||
                    path.equals("/users/login")) {
                return;
            }

            String authHeader = req.headers("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                res.status(401);
                halt(401, gson.toJson(new ErrorResponse("Missing or invalid token")));
            }

            String token = authHeader.substring(7);
            if (!SimpleJwtUtil.validateToken(token)) {
                res.status(401);
                halt(401, gson.toJson(new ErrorResponse("Invalid or expired token")));
            }
        });

        path("/users", () -> {
            get("", (req, res) -> gson.toJson(userService.getAllUsers()));

            get("/:id", (req, res) -> {
                int id = Integer.parseInt(req.params(":id"));
                User user = userService.getUser(id);
                if (user == null) {
                    res.status(404);
                    return gson.toJson(new ErrorResponse("User not found"));
                }
                return gson.toJson(user);
            });

            post("/register", (req, res) -> {
                try {
                    User user = createUserFromRole(req.body());
                    user.validateRequiredFields();

                    User registered = userService.register(user);
                    if (registered == null) {
                        res.status(400);
                        return gson.toJson(new ErrorResponse("Phone already registered"));
                    }

                    res.status(201);
                    return gson.toJson(registered);
                } catch (Exception e) {
                    res.status(400);
                    return gson.toJson(new ErrorResponse(e.getMessage()));
                }
            });

            post("/login", (req, res) -> {
                LoginRequest loginReq = gson.fromJson(req.body(), LoginRequest.class);

                User loggedIn = userService.login(loginReq.getPhone(), loginReq.getPassword());
                if (loggedIn == null) {
                    res.status(401);
                    return gson.toJson(new ErrorResponse("Invalid phone or password"));
                }

                String token = SimpleJwtUtil.generateToken(loggedIn.getId(), loggedIn.getRole());

                LoginResponse response = new LoginResponse(loggedIn);
                response.token = token;
                return gson.toJson(response);
            });

            get("/:id/profile", (req, res) -> {
                int id = Integer.parseInt(req.params(":id"));
                User user = userService.getUser(id);
                if (user == null) {
                    res.status(404);
                    return gson.toJson(new ErrorResponse("User not found"));
                }
                return gson.toJson(user);
            });

            put("/:id/profile", (req, res) -> {
                int id = Integer.parseInt(req.params(":id"));
                User existing = userService.getUser(id);
                if (existing == null) {
                    res.status(404);
                    return gson.toJson(new ErrorResponse("User not found"));
                }

                User update = createUserFromRole(req.body());

                if (update.getFullName() != null) existing.setFullName(update.getFullName());
                if (update.getPhone() != null) existing.setPhone(update.getPhone());
                if (update.getEmail() != null) existing.setEmail(update.getEmail());
                if (update.getAddress() != null) existing.setAddress(update.getAddress());
                if (update.getProfileImageBase64() != null) existing.setProfileImageBase64(update.getProfileImageBase64());

                if (existing instanceof Vendor && update instanceof Vendor) {
                    ((Vendor) existing).setBankInfo(((Vendor) update).getBankInfo());
                } else if (existing instanceof Courier && update instanceof Courier) {
                    ((Courier) existing).setBankInfo(((Courier) update).getBankInfo());
                }

                User updated = userService.updateUser(id, existing);
                if (updated == null) {
                    res.status(500);
                    return gson.toJson(new ErrorResponse("Update failed"));
                }

                return gson.toJson(updated);
            });

            delete("/:id", (req, res) -> {
                int id = Integer.parseInt(req.params(":id"));
                boolean deleted = userService.deleteUser(id);
                if (!deleted) {
                    res.status(404);
                    return gson.toJson(new ErrorResponse("User not found"));
                }
                return gson.toJson(new SuccessResponse("User deleted successfully"));
            });
        });
    }

    private static User createUserFromRole(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        if (!jsonObject.has("role")) {
            throw new IllegalArgumentException("Role field is required");
        }
        String role = jsonObject.get("role").getAsString();

        switch (role.toLowerCase()) {
            case "vendor":
                return gson.fromJson(json, Vendor.class);
            case "buyer":
                return gson.fromJson(json, Buyer.class);
            case "courier":
                return gson.fromJson(json, Courier.class);
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    private static class LoginResponse {
        int id;
        String name;
        String role;
        String message;
        String token;

        public LoginResponse(User user) {
            this.id = user.getId();
            this.name = user.getFullName();
            this.role = user.getRole();
            this.message = "Login successful!";
        }
    }

    private static class LoginRequest {
        private String phone;
        private String password;

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}