package com.project.shopapp.Models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_images")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductImage {

    public static final int MAXIMUM_IMAGES_PER_PRODUCT = 5;

    public static final int MINIMUM_IMAGES_PER_PRODUCT = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "image_url", length = 300)
    private String imageUrl;
}
