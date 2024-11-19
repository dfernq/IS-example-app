package model;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EntityMapper {

    /**
     * Map database result set to entity object
     * @param resultSet database record
     * @param entityClass entity class to map to
     * @return entity object
     */
    public static <T> T mapResultSetToEntity(ResultSet resultSet, Class<T> entityClass) {
        T entity = null;
        try {
            // Create a new instance of the entity class
            entity = entityClass.getDeclaredConstructor().newInstance();

            // Iterate over all fields in the class
            Field[] fields = entityClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true); // Access private fields
                try {
                    // Set field value using the ResultSet if the column exists
                    Object value = resultSet.getObject(field.getName());
                    if (value != null) {
                        field.set(entity, value);
                    }
                } catch (SQLException ignored) {
                    // Ignore fields not present in the ResultSet
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error mapping ResultSet to entity", e);
        }
        return entity;
    }
}

