package com.modsen.profile.controller.api;

import com.modsen.profile.dto.request.ProfileRequest;
import com.modsen.profile.dto.response.ProfileResponse;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public interface ProfileApi {
    String URI = "/api/profiles";

    ResponseEntity<Void> createProfile(ProfileRequest profileRequest);

    ResponseEntity<Void> deleteProfile(UUID id);

    ResponseEntity<ProfileResponse> updateProfile(UUID id, ProfileRequest profileRequest);

    ResponseEntity<ProfileResponse> getById(UUID id);
}
