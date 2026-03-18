package com.test.task.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTaskRequest {

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Long assigneeId;

    @FutureOrPresent
    private LocalDate dueDate;
}
