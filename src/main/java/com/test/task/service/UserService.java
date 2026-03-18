package com.test.task.service;

import com.test.task.dto.request.CreateUserRequest;
import com.test.task.dto.response.UserResponse;
import com.test.task.entity.User;
import com.test.task.exception.AppException;
import com.test.task.exception.ErrorCode;
import com.test.task.mapper.UserMapper;
import com.test.task.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    final UserRepository userRepository;
    UserMapper userMapper;


    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserResponse(user);
    }

    public List<UserResponse> getUsers(String name, int pageNo, int  pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<User> users;
        if (name==null || name.isEmpty()) {
            users = userRepository.findAll(pageable);
        } else {
            users = userRepository.findAllByName(name, pageable);
        }
        return users.getContent().stream()
                .map(userMapper::toUserResponse).toList();
    }
}
