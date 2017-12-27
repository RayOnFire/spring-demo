package com.example.demo.repository;

import com.example.demo.model.MerchandiseRecommend;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Ray on 2017/7/15.
 */
public interface MerchandiseRecommendRepository extends CrudRepository<MerchandiseRecommend, Long> {
    List<MerchandiseRecommend> findAllByMerchandise_Id(long id);
}
