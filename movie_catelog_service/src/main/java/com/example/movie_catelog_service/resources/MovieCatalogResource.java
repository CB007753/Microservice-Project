package com.example.movie_catelog_service.resources;

import com.example.movie_catelog_service.models.CatalogItem;
import com.example.movie_catelog_service.models.Movie;
import com.example.movie_catelog_service.models.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

//Creating a dummy api
    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

        //hard coded movie ratings
        List<Rating> ratings = Arrays.asList(

                new Rating("1234", 4),
                new Rating("5678", 3)

        );


        return ratings.stream().map(rating -> {
            //Below line is used with RestTemplate to get an instance of movie
           // Movie movie = restTemplate.getForObject("http://localhost:8082/movies/"+rating.getMovieId(), Movie.class);

            //Below code will give an instance of movie using Web Client Builder
            // * webClientBuilder.build() is using a builder pattern and giving you a client
            // * .get() is telling the type of method user wants(here user wants to get an instance of movie).. another method is .post() which is used to post data
            // * .uri() is used to specify the url that you need to access
            // * .retrieve() is used to ask to fetch data from specified url
            // * bodyToMono() is used to convert whatever the body that .retrieve() gets, into an instance of Movie.class
            Movie movie = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8082/movies/"+rating.getMovieId())
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();

           return new CatalogItem(movie.getName(), "Desc", rating.getRating());
        }).collect(Collectors.toList());


    }
}
