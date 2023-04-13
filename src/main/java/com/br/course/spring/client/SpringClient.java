package com.br.course.spring.client;
import java.util.Arrays;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import com.br.course.spring.domain.Anime;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SpringClient {
  public static void main(String[] args) {
    Anime anime = new RestTemplate().getForEntity("http://localhost:8080/animes/{id}", Anime.class, 2).getBody();

    Anime obj = new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class, 3);

     List<Anime> body = new RestTemplate().exchange("http://localhost:8080/animes/all", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
    }).getBody();
    log.info(anime);
    log.info(obj);
    log.info(body); 
  }
}
