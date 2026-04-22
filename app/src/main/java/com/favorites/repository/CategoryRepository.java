package com.favorites.repository;

import com.favorites.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserIdOrderBySortOrderAsc(Long userId);
    Category findByUserIdAndName(Long userId, String name);
}