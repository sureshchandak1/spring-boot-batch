package com.spring.batch.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

    private Integer id;

    private String title;

    private String description;

    private Double price;

    private Float discount;

    private Double discountPrice;

}
