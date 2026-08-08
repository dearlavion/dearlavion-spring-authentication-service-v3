package com.dearlavion.authservice.auth;

import com.dearlavion.authservice.common.exception.UnauthorizedException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

/** Verifies Google ID tokens (same google-api-client library the original Java v1 service used). */
@Service
public class GoogleVerifierService {

    private final GoogleIdTokenVerifier verifier;

    public GoogleVerifierService(@Value("${app.google-client-id}") String googleClientId) {
        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
    }

    public GoogleIdToken.Payload verify(String idTokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) throw new IllegalStateException("empty payload");
            return idToken.getPayload();
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid Google token");
        }
    }
}
