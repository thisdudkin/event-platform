package com.modsen.auth.mapper;

import com.modsen.auth.dto.request.RegistrationFields;
import com.modsen.auth.dto.response.UserResponse;
import com.modsen.auth.model.User;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * @author Alexander Dudkin
 */
@Mapper(componentModel = SPRING)
public interface UserMapper {
    User toUser(RegistrationFields request);
    UserResponse toResponse(User user);
}
