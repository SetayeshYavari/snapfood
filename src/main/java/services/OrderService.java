package services;

import DAO.OrderDAO;
import model.Order;
import model.OrderItem;

import java.time.LocalDate;
import java.util.List;

public class OrderService {
    private final OrderDAO dao = new OrderDAO();

    private static final double TAX_RATE = 0.1;
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

    public List<Order> getOrdersByRestaurant(int restaurantId) {
        return dao.getOrdersByRestaurant(restaurantId);
    }

    public boolean confirmOrder(int orderId, int restaurantId) {
        Order order = dao.getOrderById(orderId);
        if (order == null) return false;
        if (order.getRestaurantId() != restaurantId) return false;
        if (!order.getStatus().equalsIgnoreCase("PENDING")) return false;
        return dao.updateOrderStatus(orderId, "IN_PROGRESS");
    }

    public boolean assignCourier(int orderId, int courierId) {
        Order order = dao.getOrderById(orderId);
        if (order == null || !order.getStatus().equalsIgnoreCase("IN_PROGRESS")) {
            return false;
        }
        return dao.assignCourier(orderId, courierId);
    }

    public boolean updateDeliveryStatus(int orderId, int courierId, String newStatus) {
        Order order = dao.getOrderById(orderId);
        if (order == null || !courierIdEquals(order, courierId)) return false;
        if (!newStatus.equals("ON_THE_WAY") && !newStatus.equals("DELIVERED")) return false;
        return dao.updateOrderStatus(orderId, newStatus);
    }

    private boolean courierIdEquals(Order order, int courierId) {
        return order.getCourierId() != null && order.getCourierId() == courierId;
    }

    public List<Order> getUserOrderHistory(int userId, String status, LocalDate fromDate, LocalDate toDate) {
        return dao.getOrdersByUserWithFilters(userId, status, fromDate, toDate);
    }

    public List<Order> getOngoingOrders() {
        return OrderDAO.getOrdersByStatus("READY");
    }

    public List<Order> getDeliveredOrders() {
        return OrderDAO.getOrdersByStatus("DELIVERED");
    }

    public List<Order> getAllOrders() {
        return OrderDAO.getAllOrders();
    }

    public boolean updateStatusBySeller(int orderId, String newStatus) {
        Order order = dao.getOrderById(orderId);
        if (order == null) return false;

        boolean updated = dao.updateOrderStatus(orderId, newStatus);
        if (updated) {
            dao.addStatusHistory(orderId, newStatus); // track status change
            // You can later add notification trigger here
            return true;
        }
        return false;
    }

    public List<String> getStatusHistory(int orderId) {
        return dao.getOrderStatusHistory(orderId);
    }


}
