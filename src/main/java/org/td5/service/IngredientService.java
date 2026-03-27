package org.td5.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.td5.entity.Ingredient;
import org.td5.exception.NotFoundException;
import org.td5.repository.IngredientRepository;

@Service
@AllArgsConstructor
public class IngredientService {
  private final IngredientRepository repository;

  public List<Ingredient> getAllIngredients() {
    return repository.findAll();
  }

  public Ingredient getIngredientById(Integer id) throws NotFoundException {
    Ingredient ingredient = repository.findById(id);
    if(ingredient == null){
      throw new NotFoundException("Ingredient.id=" + id + " is not found");
    }else{
      return ingredient;
    }
  }
}
