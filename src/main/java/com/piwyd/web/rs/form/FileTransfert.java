package com.piwyd.web.rs.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileTransfert {
    public byte[] file;
    private String fileName;
    private Long idOwner;
}
