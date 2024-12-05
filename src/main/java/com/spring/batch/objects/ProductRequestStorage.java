package com.spring.batch.objects;

import java.util.List;

import org.springframework.stereotype.Component;

import com.spring.batch.dtos.product.ProductDto;

@Component
public class ProductRequestStorage {

    private List<ProductDto> productList;

    public List<ProductDto> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductDto> productList) {
        this.productList = productList;
    }

}
