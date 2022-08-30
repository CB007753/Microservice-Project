package com.example.movie_catelog_service.resources;

import com.example.movie_catelog_service.models.CatalogItem;
import com.example.movie_catelog_service.models.Movie;
import com.example.movie_catelog_service.models.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

//Creating a dummy api
    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

        //hard coded movie ratings
        List<Rating> ratings = Arrays.asList(

                new Rating("1234", 4),
                new Rating("5678", 3)

        );


        return ratings.stream().map(rating -> {

            Movie movie = restTemplate.getForObject("http://localhost:8082/movies/"+rating.getMovieId(), Movie.class);

           return new CatalogItem(movie.getName(), "Desc", rating.getRating());
        }).collect(Collectors.toList());


    }
}
