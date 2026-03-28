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
import org.td5.entity.Ingredient;
import org.td5.entity.StockMovement;
import org.td5.entity.StockValue;
import org.td5.entity.enums.CategoryEnum;
import org.td5.entity.enums.MovementTypeEnum;
import org.td5.entity.enums.UnitType;
import org.td5.repository.IngredientRepository;

@Repository
@AllArgsConstructor
public class IngredientRepositoryImpl implements IngredientRepository {
  private final DataSource dataSource;

  @Override
  public List<Ingredient> findAll() {
    String sql =
        """
            with stock_movements_data as (
                select
                    sm.id_ingredient,
                    sm.id as sm_id,
                    sm.quantity,
                    sm.type,
                    sm.unit,
                    sm.creation_datetime
                from stock_movement sm
                order by sm.creation_datetime desc
            )
            select
                i.id as i_id,
                i.name as i_name,
                i.price as i_price,
                i.category as i_category,
                smd.sm_id,
                smd.quantity as sm_quantity,
                smd.type as sm_type,
                smd.unit as sm_unit,
                smd.creation_datetime as sm_creation_datetime
            from ingredient i
            left join stock_movements_data smd on i.id = smd.id_ingredient
            order by i.id, smd.creation_datetime desc
            """;

    Map<Integer, Ingredient> ingredientMap = new HashMap<>();

    try (Connection conn = dataSource.getDBConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {

      while (rs.next()) {
        Integer ingredientId = rs.getInt("i_id");
        Ingredient ingredient = ingredientMap.get(ingredientId);

        if (ingredient == null) {
          ingredient = new Ingredient();
          ingredient.setId(ingredientId);
          ingredient.setName(rs.getString("i_name"));
          ingredient.setPrice(rs.getDouble("i_price"));
          ingredient.setCategory(CategoryEnum.valueOf(rs.getString("i_category")));
          ingredient.setStockMovementList(new ArrayList<>());
          ingredientMap.put(ingredientId, ingredient);
        }

        if (rs.getObject("sm_id") != null) {
          StockMovement stockMovement = new StockMovement();
          stockMovement.setId(rs.getInt("sm_id"));

          StockValue stockValue = new StockValue();
          stockValue.setQuantity(rs.getDouble("sm_quantity"));
          stockValue.setUnit(UnitType.valueOf(rs.getString("sm_unit")));
          stockMovement.setValue(stockValue);

          stockMovement.setType(MovementTypeEnum.valueOf(rs.getString("sm_type")));
          stockMovement.setCreationDatetime(rs.getTimestamp("sm_creation_datetime").toInstant());

          ingredient.getStockMovementList().add(stockMovement);
        }
      }

      return new ArrayList<>(ingredientMap.values());

    } catch (SQLException e) {
      throw new RuntimeException("error fetching all ingredients", e);
    }
  }

  @Override
  public Optional<Ingredient> findById(Integer id) {
    String sql =
        """
            with stock_movements_data as (
                select
                    sm.id_ingredient,
                    sm.id as sm_id,
                    sm.quantity,
                    sm.type,
                    sm.unit,
                    sm.creation_datetime
                from stock_movement sm
                where sm.id_ingredient = ?
                order by sm.creation_datetime desc
            )
            select
                i.id as i_id,
                i.name as i_name,
                i.price as i_price,
                i.category as i_category,
                smd.sm_id,
                smd.quantity as sm_quantity,
                smd.type as sm_type,
                smd.unit as sm_unit,
                smd.creation_datetime as sm_creation_datetime
            from ingredient i
            left join stock_movements_data smd on i.id = smd.id_ingredient
            where i.id = ?
            order by smd.creation_datetime desc
            """;

    try (Connection conn = dataSource.getDBConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setInt(1, id);
      ps.setInt(2, id);

      try (ResultSet rs = ps.executeQuery()) {
        Ingredient ingredient = null;

        while (rs.next()) {
          if (ingredient == null) {
            ingredient = new Ingredient();
            ingredient.setId(rs.getInt("i_id"));
            ingredient.setName(rs.getString("i_name"));
            ingredient.setPrice(rs.getDouble("i_price"));
            ingredient.setCategory(CategoryEnum.valueOf(rs.getString("i_category")));
            ingredient.setStockMovementList(new ArrayList<>());
          }

          if (rs.getObject("sm_id") != null) {
            StockMovement stockMovement = new StockMovement();
            stockMovement.setId(rs.getInt("sm_id"));

            StockValue stockValue = new StockValue();
            stockValue.setQuantity(rs.getDouble("sm_quantity"));
            stockValue.setUnit(UnitType.valueOf(rs.getString("sm_unit")));
            stockMovement.setValue(stockValue);

            stockMovement.setType(MovementTypeEnum.valueOf(rs.getString("sm_type")));
            stockMovement.setCreationDatetime(rs.getTimestamp("sm_creation_datetime").toInstant());

            ingredient.getStockMovementList().add(stockMovement);
          }
        }

        return Optional.ofNullable(ingredient);
      }

    } catch (SQLException e) {
      throw new RuntimeException("error fetching ingredient by id: " + id, e);
    }
  }
}
