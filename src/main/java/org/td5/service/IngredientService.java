package org.td5.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.td5.entity.Ingredient;
import org.td5.repository.IngredientRepository;

@Service
@AllArgsConstructor
public class IngredientService {
  private final IngredientRepository repository;

  public List<Ingredient> getAllIngredients() {
    return repository.getAllIngredients();
  }

  public Ingredient getIngredientById(Integer id) {
    return repository.getIngredientById(id);
  }
}
