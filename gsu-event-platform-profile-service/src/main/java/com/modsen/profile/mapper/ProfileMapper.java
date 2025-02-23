package com.modsen.profile.mapper;

import com.modsen.profile.dto.request.ProfileRequest;
import com.modsen.profile.dto.response.ProfileResponse;
import com.modsen.profile.model.Profile;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * @author Alexander Dudkin
 */
@Mapper(componentModel = SPRING)
public interface ProfileMapper {
    Profile toProfile(ProfileRequest profileRequest);
    ProfileResponse toResponse(Profile profile);
}
