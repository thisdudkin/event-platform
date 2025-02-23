package com.modsen.profile.controller;

import com.modsen.profile.controller.api.ProfileApi;
import com.modsen.profile.dto.request.ProfileRequest;
import com.modsen.profile.dto.response.ProfileResponse;
import com.modsen.profile.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@RestController
@RequestMapping(ProfileApi.URI)
public class ProfileController implements ProfileApi {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Override
    public ResponseEntity<Void> createProfile(ProfileRequest profileRequest) {
        profileService.saveProfile(profileRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }


    @Override
    public ResponseEntity<Void> deleteProfile(UUID id) {
        profileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ProfileResponse> updateProfile(UUID id, ProfileRequest profileRequest) {
        return ResponseEntity.ok(profileService.updateProfile(id, profileRequest));
    }

    @Override
    public ResponseEntity<ProfileResponse> getById(UUID id) {
        return ResponseEntity.of(profileService.findById(id));
    }

}
