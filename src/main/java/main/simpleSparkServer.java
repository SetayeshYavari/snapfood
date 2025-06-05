package main;
import static spark.Spark.*;

import controller.UserController;

public class simpleSparkServer {
    public static void main(String[] args) {
        port(8080);

        UserController.initRoutes();
        get("/", (req, res) -> "Hello from Spark Java!");

    }
}