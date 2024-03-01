package com.project.shopapp.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_details")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "price")
    private Float price;

    @Column(name = "number_of_products")
    @JsonProperty("number_of_products")
    private int numberOfProducts;

    @Column(name = "total_money", nullable = false)
    @JsonProperty("total_money")
    private Float totalMoney;

    @Column(name = "color")
    private String color;
}
