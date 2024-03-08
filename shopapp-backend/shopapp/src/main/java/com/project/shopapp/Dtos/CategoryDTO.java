package com.project.shopapp.Dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CategoryDTO {
    @NotEmpty(message = "Category's name cannot be empty")
    private String name;

}
