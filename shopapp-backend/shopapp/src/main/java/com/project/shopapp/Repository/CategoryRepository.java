package com.project.shopapp.Repository;

import com.project.shopapp.Models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
