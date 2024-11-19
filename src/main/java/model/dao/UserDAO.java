package model.dao;

import model.entities.User;

import java.util.List;

public interface UserDAO {

    /**
     * Create a new user
     * @param user User object
     * @return User object
     */
    User create(User user);

    /**
     * Read a user by id
     * @param id User id
     * @return User object
     */
    User read(Long id);

    /**
     * Update a user by id
     * @param id User id
     * @return User object
     */
    User update(Long id);

    /**
     * Delete a user by id
     * @param id User id
     */
    void delete(Long id);

    /**
     * List all users
     * @return List of User objects
     */
    List<User> list();

}
