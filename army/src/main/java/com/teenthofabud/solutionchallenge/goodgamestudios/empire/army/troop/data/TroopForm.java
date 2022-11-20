package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TroopForm {

    @ToString.Include
    private Integer strength;

}
