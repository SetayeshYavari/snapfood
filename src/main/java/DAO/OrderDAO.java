package DAO;

import model.Order;
import model.Order.Status;
import utils.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // اضافه کردن سفارش جدید و برگردوندن خودش به همراه id
    public static Order addOrder(Order order) {
        String sql = "INSERT INTO orders (delivery_address, customer_id, vendor_id, coupon_id, item_ids, raw_price, tax_fee, additional_fee, courier_fee, pay_price, courier_id, status, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, order.getDeliveryAddress());
            stmt.setInt(2, order.getCustomerId());
            stmt.setInt(3, order.getVendorId());
            stmt.setInt(4, order.getCouponId());
            stmt.setInt(5, order.getItemIds());
            stmt.setInt(6, order.getRawPrice());
            stmt.setInt(7, order.getTaxFee());
            stmt.setInt(8, order.getAdditionalFee());
            stmt.setInt(9, order.getCourierFee());
            stmt.setInt(10, order.getPayPrice());
            stmt.setInt(11, order.getCourierId());
            stmt.setString(12, order.getStatus().name());
            stmt.setString(13, order.getCreatedAt());
            stmt.setString(14, order.getUpdatedAt());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                order.setId(rs.getInt("id"));
                return order;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // به‌روزرسانی سفارش
    public static boolean updateOrder(Order order) {
        String sql = "UPDATE orders SET delivery_address=?, customer_id=?, vendor_id=?, coupon_id=?, item_ids=?, raw_price=?, tax_fee=?, additional_fee=?, courier_fee=?, pay_price=?, courier_id=?, status=?, created_at=?, updated_at=? WHERE id=?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, order.getDeliveryAddress());
            stmt.setInt(2, order.getCustomerId());
            stmt.setInt(3, order.getVendorId());
            stmt.setInt(4, order.getCouponId());
            stmt.setInt(5, order.getItemIds());
            stmt.setInt(6, order.getRawPrice());
            stmt.setInt(7, order.getTaxFee());
            stmt.setInt(8, order.getAdditionalFee());
            stmt.setInt(9, order.getCourierFee());
            stmt.setInt(10, order.getPayPrice());
            stmt.setInt(11, order.getCourierId());
            stmt.setString(12, order.getStatus().name());
            stmt.setString(13, order.getCreatedAt());
            stmt.setString(14, order.getUpdatedAt());
            stmt.setInt(15, order.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // حذف سفارش
    public static boolean deleteOrder(int id) {
        String sql = "DELETE FROM orders WHERE id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // گرفتن سفارش بر اساس id
    public static Order getOrderById(int id) {
        String sql = "SELECT * FROM orders WHERE id = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractOrder(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // گرفتن همه سفارش‌ها
    public static List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";

        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                orders.add(extractOrder(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    // گرفتن سفارش‌ها بر اساس وضعیت (مثلاً ongoing، completed و غیره)
    public static List<Order> getOrdersByStatus(Status status) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE status = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                orders.add(extractOrder(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    // تبدیل ResultSet به شیء Order
    private static Order extractOrder(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String deliveryAddress = rs.getString("delivery_address");
        int customerId = rs.getInt("customer_id");
        int vendorId = rs.getInt("vendor_id");
        int couponId = rs.getInt("coupon_id");
        int itemIds = rs.getInt("item_ids");
        int rawPrice = rs.getInt("raw_price");
        int taxFee = rs.getInt("tax_fee");
        int additionalFee = rs.getInt("additional_fee");
        int courierFee = rs.getInt("courier_fee");
        int payPrice = rs.getInt("pay_price");
        int courierId = rs.getInt("courier_id");
        Order.Status status = Order.Status.valueOf(rs.getString("status"));
        String createdAt = rs.getString("created_at");
        String updatedAt = rs.getString("updated_at");

        return new Order(id, deliveryAddress, customerId, vendorId, couponId, itemIds,
                rawPrice, taxFee, additionalFee, courierFee, payPrice, courierId,
                status, createdAt, updatedAt);
    }
}
