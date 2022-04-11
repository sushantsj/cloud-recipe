package com.cloud.recipe.controller;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cloud.recipe.entity.YoutubeVideo;
import com.cloud.recipe.serviceimpl.YoutubeService;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api/externalapis")
public class YoutubeController {

	private final Logger log = LoggerFactory.getLogger(YoutubeController.class);

	@Autowired
	private YoutubeService youtubeService;

	/*
	 * //starting page for YouTube api demo
	 * 
	 * @GetMapping("/youtubeDemo") public String youtubeDemo(Model model) {
	 * //instantiate an empty address object YoutubeSearchCriteria
	 * youtubeSearchCriteria = new YoutubeSearchCriteria();
	 * 
	 * //put the object in the model model.addAttribute("youtubeSearchCriteria",
	 * youtubeSearchCriteria);
	 * 
	 * //get out return "youtubeDemo"; }
	 */

	@GetMapping("/youtubeDemo/{query}")
	public List<YoutubeVideo> formSubmit(@PathVariable("query") String query) {
		log.info("Get videos link by query: {}", query);
		log.info("Create your own youtube video api key and update in youtube service file");
		// get the list of YouTube videos that match the search term
		List<YoutubeVideo> videos = youtubeService.fetchVideosByQuery(query);
		log.info("videos list: {}", videos.toString());
		return videos;
	}

	@GetMapping("/public-api")
	public List<Object> getCountries() {
		log.info("Enter your api key generated on the moviedb website and update below");
		String url = "https://api.themoviedb.org/3/movie/550?api_key = <update your api key>";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<JsonNode> countries = restTemplate.getForEntity(url, JsonNode.class);
		return Arrays.asList(countries);

	}
}
