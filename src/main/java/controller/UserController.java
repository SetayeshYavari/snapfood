package controller;

import com.google.gson.Gson;
import model.User;
import services.UserService;
import utils.SimpleJwtUtil;

import static spark.Spark.*;

public class UserController {
    private static final UserService userService = new UserService();
    private static final Gson gson = new Gson();

    public static void initRoutes() {
        before("/users/*", (req, res) -> {
            if (req.requestMethod().equals("OPTIONS") ||
                    req.pathInfo().equals("/users/register") ||
                    req.pathInfo().equals("/users/login")) {
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
                    return "User not found";
                }
                return gson.toJson(user);
            });

            // Register
            post("/register", (req, res) -> {
                User user = gson.fromJson(req.body(), User.class);
                User registered = userService.register(user);
                if (registered == null) {
                    res.status(400);
                    return gson.toJson(new ErrorResponse("Phone already registered"));
                }
                res.status(201);
                return gson.toJson(registered);
            });

            // Login
            post("/login", (req, res) -> {
                User loginUser = gson.fromJson(req.body(), User.class);
                User loggedIn = userService.login(loginUser.getPhone(), loginUser.getPassword());
                if (loggedIn == null) {
                    res.status(401);
                    return gson.toJson(new ErrorResponse("Invalid phone or password"));
                }

                String token = SimpleJwtUtil.generateToken(loggedIn.getId(), loggedIn.getRole());
                LoginResponse response = new LoginResponse(loggedIn);
                response.token = token;
                return gson.toJson(response);
            });

            before("/users/*", (req, res) -> {
                String path = req.pathInfo();
                if (req.requestMethod().equals("OPTIONS") ||
                        path.equals("/users/login") ||
                        path.equals("/users/register")) return;

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

                User update = gson.fromJson(req.body(), User.class);

                // Always updatable for all roles
                if (update.getFullName() != null) existing.setFullName(update.getFullName());
                if (update.getPhone() != null) existing.setPhone(update.getPhone());
                if (update.getEmail() != null) existing.setEmail(update.getEmail());
                if (update.getAddress() != null) existing.setAddress(update.getAddress());
                if (update.getProfilePhotoUrl() != null) existing.setProfilePhotoUrl(update.getProfilePhotoUrl());

                // Bank info only for seller or courier
                if ("seller".equalsIgnoreCase(existing.getRole()) || "courier".equalsIgnoreCase(existing.getRole())) {
                    if (update.getBankInfo() != null) {
                        existing.setBankInfo(update.getBankInfo());
                    }
                }

                // Restaurant info only for seller
                if ("seller".equalsIgnoreCase(existing.getRole())) {
                    if (update.getBrandName() != null) {
                        existing.setBrandName(update.getBrandName());
                    }
                    if (update.getRestaurantDescription() != null) {
                        existing.setRestaurantDescription(update.getRestaurantDescription());
                    }
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
                    return "User not found";
                }
                return "User deleted";
            });
        });
    }

    private static class ErrorResponse {
        String error;
        public ErrorResponse(String error) { this.error = error; }
    }

    private static class LoginResponse {
        int id;
        String name;
        String role;
        String message;
        String token; // JWT token

        public LoginResponse(User user) {
            this.id = user.getId();
            this.name = user.getFullName();
            this.role = user.getRole();
            this.message = "Login successful!";
        }
    }
}

