package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.integration.soldier.error;

import com.teenthofabud.core.common.error.TOABFeignException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UnitException extends TOABFeignException {

    public UnitException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public UnitException(String errorCode, String errorMessage, String errorDomain) {
        super(errorCode, errorMessage, errorDomain);
    }
}
