package com.fampay.assignment.videosretrievalserviceserver.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class YoutubeApiConfigurationTest {
    public YoutubeApiConfiguration ytubeApiConfig = new YoutubeApiConfiguration();;

    @Test
    @DisplayName("Creating new API config (setters test)")
    public void creatingNewApi()
    {
        ytubeApiConfig.setUrl("test_url;");
        ytubeApiConfig.setEndpoint("test_endpoint");
        ytubeApiConfig.setFrequencyMillis(1000L);
        ytubeApiConfig.setPageSize(20);
        ytubeApiConfig.setSearchQuery("test_api_search_query");
    }

    @Test
    @DisplayName("API config test (getters test)")
    void verifyConfigValues() {
        ytubeApiConfig.setUrl("test_url");
        ytubeApiConfig.setEndpoint("test_endpoint");
        ytubeApiConfig.setFrequencyMillis(1000L);
        ytubeApiConfig.setPageSize(20);
        ytubeApiConfig.setSearchQuery("test_api_search_query");
        Assertions.assertEquals("test_url", ytubeApiConfig.getUrl());
        Assertions.assertEquals("test_endpoint", ytubeApiConfig.getEndpoint());
        Assertions.assertEquals(1000L, ytubeApiConfig.getFrequencyMillis());
        Assertions.assertEquals(20, ytubeApiConfig.getPageSize());
        Assertions.assertEquals("test_api_search_query", ytubeApiConfig.getSearchQuery());
    }

}