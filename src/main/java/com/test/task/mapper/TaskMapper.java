package com.test.task.mapper;

import com.test.task.dto.request.CreateTaskRequest;
import com.test.task.dto.response.TaskResponse;
import com.test.task.entity.Task;
import com.test.task.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task toTask(CreateTaskRequest request);
    @Mapping(target = "assigneeId", ignore = true)
    TaskResponse toTaskResponse (Task task);
}
