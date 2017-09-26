package com.piwyd.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private int idUser;

    @Column
    @NotEmpty
    @NotNull
    private String nameUser;
}
