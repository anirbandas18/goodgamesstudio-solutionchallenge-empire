package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.teenthofabud.core.common.data.vo.TOABBaseVo;
import lombok.*;

import java.util.Comparator;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TroopVo extends TOABBaseVo implements Comparable<TroopVo> {

    @EqualsAndHashCode.Include
    @ToString.Include
    private Integer quantity;

    @EqualsAndHashCode.Include
    @ToString.Include
    private String unit;

    @Override
    public int compareTo(TroopVo o) {
        return Comparator.comparing(TroopVo::getQuantity)
                .thenComparing(TroopVo::getUnit)
                .compare(this, o);
    }
}
