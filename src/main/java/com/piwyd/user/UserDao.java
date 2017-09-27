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
@Table
public class UserDao {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    @NotNull
    private Long idUser;

    @Column
    @NotEmpty
    @NotNull
    @Email
    private String emailUser;

    @Column
    @NotNull
    @NotEmpty
    @Length(min = 8)
    private String passwordUser;
}
