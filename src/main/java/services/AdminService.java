package services;

import model.Coupon;
import model.User;
import model.Order;

import DAO.CouponDAO;
import DAO.UserDAO;
import DAO.OrderDAO;

import java.util.List;

public class AdminService {

    // -------------- COUPON --------------

    public List<Coupon> getAllCoupons() {
        return CouponDAO.getAllCoupons();
    }

    public Coupon createCoupon(Coupon coupon) {
        return CouponDAO.addCoupon(coupon);
    }

    public boolean deleteCoupon(int id) {
        return CouponDAO.deleteCoupon(id);
    }

    public Coupon updateCoupon(int id, Coupon updatedCoupon) {
        updatedCoupon.setId(id);
        boolean success = CouponDAO.updateCoupon(updatedCoupon);
        return success ? updatedCoupon : null;
    }

    public Coupon getCouponById(int id) {
        return CouponDAO.getCouponById(id);
    }

    // -------------- USER --------------

    public List<User> getAllUsers() {
        return UserDAO.getAllUsers();
    }

    public User getUserById(int id) {
        return UserDAO.getUserById(id);
    }

    public boolean updateUserStatus(int id, String status) {
        User user = UserDAO.getUserById(id);
        if (user == null) return false;
        user.setStatus(status);
        return UserDAO.updateUser(user);
    }

    // -------------- ORDER --------------

    public List<Order> getAllOrders(String search, String vendor, String courier, String customer, String status) {
        return OrderDAO.getOrdersFiltered(search, vendor, courier, customer, status);
    }
}
