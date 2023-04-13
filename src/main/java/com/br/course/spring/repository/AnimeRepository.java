package com.br.course.spring.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.br.course.spring.domain.Anime;

public interface AnimeRepository extends JpaRepository<Anime, Long>{
  List<Anime> findByName(String name);
}
