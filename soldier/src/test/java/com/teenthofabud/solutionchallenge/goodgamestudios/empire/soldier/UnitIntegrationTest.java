package com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier;

import com.teenthofabud.core.common.constant.TOABCascadeLevel;
import com.teenthofabud.core.common.data.form.PatchOperationForm;
import com.teenthofabud.core.common.data.vo.ErrorVo;
import com.teenthofabud.core.common.error.TOABErrorCode;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.error.SoldierErrorCode;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.data.UnitEntity;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.data.UnitForm;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.data.UnitVo;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.repository.UnitRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UnitIntegrationTest extends SoldierIntegrationBaseTest {

    private static final String MEDIA_TYPE_APPLICATION_JSON_PATCH = "application/json-patch+json";

    private static final String UNIT_URI = "/unit";
    private static final String UNIT_URI_BY_ID = "/unit/{id}";
    private static final String UNIT_URI_FILTER = "/unit/filter";

    private UnitRepository unitRepository;

    @Autowired
    public void setUnitRepository(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }


    private UnitForm unitForm;
    private UnitVo unitVo1;
    private UnitVo unitVo2;
    private UnitVo unitVo3;
    private UnitVo unitVo4;
    private UnitEntity unitEntity1;
    private UnitEntity unitEntity2;
    private UnitEntity unitEntity3;
    private UnitEntity unitEntity4;

    private List<PatchOperationForm> patches;

    @BeforeEach
    private void init() {

        /**
         * Unit
         */

        unitForm = new UnitForm();
        unitForm.setName("New Name");
        unitForm.setDescription("New Description");

        patches = Arrays.asList(
                new PatchOperationForm("replace", "/name", "patched name"),
                new PatchOperationForm("replace", "/description", "patched description"));

        unitEntity1 = new UnitEntity();
        unitEntity1.setName("Unit 1 Name");
        unitEntity1.setDescription("Unit 1 Description");
        unitEntity1.setActive(Boolean.TRUE);

        unitEntity2 = new UnitEntity();
        unitEntity2.setName("Unit 2 Name");
        unitEntity2.setDescription("Unit 2 Description");
        unitEntity2.setActive(Boolean.TRUE);

        unitEntity3 = new UnitEntity();
        unitEntity3.setName("Unit 3 Name");
        unitEntity3.setDescription("Unit 3 Description");
        unitEntity3.setActive(Boolean.TRUE);

        unitEntity4 = new UnitEntity();
        unitEntity4.setName("Unit 4 Name");
        unitEntity4.setDescription("Unit 4 Description");
        unitEntity4.setActive(Boolean.FALSE);

        unitEntity1 = unitRepository.save(unitEntity1);

        unitVo1 = new UnitVo();
        unitVo1.setId(unitEntity1.getId().toString());
        unitVo1.setName(unitEntity1.getName());
        unitVo1.setDescription(unitEntity1.getDescription());

        unitEntity2 = unitRepository.save(unitEntity2);

        unitVo2 = new UnitVo();
        unitVo2.setId(unitEntity2.getId().toString());
        unitVo2.setName(unitEntity2.getName());
        unitVo2.setDescription(unitEntity2.getDescription());

        unitEntity3 = unitRepository.save(unitEntity3);

        unitVo3 = new UnitVo();
        unitVo3.setId(unitEntity3.getId().toString());
        unitVo3.setName(unitEntity3.getName());
        unitVo3.setDescription(unitEntity3.getDescription());

        unitEntity4 = unitRepository.save(unitEntity4);

        unitVo4 = new UnitVo();
        unitVo4.setId(unitEntity4.getId().toString());
        unitVo4.setName(unitEntity4.getName());
        unitVo4.setDescription(unitEntity4.getDescription());

        unitEntity1 = unitRepository.save(unitEntity1);
        unitEntity2 = unitRepository.save(unitEntity2);
        unitEntity3 = unitRepository.save(unitEntity3);
        unitEntity4 = unitRepository.save(unitEntity4);

    }

    @AfterEach
    private void destroy() {
        unitRepository.deleteById(unitEntity1.getId());
        unitRepository.deleteById(unitEntity2.getId());
        unitRepository.deleteById(unitEntity3.getId());
        unitRepository.deleteById(unitEntity4.getId());
    }

    @Test
    public void test_Unit_Post_ShouldReturn_201Response_And_NewUnitId_WhenPosted_WithValidUnitForm() throws Exception {
        MvcResult mvcResult = null;

        mvcResult = this.mockMvc.perform(post(UNIT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(unitForm)))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
        Assertions.assertTrue(StringUtils.hasText(mvcResult.getResponse().getContentAsString()));
    }

    @Test
    public void test_Unit_Post_ShouldReturn_400Response_And_ErrorCode_RES_SOLDIER_001_WhenRequested_WithEmptyName() throws Exception {
        MvcResult mvcResult = null;
        String errorCode = SoldierErrorCode.SOLDIER_ATTRIBUTE_INVALID.getErrorCode();
        String fieldName = "name";
        unitForm.setName("");

        mvcResult = mockMvc.perform(post(UNIT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(unitForm)))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));

    }

    @Test
    public void test_Unit_Post_ShouldReturn_201Response_And_NewUnitId_WhenPosted_WithEmptyDescription() throws Exception {
        MvcResult mvcResult = null;
        unitForm.setDescription("");

        mvcResult = mockMvc.perform(post(UNIT_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(unitForm)))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
        Assertions.assertTrue(StringUtils.hasText(mvcResult.getResponse().getContentAsString()));
    }

    @Test
    public void test_Unit_Post_ShouldReturn_422Response_And_ErrorCode_RES_SOLDIER_003_WhenPosted_WithNoUnitForm() throws Exception {
        String id = unitEntity1.getId().toString();
        MvcResult mvcResult = null;
        String errorCode = SoldierErrorCode.SOLDIER_ATTRIBUTE_UNEXPECTED.getErrorCode();
        String fieldName = "form";
        String message = "not provided";

        mvcResult = mockMvc.perform(post(UNIT_URI)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(message));

    }

    @Test
    public void test_Unit_Get_ShouldReturn_200Response_And_UnitListNaturallyOrdered_WhenRequested_ForAllCategories() throws Exception {
        MvcResult mvcResult = null;
        Set<UnitVo> unitList = new TreeSet<>(Arrays.asList(unitVo1, unitVo2, unitVo3, unitVo4));

        mvcResult = this.mockMvc.perform(get(UNIT_URI))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(unitList.size(), om.readValue(mvcResult.getResponse().getContentAsString(), UnitVo[].class).length);
    }

    @ParameterizedTest
    @ValueSource(strings = { "", " " })
    public void test_Unit_Get_ShouldReturn_400Response_And_ErrorCode_RES_SOLDIER_001_WhenRequestedBy_EmptyNameOnly(String name) throws Exception {
        MvcResult mvcResult = null;
        String errorCode = SoldierErrorCode.SOLDIER_ATTRIBUTE_INVALID.getErrorCode();
        String fieldName = "filters";

        mvcResult = this.mockMvc.perform(get(UNIT_URI_FILTER).queryParam("name", name))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
    }

    @ParameterizedTest
    @ValueSource(strings = { "", " " })
    public void test_Unit_Get_ShouldReturn_400Response_And_ErrorCode_RES_SOLDIER_001_WhenRequestedBy_EmptyDescriptionOnly(String description) throws Exception {
        MvcResult mvcResult = null;
        String errorCode = SoldierErrorCode.SOLDIER_ATTRIBUTE_INVALID.getErrorCode();
        String fieldName = "filters";

        mvcResult = this.mockMvc.perform(get(UNIT_URI_FILTER).queryParam("description", description))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
    }

    @Test
    public void test_Unit_Get_ShouldReturn_200Response_And_EmptyUnitList_WhenRequestedBy_AbsentName() throws Exception {
        MvcResult mvcResult = null;

        mvcResult = this.mockMvc.perform(get(UNIT_URI_FILTER).queryParam("name", "Hey"))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(0, om.readValue(mvcResult.getResponse().getContentAsString(), UnitVo[].class).length);
    }

    @Test
    public void test_Unit_Get_ShouldReturn_200Response_And_EmptyUnitList_WhenRequestedBy_AbsentDescription() throws Exception {
        MvcResult mvcResult = null;

        mvcResult = this.mockMvc.perform(get(UNIT_URI_FILTER).queryParam("description", "Hey"))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(0, om.readValue(mvcResult.getResponse().getContentAsString(), UnitVo[].class).length);
    }

    @Test
    public void test_Unit_Get_ShouldReturn_200Response_And_UnitListNaturallyOrdered_WhenRequested_ForCategories_WithName() throws Exception {
        MvcResult mvcResult = null;
        List<UnitVo> unitList = new ArrayList<>(Arrays.asList(unitVo1, unitVo2, unitVo3, unitVo4));

        mvcResult = this.mockMvc.perform(get(UNIT_URI_FILTER)
                        .queryParam("name", "Unit"))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(unitList.size(), om.readValue(mvcResult.getResponse().getContentAsString(), UnitVo[].class).length);
    }

    @Test
    public void test_Unit_Get_ShouldReturn_200Response_And_UnitListNaturallyOrdered_WhenRequested_ForCategories_WithDescription() throws Exception {
        MvcResult mvcResult = null;
        List<UnitVo> unitList = new ArrayList<>(Arrays.asList(unitVo2));

        mvcResult = this.mockMvc.perform(get(UNIT_URI_FILTER)
                        .queryParam("description", "Unit 2"))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(unitList.size(), om.readValue(mvcResult.getResponse().getContentAsString(), UnitVo[].class).length);
    }

    @Test
    public void test_Unit_Get_ShouldReturn_200Response_And_UnitListNaturallyOrdered_WhenRequested_ForCategories_WithNameAndDescription() throws Exception {
        MvcResult mvcResult = null;
        Set<UnitVo> unitList = new TreeSet<>(Arrays.asList(unitVo1));

        mvcResult = this.mockMvc.perform(get(UNIT_URI_FILTER)
                        .queryParam("name", "Unit 1")
                        .queryParam("description", "Unit 1"))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(unitList.size(), om.readValue(mvcResult.getResponse().getContentAsString(), UnitVo[].class).length);
    }

    @Test
    public void test_Unit_Get_ShouldReturn_200Response_And_EmptyUnitList_WhenRequested_ForCategories_WithAbsent_WithNameAndDescription() throws Exception {
        MvcResult mvcResult = null;
        Set<UnitVo> unitList = new TreeSet<>();

        mvcResult = this.mockMvc.perform(get(UNIT_URI_FILTER)
                        .queryParam("name", "Unit 1")
                        .queryParam("description", UUID.randomUUID().toString()))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(unitList.size(), om.readValue(mvcResult.getResponse().getContentAsString(), UnitVo[].class).length);
    }

    @Test
    public void test_Unit_Get_ShouldReturn_200Response_And_UnitDetails_WhenRequested_ById() throws Exception {
        String id = unitEntity1.getId().toString();
        MvcResult mvcResult = null;

        mvcResult = this.mockMvc.perform(get(UNIT_URI_BY_ID, id))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(om.writeValueAsString(unitVo1), mvcResult.getResponse().getContentAsString());
        Assertions.assertEquals(unitVo1.getId(), om.readValue(mvcResult.getResponse().getContentAsString(), UnitVo.class).getId());
    }

    @ParameterizedTest
    @ValueSource(strings = { " ", "r" })
    public void test_Unit_Get_ShouldReturn_400Response_And_ErrorCode_RES_SOLDIER_001_WhenRequestedBy_EmptyInvalidId(String id) throws Exception {
        MvcResult mvcResult = null;
        String errorCode = SoldierErrorCode.SOLDIER_ATTRIBUTE_INVALID.getErrorCode();
        String fieldName = "id";

        mvcResult = this.mockMvc.perform(get(UNIT_URI_BY_ID, id))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
    }

    @Test
    public void test_Unit_Get_ShouldReturn_400Response_And_ErrorCode_RES_SOLDIER_002_WhenRequested_ByAbsentId() throws Exception {
        String id = "5";
        MvcResult mvcResult = null;
        String errorCode = SoldierErrorCode.SOLDIER_NOT_FOUND.getErrorCode();
        String fieldName = "id";

        mvcResult = this.mockMvc.perform(get(UNIT_URI_BY_ID, id))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
    }

    @Test
    public void test_Unit_Delete_ShouldReturn_204Response_And_NoResponseBody_WhenDeleted_ById() throws Exception {
        String id = unitEntity3.getId().toString();
        MvcResult mvcResult = null;

        mvcResult = this.mockMvc.perform(delete(UNIT_URI_BY_ID, id))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals("", mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void test_Unit_Get_ShouldReturn_200Response_And_DomainDetails_WhenRequested_ById_AndFirstLevel_Cascade() throws Exception {
        String id = unitEntity1.getId().toString();
        MvcResult mvcResult = null;

        mvcResult = this.mockMvc.perform(get(UNIT_URI_BY_ID, id)
                        .queryParam("cascadeUntilLevel", TOABCascadeLevel.ONE.getLevelCode()))
                .andDo(print())
                .andReturn();
        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(unitVo1.getId(), om.readValue(mvcResult.getResponse().getContentAsString(), UnitVo.class).getId());
        Assertions.assertEquals(unitVo1.getName(), om.readValue(mvcResult.getResponse().getContentAsString(), UnitVo.class).getName());
        Assertions.assertEquals(unitVo1.getDescription(), om.readValue(mvcResult.getResponse().getContentAsString(), UnitVo.class).getDescription());
        Assertions.assertTrue(ObjectUtils.isEmpty(om.readValue(mvcResult.getResponse().getContentAsString(), UnitVo.class).getCreatedBy()));
        Assertions.assertTrue(ObjectUtils.isEmpty(om.readValue(mvcResult.getResponse().getContentAsString(), UnitVo.class).getModifiedBy()));
        Assertions.assertTrue(ObjectUtils.isEmpty(om.readValue(mvcResult.getResponse().getContentAsString(), UnitVo.class).getCreatedOn()));
        Assertions.assertTrue(ObjectUtils.isEmpty(om.readValue(mvcResult.getResponse().getContentAsString(), UnitVo.class).getModifiedOn()));
        Assertions.assertFalse(ObjectUtils.isEmpty(om.readValue(mvcResult.getResponse().getContentAsString(), UnitVo.class).getActive()));
    }

    @Test
    public void test_Unit_Get_ShouldReturn_200Response_And_DomainDetails_WhenRequested_ById_AndSecondLevel_Cascade() throws Exception {
        String id = unitEntity1.getId().toString();
        MvcResult mvcResult = null;

        mvcResult = this.mockMvc.perform(get(UNIT_URI_BY_ID, id)
                        .queryParam("cascadeUntilLevel", TOABCascadeLevel.TWO.getLevelCode()))
                .andDo(print())
                .andReturn();
        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(unitVo1.getId(), om.readValue(mvcResult.getResponse().getContentAsString(), UnitVo.class).getId());
        Assertions.assertEquals(unitVo1.getName(), om.readValue(mvcResult.getResponse().getContentAsString(), UnitVo.class).getName());
        Assertions.assertEquals(unitVo1.getDescription(), om.readValue(mvcResult.getResponse().getContentAsString(), UnitVo.class).getDescription());
        Assertions.assertTrue(!ObjectUtils.isEmpty(om.readValue(mvcResult.getResponse().getContentAsString(), UnitVo.class).getCreatedBy()));
        Assertions.assertTrue(!ObjectUtils.isEmpty(om.readValue(mvcResult.getResponse().getContentAsString(), UnitVo.class).getModifiedBy()));
        Assertions.assertTrue(!ObjectUtils.isEmpty(om.readValue(mvcResult.getResponse().getContentAsString(), UnitVo.class).getCreatedOn()));
        Assertions.assertTrue(!ObjectUtils.isEmpty(om.readValue(mvcResult.getResponse().getContentAsString(), UnitVo.class).getModifiedOn()));
        Assertions.assertTrue(!ObjectUtils.isEmpty(om.readValue(mvcResult.getResponse().getContentAsString(), UnitVo.class).getActive()));
    }

    @Test
    public void test_Unit_Delete_ShouldReturn_400Response_And_ErrorCode_RES_SOLDIER_001__WhenDeleted_ByEmptyId() throws Exception {
        MvcResult mvcResult = null;
        String errorCode = SoldierErrorCode.SOLDIER_ATTRIBUTE_INVALID.getErrorCode();
        String fieldName = "id";

        mvcResult = this.mockMvc.perform(delete(UNIT_URI_BY_ID, " "))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
    }

    @Test
    public void test_Unit_Delete_ShouldReturn_422Response_And_ErrorCode_RES_SOLDIER_003_WhenDeleted_ByInvalidId() throws Exception {
        String id = " ";
        MvcResult mvcResult = null;
        String errorCode = SoldierErrorCode.SOLDIER_ATTRIBUTE_INVALID.getErrorCode();
        String fieldName = "id";

        mvcResult = this.mockMvc.perform(delete(UNIT_URI_BY_ID, id))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
    }

    @Test
    public void test_Unit_Delete_ShouldReturn_400Response_And_ErrorCode_RES_SOLDIER_005_WhenDeleted_ByInactiveId() throws Exception {
        String id = unitEntity4.getId().toString();
        MvcResult mvcResult = null;
        String errorCode = SoldierErrorCode.SOLDIER_INACTIVE.getErrorCode();

        mvcResult = this.mockMvc.perform(delete(UNIT_URI_BY_ID, id))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(id.toString()));
    }

    @Test
    public void test_Unit_Delete_ShouldReturn_404Response_And_ErrorCode_RES_SOLDIER_002_WhenDeleted_ByAbsentId() throws Exception {
        String id = "5";
        MvcResult mvcResult = null;
        String errorCode = SoldierErrorCode.SOLDIER_NOT_FOUND.getErrorCode();
        String fieldName = "id";

        mvcResult = this.mockMvc.perform(delete(UNIT_URI_BY_ID, id))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
    }

    @Test
    public void test_Unit_Put_ShouldReturn_204Response_And_NoResponseBody_WhenUpdated_ById_AndUnitDetails() throws Exception {
        String id = unitEntity1.getId().toString();
        MvcResult mvcResult = null;
        unitForm.setName("Ferran");

        mvcResult = this.mockMvc.perform(put(UNIT_URI_BY_ID, id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(unitForm)))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals("", mvcResult.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(strings = { " ", "r" })
    public void test_Unit_Put_ShouldReturn_400Response_And_ErrorCode_RES_SOLDIER_001_WhenUpdatedBy_EmptyInvalidId_AndUnitDetails(String id) throws Exception {
        MvcResult mvcResult = null;
        String errorCode = SoldierErrorCode.SOLDIER_ATTRIBUTE_INVALID.getErrorCode();
        String fieldName = "id";

        mvcResult = this.mockMvc.perform(put(UNIT_URI_BY_ID, id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(unitForm)))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
    }

    @Test
    public void test_Unit_Put_ShouldReturn_404Response_And_ErrorCode_RES_SOLDIER_002_WhenUpdated_ByAbsentId_AndUnitDetails() throws Exception {
        String id = "5";
        MvcResult mvcResult = null;
        String errorCode = SoldierErrorCode.SOLDIER_NOT_FOUND.getErrorCode();
        String fieldName = "id";

        mvcResult = this.mockMvc.perform(put(UNIT_URI_BY_ID, id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(unitForm)))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(id.toString()));
    }

    @Test
    public void test_Unit_Put_ShouldReturn_400Response_And_ErrorCode_RES_SOLDIER_005_WhenUpdated_ByInactiveId_AndUnitDetails() throws Exception {
        String id = unitEntity4.getId().toString();
        MvcResult mvcResult = null;
        String errorCode = SoldierErrorCode.SOLDIER_INACTIVE.getErrorCode();

        mvcResult = this.mockMvc.perform(put(UNIT_URI_BY_ID, id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(unitForm)))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(id.toString()));
    }

    @Test
    public void test_Unit_Put_ShouldReturn_422Response_And_ErrorCode_RES_SOLDIER_003_WhenUpdated_ById_AndNoUnitDetails() throws Exception {
        String id = unitEntity1.getId().toString();
        MvcResult mvcResult = null;
        String errorCode = SoldierErrorCode.SOLDIER_ATTRIBUTE_UNEXPECTED.getErrorCode();
        String fieldName = "form";
        String message = "not provided";

        mvcResult = this.mockMvc.perform(put(UNIT_URI_BY_ID, id)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(message));
    }

    @Test
    public void test_Unit_Put_ShouldReturn_400Response_And_ErrorCode_RES_SOLDIER_001_WhenRequested_ById_AndInvalidName() throws Exception {
        String id = unitEntity1.getId().toString();
        MvcResult mvcResult = null;
        String errorCode = SoldierErrorCode.SOLDIER_ATTRIBUTE_INVALID.getErrorCode();
        String fieldName = "name";
        unitForm.setName("");

        mvcResult = mockMvc.perform(put(UNIT_URI_BY_ID, id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(unitForm)))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
    }

    @Test
    public void test_Unit_Put_ShouldReturn_400Response_And_ErrorCode_RES_SOLDIER_001_WhenRequested_ById_AndInvalidDescription() throws Exception {
        String id = unitEntity1.getId().toString();
        MvcResult mvcResult = null;
        String errorCode = SoldierErrorCode.SOLDIER_ATTRIBUTE_INVALID.getErrorCode();
        String fieldName = "description";
        unitForm.setDescription("");

        mvcResult = mockMvc.perform(put(UNIT_URI_BY_ID, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(unitForm)))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
    }

    @Test
    public void test_Unit_Put_ShouldReturn_422Response_And_ErrorCode_RES_SOLDIER_003_WhenUpdated_ById_AndEmptyUnitDetails() throws Exception {
        String id = unitEntity1.getId().toString();
        MvcResult mvcResult = null;
        String errorCode = SoldierErrorCode.SOLDIER_ATTRIBUTE_UNEXPECTED.getErrorCode();
        String fieldName = "form";
        String message = "fields are expected with new values";

        mvcResult = this.mockMvc.perform(put(UNIT_URI_BY_ID, id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(new UnitForm())))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(message));
    }

    @Test
    public void test_Unit_Patch_ShouldReturn_204Response_And_NoResponseBody_WhenUpdated_ById_AndUnitDetails() throws Exception {
        String id = unitEntity4.getId().toString();
        MvcResult mvcResult = null;

        mvcResult = this.mockMvc.perform(patch(UNIT_URI_BY_ID, id)
                .contentType(MEDIA_TYPE_APPLICATION_JSON_PATCH)
                .content(om.writeValueAsString(patches)))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals("", mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void test_Unit_Patch_ShouldReturn_400Response_And_ErrorCode_TOAB_COMMON_001_WhenUpdated_ByEmptyId_AndUnitDetails() throws Exception {
        MvcResult mvcResult = null;
        String errorCode = SoldierErrorCode.SOLDIER_ATTRIBUTE_INVALID.getErrorCode();
        String fieldName = "id";

        mvcResult = this.mockMvc.perform(patch(UNIT_URI_BY_ID, " ")
                .contentType(MEDIA_TYPE_APPLICATION_JSON_PATCH)
                .content(om.writeValueAsString(patches)))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
    }

    @Test
    public void test_Unit_Patch_ShouldReturn_400Response_And_ErrorCode_RES_SOLDIER_002_WhenUpdated_ByInvalidId_AndUnitDetails() throws Exception {
        String id = "r";
        MvcResult mvcResult = null;
        String errorCode = SoldierErrorCode.SOLDIER_ATTRIBUTE_INVALID.getErrorCode();
        String fieldName = "id";

        mvcResult = this.mockMvc.perform(patch(UNIT_URI_BY_ID, id)
                .contentType(MEDIA_TYPE_APPLICATION_JSON_PATCH)
                .content(om.writeValueAsString(patches)))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(id));
    }

    @Test
    public void test_Unit_Patch_ShouldReturn_404Response_And_ErrorCode_RES_SOLDIER_002_WhenUpdated_ByAbsentId_AndUnitDetails() throws Exception {
        String id = "5";
        MvcResult mvcResult = null;
        String errorCode = SoldierErrorCode.SOLDIER_NOT_FOUND.getErrorCode();
        String fieldName = "id";

        mvcResult = this.mockMvc.perform(patch(UNIT_URI_BY_ID, id)
                .contentType(MEDIA_TYPE_APPLICATION_JSON_PATCH)
                .content(om.writeValueAsString(patches)))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(id.toString()));
    }

    @Test
    public void test_Unit_Patch_ShouldReturn_422Response_And_ErrorCode_RES_SOLDIER_003_WhenUpdated_ById_AndNoUnitDetails() throws Exception {
        String id = unitEntity1.getId().toString();
        MvcResult mvcResult = null;
        String errorCode = SoldierErrorCode.SOLDIER_ATTRIBUTE_UNEXPECTED.getErrorCode();
        String fieldName = "patch";
        String message = "not provided";

        mvcResult = this.mockMvc.perform(patch(UNIT_URI_BY_ID, id))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(message));
    }

    @Test
    public void test_Unit_Patch_ShouldReturn_400Response_And_ErrorCode_RES_SOLDIER_001_WhenRequested_ById_AndInvalidActive() throws Exception {
        String id = unitEntity1.getId().toString();
        MvcResult mvcResult = null;
        String errorCode = SoldierErrorCode.SOLDIER_ATTRIBUTE_INVALID.getErrorCode();
        String fieldName = "active";
        patches = Arrays.asList(
                new PatchOperationForm("replace", "/active", "x"));

        mvcResult = mockMvc.perform(patch(UNIT_URI_BY_ID, id)
                .contentType(MEDIA_TYPE_APPLICATION_JSON_PATCH)
                .content(om.writeValueAsString(patches)))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));

    }

    @Test
    public void test_Unit_Patch_ShouldReturn_400Response_And_ErrorCode_TOAB_COMMON_001_WhenRequested_ById_AndInvalidName() throws Exception {
        String id = unitEntity1.getId().toString();
        MvcResult mvcResult = null;
        String errorCode = TOABErrorCode.PATCH_ATTRIBUTE_INVALID.getErrorCode();
        String fieldName = "value";
        patches = Arrays.asList(
                new PatchOperationForm("replace", "/name", " "));

        mvcResult = mockMvc.perform(patch(UNIT_URI_BY_ID, id)
                .contentType(MEDIA_TYPE_APPLICATION_JSON_PATCH)
                .content(om.writeValueAsString(patches)))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));

    }

    @Test
    public void test_Unit_Patch_ShouldReturn_400Response_And_ErrorCode_RES_SOLDIER_001_WhenRequested_ById_AndInvalidDefinitionOfUnitAttribute() throws Exception {
        String id = unitEntity1.getId().toString();
        MvcResult mvcResult = null;
        String errorCode = SoldierErrorCode.SOLDIER_ATTRIBUTE_INVALID.getErrorCode();
        String fieldName = "path";
        patches = Arrays.asList(
                new PatchOperationForm("replace", "/x", "x"));

        mvcResult = mockMvc.perform(patch(UNIT_URI_BY_ID, id)
                .contentType(MEDIA_TYPE_APPLICATION_JSON_PATCH)
                .content(om.writeValueAsString(patches)))
                .andDo(print())
                .andReturn();

        Assertions.assertNotNull(mvcResult);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(errorCode, om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getCode());
        Assertions.assertTrue(om.readValue(mvcResult.getResponse().getContentAsString(), ErrorVo.class).getMessage().contains(fieldName));

    }

}
