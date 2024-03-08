package com.project.shopapp.Models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EntityListeners(ProductListener.class)
public class Product extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 350)
    private String name;

    @Column(name = "price")
    private Float price;

    @Column(name = "thumbnail", nullable = false, length = 350)
    private String thumbnail;

    @Column(name = "description", length = 300)
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
