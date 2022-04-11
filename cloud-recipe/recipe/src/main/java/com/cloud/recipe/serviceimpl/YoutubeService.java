package com.cloud.recipe.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cloud.recipe.entity.YoutubeVideo;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import io.micrometer.core.annotation.Timed;

@Service
public class YoutubeService {

	private static final long MAX_SEARCH_RESULTS = 5;

	/**
	 * Returns the first 5 YouTube videos that match the query term
	 */
	@Timed(value = "youtube.time", description = "Time taken to return youtube search")
	public List<YoutubeVideo> fetchVideosByQuery(String queryTerm) {
		List<YoutubeVideo> videos = new ArrayList<YoutubeVideo>();

		try {
			// instantiate youtube object
			YouTube youtube = getYouTube();

			// define what info we want to get
			YouTube.Search.List search = youtube.search().list("id,snippet");

			// Enter the api key created by youtube credentials
			String apiKey = "< CREATE YOUR OWN API KEY>";
			search.setKey(apiKey);

			// set the search term
			search.setQ(queryTerm);

			// we only want video results
			search.setType("video");

			// set the fields that we're going to use
			search.setFields(
					"items(id/kind,id/videoId,snippet/title,snippet/description,snippet/publishedAt,snippet/thumbnails/default/url)");

			// set the max results
			search.setMaxResults(MAX_SEARCH_RESULTS);

			// perform the search and parse the results
			SearchListResponse searchResponse = search.execute();
			List<SearchResult> searchResultList = searchResponse.getItems();
			if (searchResultList != null) {
				for (SearchResult result : searchResultList) {
					YoutubeVideo video = new YoutubeVideo();
					video.setTitle(result.getSnippet().getTitle());
					video.setUrl(buildVideoUrl(result.getId().getVideoId()));
					videos.add(video);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return videos;
	}

	/**
	 * Constructs the URL to play the YouTube video
	 */
	private String buildVideoUrl(String videoId) {
		StringBuilder builder = new StringBuilder();
		builder.append("https://www.youtube.com/watch?v=");
		builder.append(videoId);

		return builder.toString();
	}

	/**
	 * Instantiates the YouTube object
	 */
	private YouTube getYouTube() {
		YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), (reqeust) -> {
		}).setApplicationName("youtube-spring-boot-demo").build();

		return youtube;
	}

	public static void main(String[] args) {
		new YoutubeService().fetchVideosByQuery("bill maher");
	}
}
