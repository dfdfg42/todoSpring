package com.example.todo.security;

import org.springframework.stereotype.Service;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.util.Collections;

@Service
public class GoogleTokenVerifier {

    private final GoogleIdTokenVerifier verifier;

    public GoogleTokenVerifier() {
        verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList("139796939141-l9nb08r81e2cmgj51ml18fg5btf4tsn9.apps.googleusercontent.com"))
                .build();
    }

    public String verify(String idTokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                return payload.getSubject(); // 사용자의 Google ID 반환
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getEmail(String idTokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                return payload.getEmail(); // 사용자의 이메일 반환
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getName(String idTokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                return (String) payload.get("name"); // 사용자의 이름 반환
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}