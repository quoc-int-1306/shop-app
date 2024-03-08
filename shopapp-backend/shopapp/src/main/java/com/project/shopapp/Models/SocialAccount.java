package com.project.shopapp.Models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "social_accounts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider", nullable = false, length = 20)
    private String provider;

    @Column(name = "provider_id", length = 50)
    private String providerId;

    @Column(name = "name", length = 150)
    private String name;

    @Column(name = "email", length =  150)
    private String email;
}
