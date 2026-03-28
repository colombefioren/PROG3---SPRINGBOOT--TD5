package org.td5.repository;

import org.td5.entity.DishIngredient;

import java.util.List;

public interface DishIngredientRepository {
    List<DishIngredient> findById(Integer dishId);
    void saveAll(List<DishIngredient> dishIngredients);
    void deleteByDishId(Integer dishId);
}
