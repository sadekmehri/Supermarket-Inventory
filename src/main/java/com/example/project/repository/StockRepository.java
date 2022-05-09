package com.example.project.repository;

import com.example.project.entity.Stock;
import com.example.project.key.StockKeys;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, StockKeys> {

    Page<Stock> findProductsByStoreId(int storeId, Pageable pagingSort);

}
