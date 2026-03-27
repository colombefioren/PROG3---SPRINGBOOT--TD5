package org.td5.repository.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
select i.id as i_id, i.name as i_name, i.price as i_price, i.category as i_category
                            from ingredient i
                            order by i.id
""";

    List<Ingredient> ingredients = new ArrayList<>();

    try (Connection conn = dataSource.getDBConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {

      while (rs.next()) {
        ingredients.add(mapResultSetToIngredient(rs));
      }
      return ingredients;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Ingredient findById(Integer id) {
    String sql =
"""
                  select i.id as i_id, i.name as i_name, i.price as i_price, i.category as i_category from ingredient i where i.id = ?
""";

    Ingredient ingredient = null;

    try (Connection conn = dataSource.getDBConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, id);

      try (ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
          ingredient = mapResultSetToIngredient(rs);
        }
      }

      return ingredient;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<StockMovement> findStockMovementsByIngredientId(Integer id) {
    String findStocksSql =
        """
                        select st.id as st_id, st.id_ingredient, st.quantity as st_quantity, st.type as st_type, st.unit as st_unit, st.creation_datetime as st_creation_datetime from stock_movement st where id_ingredient = ? order by st_creation_datetime desc
                    """;

    List<StockMovement> stockMovements = new ArrayList<>();

    try (Connection conn = dataSource.getDBConnection();
        PreparedStatement ps = conn.prepareStatement(findStocksSql); ) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          stockMovements.add(mapResultSetToStockMovement(rs));
        }
      }
      return stockMovements;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private Ingredient mapResultSetToIngredient(ResultSet rs) throws SQLException {
    return Ingredient.builder()
        .id(rs.getInt("i_id"))
        .name(rs.getString("i_name"))
        .category(CategoryEnum.valueOf(rs.getString("i_category")))
        .price(rs.getDouble("i_price"))
        .build();
  }

  private StockMovement mapResultSetToStockMovement(ResultSet rs) throws SQLException {
    return StockMovement.builder()
        .id(rs.getInt("st_id"))
        .value(
            StockValue.builder()
                .quantity(rs.getDouble("st_quantity"))
                .unit(UnitType.valueOf(rs.getString("st_unit")))
                .build())
        .type(MovementTypeEnum.valueOf(rs.getString("st_type")))
        .creationDatetime(rs.getTimestamp("st_creation_datetime").toInstant())
        .build();
  }
}
