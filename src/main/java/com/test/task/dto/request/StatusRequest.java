package com.test.task.dto.request;

import com.test.task.entity.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusRequest {

    @NotNull
    private TaskStatus status;
}
