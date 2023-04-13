package com.br.course.spring.service;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.br.course.spring.domain.Anime;

import com.br.course.spring.repository.AnimeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimeService {
  private final AnimeRepository animeRepository;
  List<Anime> animes = new ArrayList<>(List.of(new Anime(1L, "Boku No Hero"), new Anime(2L, "Berserk")));

  public Page<Anime> listAll(Pageable pageable) {
    return animeRepository.findAll(pageable);
  }
  public List<Anime> findName(String name) {
    return animeRepository.findByName(name);
  }
  public Anime findOne(long id) {
    return animeRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "NOT FOUND ANIME BY ID: " + id));
  }

  public Anime save(Anime anime) {
    return animeRepository.save(anime);
  }

  public void delete(long id) {
    Anime animeExists = findOne(id);
    animeRepository.delete(animeExists);
  }

  public Anime update(Anime anime) {
    findOne(anime.getId());
    return animeRepository.save(anime);
  }
  public List<Anime> listAllNoPageable() {
    return animeRepository.findAll();
  }
}
