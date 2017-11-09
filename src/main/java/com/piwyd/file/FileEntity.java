package com.piwyd.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "file")
public class FileEntity {

    @Id
    private Long id;

    @Column
    @NotEmpty
    @NotNull
    private String name;

    @Column
    @NotEmpty
    @NotNull
    private String path;

    @Column
    @NotNull
    private Long idOwner;
}
