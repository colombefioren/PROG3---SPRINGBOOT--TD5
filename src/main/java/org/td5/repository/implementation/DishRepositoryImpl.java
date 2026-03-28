package org.td5.repository.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.td5.configuration.DataSource;
import org.td5.entity.Dish;
import org.td5.entity.DishIngredient;
import org.td5.entity.Ingredient;
import org.td5.entity.enums.CategoryEnum;
import org.td5.entity.enums.UnitType;
import org.td5.repository.DishRepository;

@Repository
@AllArgsConstructor
public class DishRepositoryImpl implements DishRepository {
  private final DataSource dataSource;

  @Override
  public List<Dish> findAll() {
    String sql =
        """
            with dish_ingredients_data as (
                select
                    di.id_dish,
                    di.id as di_id,
                    di.quantity_required,
                    di.unit,
                    i.id as i_id,
                    i.name as i_name,
                    i.price as i_price,
                    i.category as i_category
                from dish_ingredient di
                inner join ingredient i on di.id_ingredient = i.id
            )
            select
                d.id as d_id,
                d.name as d_name,
                d.selling_price as d_price,
                did.di_id,
                did.quantity_required,
                did.unit,
                did.i_id,
                did.i_name,
                did.i_price,
                did.i_category
            from dish d
            left join dish_ingredients_data did on d.id = did.id_dish
            order by d.id, did.di_id
            """;

    Map<Integer, Dish> dishMap = new HashMap<>();

    try (Connection conn = dataSource.getDBConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {

      while (rs.next()) {
        Integer dishId = rs.getInt("d_id");
        Dish dish = dishMap.get(dishId);

        if (dish == null) {
          dish = new Dish();
          dish.setId(dishId);
          dish.setName(rs.getString("d_name"));
          dish.setPrice(rs.getDouble("d_price"));
          dish.setDishIngredients(new ArrayList<>());
          dishMap.put(dishId, dish);
        }

        if (rs.getObject("di_id") != null) {
          Ingredient ingredient = new Ingredient();
          ingredient.setId(rs.getInt("i_id"));
          ingredient.setName(rs.getString("i_name"));
          ingredient.setPrice(rs.getDouble("i_price"));
          ingredient.setCategory(CategoryEnum.valueOf(rs.getString("i_category")));
          ingredient.setStockMovementList(new ArrayList<>());

          DishIngredient dishIngredient = new DishIngredient();
          dishIngredient.setId(rs.getInt("di_id"));
          dishIngredient.setDish(dish);
          dishIngredient.setIngredient(ingredient);
          dishIngredient.setQuantityRequired(rs.getDouble("quantity_required"));
          if (rs.getString("unit") != null) {
            dishIngredient.setUnit(UnitType.valueOf(rs.getString("unit")));
          }

          dish.getDishIngredients().add(dishIngredient);
        }
      }

      return new ArrayList<>(dishMap.values());

    } catch (SQLException e) {
      throw new RuntimeException("error fetching all dishes", e);
    }
  }

  @Override
  public Optional<Dish> findById(Integer id) {
    String sql =
        """
            with dish_ingredients_data as (
                select
                    di.id_dish,
                    di.id as di_id,
                    di.quantity_required,
                    di.unit,
                    i.id as i_id,
                    i.name as i_name,
                    i.price as i_price,
                    i.category as i_category
                from dish_ingredient di
                inner join ingredient i on di.id_ingredient = i.id
                where di.id_dish = ?
            )
            select
                d.id as d_id,
                d.name as d_name,
                d.selling_price as d_price,
                did.di_id,
                did.quantity_required,
                did.unit,
                did.i_id,
                did.i_name,
                did.i_price,
                did.i_category
            from dish d
            left join dish_ingredients_data did on d.id = did.id_dish
            where d.id = ?
            order by did.di_id
            """;

    try (Connection conn = dataSource.getDBConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setInt(1, id);
      ps.setInt(2, id);

      try (ResultSet rs = ps.executeQuery()) {
        Dish dish = null;

        while (rs.next()) {
          if (dish == null) {
            dish = new Dish();
            dish.setId(rs.getInt("d_id"));
            dish.setName(rs.getString("d_name"));
            dish.setPrice(rs.getDouble("d_price"));
            dish.setDishIngredients(new ArrayList<>());
          }

          if (rs.getObject("di_id") != null) {
            Ingredient ingredient = new Ingredient();
            ingredient.setId(rs.getInt("i_id"));
            ingredient.setName(rs.getString("i_name"));
            ingredient.setPrice(rs.getDouble("i_price"));
            ingredient.setCategory(CategoryEnum.valueOf(rs.getString("i_category")));
            ingredient.setStockMovementList(new ArrayList<>());

            DishIngredient dishIngredient = new DishIngredient();
            dishIngredient.setId(rs.getInt("di_id"));
            dishIngredient.setDish(dish);
            dishIngredient.setIngredient(ingredient);
            dishIngredient.setQuantityRequired(rs.getDouble("quantity_required"));
            if (rs.getString("unit") != null) {
              dishIngredient.setUnit(UnitType.valueOf(rs.getString("unit")));
            }

            dish.getDishIngredients().add(dishIngredient);
          }
        }

        return Optional.ofNullable(dish);
      }

    } catch (SQLException e) {
      throw new RuntimeException("error fetching dish by id: " + id, e);
    }
  }

  @Override
  public Dish updateIngredientsInDish(Integer dishId, List<Ingredient> ingredientList) {
    String deleteSql = "delete from dish_ingredient where id_dish = ?";
    String insertSql =
        "insert into dish_ingredient (id_dish, id_ingredient, quantity_required, unit) values (?, ?, ?, ?)";

    Connection conn = null;
    try {
      conn = dataSource.getDBConnection();
      conn.setAutoCommit(false);

      try (PreparedStatement ps = conn.prepareStatement(deleteSql)) {
        ps.setInt(1, dishId);
        ps.executeUpdate();
      }

      if (ingredientList != null && !ingredientList.isEmpty()) {
        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
          for (Ingredient ingredient : ingredientList) {
            ps.setInt(1, dishId);
            ps.setInt(2, ingredient.getId());
            ps.setDouble(3, 0.0);
            ps.setString(4, null);
            ps.addBatch();
          }
          ps.executeBatch();
        }
      }

      conn.commit();

      return findById(dishId).orElse(null);

    } catch (SQLException e) {
      if (conn != null) {
        try {
          conn.rollback();
        } catch (SQLException rollbackEx) {
          throw new RuntimeException("failed to rollback transaction", rollbackEx);
        }
      }
      throw new RuntimeException("failed to update dish ingredients", e);
    } finally {
      if (conn != null) {
        try {
          conn.setAutoCommit(true);
          conn.close();
        } catch (SQLException e) {
          System.err.println("error closing connection: " + e.getMessage());
        }
      }
    }
  }
}
