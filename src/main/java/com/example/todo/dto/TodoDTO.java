package com.example.todo.dto;

import com.example.todo.model.TodoEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoDTO {
	private String id;
	private String title;
	private boolean done;
	private Date date; // 날짜 필드 추가

	@JsonProperty("isMainTask") // JSON 필드명 설정
	private boolean isMainTask;

	public TodoDTO(final TodoEntity entity) {
		this.id = entity.getId();
		this.title = entity.getTitle();
		this.done = entity.isDone();
		this.date = entity.getDate(); // 날짜 필드 추가
		this.isMainTask = entity.isMainTask(); // 메인 할 일 여부
	}

	public static TodoEntity toEntity(final TodoDTO dto) {
		return TodoEntity.builder()
				.id(dto.getId())
				.title(dto.getTitle())
				.done(dto.isDone())
				.date(dto.getDate()) // 날짜 필드 추가
				.isMainTask(dto.isMainTask()) // 메인 할 일 여부
				.build();
	}
}
