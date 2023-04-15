package com.brainstation23.erp.service;

import com.brainstation23.erp.exception.custom.custom.NotFoundException;
import com.brainstation23.erp.mapper.UserMapper;
import com.brainstation23.erp.model.domain.User;
import com.brainstation23.erp.model.dto.*;
import com.brainstation23.erp.persistence.entity.UserEntity;
import com.brainstation23.erp.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    public static final String USER_NOT_FOUND = "Organization Not Found";
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Page<User> getAll(Pageable pageable) {
        var entities = userRepository.findAll(pageable);
        return entities.map(userMapper::entityToDomain);
    }

    public User getOne(UUID id) {
        var entity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        return userMapper.entityToDomain(entity);
    }

    public UUID createOne(CreateUserRequest createRequest) {
        var entity = new UserEntity();
        entity.setId(UUID.randomUUID())
                .setFirstName(createRequest.getFirstName())
                .setLastName(createRequest.getLastName());
        var createdEntity = userRepository.save(entity);
        return createdEntity.getId();
    }

    public void updateOne(UUID id, UpdateUserRequest updateRequest) {
        var entity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        entity.setFirstName(updateRequest.getFirstName());
        userRepository.save(entity);
    }

    public void deleteOne(UUID id) {
        userRepository.deleteById(id);
    }

//    private String generateOrganizationCode() {
//        return RandomUtils.generateAlphaNumeric(6).toUpperCase();
//    }
}
