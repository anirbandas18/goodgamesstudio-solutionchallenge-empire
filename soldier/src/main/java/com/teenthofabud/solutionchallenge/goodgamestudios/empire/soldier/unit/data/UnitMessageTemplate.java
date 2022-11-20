package com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.data;

import lombok.Getter;

@Getter
public enum UnitMessageTemplate {

    MSG_TEMPLATE_SEARCHING_FOR_UNIT_ENTITY_ID("Searching for unitEntity with id: {}"),
    MSG_TEMPLATE_NO_UNIT_ENTITY_ID_AVAILABLE("No unitEntity available with id: {}"),
    MSG_TEMPLATE_FOUND_UNIT_ENTITY_ID("Found unitEntity with id: {}"),
    MSG_TEMPLATE_UNIT_ID_VALID("unit id: {} is semantically valid"),
    MSG_TEMPLATE_UNIT_ID_INVALID("unit id: {} is invalid"),
    MSG_TEMPLATE_UNIT_DOB_INVALID("unit dateOfBirth: {} is invalid"),
    MSG_TEMPLATE_UNIT_ID_EMPTY("unit id is empty"),
    MSG_TEMPLATE_UNIT_CASCADE_LEVEL_EMPTY("unit cascadeLevel is empty"),
    MSG_TEMPLATE_UNIT_EXISTENCE_BY_NAME ("Checking existence of unitEntity with name: {}"),
    MSG_TEMPLATE_UNIT_EXISTS_BY_NAME ("unitEntity already exists with name: {}"),
    MSG_TEMPLATE_UNIT_NON_EXISTENCE_BY_NAME ("No unitEntity exists with name: {}");

    private String value;

    private UnitMessageTemplate(String value) {
        this.value = value;
    }


}
