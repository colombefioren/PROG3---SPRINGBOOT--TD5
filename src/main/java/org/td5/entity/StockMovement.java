package org.td5.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.td5.entity.enums.MovementTypeEnum;
import org.td5.entity.enums.UnitType;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockMovement {
    private Integer id;
    @JsonIgnore
    private StockValue value;
    private MovementTypeEnum type;
    private Instant creationDatetime;

    @JsonProperty("unit")
    private UnitType getStockUnit(){
        return value.getUnit();
    }

    @JsonProperty("value")
    private Double getStockValue(){
        return value.getQuantity();
    }

}
