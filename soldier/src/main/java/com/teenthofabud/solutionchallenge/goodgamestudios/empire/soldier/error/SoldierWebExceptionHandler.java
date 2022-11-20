package com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.error;

import com.teenthofabud.core.common.data.vo.ErrorVo;
import com.teenthofabud.core.common.error.TOABBaseException;
import com.teenthofabud.core.common.handler.TOABBaseWebExceptionHandler;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.data.UnitException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SoldierWebExceptionHandler extends TOABBaseWebExceptionHandler {

    @ExceptionHandler(value = { UnitException.class })
    public ResponseEntity<ErrorVo> handleSoldierSubDomainExceptions(TOABBaseException e) {
        ResponseEntity<ErrorVo>  response = super.parseExceptionToResponse(e);
        return response;
    }

}
