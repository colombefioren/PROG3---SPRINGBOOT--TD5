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
import org.td5.entity.StockMovement;
import org.td5.entity.StockValue;
import org.td5.entity.enums.MovementTypeEnum;
import org.td5.entity.enums.UnitType;
import org.td5.repository.StockMovementRepository;

@Repository
@AllArgsConstructor
public class StockMovementRepositoryImp implements StockMovementRepository {
    private final DataSource dataSource;
    @Override
    public List<StockMovement> findByIngredientId(Integer ingredientId) {
        String findStocksSql =
                """
                                select st.id as st_id, st.id_ingredient, st.quantity as st_quantity, st.type as st_type, st.unit as st_unit, st.creation_datetime as st_creation_datetime from stock_movement st where id_ingredient = ? order by st_creation_datetime desc
                            """;

        List<StockMovement> stockMovements = new ArrayList<>();

        try (Connection conn = dataSource.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(findStocksSql); ) {
            ps.setInt(1, ingredientId);
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
