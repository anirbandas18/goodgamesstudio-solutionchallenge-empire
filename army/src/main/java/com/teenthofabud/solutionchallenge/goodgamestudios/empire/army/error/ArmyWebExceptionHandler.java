package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.error;

import com.teenthofabud.core.common.data.vo.ErrorVo;
import com.teenthofabud.core.common.error.TOABBaseException;
import com.teenthofabud.core.common.handler.TOABBaseWebExceptionHandler;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data.TroopException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ArmyWebExceptionHandler extends TOABBaseWebExceptionHandler {

    @ExceptionHandler(value = {TroopException.class })
    public ResponseEntity<ErrorVo> handleArmySubDomainExceptions(TOABBaseException e) {
        ResponseEntity<ErrorVo>  response = super.parseExceptionToResponse(e);
        return response;
    }

}
