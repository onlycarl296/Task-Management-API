package com.test.task.controller;

import com.test.task.dto.request.CreateTaskRequest;
import com.test.task.dto.request.StatusRequest;
import com.test.task.dto.response.ApiResponse;
import com.test.task.dto.response.TaskResponse;
import com.test.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ApiResponse<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        return ApiResponse.<TaskResponse>builder()
                .result(taskService.createTask(request))
                .build();
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<TaskResponse> updateStatus(@PathVariable Long id,
                             @RequestBody StatusRequest request) {
        return ApiResponse.<TaskResponse>builder()
                .result(taskService.updateStatus(id, request.getStatus()))
                .build();
    }

    @GetMapping()
    public ApiResponse<List<TaskResponse>> getTasks(@RequestParam(required = false) Long assigneeId,
                                                   @RequestParam(required = false) String status,
                                                   @RequestParam(required = false) LocalDate dueDate,
                                                   @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                   @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return ApiResponse.<List<TaskResponse>>builder()
                .result(taskService.getTasks(assigneeId, status,dueDate,pageNo,pageSize))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<TaskResponse> getTask(@PathVariable Long id) {
        return ApiResponse.<TaskResponse>builder()
                .result(taskService.getTask(id))
                .build();
    }
}
