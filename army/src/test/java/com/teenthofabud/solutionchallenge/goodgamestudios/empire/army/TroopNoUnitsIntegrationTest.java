package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army;

import com.teenthofabud.core.common.data.vo.ErrorVo;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.error.ArmyErrorCode;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.integration.soldier.data.UnitVo;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.integration.soldier.proxy.SoldierServiceClient;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data.TroopForm;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data.TroopVo;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = { ArmyServiceApplication.class })
public class TroopNoUnitsIntegrationTest extends ArmyIntegrationBaseTest {

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

    private void assertTroop(TroopVo troopVo) {
        Assert.assertTrue(StringUtils.hasText(StringUtils.trimWhitespace(troopVo.getUnit())));
        Assert.assertTrue(troopVo.getQuantity() > 0);
    }

    @Test
    public void test_Troop_Post_ShouldReturn_400Response_And_ErrorCode_EMP_ARMY_001_WhenPostedWith_MoreThanSufficient_ArmyStrength() throws Exception {
        MvcResult mvcResult = null;
        String errorCode = ArmyErrorCode.ARMY_ATTRIBUTE_INVALID.getErrorCode();
        List<UnitVo> unitVoList = soldierServiceClient.getAllUnitDetails();
        troopForm = new TroopForm(unitVoList.size() * 100);
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

    @Test
    public void test_Troop_Post_ShouldReturn_400Response_And_ErrorCode_EMP_ARMY_001_WhenPostedWith_JustSufficient_ArmyStrength() throws Exception {
        MvcResult mvcResult = null;
        String errorCode = ArmyErrorCode.ARMY_ATTRIBUTE_INVALID.getErrorCode();
        List<UnitVo> unitVoList = soldierServiceClient.getAllUnitDetails();
        troopForm = new TroopForm(unitVoList.size());
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

    @Test
    public void test_Troop_Post_ShouldReturn_400Response_And_ErrorCode_EMP_ARMY_001_WhenPostedWith_LessThanSufficient_ArmyStrength() throws Exception {
        MvcResult mvcResult = null;
        String errorCode = ArmyErrorCode.ARMY_ATTRIBUTE_INVALID.getErrorCode();
        List<UnitVo> unitVoList = soldierServiceClient.getAllUnitDetails();
        troopForm = new TroopForm(unitVoList.size() * 0);
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
        return new String[] { String.join("/", getSimulationBaseLocation(), "simulation-none-available.json") };
    }
}
