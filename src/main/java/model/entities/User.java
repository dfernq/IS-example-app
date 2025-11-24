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
public class User implements Validatable {
    private Long id;
    private String username;
    private String password;

    @Override
    public void validate() throws ValidationException {
        if (username == null || username.isBlank()) {
            throw new ValidationException("Username cannot be empty");
        }
        if (password == null || password.isBlank()) {
            throw new ValidationException("Password cannot be empty");
        }
    }
}
