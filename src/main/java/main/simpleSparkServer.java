package main;

import static spark.Spark.*;
import controller.UserController;
import controller.RestaurantController;
import controller.FoodController;



public class simpleSparkServer {
    public static void main(String[] args) {
        port(8080);
        UserController.initRoutes();
        RestaurantController.initRoutes();
        FoodController.initRoutes();
        get("/", (req, res) -> "Food Service API is running!");
        System.out.println("==> Spark has started on http://localhost:8080");
    }
}