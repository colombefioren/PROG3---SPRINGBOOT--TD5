package org.td5.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.td5.entity.enums.CategoryEnum;
import org.td5.entity.enums.MovementTypeEnum;
import org.td5.entity.enums.UnitType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient {
  private Integer id;
  private String name;
  private CategoryEnum category;
  private Double price;
  @JsonIgnore private List<StockMovement> stockMovementList;

  public StockValue getStockValue(Instant instant, UnitType unit) {
    if (stockMovementList == null || stockMovementList.isEmpty()) {
      return new StockValue(0.0, unit);
    }
    double total = 0.0;
    for (StockMovement movement : stockMovementList) {

      if (!movement.getCreationDatetime().isAfter(instant)) {
        if (movement.getType().equals(MovementTypeEnum.IN)) {
          total += movement.getValue().getQuantity();
        } else if (movement.getType().equals(MovementTypeEnum.OUT)) {
          total -= movement.getValue().getQuantity();
        }
      }
    }
    return new StockValue(total, unit);
  }

  @JsonIgnore
  public List<StockMovement> getStockMovements(Instant from,Instant to) {
    if (stockMovementList == null || stockMovementList.isEmpty()) {
      return new ArrayList<>();
    }
    List<StockMovement> stockMovements = new ArrayList<>();
    for (StockMovement movement : stockMovementList) {
      if(movement.getCreationDatetime().isAfter(from) && movement.getCreationDatetime().isBefore(to)) {
        stockMovements.add(movement);
      }
    }
    return stockMovements;
  }
}
