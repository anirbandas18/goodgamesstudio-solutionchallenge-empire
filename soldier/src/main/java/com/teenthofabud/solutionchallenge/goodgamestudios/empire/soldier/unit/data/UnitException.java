package com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.data;

import com.teenthofabud.core.common.error.TOABBaseException;
import com.teenthofabud.core.common.error.TOABError;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UnitException extends TOABBaseException {

    @ToString.Include
    private transient TOABError error;

    public UnitException(String message) {
        super(message);
    }

    public UnitException(String message, Object[] parameters) {
        super(message, parameters);
    }

    public UnitException(TOABError error, String message, Object[] parameters) {
        super(error, message, parameters);
        this.error = error;
    }

    public UnitException(TOABError error, Object[] parameters) {
        super(error, parameters);
        this.error = error;
    }

    @Override
    public String getSubDomain() {
        return "Unit";
    }

}
