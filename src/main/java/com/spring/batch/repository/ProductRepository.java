package com.spring.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.batch.tables.ProductTable;

@Repository
public interface ProductRepository extends JpaRepository<ProductTable, Integer> {

}
