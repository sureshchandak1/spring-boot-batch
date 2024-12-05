package com.spring.batch.config;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.spring.batch.dtos.product.ProductDto;
import com.spring.batch.tables.ProductTable;

public class CustomItemProcessor implements ItemProcessor<ProductDto, ProductTable> {

    @Override
    @Nullable
    public ProductTable process(@NonNull ProductDto product) throws Exception {

        // process data

        double discount = (product.getDiscount() / 100) * product.getPrice();
        double finalPrice = product.getPrice() - discount;

        ProductTable result = new ProductTable();
        result.setId(product.getId());
        result.setTitle(product.getTitle());
        result.setDescription(product.getDescription());
        result.setPrice(product.getPrice());
        result.setDiscount(product.getDiscount());
        result.setDiscountPrice(finalPrice);

        return result;
    }

}
