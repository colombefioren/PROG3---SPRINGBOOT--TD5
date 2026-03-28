package org.td5.repository;

import org.td5.entity.DishIngredient;

import java.util.List;

public interface DishIngredientRepository {
    List<DishIngredient> findByDishId(Integer dishId);
    void saveAll(List<DishIngredient> dishIngredients);
    void deleteByDishId(Integer dishId);
}
