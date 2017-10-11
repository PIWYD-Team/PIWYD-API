package com.piwyd.user.face;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class FaceServiceImpl implements FaceService {

    @Value("${face-recognition.url}")
    private String URL;

    @Value("${face-recognition.app.id}")
    private String ID;

    @Value("${face-recognition.app.key}")
    private String KEY;

    private HttpHeaders getHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();

		httpHeaders.setContentType(APPLICATION_JSON);
		httpHeaders.set("app_id", ID);
		httpHeaders.set("app_key", KEY);

		return httpHeaders;
	}

	@Override
    public ResponseEntity registerNewFace(String fileBase64, Long idUser) {
        RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = getHeaders();
        FaceImage faceImage = new FaceImage(fileBase64, String.valueOf(idUser));
        HttpEntity<FaceImage> httpEntity = new HttpEntity<>(faceImage, httpHeaders);

        String urlComplete = URL + "/enroll";
        URI uri = null;
        try {
            uri = new URI(urlComplete);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return restTemplate.exchange(uri, POST, httpEntity, String.class);
    }

    @Override
    public ResponseEntity<String> verifyUserFace(String fileBase64, Long idUser) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = getHeaders();

		FaceImage faceImage = new FaceImage(fileBase64, String.valueOf(idUser));
		HttpEntity<FaceImage> httpEntity = new HttpEntity<>(faceImage, httpHeaders);

		String urlComplete = URL + "/verify";

		return restTemplate.exchange(urlComplete, POST, httpEntity, String.class);
	}
}
