package org.td5.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.td5.entity.Dish;
import org.td5.entity.Ingredient;
import org.td5.exception.NotFoundException;
import org.td5.repository.DishRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class DishService {
  private final DishRepository repository;

  public List<Dish> getAllDishes() {
    return repository.findAll();
  }

  public Dish updateIngredientsInDish(Integer dishId, List<Ingredient> ingredientList) throws NotFoundException {
    Dish dish = repository.findById(dishId);
    if(dish == null){
      throw new NotFoundException("Dish.id=" + dishId + " is not found");
    }
    return repository.updateIngredientsInDish(dishId, ingredientList);
  }
}
