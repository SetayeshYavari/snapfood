package services;

import DAO.OrderDAO;
import model.Order;
import model.OrderItem;

import java.util.List;

public class OrderService {
    private final OrderDAO dao = new OrderDAO();

    // Example tax rate and delivery fee - adjust as needed
    private static final double TAX_RATE = 0.09;    // 9%
    private static final double DELIVERY_FEE = 5.0; // fixed delivery fee

    public Order placeOrder(Order order) {
        // Calculate total items price
        double itemsTotal = 0.0;
        List<OrderItem> items = order.getItems();
        if (items != null) {
            for (OrderItem item : items) {
                itemsTotal += item.getUnitPrice() * item.getQuantity();
            }
        }

        double tax = itemsTotal * TAX_RATE;
        double totalAmount = itemsTotal + tax + DELIVERY_FEE;

        order.setTax(tax);
        order.setDeliveryFee(DELIVERY_FEE);
        order.setTotalAmount(totalAmount);
        order.setStatus("PENDING");

        return dao.createOrder(order);
    }

    public List<Order> getOrdersByUser(int userId) {
        return dao.getOrdersByUser(userId);
    }

    public boolean cancelOrder(int orderId, int userId) {
        Order order = dao.getOrderById(orderId);
        if (order == null) return false;
        if (order.getUserId() != userId) return false;
        if (!order.getStatus().equalsIgnoreCase("PENDING")) return false;
        return dao.updateOrderStatus(orderId, "CANCELED");
    }
}
