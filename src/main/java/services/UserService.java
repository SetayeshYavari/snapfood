
package services;
import model.User;
import java.util.*;

public class UserService {
    private final Map<Integer, User> userMap = new HashMap<>();
    private int currentId = 1;

    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    public User getUser(int id) {
        return userMap.get(id);
    }

    public User createUser(User user) {
        user.setId(currentId++);
        userMap.put(user.getId(), user);
        return user;
    }

    public User updateUser(int id, User updatedUser) {
        if (userMap.containsKey(id)) {
            updatedUser.setId(id);
            userMap.put(id, updatedUser);
            return updatedUser;
        }
        return null;
    }

    public boolean deleteUser(int id) {
        return userMap.remove(id) != null;
    }
}
