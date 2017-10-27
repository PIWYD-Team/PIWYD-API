package com.piwyd.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    private Long id;

    @Column
    private String name;

    @Column
    @NotEmpty
    @NotNull
    @Email
    private String email;

    @Column
    @NotNull
    @NotEmpty
    @Length(min = 8)
    private String password;

    @Column
    @NotNull
    private String privateKey;
}
