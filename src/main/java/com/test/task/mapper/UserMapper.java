package com.test.task.mapper;


import com.test.task.dto.request.CreateUserRequest;
import com.test.task.dto.response.UserResponse;
import com.test.task.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(CreateUserRequest request);

    UserResponse toUserResponse(User user);

}
