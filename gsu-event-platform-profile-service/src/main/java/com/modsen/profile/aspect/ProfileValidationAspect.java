package com.modsen.profile.aspect;

import com.modsen.profile.dto.request.ProfileRequest;
import com.modsen.profile.exception.DuplicateEmailException;
import com.modsen.profile.model.Profile;
import com.modsen.profile.repository.ProfileRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@Aspect
@Component
public class ProfileValidationAspect {

    private final ProfileRepository profileRepository;

    public ProfileValidationAspect(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Around("execution(* com.modsen.profile.service.ProfileService.saveProfile(..)) && args(profileRequest)")
    public Object validateBeforeSave(ProceedingJoinPoint joinPoint, ProfileRequest profileRequest) throws Throwable {
        if (profileRepository.existsByEmail(profileRequest.email())) {
            throw new DuplicateEmailException("Email already taken: " + profileRequest.email());
        }

        // TODO: Validation for userId
        return joinPoint.proceed();
    }

    @Around(value = "execution(* com.modsen.profile.service.ProfileService.updateProfile(..)) && args(id, profileRequest)", argNames = "joinPoint,id,profileRequest")
    public Object validateBeforeUpdate(ProceedingJoinPoint joinPoint, UUID id, ProfileRequest profileRequest) throws Throwable {
        Optional<Profile> profile = profileRepository.findByEmail(profileRequest.email());
        if (profile.isPresent() && !profile.get().id().equals(id)) {
            throw new DuplicateEmailException("Email already taken: " + profileRequest.email());
        }

        return joinPoint.proceed();
    }

}
