package org.td5.repository;

import org.td5.entity.Dish;
import org.td5.entity.Ingredient;

import java.util.List;

public interface DishRepository {
    List<Dish> getAllDishes();
    Dish modifyIngredientInDish(Integer dishId, List<Ingredient> ingredientList);
}
