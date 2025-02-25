package com.modsen.profile.controller;

import com.modsen.profile.controller.api.ProfileApi;
import com.modsen.profile.dto.request.ProfileRequest;
import com.modsen.profile.dto.response.ProfileResponse;
import com.modsen.profile.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @PostMapping
    public ResponseEntity<Void> createProfile(@RequestBody ProfileRequest profileRequest) {
        profileService.saveProfile(profileRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }


    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable UUID id) {
        profileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponse> updateProfile(@PathVariable UUID id,
                                                         @RequestBody ProfileRequest profileRequest) {
        return ResponseEntity.ok(profileService.updateProfile(id, profileRequest));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.of(profileService.findById(id));
    }

}
