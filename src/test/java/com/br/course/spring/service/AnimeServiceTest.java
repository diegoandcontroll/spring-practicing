package com.br.course.spring.service;

import com.br.course.spring.domain.Anime;
import com.br.course.spring.dtos.CreateAnimeDto;
import com.br.course.spring.dtos.UpdateAnime;
import com.br.course.spring.repository.AnimeRepository;
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
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {
    @InjectMocks
    AnimeService animeServiceMock;

    @Mock
    AnimeRepository animeRepositoryMock;

    @BeforeEach
    void setup(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(animePage);
        BDDMockito.when(animeRepositoryMock.findAll()).thenReturn(List.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(AnimeCreator.createValidAnime());
        BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));
    }
    @Test
    @DisplayName("ListAll paginable animes list")
    void listAllPaginableAimes() {

           String name = AnimeCreator.createValidAnime().getName();
           Page<Anime> animePage = animeServiceMock.listAll(PageRequest.of(1, 1));

           Assertions.assertThat(animePage).isNotNull();

            Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

            Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(name);
    }
    @Test
    @DisplayName("List animes no paginable")
    void listNoPaginable(){
        String name = AnimeCreator.createValidAnime().getName();

        List<Anime> anime = animeServiceMock.listAllNoPageable();

        Assertions.assertThat(anime).
                isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(anime.get(0).getName()).isEqualTo(name);

    }
    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns anime when successful")
    void findIdWhenSuccessful(){
        Long expectedId = AnimeCreator.createValidAnime().getId();

        Anime anime = animeServiceMock.findOne(1);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
    }
    @Test
    @DisplayName("findByName returns a list of anime when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animes = animeServiceMock.findName("anime");

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of anime when anime is not found")
    void findByName_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound(){
        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> animes = animeServiceMock.findName("anime");

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();

    }
    @Test
    @DisplayName("save returns anime when successful")
    void save_ReturnsAnime_WhenSuccessful(){
        Anime anime = animeServiceMock.save(AnimeCreator.createValidAnime());
        Assertions.assertThat(anime).isNotNull().isEqualTo(AnimeCreator.createValidAnime());
    }
    @Test
    @DisplayName("Update returns anime successful")
    void updateAnimeSuccessful(){
        Anime validUpdatedAnime = AnimeCreator.createValidUpdatedAnime();
        Anime updatedAnime = new Anime();
        updatedAnime.setId(validUpdatedAnime.getId());
        updatedAnime.setName(validUpdatedAnime.getName());
        Anime update = animeServiceMock.update(updatedAnime);

        Assertions.assertThat(updatedAnime)
                .isNotNull();

        Assertions.assertThat(updatedAnime.getName()).isEqualTo(validUpdatedAnime.getName());


    }
    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful(){

        Assertions.assertThatCode(() ->animeServiceMock.delete(1))
                .doesNotThrowAnyException();
    }

}