package com.test.task.service;

import com.test.task.dto.request.CreateTaskRequest;
import com.test.task.dto.response.TaskResponse;
import com.test.task.dto.response.UserResponse;
import com.test.task.entity.Task;
import com.test.task.entity.TaskStatus;
import com.test.task.entity.User;
import com.test.task.exception.AppException;
import com.test.task.exception.ErrorCode;
import com.test.task.mapper.TaskMapper;
import com.test.task.repository.TaskRepository;
import com.test.task.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskService {

    final TaskRepository taskRepository;
    final UserRepository userRepository;
    TaskMapper taskMapper;

    public TaskResponse createTask(CreateTaskRequest request) {

        User user = userRepository.findById(request.getAssigneeId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (request.getDueDate().isBefore(LocalDate.now())) {
            throw new AppException(ErrorCode.DUE_DATE_NOT_VALID);
        }

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        task.setStatus(TaskStatus.TODO);
        task.setAssignee(user);

        try {
            task = taskRepository.save(task);
        } catch (DataIntegrityViolationException exception){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        TaskResponse taskResponse=  taskMapper.toTaskResponse(task);
        taskResponse.setAssigneeId(user.getId());
        return taskResponse;
    }

    public TaskResponse updateStatus(Long id, TaskStatus newStatus) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_EXISTED));

        TaskStatus current = task.getStatus();

        if (current == TaskStatus.TODO && newStatus == TaskStatus.DONE) {
            throw new AppException(ErrorCode.INVALID_TRANSITION);
        }

        task.setStatus(newStatus);
        task=taskRepository.save(task);
        return taskMapper.toTaskResponse(task);
    }

    public List<TaskResponse> getTasks(Long assigneeId,String status,LocalDate dueDate, int pageNo, int  pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Task> taskPage =  taskRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (assigneeId != null) {
                predicates.add(cb.equal(root.get("assignee").get("id"), assigneeId));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (dueDate != null) {
                predicates.add(cb.equal(root.get("dueDate"), dueDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
        return  taskPage.getContent().stream().map(taskMapper::toTaskResponse).toList();
    }
}
