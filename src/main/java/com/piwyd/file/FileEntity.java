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
    private String fileName;

    @Column
    private String filePath;

    @Column
    private Long idOwner;
}
