package services;

import model.User;
import DAO.UserDAO;
import utils.PasswordUtil;

import java.util.List;

public class UserService {

    public User register(User user) {
        // Check if user with this phone already exists
        System.out.println("☎️ Registering phone: " + user.getPhone());
        User existing = UserDAO.findByPhone(user.getPhone());
        System.out.println("🧠 Existing user: " + (existing == null ? "none" : existing.getId()));
        if (existing != null) {
            return null; // Phone already registered
        }

        // Generate salt and hash password before storing
        String salt = PasswordUtil.generateSalt();
        String hashedPassword = PasswordUtil.hashPassword(user.getPassword(), salt);

        // Set the hashed password and salt
        user.setPassword(hashedPassword);
        user.setSalt(salt);

        // Store the user with hashed password
        return UserDAO.addUser(user);
    }

    public User login(String phone, String password) {
        User user = UserDAO.findByPhone(phone);
        if (user != null) {
            // Verify password with salt
            System.out.println("🔍 User found in DB: " + user.getPhone());
            boolean passwordCorrect = PasswordUtil.verifyPassword(
                    password,
                    user.getSalt(),
                    user.getPassword()
            );
            System.out.println("🔐 Password correct? " + passwordCorrect);
            if (passwordCorrect) {
                return user;
            }
        } else {
            System.out.println("❌ No user found with phone: " + phone);
        }
        return null;
    }

    // The rest of your methods remain unchanged
    public List<User> getAllUsers() {
        return UserDAO.getAllUsers();
    }

    public User getUser(int id) {
        return UserDAO.getUserById(id);
    }

    public User updateUser(int id, User updatedUser) {
        updatedUser.setId(id); // Ensure correct ID
        boolean success = UserDAO.updateUser(updatedUser);
        return success ? updatedUser : null;
    }

    public boolean deleteUser(int id) {
        return UserDAO.deleteUser(id);
    }
}