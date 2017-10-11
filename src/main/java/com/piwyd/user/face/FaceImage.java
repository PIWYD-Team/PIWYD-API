package com.piwyd.user.face;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FaceImage {

    private static final String GALLERY_NAME = "gallery1";

    private String url;
    private String subject_id;
    private String gallery_name;

    public FaceImage(String file, String idUser) {
        this.url = file;
        this.subject_id = idUser;
        this.gallery_name = GALLERY_NAME;
    }
}
