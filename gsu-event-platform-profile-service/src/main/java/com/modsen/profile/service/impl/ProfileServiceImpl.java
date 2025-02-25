package com.modsen.profile.service.impl;

import com.github.f4b6a3.uuid.UuidCreator;
import com.modsen.profile.dto.request.ProfileRequest;
import com.modsen.profile.dto.response.ProfileResponse;
import com.modsen.profile.exception.ProfileNotFoundException;
import com.modsen.profile.mapper.ProfileMapper;
import com.modsen.profile.model.Profile;
import com.modsen.profile.repository.ProfileRepository;
import com.modsen.profile.service.ProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final ProfileMapper profileMapper;
    private final ProfileRepository profileRepository;

    public ProfileServiceImpl(ProfileMapper profileMapper, ProfileRepository profileRepository) {
        this.profileMapper = profileMapper;
        this.profileRepository = profileRepository;
    }

    @Override
    public ProfileResponse saveProfile(ProfileRequest request) {
        // TODO: Aspect validation, if there already entity with same email/userId in the database.
        Profile profile = profileMapper.toProfile(request);
        // TODO: Retrieve userId from Authorization Header
        profile = profileRepository.save(profile.withUserId(UuidCreator.getTimeOrderedEpoch()));

        return profileMapper.toResponse(profile);
    }

    @Override
    public ProfileResponse updateProfile(UUID id, ProfileRequest request) {
        // TODO: Aspect validation, if there already entity with same email in the database.
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found with ID: " + id));

        profile = profileRepository.update(profile.id(), profileMapper.toProfile(request).withUserId(profile.userId()));

        return profileMapper.toResponse(profile);
    }

    @Override
    public void deleteProfile(UUID id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found with ID: " + id));

        profileRepository.delete(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProfileResponse> findById(UUID id) {
        return profileRepository.findById(id)
                .map(profileMapper::toResponse);
    }

}
