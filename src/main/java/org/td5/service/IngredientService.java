package org.td5.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.td5.entity.Ingredient;
import org.td5.repository.IngredientRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class IngredientService {
    private final IngredientRepository repository;

    public List<Ingredient> getAllIngredients(){
        return repository.getAllIngredients();
    }
}
