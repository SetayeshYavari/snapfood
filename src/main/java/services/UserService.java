package services;

import model.User;
import DAO.UserDAO;
import utils.PasswordUtil;

import java.util.List;

public class UserService {

    public User register(User rawUser) {
        // بررسی وجود شماره تلفن
        User existing = UserDAO.findByPhone(rawUser.getPhone());
        if (existing != null) {
            return null; // شماره قبلا ثبت شده
        }

        // ساخت سالت و هش کردن پسورد
        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword(rawUser.getPassword(), salt);

        rawUser.setPassword(hashedPassword);
        rawUser.setSalt(salt);

        return UserDAO.addUser(rawUser);
    }

    public User login(String phone, String password) {
        User user = UserDAO.findByPhone(phone);
        if (user != null) {
            boolean passwordCorrect = PasswordUtil.verifyPassword(password, user.getSalt(), user.getPassword());
            if (passwordCorrect) {
                return user;
            }
        }
        return null;
    }

    public List<User> getAllUsers() {
        return UserDAO.getAllUsers();
    }

    public User getUser(int id) {
        return UserDAO.getUserById(id);
    }

    public User updateUser(int id, User updatedUser) {
        updatedUser.setId(id);
        boolean success = UserDAO.updateUser(updatedUser);
        return success ? updatedUser : null;
    }

    public boolean deleteUser(int id) {
        return UserDAO.deleteUser(id);
    }
}
