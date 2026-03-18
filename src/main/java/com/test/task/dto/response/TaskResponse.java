package com.test.task.dto.response;

import com.test.task.entity.TaskStatus;
import com.test.task.entity.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskResponse {
    Long id;
    String title;
    String description;
    LocalDate dueDate;
    TaskStatus status;
    Long assigneeId;
}
