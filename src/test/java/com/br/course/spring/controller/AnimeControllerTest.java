package com.br.course.spring.controller;

import com.br.course.spring.domain.Anime;
import com.br.course.spring.dtos.CreateAnimeDto;
import com.br.course.spring.dtos.UpdateAnime;
import com.br.course.spring.service.AnimeService;
import com.br.course.spring.util.AnimeCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Controller Anime test")
class AnimeControllerTest {
    @InjectMocks
    private AnimeController animeControllerMock;
    @Mock
    private AnimeService animeServiceMock;

    @BeforeEach
    void setup(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(animePage);

        BDDMockito.when(animeServiceMock.listAllNoPageable())
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeServiceMock.findOne(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.when(animeServiceMock.findName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.when(animeServiceMock.update(ArgumentMatchers.any(Anime.class)))
                        .thenReturn(AnimeCreator.createValidUpdatedAnime());

        BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyLong());


    }
    @Test
    @DisplayName("Paginate return test")
    void listPaginateWhenSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();

        Page<Anime> animePage = animeControllerMock.list(null).getBody();

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAll returns list of anime when successful")
    void listAll_ReturnsListOfAnimes_WhenSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animes = animeControllerMock.listAll().getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns anime when successful")
    void findById_ReturnsAnime_WhenSuccessful(){
        Long expectedId = AnimeCreator.createValidAnime().getId();

        Anime anime = animeControllerMock.findOne(1).getBody();

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns a list of anime when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animes = animeControllerMock.findName(expectedName).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of anime when anime is not found")
    void findByName_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound(){
        BDDMockito.when(animeServiceMock.findName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> animes = animeControllerMock.findName("anime").getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();

    }

    @Test
    @DisplayName("save returns anime when successful")
    void save_ReturnsAnime_WhenSuccessful(){
        CreateAnimeDto created = new CreateAnimeDto();
        String name = AnimeCreator.createValidAnime().getName();
        created.setName(name);

        Anime anime = animeControllerMock.create(created).getBody();

        Assertions.assertThat(anime).isNotNull().isEqualTo(AnimeCreator.createValidAnime());

    }

    @Test
    @DisplayName("replace updates anime when successful")
    void replace_UpdatesAnime_WhenSuccessful(){
        Anime validUpdatedAnime = AnimeCreator.createValidUpdatedAnime();
        UpdateAnime updatedAnime = new UpdateAnime();
        updatedAnime.setId(validUpdatedAnime.getId());
        updatedAnime.setName(validUpdatedAnime.getName());

        ResponseEntity<Anime> update = animeControllerMock.update(updatedAnime);

        Assertions.assertThat(update).isNotNull();

        Assertions.assertThat(update.getBody().getName()).isEqualTo(validUpdatedAnime.getName());
    }

    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful(){

        Assertions.assertThatCode(() ->animeControllerMock.delete(1))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = animeControllerMock.delete(1);

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}