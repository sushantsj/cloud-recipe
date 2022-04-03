package com.cloud.recipe.controller;

import javax.validation.Valid;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.recipe.entity.YoutubeSearchCriteria;
import com.cloud.recipe.entity.YoutubeVideo;
import com.cloud.recipe.serviceimpl.YoutubeService;

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

	@PostMapping("/youtubeDemo")
	public List<YoutubeVideo> formSubmit(@Valid YoutubeSearchCriteria youtubeSearchCriteria) {
		log.info("Get videos link by query: {}", youtubeSearchCriteria.getQueryTerm());
		// get the list of YouTube videos that match the search term
		List<YoutubeVideo> videos = youtubeService.fetchVideosByQuery(youtubeSearchCriteria.getQueryTerm());
		log.info("videos list: {}", videos.toString());
		return videos;
	}
}
