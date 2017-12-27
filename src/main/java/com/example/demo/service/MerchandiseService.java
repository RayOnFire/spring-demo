package com.example.demo.service;

import com.example.demo.exception.ObjectNotFoundException;
import com.example.demo.model.Merchandise;
import com.example.demo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by ray on 17-7-18.
 */
public interface MerchandiseService {

    Merchandise setInventory(long id, long amount) throws ObjectNotFoundException;

    List<Merchandise> getMerchandiseList(Long publisherId, Pageable pageable);
}
