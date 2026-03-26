package org.td5.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.td5.entity.enums.UnitType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockValue {
    private Double quantity;
    private UnitType unit;
}
