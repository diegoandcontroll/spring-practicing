package com.br.course.spring.repository;

import com.br.course.spring.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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

    private Anime createAnime() {
        return Anime.builder()
                .name("Hajime no Ippo")
                .build();
    }
}