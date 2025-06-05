package controller;

import com.google.gson.Gson;
import model.User;
import services.UserService;
import static spark.Spark.*;

public class UserController {
    private static final UserService userService = new UserService();
    private static final Gson gson = new Gson();

    public static void initRoutes() {
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

            post("", (req, res) -> {
                User user = gson.fromJson(req.body(), User.class);
                return gson.toJson(userService.createUser(user));
            });

            put("/:id", (req, res) -> {
                int id = Integer.parseInt(req.params(":id"));
                User user = gson.fromJson(req.body(), User.class);
                User updated = userService.updateUser(id, user);
                if (updated == null) {
                    res.status(404);
                    return "User not found";
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
}
