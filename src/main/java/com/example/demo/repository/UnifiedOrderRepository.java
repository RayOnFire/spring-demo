package com.example.demo.repository;

import com.example.demo.model.UnifiedOrder;
import com.example.demo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Ray on 2017/7/11.
 */
public interface UnifiedOrderRepository extends CrudRepository<UnifiedOrder, Long> {
    Page<UnifiedOrder> findAllByBuyer(User buyer, Pageable pageable);
    Page<UnifiedOrder> findAllBySeller(User seller, Pageable pageable);
}
