package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army;


import com.fasterxml.jackson.core.type.TypeReference;
import com.teenthofabud.core.common.data.vo.ErrorVo;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.error.ArmyErrorCode;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.integration.soldier.data.UnitVo;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.integration.soldier.proxy.SoldierServiceClient;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data.TroopForm;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data.TroopVo;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = { ArmyServiceApplication.class })
public class TroopActiveUnitsIntegrationTest extends ArmyIntegrationBaseTest {

    private static final String TROOP_URI = "/troop";

    private SoldierServiceClient soldierServiceClient;

    private Integer integrationPort;


    @Value("${emp.army.integration.gateway.port}")
    public void setIntegrationPort(Integer integrationPort) {
        this.integrationPort = integrationPort;
    }

    @Autowired
    public void setSoldierServiceClient(SoldierServiceClient soldierServiceClient) {
        this.soldierServiceClient = soldierServiceClient;
    }

    private TroopForm troopForm;

    private UnitVo unitVo(String fileName) throws IOException {
        String filePath = String.format("file:src/test/resources/simulation/soldier-service/%s", fileName);
        UnitVo unitVo = om.readValue(new URL(filePath), UnitVo.class);
        return unitVo;
    }

    private void assertTroop(TroopVo troopVo) {
        Assert.assertTrue(StringUtils.hasText(StringUtils.trimWhitespace(troopVo.getUnit())));
        Assert.assertTrue(troopVo.getQuantity() > 0);
    }

    /*private TroopEntity troopEntity(String transactionId, String unitId, Integer quantity, boolean active) {
        TroopEntity troopEntity = new TroopEntity();
        troopEntity.setTransactionId(transactionId);
        troopEntity.setUnitId(unitId);
        troopEntity.setQuantity(quantity);
        troopEntity.setActive(active);
        return troopEntity;
    }*/

    @BeforeEach
    private void init() throws IOException {

        /**
         * Unit
         */

        /*unitVo1 = this.unitVo("unit-200-response-data-id-1.json");
        unitVo2 = this.unitVo("unit-200-response-data-id-2.json");
        unitVo3 = this.unitVo("unit-200-response-data-id-3.json");*/

    }

    @Test
    public void test_Troop_Post_ShouldReturn_201Response_And_RandomizedArmy_WhenPostedWith_MoreThanSufficient_ArmyStrength() throws Exception {
        MvcResult mvcResult = null;
        List<UnitVo> unitVoList = soldierServiceClient.getAllUnitDetails();
        List<UnitVo> activeUnits = unitVoList.stream().filter(e -> e.getActive() == true).collect(Collectors.toList());
        troopForm = new TroopForm(activeUnits.size() * 100);

        mvcResult = this.mockMvc.perform(post(TROOP_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(troopForm)))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
        Assertions.assertTrue(StringUtils.hasText(mvcResult.getResponse().getContentAsString()));
        List<TroopVo> troops = om.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<TroopVo>>() {});
        Assert.assertTrue(troops.size() == activeUnits.size());
        troops.forEach(e -> this.assertTroop(e));
    }

    @Test
    public void test_Troop_Post_ShouldReturn_201Response_And_RandomizedArmy_WhenPostedWith_JustSufficient_ArmyStrength() throws Exception {
        MvcResult mvcResult = null;
        List<UnitVo> unitVoList = soldierServiceClient.getAllUnitDetails();
        List<UnitVo> activeUnits = unitVoList.stream().filter(e -> e.getActive() == true).collect(Collectors.toList());
        troopForm = new TroopForm(activeUnits.size());

        mvcResult = this.mockMvc.perform(post(TROOP_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(troopForm)))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
        Assertions.assertTrue(StringUtils.hasText(mvcResult.getResponse().getContentAsString()));
        List<TroopVo> troops = om.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<TroopVo>>() {});
        Assert.assertTrue(troops.size() == activeUnits.size());
        troops.forEach(e -> this.assertTroop(e));
    }

    @Test
    public void test_Troop_Post_ShouldReturn_400Response_And_ErrorCode_EMP_ARMY_001_WhenPostedWith_LessThanSufficient_ArmyStrength() throws Exception {
        MvcResult mvcResult = null;
        String errorCode = ArmyErrorCode.ARMY_ATTRIBUTE_INVALID.getErrorCode();
        List<UnitVo> unitVoList = soldierServiceClient.getAllUnitDetails();
        List<UnitVo> activeUnits = unitVoList.stream().filter(e -> e.getActive() == true).collect(Collectors.toList());
        troopForm = new TroopForm(activeUnits.size() * 0);
        String fieldId = "strength";

        mvcResult = mockMvc.perform(post(TROOP_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(troopForm)))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldId));

    }

    @Override
    public String getSimulationBaseLocation() {
        return "simulation/soldier-service";
    }

    @Override
    public Integer getServicePort() throws UnsupportedOperationException {
        return integrationPort;
    }

    @Override
    public String[] getSimulationFilePaths() {
        return new String[] { String.join("/", getSimulationBaseLocation(), "simulation-all-active.json") };
    }
}
