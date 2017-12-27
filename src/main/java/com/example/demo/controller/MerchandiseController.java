package com.example.demo.controller;

import com.example.demo.annotation.CurrentUser;
import com.example.demo.exception.ObjectNotFoundException;
import com.example.demo.exception.UserNotMatchException;
import com.example.demo.model.Merchandise;
import com.example.demo.model.MerchandiseRecommend;
import com.example.demo.model.User;
import com.example.demo.repository.MerchandiseRecommendRepository;
import com.example.demo.repository.MerchandiseRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.MerchandiseService;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Ray on 2017/7/10.
 */
@RestController
@RequestMapping("/merchandise")
public class MerchandiseController {

    @Autowired
    private MerchandiseRepository merchandiseRepository;

    @Autowired
    private MerchandiseRecommendRepository merchandiseRecommendRepository;

    @Autowired
    private MerchandiseService merchandiseService;

    @GetMapping("/list")
    public List<Merchandise> getMerchandises(@RequestParam(value = "publisher", required = false) Long publisherId,
                                             @RequestParam(value = "limit", defaultValue = "20") int limit,
                                             @RequestParam(value = "page", defaultValue = "1") int page)
            throws UsernameNotFoundException {

        return merchandiseService.getMerchandiseList(publisherId, new PageRequest(page, limit));
    }

    @GetMapping
    public Merchandise getMerchandise(@RequestParam("id") long itemId) throws ObjectNotFoundException {
        return merchandiseRepository.findOne(itemId);
    }

    @PostMapping
    public Merchandise addMerchandise(@CurrentUser User user,
                                      @RequestParam("name") String itemName,
                                      @RequestParam("price") double itemPrice,
                                      @RequestParam("inventory") Long inventory,
                                      @RequestParam(value = "description", required = false) String description) {
        if (description == null) description = "";
        Merchandise merchandise = new Merchandise(itemName, itemPrice, user, new Timestamp(System.currentTimeMillis()), inventory, description);
        merchandiseRepository.save(merchandise);
        return merchandise;
    }

    @PostMapping("/edit")
    public Merchandise modifyMerchandise(@CurrentUser User user,
                                         @RequestParam(value = "id") long itemId,
                                         @RequestParam(value = "inventory", required = false) Long requestInventory,
                                         @RequestParam(value = "name", required = false) String requestName,
                                         @RequestParam(value = "price", required = false) Double requestPrice,
                                         @RequestParam(value = "description", required = false) String description)
            throws ObjectNotFoundException, UserNotMatchException {
        Merchandise merchandise = merchandiseRepository.findOne(itemId);
        if (merchandise.getPublisher() != user)
            throw new UserNotMatchException("You are not the publisher of merchandise");
        if (requestInventory != null) merchandise.setInventory(requestInventory);
        if (requestName != null) merchandise.setName(requestName);
        if (requestPrice != null) merchandise.setPrice(requestPrice);
        if (description != null) merchandise.setDescription(description);
        merchandiseRepository.save(merchandise);
        return merchandise;
    }

    @DeleteMapping
    public String deleteMerchandise(@CurrentUser User user,
                                    @RequestParam("id") Long itemId)
            throws ObjectNotFoundException, UserNotMatchException {
        Merchandise merchandise = merchandiseRepository.findOne(itemId);
        // only publisher can delete merchandise
        if (merchandise.getPublisher() != user) throw new UserNotMatchException("You are not the publisher of this merchandise");
        merchandiseRepository.delete(merchandise);
        return "success";
    }

    @GetMapping("/recommend")
    public List<Merchandise> getMerchandiseRecommends(@RequestParam("id") Long merchandiseId) {
        List<MerchandiseRecommend> merchandiseRecommends = merchandiseRecommendRepository.findAllByMerchandise_Id(merchandiseId);
        List<Merchandise> merchandiseList = merchandiseRecommends.stream().map(item -> item.getRecommendation()).collect(Collectors.toList());
        return merchandiseList;
    }


    public class ListExcludeStrategy implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            String name = f.getName();
            return name.equals("description") ||
                    name.equals("pageHtml") ||
                    name.equals("slogan") ||
                    name.equals("recommends") ||
                    name.equals("publisher");
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
