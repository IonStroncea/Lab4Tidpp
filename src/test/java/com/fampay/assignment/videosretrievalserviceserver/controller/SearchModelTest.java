package com.fampay.assignment.videosretrievalserviceserver.controller;

import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SearchModelTest {
    SearchModel sm;

    @Test
    @DisplayName("Creates new search model (parametrized constructor test)")
    public void createSearchModel()
    {
        sm = new SearchModel("test_search_query");
        Assertions.assertEquals("test_search_query", sm.getSearchQuery());

    }
}