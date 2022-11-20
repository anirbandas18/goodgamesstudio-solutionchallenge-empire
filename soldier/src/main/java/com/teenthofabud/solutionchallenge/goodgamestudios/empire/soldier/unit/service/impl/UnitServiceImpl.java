package com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.teenthofabud.core.common.constant.TOABBaseMessageTemplate;
import com.teenthofabud.core.common.constant.TOABCascadeLevel;
import com.teenthofabud.core.common.data.dto.TOABRequestContextHolder;
import com.teenthofabud.core.common.data.form.PatchOperationForm;
import com.teenthofabud.core.common.error.TOABBaseException;
import com.teenthofabud.core.common.error.TOABSystemException;
import com.teenthofabud.core.common.service.TOABBaseService;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.error.SoldierErrorCode;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.converter.UnitDto2EntityConverter;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.converter.UnitForm2EntityConverter;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.data.*;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.mapper.UnitEntitySelfMapper;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.mapper.UnitForm2EntityMapper;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.repository.UnitRepository;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.service.UnitService;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.validator.UnitDtoValidator;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.validator.UnitFormRelaxedValidator;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.validator.UnitFormValidator;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.utils.SoldierServiceHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.Errors;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Component
@Slf4j
public class UnitServiceImpl implements UnitService {

    private static final Comparator<UnitVo> CMP_BY_NAME = (s1, s2) -> {
        return s1.getName().compareTo(s2.getName());
    };

    private UnitForm2EntityConverter form2EntityConverter;
    private UnitDto2EntityConverter dto2EntityConverter;
    private UnitForm2EntityMapper form2EntityMapper;
    private UnitEntitySelfMapper entitySelfMapper;
    private UnitFormValidator formValidator;
    private UnitFormRelaxedValidator relaxedFormValidator;
    private UnitDtoValidator dtoValidator;
    private UnitRepository repository;
    private TOABBaseService toabBaseService;
    private ObjectMapper om;
    private SoldierServiceHelper soldierServiceHelper;

    @Autowired
    public void setToabBaseService(TOABBaseService toabBaseService) {
        this.toabBaseService = toabBaseService;
    }


    @Autowired
    public void setSoldierServiceHelper(SoldierServiceHelper soldierServiceHelper) {
        this.soldierServiceHelper = soldierServiceHelper;
    }

    @Autowired
    public void setDto2EntityConverter(UnitDto2EntityConverter dto2EntityConverter) {
        this.dto2EntityConverter = dto2EntityConverter;
    }

    @Autowired
    public void setForm2EntityMapper(UnitForm2EntityMapper form2EntityMapper) {
        this.form2EntityMapper = form2EntityMapper;
    }

    @Autowired
    public void setEntitySelfMapper(UnitEntitySelfMapper entitySelfMapper) {
        this.entitySelfMapper = entitySelfMapper;
    }

    @Autowired
    public void setRelaxedFormValidator(UnitFormRelaxedValidator relaxedFormValidator) {
        this.relaxedFormValidator = relaxedFormValidator;
    }

    @Autowired
    public void setPatchUnitValidator(TOABBaseService toabBaseService) {
        this.toabBaseService = toabBaseService;
    }

    @Autowired
    public void setOm(ObjectMapper om) {
        this.om = om;
    }

    @Autowired
    public void setDtoValidator(UnitDtoValidator dtoValidator) {
        this.dtoValidator = dtoValidator;
    }

    @Autowired
    public void setForm2EntityConverter(UnitForm2EntityConverter form2EntityConverter) {
        this.form2EntityConverter = form2EntityConverter;
    }

    @Autowired
    public void setRepository(UnitRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setFormValidator(UnitFormValidator formValidator) {
        this.formValidator = formValidator;
    }

    private Long parseUnitId(String id) throws UnitException {
        Long unitId = null;
        try {
            unitId = Long.parseLong(id);
            log.debug("Parsed id {} to unit id {} in numeric format", id, unitId);
            if(unitId <= 0) {
                throw new NumberFormatException("unit id can't be zero/negative");
            }
        } catch (NumberFormatException e) {
            log.error("Unable to parse unit id", e);
            log.debug(UnitMessageTemplate.MSG_TEMPLATE_UNIT_ID_INVALID.getValue(), id);
            throw new UnitException(SoldierErrorCode.SOLDIER_ATTRIBUTE_INVALID, new Object[] { "id", id });
        }
        return unitId;
    }

    @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
    @Override
    public Set<UnitVo> retrieveAllByNaturalOrdering() {
        log.info("Requesting all UnitEntity by their natural ordering");
        List<UnitEntity> unitEntityList = repository.findAll();
        TOABRequestContextHolder.setCascadeLevelContext(TOABCascadeLevel.ONE);
        List<UnitVo> unitVoList = soldierServiceHelper.unitEntity2DetailedVo(unitEntityList);
        Set<UnitVo> naturallyOrderedSet = new TreeSet<>(CMP_BY_NAME);
        naturallyOrderedSet.addAll(unitVoList);
        log.info("{} UnitVo available", naturallyOrderedSet.size());
        TOABRequestContextHolder.clearCascadeLevelContext();
        return naturallyOrderedSet;
    }

    @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
    @Override
    public UnitVo retrieveDetailsById(String id, Optional<TOABCascadeLevel> optionalCascadeLevel) throws UnitException {
        log.info("Requesting UnitEntity by id: {}", id);
        Long unitId = parseUnitId(id);
        Optional<UnitEntity> optEntity = repository.findById(unitId);
        if(optEntity.isEmpty()) {
            log.debug("No UnitEntity found by id: {}", id);
            throw new UnitException(SoldierErrorCode.SOLDIER_NOT_FOUND, new Object[] { "id", String.valueOf(id) });
        }
        log.info("Found UnitVo by id: {}", id);
        UnitEntity entity = optEntity.get();
        TOABCascadeLevel cascadeLevel = optionalCascadeLevel.isPresent() ? optionalCascadeLevel.get() : TOABCascadeLevel.ZERO;
        TOABRequestContextHolder.setCascadeLevelContext(cascadeLevel);
        UnitVo vo = soldierServiceHelper.unitEntity2DetailedVo(entity);
        log.debug("UnitVo populated with fields cascaded to level: {}", cascadeLevel);
        TOABRequestContextHolder.clearCascadeLevelContext();
        return vo;
    }

    @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
    @Override
    public List<UnitVo> retrieveAllMatchingDetailsByCriteria(
            Optional<String> optionalName, Optional<String> optionalDescription) throws UnitException {
        if(optionalName.isEmpty() && optionalDescription.isEmpty()) {
            log.debug("No search parameters provided");
        }
        String name = optionalName.isPresent() ? optionalName.get() : "";
        String description = optionalDescription.isPresent() ? optionalDescription.get() : "";
        if(StringUtils.isEmpty(StringUtils.trimWhitespace(name)) && StringUtils.isEmpty(StringUtils.trimWhitespace(description))) {
            log.debug("All search parameters are empty");
        }
        List<UnitVo> matchedUnitList = new LinkedList<>();
        Map<String, String> providedFilters = new LinkedHashMap<>();
        UnitEntity entity = new UnitEntity();
        ExampleMatcher matcherCriteria = ExampleMatcher.matchingAll();
        if(StringUtils.hasText(StringUtils.trimWhitespace(name))) {
            log.debug("name {} is valid", name);
            providedFilters.put("name", name);
            entity.setName(name);
            matcherCriteria = matcherCriteria.withMatcher("name", match -> match.contains());
        }
        if(StringUtils.hasText(StringUtils.trimWhitespace(description))) {
            log.debug("description {} is valid", description);
            providedFilters.put("description", description);
            entity.setDescription(description);
            matcherCriteria = matcherCriteria.withMatcher("description", match -> match.contains());
        }
        if(providedFilters.isEmpty()) {
            log.debug("search parameters are not valid");
        } else {
            log.debug("search parameters {} are valid", providedFilters);
        }
        Example<UnitEntity> unitEntityExample = Example.of(entity, matcherCriteria);
        List<UnitEntity> unitEntityList = repository.findAll(unitEntityExample);
        matchedUnitList = soldierServiceHelper.unitEntity2DetailedVo(unitEntityList);
        log.info("Found {} UnitVo matching with provided parameters : {}", matchedUnitList.size(), providedFilters);
        log.info("No UnitVo available matching with provided parameters : {}", matchedUnitList.size(), providedFilters);
        return matchedUnitList;
    }

    @Transactional
    @Override
    public String createUnit(UnitForm form) throws UnitException {
        log.info("Creating new UnitEntity");

        if(form == null) {
            log.debug("UnitForm provided is null");
            throw new UnitException(SoldierErrorCode.SOLDIER_ATTRIBUTE_UNEXPECTED,
                    new Object[]{ "form", TOABBaseMessageTemplate.MSG_TEMPLATE_NOT_PROVIDED });
        }
        log.debug("Form details: {}", form);

        log.debug("Validating provided attributes of UnitForm");
        Errors err = new DirectFieldBindingResult(form, form.getClass().getSimpleName());
        formValidator.validate(form, err);
        if(err.hasErrors()) {
            log.debug("UnitForm has {} errors", err.getErrorCount());
            SoldierErrorCode ec = SoldierErrorCode.valueOf(err.getFieldError().getCode());
            log.debug("UnitForm error detail: {}", ec);
            throw new UnitException(ec, new Object[] { err.getFieldError().getField() });
        }
        log.debug("All attributes of UnitForm are valid");

        UnitEntity expectedEntity = form2EntityConverter.convert(form);

        log.debug(UnitMessageTemplate.MSG_TEMPLATE_UNIT_EXISTENCE_BY_NAME.getValue(), form.getName());
        if(repository.existsByName(expectedEntity.getName())) {
            log.debug(UnitMessageTemplate.MSG_TEMPLATE_UNIT_EXISTS_BY_NAME.getValue(), expectedEntity.getName());
            throw new UnitException(SoldierErrorCode.SOLDIER_EXISTS,
                    new Object[]{ "name", form.getName() });
        }
        log.debug(UnitMessageTemplate.MSG_TEMPLATE_UNIT_NON_EXISTENCE_BY_NAME.getValue(), expectedEntity.getName());

        log.debug("Saving {}", expectedEntity);
        UnitEntity actualEntity = repository.save(expectedEntity);
        log.debug("Saved {}", actualEntity);

        if(actualEntity == null) {
            log.debug("Unable to create {}", expectedEntity);
            throw new UnitException(SoldierErrorCode.SOLDIER_ACTION_FAILURE,
                    new Object[]{ "creation", "unable to persist UnitForm details" });
        }
        log.info("Created new UnitForm with id: {}", actualEntity.getId());
        return actualEntity.getId().toString();
    }

    @Transactional
    @Override
    public void updateUnit(String id, UnitForm form) throws UnitException {
        log.info("Updating UnitForm by id: {}", id);

        log.debug(UnitMessageTemplate.MSG_TEMPLATE_SEARCHING_FOR_UNIT_ENTITY_ID.getValue(), id);
        Long unitId = parseUnitId(id);
        Optional<UnitEntity> optActualEntity = repository.findById(unitId);
        if(optActualEntity.isEmpty()) {
            log.debug(UnitMessageTemplate.MSG_TEMPLATE_NO_UNIT_ENTITY_ID_AVAILABLE.getValue(), id);
            throw new UnitException(SoldierErrorCode.SOLDIER_NOT_FOUND, new Object[] { "id", String.valueOf(id) });
        }
        log.debug(UnitMessageTemplate.MSG_TEMPLATE_FOUND_UNIT_ENTITY_ID.getValue(), id);

        UnitEntity actualEntity = optActualEntity.get();
        if(!actualEntity.getActive()) {
            log.debug("UnitEntity is inactive with id: {}", id);
            throw new UnitException(SoldierErrorCode.SOLDIER_INACTIVE, new Object[] { String.valueOf(id) });
        }
        log.debug("UnitEntity is active with id: {}", id);

        if(form == null) {
            log.debug("UnitForm is null");
            throw new UnitException(SoldierErrorCode.SOLDIER_ATTRIBUTE_UNEXPECTED, new Object[]{ "form", TOABBaseMessageTemplate.MSG_TEMPLATE_NOT_PROVIDED });
        }
        log.debug("Form details : {}", form);

        log.debug("Validating provided attributes of UnitForm");
        Errors err = new DirectFieldBindingResult(form, form.getClass().getSimpleName());
        Boolean allEmpty = relaxedFormValidator.validateLoosely(form, err);
        if(err.hasErrors()) {
            log.debug("UnitForm has {} errors", err.getErrorCount());
            SoldierErrorCode ec = SoldierErrorCode.valueOf(err.getFieldError().getCode());
            log.debug("UnitForm error detail: {}", ec);
            throw new UnitException(ec, new Object[] { err.getFieldError().getField(), err.getFieldError().getCode() });
        } else if (!allEmpty) {
            log.debug("All attributes of UnitForm are empty");
            throw new UnitException(SoldierErrorCode.SOLDIER_ATTRIBUTE_UNEXPECTED, new Object[]{ "form", "fields are empty" });
        }
        log.debug("All attributes of UnitForm are valid");

        Optional<UnitEntity> optExpectedEntity = form2EntityMapper.compareAndMap(actualEntity, form);
        if(optExpectedEntity.isEmpty()) {
            log.debug("No new value for attributes of UnitForm");
            throw new UnitException(SoldierErrorCode.SOLDIER_ATTRIBUTE_UNEXPECTED, new Object[]{ "form", "fields are expected with new values" });
        }
        log.debug("Successfully compared and copied attributes from UnitForm to UnitEntity");

        UnitEntity expectedEntity = optExpectedEntity.get();

        log.debug(UnitMessageTemplate.MSG_TEMPLATE_UNIT_EXISTENCE_BY_NAME.getValue(), form.getName());
        if(actualEntity.getName().compareTo(expectedEntity.getName()) == 0
                || repository.existsByName(expectedEntity.getName())) {
            log.debug(UnitMessageTemplate.MSG_TEMPLATE_UNIT_EXISTS_BY_NAME.getValue(), expectedEntity.getName());
            throw new UnitException(SoldierErrorCode.SOLDIER_EXISTS,
                    new Object[]{ "name", actualEntity.getName() });
        }
        log.debug(UnitMessageTemplate.MSG_TEMPLATE_UNIT_NON_EXISTENCE_BY_NAME.getValue(), expectedEntity.getName());

        entitySelfMapper.compareAndMap(expectedEntity, actualEntity);
        log.debug("Compared and copied attributes from UnitEntity to UnitForm");
        actualEntity.setModifiedOn(LocalDateTime.now(ZoneOffset.UTC));

        log.debug("Updating: {}", actualEntity);
        actualEntity = repository.save(actualEntity);
        log.debug("Updated: {}", actualEntity);
        if(actualEntity == null) {
            log.debug("Unable to update {}", actualEntity);
            throw new UnitException(SoldierErrorCode.SOLDIER_ACTION_FAILURE,
                    new Object[]{ "update", "unable to persist currency unit details" });
        }
        log.info("Updated existing UnitEntity with id: {}", actualEntity.getId());
    }

    @Transactional
    @Override
    public void deleteUnit(String id) throws UnitException {
        log.info("Soft deleting UnitEntity by id: {}", id);

        log.debug(UnitMessageTemplate.MSG_TEMPLATE_SEARCHING_FOR_UNIT_ENTITY_ID.getValue(), id);
        Long unitId = parseUnitId(id);
        Optional<UnitEntity> optEntity = repository.findById(unitId);
        if(optEntity.isEmpty()) {
            log.debug(UnitMessageTemplate.MSG_TEMPLATE_NO_UNIT_ENTITY_ID_AVAILABLE.getValue(), id);
            throw new UnitException(SoldierErrorCode.SOLDIER_NOT_FOUND, new Object[] { "id", String.valueOf(id) });
        }
        log.debug(UnitMessageTemplate.MSG_TEMPLATE_FOUND_UNIT_ENTITY_ID.getValue(), id);

        UnitEntity actualEntity = optEntity.get();
        if(!actualEntity.getActive()) {
            log.debug("UnitEntity is inactive with id: {}", id);
            throw new UnitException(SoldierErrorCode.SOLDIER_INACTIVE, new Object[] { String.valueOf(id) });
        }
        log.debug("UnitEntity is active with id: {}", id);

        actualEntity.setActive(Boolean.FALSE);
        actualEntity.setModifiedOn(LocalDateTime.now(ZoneOffset.UTC));
        log.debug("Soft deleting: {}", actualEntity);
        UnitEntity expectedEntity = repository.save(actualEntity);
        log.debug("Soft deleted: {}", expectedEntity);
        if(expectedEntity == null) {
            log.debug("Unable to soft delete {}", actualEntity);
            throw new UnitException(SoldierErrorCode.SOLDIER_ACTION_FAILURE,
                    new Object[]{ "deletion", "unable to soft delete current unit details with id:" + id });
        }

        log.info("Soft deleted existing UnitEntity with id: {}", actualEntity.getId());
    }

    @Transactional
    @Override
    public void applyPatchOnUnit(String id, List<PatchOperationForm> patches) throws UnitException {
        log.info("Patching UnitEntity by id: {}", id);

        log.debug(UnitMessageTemplate.MSG_TEMPLATE_SEARCHING_FOR_UNIT_ENTITY_ID.getValue(), id);
        Long unitId = parseUnitId(id);
        Optional<UnitEntity> optActualEntity = repository.findById(unitId);
        if(optActualEntity.isEmpty()) {
            log.debug(UnitMessageTemplate.MSG_TEMPLATE_NO_UNIT_ENTITY_ID_AVAILABLE.getValue(), id);
            throw new UnitException(SoldierErrorCode.SOLDIER_NOT_FOUND, new Object[] { "id", String.valueOf(id) });
        }
        log.debug(UnitMessageTemplate.MSG_TEMPLATE_FOUND_UNIT_ENTITY_ID.getValue(), id);

        UnitEntity actualEntity = optActualEntity.get();
        if(patches == null || (patches != null && patches.isEmpty())) {
            log.debug("Unit patch list not provided");
            throw new UnitException(SoldierErrorCode.SOLDIER_ATTRIBUTE_UNEXPECTED, new Object[]{ "patch", TOABBaseMessageTemplate.MSG_TEMPLATE_NOT_PROVIDED });
        }
        log.debug("Unit patch list has {} items", patches.size());


        log.debug("Validating patch list items for Unit");
        try {
            toabBaseService.validatePatches(patches, SoldierErrorCode.SOLDIER_EXISTS.getDomain() + ":LOV");
            log.debug("All Unit patch list items are valid");
        } catch (TOABSystemException e) {
            log.debug("Some of the Unit patch item are invalid");
            throw new UnitException(e.getError(), e.getParameters());
        }
        log.debug("Validated patch list items for Unit");


        log.debug("Patching list items to UnitDto");
        UnitDto patchedUnitForm = new UnitDto();
        try {
            log.debug("Preparing patch list items for Unit");
            JsonNode unitDtoTree = om.convertValue(patches, JsonNode.class);
            JsonPatch unitPatch = JsonPatch.fromJson(unitDtoTree);
            log.debug("Prepared patch list items for Unit");
            JsonNode blankUnitDtoTree = om.convertValue(new UnitDto(), JsonNode.class);
            JsonNode patchedUnitFormTree = unitPatch.apply(blankUnitDtoTree);
            log.debug("Applying patch list items to UnitDto");
            patchedUnitForm = om.treeToValue(patchedUnitFormTree, UnitDto.class);
            log.debug("Applied patch list items to UnitDto");
        } catch (JsonPatchException e) {
            log.debug("Failed to patch list items to UnitDto: {}", e);
            UnitException ex = null;
            if(e.getMessage().contains("no such path in target")) {
                log.debug("Invalid patch attribute in UnitDto");
                ex = new UnitException(SoldierErrorCode.SOLDIER_ATTRIBUTE_INVALID, new Object[]{ "path" });
            } else {
                ex = new UnitException(SoldierErrorCode.SOLDIER_ACTION_FAILURE, new Object[]{ "patching", "internal error: " + e.getMessage() });
            }
            throw ex;
        } catch (IOException e) {
            log.debug("Failed to patch list items to UnitDto: {}", e);
            throw new UnitException(SoldierErrorCode.SOLDIER_ACTION_FAILURE, new Object[]{ "patching", "internal error: " + e.getMessage() });
        }
        log.debug("Successfully to patch list items to UnitDto");

        log.debug("Validating patched UnitDto");
        Errors err = new DirectFieldBindingResult(patchedUnitForm, patchedUnitForm.getClass().getSimpleName());
        dtoValidator.validate(patchedUnitForm, err);
        if(err.hasErrors()) {
            log.debug("Patched UnitDto has {} errors", err.getErrorCount());
            SoldierErrorCode ec = SoldierErrorCode.valueOf(err.getFieldError().getCode());
            log.debug("Patched UnitDto error detail: {}", ec);
            throw new UnitException(ec, new Object[] { err.getFieldError().getField() });
        }
        log.debug("All attributes of patched UnitDto are valid");

        log.debug(UnitMessageTemplate.MSG_TEMPLATE_UNIT_EXISTENCE_BY_NAME.getValue(), patchedUnitForm.getName().get());
        if(actualEntity.getName().compareTo(patchedUnitForm.getName().get()) == 0
                || repository.existsByName(patchedUnitForm.getName().get())) {
            log.debug(UnitMessageTemplate.MSG_TEMPLATE_UNIT_EXISTS_BY_NAME.getValue(), patchedUnitForm.getName().get());
            throw new UnitException(SoldierErrorCode.SOLDIER_EXISTS,
                    new Object[]{ "name", patchedUnitForm.getName().get() });
        }
        log.debug(UnitMessageTemplate.MSG_TEMPLATE_UNIT_NON_EXISTENCE_BY_NAME.getValue(), patchedUnitForm.getName().get());


        log.debug("Comparatively copying patched attributes from UnitDto to UnitEntity");
        try {
            dto2EntityConverter.compareAndMap(patchedUnitForm, actualEntity);
        } catch (TOABBaseException e) {
            throw (UnitException) e;
        }
        log.debug("Comparatively copied patched attributes from UnitDto to UnitEntity");

        log.debug("Saving patched UnitEntity: {}", actualEntity);
        actualEntity = repository.save(actualEntity);
        log.debug("Saved patched UnitEntity: {}", actualEntity);
        if(actualEntity == null) {
            log.debug("Unable to patch delete UnitEntity with id:{}", id);
            throw new UnitException(SoldierErrorCode.SOLDIER_ACTION_FAILURE,
                    new Object[]{ "patching", "unable to patch currency unit details with id:" + id });
        }
        log.info("Patched UnitEntity with id:{}", id);
    }
}