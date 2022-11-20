package com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.error;

import com.teenthofabud.core.common.error.TOABError;

public enum SoldierErrorCode implements TOABError {

    SOLDIER_ATTRIBUTE_INVALID("EMP-SOLDIER-001", 400), // syntactic
    SOLDIER_NOT_FOUND("EMP-SOLDIER-002", 404),
    SOLDIER_ATTRIBUTE_UNEXPECTED("EMP-SOLDIER-003", 422), // semantic
    SOLDIER_EXISTS("EMP-SOLDIER-004", 409),
    SOLDIER_INACTIVE("EMP-SOLDIER-005", 400),
    SOLDIER_ACTION_FAILURE("EMP-SOLDIER-006", 500);

    private String errorCode;
    private int httpStatusCode;

    private SoldierErrorCode(String errorCode, int httpStatusCode) {
        this.errorCode = errorCode;
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public String toString() {
        return "SoldierErrorCode{" +
                this.name() + " -> " +
                "errorCode='" + errorCode + '\'' +
                ", httpStatusCode=" + httpStatusCode +
                '}';
    }

    @Override
    public String getErrorCode() {
        return this.errorCode;
    }

    @Override
    public Integer getHttpStatusCode() {
        return this.httpStatusCode;
    }

    @Override
    public String getDomain() {
        return "Soldier";
    }

}
