package com.piwyd.user.face;

import com.piwyd.web.security.JWTLoginFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class FaceServiceImpl implements FaceService {

	private static final Logger logger = LoggerFactory.getLogger(JWTLoginFilter.class);

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
    public void registerNewFace(String fileBase64, Long idUser) throws KairosAPIException {
        RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = getHeaders();
        FaceImage faceImage = new FaceImage(fileBase64, String.valueOf(idUser));
        HttpEntity<FaceImage> httpEntity = new HttpEntity<>(faceImage, httpHeaders);

        String urlComplete = URL + "/enroll";

        ResponseEntity<String> responseEntity = restTemplate.exchange(urlComplete, POST, httpEntity, String.class);

		manageResponse(urlComplete, responseEntity);
    }

    @Override
    public String verifyUserFace(String fileBase64, Long idUser) throws KairosAPIException {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = getHeaders();
		FaceImage faceImage = new FaceImage(fileBase64, String.valueOf(idUser));
		HttpEntity<FaceImage> httpEntity = new HttpEntity<>(faceImage, httpHeaders);

		String urlComplete = URL + "/verify";

		ResponseEntity<String> responseEntity = restTemplate.exchange(urlComplete, POST, httpEntity, String.class);

		manageResponse(urlComplete, responseEntity);

		return responseEntity.getBody();
	}

	private void manageResponse(String url, ResponseEntity<String> responseEntity) throws KairosAPIException {
		String body = responseEntity.getBody();

		if (responseEntity.getStatusCode() != HttpStatus.OK) {
			logger.warn("Kairos API call " + url + ", response status : " + responseEntity.getStatusCode());
			throw new KairosAPIException("Une erreur technique est survenue");
		} else if (body.contains("Errors")) {
			logger.warn("Kairos API call " + url + ", response body : " + body);
			throw new KairosAPIException(body);
		}
	}
}
