package com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnitForm {

    @ToString.Include
    private String name;
    @ToString.Include
    private String description;

}
