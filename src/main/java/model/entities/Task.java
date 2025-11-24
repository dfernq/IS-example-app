package model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.Validatable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task implements Validatable {
    private Long id;
    private String title;
    private String description;
    private Boolean completed;
    private Long userId;

    @Override
    public void validate() throws ValidationException {
        if (title == null || title.isBlank()) {
            throw new ValidationException("Title cannot be empty");
        }
        if (description == null) {
            description = "";
        }
        if (completed == null) {
            completed = Boolean.FALSE;
        }
        if (userId == null) {
            throw new ValidationException("Task must belong to a user");
        }
    }

    @Override
    public String toString() {
        return (Boolean.TRUE.equals(completed) ? "[x] " : "[ ] ") + title;
    }
}
