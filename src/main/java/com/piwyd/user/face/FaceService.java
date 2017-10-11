package com.piwyd.user.face;

import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;

public interface FaceService {

	ResponseEntity registerNewFace(String fileBase64, Long idUser);

	ResponseEntity<String> verifyUserFace(String fileBase64, Long idUser);
}
