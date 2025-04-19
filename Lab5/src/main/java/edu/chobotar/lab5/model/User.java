package edu.chobotar.lab5.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/*
  @author Harsteel
  @project Lab5
  @class User
  @version 1.0.0
  @since 19.04.2025 - 22.53
*/
@Data
@Document
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    public String id;
    public String name;
    public String username;
    public String email;
    public String phoneNumber;
    public String gender;

    public User(String name, String username, String email, String phoneNumber, String gender) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return getId().equals(user.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
