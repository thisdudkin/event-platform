package com.modsen.profile.service;

import com.modsen.profile.dto.request.ProfileRequest;
import com.modsen.profile.dto.response.ProfileResponse;
import com.modsen.profile.model.Profile;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public interface ProfileService {

    void deleteProfile(UUID id);

    ProfileResponse saveProfile(ProfileRequest profileRequest);

    ProfileResponse updateProfile(UUID id, ProfileRequest profileRequest);

    Optional<ProfileResponse> findById(UUID id);

}
