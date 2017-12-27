package com.example.demo.repository;

import com.example.demo.model.Merchandise;
import com.example.demo.model.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Ray on 2017/7/3.
 */
public interface MerchandiseRepository extends CrudRepository<Merchandise, Long> {

    Page<Merchandise> findAllByPublisher(User publisher, Pageable pageable);

    //@EntityGraph(attributePaths = {"coverImages"})
    Page<Merchandise> findAll(Pageable pageable);

    Page<Merchandise> findByNameContainingIgnoreCase(String pattern, Pageable pageable);

    @Query("select m from Merchandise m left join m.coverImages")
    Page<Merchandise> findAllWithCoverImages(Pageable pageable);

    @Query("select m from Merchandise m left join m.coverImages where m.publisher = ?1")
    Page<Merchandise> findAllByPublisherWithCoverImages(User publisher, Pageable pageable);

}
