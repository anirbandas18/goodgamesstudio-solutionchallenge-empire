package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.error;

import com.teenthofabud.core.common.error.TOABError;

public enum ArmyErrorCode implements TOABError {

    ARMY_ATTRIBUTE_INVALID("EMP-ARMY-001", 400), // syntactic
    ARMY_NOT_FOUND("EMP-ARMY-002", 404),
    ARMY_ATTRIBUTE_UNEXPECTED("EMP-ARMY-003", 422), // semantic
    ARMY_EXISTS("EMP-ARMY-004", 409),
    ARMY_INACTIVE("EMP-ARMY-005", 400),
    ARMY_ACTION_FAILURE("EMP-ARMY-006", 500);

    private String errorCode;
    private int httpStatusCode;

    private ArmyErrorCode(String errorCode, int httpStatusCode) {
        this.errorCode = errorCode;
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public String toString() {
        return "ArmyErrorCode{" +
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
        return "Army";
    }

}
