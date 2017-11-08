package com.piwyd.user.face;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FaceImage {

    private static final String GALLERY_NAME = "gallery1";

    private String image;
    private String subject_id;
    private String gallery_name;

    public FaceImage(String file, String idUser) {
        this.image = file;
        this.subject_id = idUser;
        this.gallery_name = GALLERY_NAME;
    }
}
