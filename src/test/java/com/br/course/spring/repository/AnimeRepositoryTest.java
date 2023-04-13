package com.br.course.spring.repository;

import com.br.course.spring.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@DataJpaTest()
@DisplayName("Test Anime Repository")
class AnimeRepositoryTest {
    @Autowired
    private AnimeRepository animeRepository;

    @Test()
    @DisplayName("persisted anime to db")
    void save_PersistAnime_WhenSuccessful() {

        try {
            Anime animeToBeSaved = createAnime();
            Anime animeSaved = this.animeRepository.save(animeToBeSaved);
            Assertions.assertThat(animeSaved).isNotNull();
            Assertions.assertThat(animeSaved.getId()).isNotNull();
            Assertions.assertThat(animeSaved.getName()).isEqualTo(animeToBeSaved.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Test
    @DisplayName("Save updates anime when Successful")
    void save_UpdatesAnime_WhenSuccessful(){
       try{
           Anime animeToBeSaved = createAnime();

           Anime animeSaved = this.animeRepository.save(animeToBeSaved);

           animeSaved.setName("Overlord");

           Anime animeUpdated = this.animeRepository.save(animeSaved);

           Assertions.assertThat(animeUpdated).isNotNull();

           Assertions.assertThat(animeUpdated.getId()).isNotNull();

           Assertions.assertThat(animeUpdated.getName()).isEqualTo(animeSaved.getName());
       }catch (Exception e){
           e.printStackTrace();
       }
    }
    @Test
    @DisplayName("Delete removes anime when Successful")
    void delete_RemovesAnime_WhenSuccessful(){
        try {
            Anime animeToBeSaved = createAnime();

            Anime animeSaved = this.animeRepository.save(animeToBeSaved);

            this.animeRepository.delete(animeSaved);

            Optional<Anime> animeOptional = this.animeRepository.findById(animeSaved.getId());

            Assertions.assertThat(animeOptional).isEmpty();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    @DisplayName("Find By Name returns list of anime when Successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful(){
        try {
            Anime animeToBeSaved = createAnime();

            Anime animeSaved = this.animeRepository.save(animeToBeSaved);

            String name = animeSaved.getName();

            List<Anime> anime = this.animeRepository.findByName(name);

            Assertions.assertThat(anime).isNotEmpty();

            Assertions.assertThat(anime).contains(animeSaved);

            Assertions.assertThat(anime.get(0).getName()).isEqualTo(name);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    @DisplayName("Find By Name returns empty list when no anime is found")
    void findByName_ReturnsEmptyList_WhenAnimeIsNotFound() {
       try {
           List<Anime> anime = this.animeRepository.findByName("xaxa");
           Assertions.assertThat(anime).isEmpty();
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    private Anime createAnime() {
        return Anime.builder()
                .name("Hajime no Ippo")
                .build();
    }
}