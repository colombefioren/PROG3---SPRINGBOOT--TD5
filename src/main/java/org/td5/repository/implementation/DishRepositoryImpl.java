package org.td5.repository.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.td5.configuration.DataSource;
import org.td5.entity.Dish;
import org.td5.entity.DishIngredient;
import org.td5.entity.Ingredient;
import org.td5.repository.DishIngredientRepository;
import org.td5.repository.DishRepository;

@Repository
@AllArgsConstructor
public class DishRepositoryImpl implements DishRepository {
  private final DataSource dataSource;
  private final DishIngredientRepository dishIngredientRepository;

  @Override
  public List<Dish> findAll() {
    String sql =
        """
                            select d.id as d_id, d.name as d_name, d.dish_type, d.selling_price as d_price from dish d order by d.id
                        """;

    List<Dish> dishes = new ArrayList<>();

    try (Connection conn = dataSource.getDBConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {

      while (rs.next()) {
        dishes.add(mapResultSetToDish(rs));
      }
      return dishes;

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Dish updateIngredientsInDish(Integer dishId, List<Ingredient> ingredientList) {
    Connection conn = null;
    try {
      conn = dataSource.getDBConnection();
      conn.setAutoCommit(false);

      dishIngredientRepository.deleteByDishId(dishId);

      if (ingredientList != null && !ingredientList.isEmpty()) {
        List<DishIngredient> newDishIngredients = new ArrayList<>();

        for (Ingredient ingredient : ingredientList) {
          if (ingredient != null && ingredient.getId() != null) {
            DishIngredient dishIngredient =
                DishIngredient.builder()
                    .dish(Dish.builder().id(dishId).build())
                    .ingredient(ingredient)
                    .quantityRequired(null)
                    .unit(null)
                    .build();
            newDishIngredients.add(dishIngredient);
          }
        }

        dishIngredientRepository.saveAll(newDishIngredients);
      }

      conn.commit();

      return findById(dishId).orElse(null);

    } catch (SQLException e) {
      if (conn != null) {
        try {
          conn.rollback();
        } catch (SQLException rollbackEx) {
          throw new RuntimeException("Failed to rollback transaction", rollbackEx);
        }
      }
      throw new RuntimeException("Failed to update dish ingredients", e);
    } finally {
      if (conn != null) {
        try {
          conn.setAutoCommit(true);
          conn.close();
        } catch (SQLException e) {
          System.err.println("Error closing connection: " + e.getMessage());
        }
      }
    }
  }

  @Override
  public Optional<Dish> findById(Integer id) {
    String sql =
        """
                      select d.id as d_id, d.name as d_name, d.dish_type, d.selling_price as d_price from dish d where d.id = ?
                    """;
    try (Connection conn = dataSource.getDBConnection();
        PreparedStatement ps = conn.prepareStatement(sql); ) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapResultSetToDish(rs));
        }
      }
      return Optional.empty();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private Dish mapResultSetToDish(ResultSet rs) throws SQLException {
    return Dish.builder()
        .id(rs.getInt("d_id"))
        .name(rs.getString("d_name"))
        .price(rs.getDouble("d_price"))
        .dishIngredients(dishIngredientRepository.findByDishId(rs.getInt("d_id")))
        .build();
  }
}
