package com.spring.batch.config;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import com.spring.batch.repository.ProductRepository;
import com.spring.batch.tables.ProductTable;

public class CustomItemWriter implements ItemWriter<ProductTable> {

    @Autowired
    private ProductRepository mProductRepository;

    @Override
    public void write(@NonNull Chunk<? extends ProductTable> list) throws Exception {
        for (ProductTable product : list) {
            mProductRepository.save(product);
        }

    }

}
