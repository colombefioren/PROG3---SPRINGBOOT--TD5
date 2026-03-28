package org.td5.repository;

import org.td5.entity.StockMovement;

import java.util.List;

public interface StockMovementRepository {
    List<StockMovement> findByIngredientId(Integer ingredientId);
}
