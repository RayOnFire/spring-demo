package com.example.demo.controller;

import com.example.demo.model.Merchandise;
import com.example.demo.repository.MerchandiseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ray on 17-7-19.
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    private MerchandiseRepository merchandiseRepository;

    @Autowired
    public SearchController(MerchandiseRepository merchandiseRepository) {
        this.merchandiseRepository = merchandiseRepository;
    }

    @GetMapping("/recommend")
    public List<String> getSearchRecommend(@RequestParam("query") String queryString) {
        Page<Merchandise> merchandises = merchandiseRepository.findByNameContainingIgnoreCase(queryString, new PageRequest(0, 10));
        List<String> names = new ArrayList<>();
        merchandises.forEach(i -> names.add(i.getName()));
        return names;
    }
}
