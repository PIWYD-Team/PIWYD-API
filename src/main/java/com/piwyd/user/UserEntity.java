package com.piwyd.user;

import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

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
    private String password;

    @Column
    @NotNull
    private String privateKey;

    @Column
	@NotNull
    private Date lastTimePasswordUpdated;

    @Column
	@NotNull
    private Boolean admin;
}
