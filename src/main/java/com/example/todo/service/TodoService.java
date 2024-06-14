package com.example.todo.service;

import com.example.todo.dto.TodoDTO;
import com.example.todo.model.TodoEntity;
import com.example.todo.persistence.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TodoService {

	@Autowired
	private TodoRepository repository;

	public List<TodoEntity> create(TodoEntity entity) {
		validate(entity);
		repository.save(entity);
		return repository.findByUserId(entity.getUserId());
	}

	public List<TodoEntity> retrieve(String userId) {
		return repository.findByUserId(userId);
	}

	public List<TodoEntity> update(TodoEntity entity) {
		validate(entity);
		if (repository.existsById(entity.getId())) {
			repository.save(entity);
		} else {
			throw new RuntimeException("Unknown id");
		}
		return repository.findByUserId(entity.getUserId());
	}

	public List<TodoEntity> delete(TodoEntity entity) {
		if (repository.existsById(entity.getId())) {
			repository.deleteById(entity.getId());
		} else {
			throw new RuntimeException("id does not exist");
		}
		return repository.findByUserId(entity.getUserId());
	}

	public void validate(TodoEntity entity) {
		if (entity == null) {
			throw new RuntimeException("Entity cannot be null.");
		}
		if (entity.getUserId() == null) {
			throw new RuntimeException("Unknown user.");
		}
	}

	public void saveTodosToFile(String userId, String filePath) {
		List<TodoEntity> entities = repository.findByUserId(userId);
		List<TodoDTO> todos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		StringBuilder content = new StringBuilder();

		content.append("MAIN QUEST\n");
		todos.stream().filter(TodoDTO::isMainTask).forEach(todo -> content.append(formatTodoForMainQuest(todo)).append("\n"));

		content.append("\nCLEAR QUEST\n");
		todos.stream().filter(todo -> todo.isDone() && todo.isMainTask()).forEach(todo -> content.append(formatTodoForClearQuest(todo)).append("\n"));

		content.append("\n");

		Map<String, List<TodoDTO>> dailyTodos = todos.stream().collect(Collectors.groupingBy(todo -> new SimpleDateFormat("yyyy-MM-dd").format(todo.getDate())));
		dailyTodos.forEach((date, todoList) -> {
			content.append("Daily quest ").append(date).append("\n");
			todoList.forEach(todo -> content.append(formatTodoForDailyQuest(todo)).append("\n"));
			content.append("\n");
		});

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			writer.write(content.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String formatTodoForMainQuest(TodoDTO todo) {
		return String.format("%s", todo.getTitle());
	}

	private String formatTodoForClearQuest(TodoDTO todo) {
		return String.format("%s", todo.getTitle());
	}

	private String formatTodoForDailyQuest(TodoDTO todo) {
		return String.format("%s %s", todo.getTitle(), todo.isDone() ? "CLEAR" : "");
	}
}
