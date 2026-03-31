package org.td5.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.td5.entity.enums.MovementTypeEnum;
import org.td5.entity.enums.UnitType;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class StockMovementBody {
    private UnitType unit;
    private Double value;
    private MovementTypeEnum type;
}
