package model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.Validatable;

@Data
@Builder
@NoArgsConstructor // required by our custom entity mapper
@AllArgsConstructor // required by lombock
public class User implements Validatable {
    private Long id;
    private String username;
    private String email;
    private String password;

    @Override
    public void validate() throws ValidationException {
        if (username == null || username.isEmpty()) {
            throw new ValidationException("Username cannot be empty");
        }
        if (email == null || email.isEmpty()) {
            throw new ValidationException("Email cannot be empty");
        }
        if (password == null || password.isEmpty()) {
            throw new ValidationException("Password cannot be empty");
        }
    }
}
