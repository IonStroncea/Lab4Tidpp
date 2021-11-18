package com.fampay.assignment.videosretrievalserviceserver.controller;

import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SearchModelTest {
    SearchModel sm;

    @Test
    @DisplayName("Creates new search model (test of parametrized constructor)")
    public void createSearchModel()
    {
        sm = new SearchModel("test_search-query");
    }

    @Test
    @DisplayName("Displays search model value")
    public String showSearchModel()
    {
        return sm.getSearchQuery();
    }
}