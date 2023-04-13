package com.br.course.spring.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.br.course.spring.domain.Anime;
import com.br.course.spring.dtos.CreateAnimeDto;
import com.br.course.spring.dtos.UpdateAnime;
import com.br.course.spring.service.AnimeService;
import com.br.course.spring.utils.DateUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("animes")
@Log4j2
@RequiredArgsConstructor
public class AnimeController {
  private final DateUtil dateUtil;
  private final AnimeService animeService;

  @GetMapping
  public ResponseEntity<Page<Anime>> list(Pageable pageable) {
    return ResponseEntity.ok(animeService.listAll(pageable)); 
  }

  @GetMapping(path = "all")
  public ResponseEntity<List<Anime>> listAll() {
    return ResponseEntity.ok(animeService.listAllNoPageable()); 
  }

  @GetMapping("/{id}")
  public ResponseEntity<Anime> findOne(@PathVariable long id){
    return ResponseEntity.ok(animeService.findOne(id));
  }
  @GetMapping(path = "find")
  public ResponseEntity<List<Anime>> findName(@RequestParam(required = false) String name){
    return ResponseEntity.ok(animeService.findName(name));
  }

  @PostMapping
  public ResponseEntity<Anime> create(@RequestBody @Valid CreateAnimeDto anime){
    var obj = new Anime();
    BeanUtils.copyProperties(anime, obj);
    obj.setName(anime.getName());
    return new ResponseEntity<>(animeService.save(obj), HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<Anime> update(@RequestBody UpdateAnime anime){
    var obj = new Anime();
    BeanUtils.copyProperties(anime, obj);
    obj.setId(anime.getId());
    obj.setName(anime.getName());
    return new ResponseEntity<>(animeService.update(obj), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable long id){
    animeService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
