package com.br.course.spring.dtos;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CreateAnimeDto {

  @NotEmpty(message = "Anime name cannot be empty")
  @NotNull
  private String name;
}
