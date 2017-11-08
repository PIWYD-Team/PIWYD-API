package com.piwyd.user.face;

public interface FaceService {

	void registerNewFace(String fileBase64, Long idUser) throws KairosAPIException;

	String verifyUserFace(String fileBase64, Long idUser) throws KairosAPIException;
}
