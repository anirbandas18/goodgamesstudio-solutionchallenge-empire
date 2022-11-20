package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data;

import com.teenthofabud.core.common.error.TOABBaseException;
import com.teenthofabud.core.common.error.TOABError;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TroopException extends TOABBaseException {

    @ToString.Include
    private transient TOABError error;

    public TroopException(String message) {
        super(message);
    }

    public TroopException(String message, Object[] parameters) {
        super(message, parameters);
    }

    public TroopException(TOABError error, String message, Object[] parameters) {
        super(error, message, parameters);
        this.error = error;
    }

    public TroopException(TOABError error, Object[] parameters) {
        super(error, parameters);
        this.error = error;
    }

    @Override
    public String getSubDomain() {
        return "Troop";
    }

}
