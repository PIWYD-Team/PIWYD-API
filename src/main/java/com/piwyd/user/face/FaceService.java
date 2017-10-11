package com.piwyd.user.face;

import org.springframework.http.ResponseEntity;

public interface FaceService {
    ResponseEntity registryNewFace(String fileBase64, Long idUser);
}
