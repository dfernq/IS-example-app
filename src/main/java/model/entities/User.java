package model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor // required by our custom entity mapper
@AllArgsConstructor // required by lombock
public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
}
