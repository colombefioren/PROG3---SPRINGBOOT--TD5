package org.td5.repository;

import java.util.List;
import java.util.Optional;
import org.td5.entity.Dish;
import org.td5.entity.Ingredient;

public interface DishRepository {
  List<Dish> findAll();

  Dish updateIngredientsInDish(Integer dishId, List<Ingredient> ingredientList);

  Optional<Dish> findById(Integer id);
}
