package com.project.shopapp.Models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
}
