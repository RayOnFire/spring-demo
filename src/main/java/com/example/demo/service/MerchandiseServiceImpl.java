package com.example.demo.service;

import com.example.demo.exception.ObjectNotFoundException;
import com.example.demo.model.Merchandise;
import com.example.demo.model.User;
import com.example.demo.repository.MerchandiseRepository;
import com.example.demo.repository.UserRepository;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ray on 17-7-18.
 */
@Service
public class MerchandiseServiceImpl implements MerchandiseService {

    private MerchandiseRepository merchandiseRepository;

    private EntityManager entityManager;

    private UserRepository userRepository;

    @Autowired
    public MerchandiseServiceImpl(MerchandiseRepository merchandiseRepository, EntityManager entityManager, UserRepository userRepository) {
        this.merchandiseRepository = merchandiseRepository;
        this.entityManager = entityManager;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Merchandise setInventory(long id, long amount) throws ObjectNotFoundException {
        Merchandise merchandise = entityManager.find(Merchandise.class, id);
        if (merchandise == null) throw new ObjectNotFoundException();
        merchandise.setInventory(merchandise.getInventory() - amount);
        return merchandiseRepository.save(merchandise);
    }

    @Override
    @Transactional
    public List<Merchandise> getMerchandiseList(Long publisherId, Pageable pageable) {
        Page<Merchandise> merchandisePage;
        if (publisherId == null) merchandisePage = merchandiseRepository.findAllWithCoverImages(pageable);
        else {
            User user = userRepository.findOne(publisherId);
            if (user == null) throw new UsernameNotFoundException("user not found");
            merchandisePage = merchandiseRepository.findAllByPublisherWithCoverImages(user, pageable);
        }
        List<Merchandise> merchandises = new ArrayList<>();
        merchandisePage.forEach(i -> {
            Merchandise m = new Merchandise();
            m.setId(i.getId());
            m.setName(i.getName());
            m.setCoverImages(i.getCoverImages());
            m.setPrice(i.getPrice());
            m.setPublisher(i.getPublisher());
            m.setInventory(i.getInventory());
            merchandises.add(m);
        });
        return merchandises;
    }
}
