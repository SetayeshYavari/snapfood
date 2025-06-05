package org.example;
import static spark.Spark.*;

public class simpleSparkServer {
    public static void main(String[] args) {
        port(8080);  // Set port to 8080

        get("/hello", (req, res) -> {
            return "Hi buddy";
        });
    }
}