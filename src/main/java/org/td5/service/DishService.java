package org.td5.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.td5.entity.Dish;
import org.td5.repository.DishRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class DishService {
  private final DishRepository repository;

  public List<Dish> getAllDishes() {
    return repository.findAll();
  }
}
