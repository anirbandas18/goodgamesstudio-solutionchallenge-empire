package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.controller;

import com.teenthofabud.core.common.constant.TOABBaseMessageTemplate;
import com.teenthofabud.core.common.data.vo.ErrorVo;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.error.ArmyErrorCode;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data.TroopException;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data.TroopForm;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data.TroopVo;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.service.TroopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("troop")
@Slf4j
@Tag(name = "Troop API", description = "Manage Troops and their details")
public class TroopController {

    private static final String MEDIA_ARMY_APPLICATION_JSON_PATCH = "application/json-patch+json";

    @Autowired
    public void setService(TroopService service) {
        this.service = service;
    }

    private TroopService service;

    @Operation(summary = "Create new Troop with strength")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "List of created troops order by quantity descending and unit alphabetically",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = TroopVo.class))) }),
            @ApiResponse(responseCode = "400", description = "Troop attribute's value is invalid",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal system error while trying to create new Troop",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) })
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<TroopVo> postNewTroop(@RequestBody(required = false) TroopForm form) throws TroopException {
        log.debug("Requesting to create new troop");
        if(form != null) {
            List<TroopVo> troops = service.createNewTroop(form);
            log.debug("Responding with newly created new troop");
            return troops;
        }
        log.debug("TroopForm is null");
        throw new TroopException(ArmyErrorCode.ARMY_ATTRIBUTE_UNEXPECTED, new Object[]{ "form", TOABBaseMessageTemplate.MSG_TEMPLATE_NOT_PROVIDED });
    }

}
