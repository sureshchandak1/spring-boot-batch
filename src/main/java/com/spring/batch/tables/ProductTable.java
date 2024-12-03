package com.spring.batch.tables;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "products")
public class ProductTable {

    @Id
    private Integer id;

    private String title;

    private String description;

    private Double price;

    private Float discount;

    private Double discountPrice;

}
