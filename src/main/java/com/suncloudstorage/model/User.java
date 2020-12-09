package com.suncloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("Users")
public class User {
    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;
    private Status status;
}
