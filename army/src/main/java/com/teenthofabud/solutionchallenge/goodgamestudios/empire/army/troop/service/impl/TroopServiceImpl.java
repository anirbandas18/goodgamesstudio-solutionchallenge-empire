package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.service.impl;

import brave.Tracer;
import com.teenthofabud.core.common.constant.TOABBaseMessageTemplate;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.error.ArmyErrorCode;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.integration.soldier.data.UnitVo;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.integration.soldier.proxy.SoldierServiceClient;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data.*;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.repository.TroopRepository;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.service.TroopService;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.validator.TroopFormValidator;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.utils.ArmyServiceHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.Errors;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TroopServiceImpl implements TroopService {

    private static final Comparator<TroopVo> CMP_BY_QUANTITY_AND_UNIT_ID = (s1, s2) -> {
        return Comparator.comparing(TroopVo::getQuantity)
                .thenComparing(TroopVo::getUnit)
                .compare(s1, s2);
    };

    private TroopRepository repository;
    private SoldierServiceClient soldierServiceClient;
    private TroopFormValidator formValidator;
    private Tracer tracer;
    private ArmyServiceHelper armyServiceHelper;

    @Autowired
    public void setArmyServiceHelper(ArmyServiceHelper armyServiceHelper) {
        this.armyServiceHelper = armyServiceHelper;
    }

    @Autowired
    public void setSoldierServiceClient(SoldierServiceClient soldierServiceClient) {
        this.soldierServiceClient = soldierServiceClient;
    }

    @Autowired
    public void setFormValidator(TroopFormValidator formValidator) {
        this.formValidator = formValidator;
    }

    @Autowired
    public void setTracer(Tracer tracer) {
        this.tracer = tracer;
    }

    @Autowired
    public void setRepository(TroopRepository repository) {
        this.repository = repository;
    }

    /*private TOABBaseService toabBaseService;
    private ObjectMapper om;
    private TroopForm2EntityConverter form2EntityConverter;
    private TroopDto2EntityConverter dto2EntityConverter;
    private TroopForm2EntityMapper form2EntityMapper;
    private TroopEntitySelfMapper entitySelfMapper;
    private TroopFormRelaxedValidator relaxedFormValidator;
    private TroopDtoValidator dtoValidator;*/


    /*@Autowired
    public void setToabBaseService(TOABBaseService toabBaseService) {
        this.toabBaseService = toabBaseService;
    }

    @Autowired
    public void setDto2EntityConverter(TroopDto2EntityConverter dto2EntityConverter) {
        this.dto2EntityConverter = dto2EntityConverter;
    }

    @Autowired
    public void setForm2EntityMapper(TroopForm2EntityMapper form2EntityMapper) {
        this.form2EntityMapper = form2EntityMapper;
    }

    @Autowired
    public void setEntitySelfMapper(TroopEntitySelfMapper entitySelfMapper) {
        this.entitySelfMapper = entitySelfMapper;
    }

    @Autowired
    public void setRelaxedFormValidator(TroopFormRelaxedValidator relaxedFormValidator) {
        this.relaxedFormValidator = relaxedFormValidator;
    }

    @Autowired
    public void setPatchTroopValidator(TOABBaseService toabBaseService) {
        this.toabBaseService = toabBaseService;
    }

    @Autowired
    public void setOm(ObjectMapper om) {
        this.om = om;
    }

    @Autowired
    public void setDtoValidator(TroopDtoValidator dtoValidator) {
        this.dtoValidator = dtoValidator;
    }

    @Autowired
    public void setForm2EntityConverter(TroopForm2EntityConverter form2EntityConverter) {
        this.form2EntityConverter = form2EntityConverter;
    }


    */

    /*private Long parseTroopId(String id) throws TroopException {
        Long troopId = null;
        try {
            troopId = Long.parseLong(id);
            log.debug("Parsed id {} to troop id {} in numeric format", id, troopId);
            if(troopId <= 0) {
                throw new NumberFormatException("troop id can't be zero/negative");
            }
        } catch (NumberFormatException e) {
            log.error("Unable to parse troop id", e);
            log.debug(TroopMessageTemplate.MSG_TEMPLATE_TROOP_ID_INVALID.getValue(), id);
            throw new TroopException(ArmyErrorCode.ARMY_ATTRIBUTE_INVALID, new Object[] { "id", id });
        }
        return troopId;
    }

    @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
    @Override
    public Set<TroopVo> retrieveAllByNaturalOrdering() {
        log.info("Requesting all TroopEntity by their natural ordering");
        List<TroopEntity> troopEntityList = repository.findAll();
        List<TroopVo> troopVoList = armyServiceHelper.troopEntity2DetailedVo(troopEntityList);
        Collections.sort(troopVoList, CMP_BY_NAME_AND_DESCRIPTION);
        Set<TroopVo> naturallyOrderedSet = new LinkedHashSet<>();
        naturallyOrderedSet.addAll(troopVoList);
        log.info("{} TroopVo available", naturallyOrderedSet.size());
        return naturallyOrderedSet;
    }

    @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
    @Override
    public TroopVo retrieveDetailsById(String id, Optional<TOABCascadeLevel> optionalCascadeLevel) throws TroopException {
        log.info("Requesting TroopEntity by id: {}", id);
        Long troopId = parseTroopId(id);
        Optional<TroopEntity> optEntity = repository.findById(troopId);
        if(optEntity.isEmpty()) {
            log.debug("No TroopEntity found by id: {}", id);
            throw new TroopException(ArmyErrorCode.ARMY_NOT_FOUND, new Object[] { "id", String.valueOf(id) });
        }
        log.info("Found TroopVo by id: {}", id);
        TroopEntity entity = optEntity.get();
        TOABCascadeLevel cascadeLevel = optionalCascadeLevel.isPresent() ? optionalCascadeLevel.get() : TOABCascadeLevel.ZERO;
        TOABRequestContextHolder.setCascadeLevelContext(cascadeLevel);
        TroopVo vo = armyServiceHelper.troopEntity2DetailedVo(entity);
        log.debug("TroopVo populated with fields cascaded to level: {}", cascadeLevel);
        TOABRequestContextHolder.clearCascadeLevelContext();
        return vo;
    }

    @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
    @Override
    public List<TroopVo> retrieveAllMatchingDetailsByCriteria(Optional<String> optionalName, Optional<String> optionalDescription) throws TroopException {
        if(optionalName.isEmpty() && optionalDescription.isEmpty()) {
            log.debug("No search parameters provided");
        }
        String name = optionalName.isPresent() ? optionalName.get() : "";
        String description = optionalDescription.isPresent() ? optionalDescription.get() : "";
        if(StringUtils.isEmpty(StringUtils.trimWhitespace(name)) && StringUtils.isEmpty(StringUtils.trimWhitespace(description))) {
            log.debug("All search parameters are empty");
        }
        List<TroopVo> matchedTroopList = new LinkedList<>();
        Map<String, String> providedFilters = new LinkedHashMap<>();
        TroopEntity entity = new TroopEntity();
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
        Example<TroopEntity> troopEntityExample = Example.of(entity, matcherCriteria);
        List<TroopEntity> troopEntityList = repository.findAll(troopEntityExample);
        matchedTroopList = armyServiceHelper.troopEntity2DetailedVo(troopEntityList);
        log.info("Found {} TroopVo matching with provided parameters : {}", matchedTroopList.size(), providedFilters);
        return matchedTroopList;
    }

    @Transactional
    @Override
    public String createTroop(TroopForm form) throws TroopException {
        log.info("Creating new TroopEntity");

        if(form == null) {
            log.debug("TroopForm provided is null");
            throw new TroopException(ArmyErrorCode.ARMY_ATTRIBUTE_UNEXPECTED,
                    new Object[]{ "form", TOABBaseMessageTemplate.MSG_TEMPLATE_NOT_PROVIDED });
        }
        log.debug("Form details: {}", form);

        log.debug("Validating provided attributes of TroopForm");
        Errors err = new DirectFieldBindingResult(form, form.getClass().getSimpleName());
        formValidator.validate(form, err);
        if(err.hasErrors()) {
            log.debug("TroopForm has {} errors", err.getErrorCount());
            ArmyErrorCode ec = ArmyErrorCode.valueOf(err.getFieldError().getCode());
            log.debug("TroopForm error detail: {}", ec);
            throw new TroopException(ec, new Object[] { err.getFieldError().getField() });
        }
        log.debug("All attributes of TroopForm are valid");

        TroopEntity expectedEntity = form2EntityConverter.convert(form);

        log.debug(TroopMessageTemplate.MSG_TEMPLATE_TROOP_EXISTENCE_BY_NAME.getValue(), form.getName());
        if(repository.existsByName(expectedEntity.getName())) {
            log.debug(TroopMessageTemplate.MSG_TEMPLATE_TROOP_EXISTS_BY_NAME.getValue(), expectedEntity.getName());
            throw new TroopException(ArmyErrorCode.ARMY_EXISTS,
                    new Object[]{ "name", form.getName() });
        }
        log.debug(TroopMessageTemplate.MSG_TEMPLATE_TROOP_NON_EXISTENCE_BY_NAME.getValue(), expectedEntity.getName());

        log.debug("Saving {}", expectedEntity);
        TroopEntity actualEntity = repository.save(expectedEntity);
        log.debug("Saved {}", actualEntity);

        if(actualEntity == null) {
            log.debug("Unable to create {}", expectedEntity);
            throw new TroopException(ArmyErrorCode.ARMY_ACTION_FAILURE,
                    new Object[]{ "creation", "unable to persist TroopForm details" });
        }
        log.info("Created new TroopForm with id: {}", actualEntity.getId());
        return actualEntity.getId().toString();
    }

    @Transactional
    @Override
    public void updateTroop(String id, TroopForm form) throws TroopException {
        log.info("Updating TroopForm by id: {}", id);

        log.debug(TroopMessageTemplate.MSG_TEMPLATE_SEARCHING_FOR_TROOP_ENTITY_ID.getValue(), id);
        Long troopId = parseTroopId(id);
        Optional<TroopEntity> optActualEntity = repository.findById(troopId);
        if(optActualEntity.isEmpty()) {
            log.debug(TroopMessageTemplate.MSG_TEMPLATE_NO_TROOP_ENTITY_ID_AVAILABLE.getValue(), id);
            throw new TroopException(ArmyErrorCode.ARMY_NOT_FOUND, new Object[] { "id", String.valueOf(id) });
        }
        log.debug(TroopMessageTemplate.MSG_TEMPLATE_FOUND_TROOP_ENTITY_ID.getValue(), id);

        TroopEntity actualEntity = optActualEntity.get();
        if(!actualEntity.getActive()) {
            log.debug("TroopEntity is inactive with id: {}", id);
            throw new TroopException(ArmyErrorCode.ARMY_INACTIVE, new Object[] { String.valueOf(id) });
        }
        log.debug("TroopEntity is active with id: {}", id);

        if(form == null) {
            log.debug("TroopForm is null");
            throw new TroopException(ArmyErrorCode.ARMY_ATTRIBUTE_UNEXPECTED, new Object[]{ "form", TOABBaseMessageTemplate.MSG_TEMPLATE_NOT_PROVIDED });
        }
        log.debug("Form details : {}", form);

        log.debug("Validating provided attributes of TroopForm");
        Errors err = new DirectFieldBindingResult(form, form.getClass().getSimpleName());
        Boolean allEmpty = relaxedFormValidator.validateLoosely(form, err);
        if(err.hasErrors()) {
            log.debug("TroopForm has {} errors", err.getErrorCount());
            ArmyErrorCode ec = ArmyErrorCode.valueOf(err.getFieldError().getCode());
            log.debug("TroopForm error detail: {}", ec);
            throw new TroopException(ec, new Object[] { err.getFieldError().getField(), err.getFieldError().getCode() });
        } else if (!allEmpty) {
            log.debug("All attributes of TroopForm are empty");
            throw new TroopException(ArmyErrorCode.ARMY_ATTRIBUTE_UNEXPECTED, new Object[]{ "form", "fields are empty" });
        }
        log.debug("All attributes of TroopForm are valid");

        Optional<TroopEntity> optExpectedEntity = form2EntityMapper.compareAndMap(actualEntity, form);
        if(optExpectedEntity.isEmpty()) {
            log.debug("No new value for attributes of TroopForm");
            throw new TroopException(ArmyErrorCode.ARMY_ATTRIBUTE_UNEXPECTED, new Object[]{ "form", "fields are expected with new values" });
        }
        log.debug("Successfully compared and copied attributes from TroopForm to TroopEntity");

        TroopEntity expectedEntity = optExpectedEntity.get();

        log.debug(TroopMessageTemplate.MSG_TEMPLATE_TROOP_EXISTENCE_BY_NAME.getValue(), form.getName());
        if(actualEntity.getName().compareTo(expectedEntity.getName()) == 0
                || repository.existsByName(expectedEntity.getName())) {
            log.debug(TroopMessageTemplate.MSG_TEMPLATE_TROOP_EXISTS_BY_NAME.getValue(), expectedEntity.getName());
            throw new TroopException(ArmyErrorCode.ARMY_EXISTS,
                    new Object[]{ "name", actualEntity.getName() });
        }
        log.debug(TroopMessageTemplate.MSG_TEMPLATE_TROOP_NON_EXISTENCE_BY_NAME.getValue(), expectedEntity.getName());

        entitySelfMapper.compareAndMap(expectedEntity, actualEntity);
        log.debug("Compared and copied attributes from TroopEntity to TroopForm");
        actualEntity.setModifiedOn(LocalDateTime.now(ZoneOffset.UTC));

        log.debug("Updating: {}", actualEntity);
        actualEntity = repository.save(actualEntity);
        log.debug("Updated: {}", actualEntity);
        if(actualEntity == null) {
            log.debug("Unable to update {}", actualEntity);
            throw new TroopException(ArmyErrorCode.ARMY_ACTION_FAILURE,
                    new Object[]{ "update", "unable to persist currency troop details" });
        }
        log.info("Updated existing TroopEntity with id: {}", actualEntity.getId());
    }

    @Transactional
    @Override
    public void deleteTroop(String id) throws TroopException {
        log.info("Soft deleting TroopEntity by id: {}", id);

        log.debug(TroopMessageTemplate.MSG_TEMPLATE_SEARCHING_FOR_TROOP_ENTITY_ID.getValue(), id);
        Long troopId = parseTroopId(id);
        Optional<TroopEntity> optEntity = repository.findById(troopId);
        if(optEntity.isEmpty()) {
            log.debug(TroopMessageTemplate.MSG_TEMPLATE_NO_TROOP_ENTITY_ID_AVAILABLE.getValue(), id);
            throw new TroopException(ArmyErrorCode.ARMY_NOT_FOUND, new Object[] { "id", String.valueOf(id) });
        }
        log.debug(TroopMessageTemplate.MSG_TEMPLATE_FOUND_TROOP_ENTITY_ID.getValue(), id);

        TroopEntity actualEntity = optEntity.get();
        if(!actualEntity.getActive()) {
            log.debug("TroopEntity is inactive with id: {}", id);
            throw new TroopException(ArmyErrorCode.ARMY_INACTIVE, new Object[] { String.valueOf(id) });
        }
        log.debug("TroopEntity is active with id: {}", id);

        actualEntity.setActive(Boolean.FALSE);
        actualEntity.setModifiedOn(LocalDateTime.now(ZoneOffset.UTC));
        log.debug("Soft deleting: {}", actualEntity);
        TroopEntity expectedEntity = repository.save(actualEntity);
        log.debug("Soft deleted: {}", expectedEntity);
        if(expectedEntity == null) {
            log.debug("Unable to soft delete {}", actualEntity);
            throw new TroopException(ArmyErrorCode.ARMY_ACTION_FAILURE,
                    new Object[]{ "deletion", "unable to soft delete current troop details with id:" + id });
        }

        log.info("Soft deleted existing TroopEntity with id: {}", actualEntity.getId());
    }

    @Transactional
    @Override
    public void applyPatchOnTroop(String id, List<PatchOperationForm> patches) throws TroopException {
        log.info("Patching TroopEntity by id: {}", id);

        log.debug(TroopMessageTemplate.MSG_TEMPLATE_SEARCHING_FOR_TROOP_ENTITY_ID.getValue(), id);
        Long troopId = parseTroopId(id);
        Optional<TroopEntity> optActualEntity = repository.findById(troopId);
        if(optActualEntity.isEmpty()) {
            log.debug(TroopMessageTemplate.MSG_TEMPLATE_NO_TROOP_ENTITY_ID_AVAILABLE.getValue(), id);
            throw new TroopException(ArmyErrorCode.ARMY_NOT_FOUND, new Object[] { "id", String.valueOf(id) });
        }
        log.debug(TroopMessageTemplate.MSG_TEMPLATE_FOUND_TROOP_ENTITY_ID.getValue(), id);

        TroopEntity actualEntity = optActualEntity.get();
        if(patches == null || (patches != null && patches.isEmpty())) {
            log.debug("Troop patch list not provided");
            throw new TroopException(ArmyErrorCode.ARMY_ATTRIBUTE_UNEXPECTED, new Object[]{ "patch", TOABBaseMessageTemplate.MSG_TEMPLATE_NOT_PROVIDED });
        }
        log.debug("Troop patch list has {} items", patches.size());


        log.debug("Validating patch list items for Troop");
        try {
            toabBaseService.validatePatches(patches, ArmyErrorCode.ARMY_EXISTS.getDomain() + ":LOV");
            log.debug("All Troop patch list items are valid");
        } catch (TOABSystemException e) {
            log.debug("Some of the Troop patch item are invalid");
            throw new TroopException(e.getError(), e.getParameters());
        }
        log.debug("Validated patch list items for Troop");


        log.debug("Patching list items to TroopDto");
        TroopDto patchedTroopForm = new TroopDto();
        try {
            log.debug("Preparing patch list items for Troop");
            JsonNode troopDtoTree = om.convertValue(patches, JsonNode.class);
            JsonPatch troopPatch = JsonPatch.fromJson(troopDtoTree);
            log.debug("Prepared patch list items for Troop");
            JsonNode blankTroopDtoTree = om.convertValue(new TroopDto(), JsonNode.class);
            JsonNode patchedTroopFormTree = troopPatch.apply(blankTroopDtoTree);
            log.debug("Applying patch list items to TroopDto");
            patchedTroopForm = om.treeToValue(patchedTroopFormTree, TroopDto.class);
            log.debug("Applied patch list items to TroopDto");
        } catch (JsonPatchException e) {
            log.debug("Failed to patch list items to TroopDto: {}", e);
            TroopException ex = null;
            if(e.getMessage().contains("no such path in target")) {
                log.debug("Invalid patch attribute in TroopDto");
                ex = new TroopException(ArmyErrorCode.ARMY_ATTRIBUTE_INVALID, new Object[]{ "path" });
            } else {
                ex = new TroopException(ArmyErrorCode.ARMY_ACTION_FAILURE, new Object[]{ "patching", "internal error: " + e.getMessage() });
            }
            throw ex;
        } catch (IOException e) {
            log.debug("Failed to patch list items to TroopDto: {}", e);
            throw new TroopException(ArmyErrorCode.ARMY_ACTION_FAILURE, new Object[]{ "patching", "internal error: " + e.getMessage() });
        }
        log.debug("Successfully to patch list items to TroopDto");

        log.debug("Validating patched TroopDto");
        Errors err = new DirectFieldBindingResult(patchedTroopForm, patchedTroopForm.getClass().getSimpleName());
        dtoValidator.validate(patchedTroopForm, err);
        if(err.hasErrors()) {
            log.debug("Patched TroopDto has {} errors", err.getErrorCount());
            ArmyErrorCode ec = ArmyErrorCode.valueOf(err.getFieldError().getCode());
            log.debug("Patched TroopDto error detail: {}", ec);
            throw new TroopException(ec, new Object[] { err.getFieldError().getField() });
        }
        log.debug("All attributes of patched TroopDto are valid");

        log.debug(TroopMessageTemplate.MSG_TEMPLATE_TROOP_EXISTENCE_BY_NAME.getValue(), patchedTroopForm.getName().get());
        if(actualEntity.getName().compareTo(patchedTroopForm.getName().get()) == 0
                || repository.existsByName(patchedTroopForm.getName().get())) {
            log.debug(TroopMessageTemplate.MSG_TEMPLATE_TROOP_EXISTS_BY_NAME.getValue(), patchedTroopForm.getName().get());
            throw new TroopException(ArmyErrorCode.ARMY_EXISTS,
                    new Object[]{ "name", patchedTroopForm.getName().get() });
        }
        log.debug(TroopMessageTemplate.MSG_TEMPLATE_TROOP_NON_EXISTENCE_BY_NAME.getValue(), patchedTroopForm.getName().get());


        log.debug("Comparatively copying patched attributes from TroopDto to TroopEntity");
        try {
            dto2EntityConverter.compareAndMap(patchedTroopForm, actualEntity);
        } catch (TOABBaseException e) {
            throw (TroopException) e;
        }
        log.debug("Comparatively copied patched attributes from TroopDto to TroopEntity");

        log.debug("Saving patched TroopEntity: {}", actualEntity);
        actualEntity = repository.save(actualEntity);
        log.debug("Saved patched TroopEntity: {}", actualEntity);
        if(actualEntity == null) {
            log.debug("Unable to patch delete TroopEntity with id:{}", id);
            throw new TroopException(ArmyErrorCode.ARMY_ACTION_FAILURE,
                    new Object[]{ "patching", "unable to patch currency troop details with id:" + id });
        }
        log.info("Patched TroopEntity with id:{}", id);
    }*/

    /**
     * randomly arrange units among itself and assign them to natural ordering of divisions in 1:1 parallel operation
     * @param divisions
     * @param unitVoList
     * @return assignment of units to troop divisions in natural order of divisions
     */
    /*private List<TroopVo> makeTroop(List<Integer> divisions, List<UnitVo> unitVoList) {
        Collections.shuffle(unitVoList);
        List<TroopVo> troops = new ArrayList<>(divisions.size());
        Iterator<Integer> divisionItr = divisions.iterator();
        Iterator<UnitVo> unitVoItr = unitVoList.iterator();
        while(divisionItr.hasNext() && unitVoItr.hasNext()) {
            Integer quantity = divisionItr.next();
            UnitVo unitVo = unitVoItr.next();
            String txId = tracer.currentSpan().context().traceIdString();
            TroopEntity troopEntity = new TroopEntity(txId, unitVo.getId(), quantity);
            troopEntity = repository.save(troopEntity);
            log.debug("For division: {} and unit: {} @ transaction: {}, made: {}", quantity, unitVo.getName(), txId, troopEntity);
            TroopVo troopVo = new TroopVo(quantity, unitVo.getName());
            troops.add(troopVo);
        }
        return troops;
    }*/

    private List<TroopEntity> makeTroop(List<Integer> divisions, List<UnitVo> unitVoList) {
        Collections.shuffle(unitVoList);
        List<TroopEntity> troops = new ArrayList<>(divisions.size());
        Iterator<Integer> divisionItr = divisions.iterator();
        Iterator<UnitVo> unitVoItr = unitVoList.iterator();
        while(divisionItr.hasNext() && unitVoItr.hasNext()) {
            Integer quantity = divisionItr.next();
            UnitVo unitVo = unitVoItr.next();
            String txId = tracer.currentSpan().context().traceIdString();
            TroopEntity troopEntity = new TroopEntity(txId, unitVo.getId(), quantity);
            log.debug("For division: {} and unit: {} @ transaction: {}, made: {}", quantity, unitVo.getName(), txId, troopEntity);
            troops.add(troopEntity);
        }
        return troops;
    }

    /**
     * divides strength into unitTypeCount parts such that each part is unbiased and different from other parts, thereby returning all parts arranged in descending order
     * @param strength
     * @param unitTypeCount
     * @return parts making up strength in descending order
     */
    private List<Integer> generateDivisions(int strength, int unitTypeCount) {
        List<Integer> divisions = new ArrayList<>(unitTypeCount);
        int sum = 0;
        for(int i = 0 ; i < unitTypeCount - 1 ; i++) {
            int limit = strength - sum - unitTypeCount + 1 + i;
            int n = (new Random().nextInt(limit) + 1);
            sum = sum + n;
            divisions.add(n);
        }
        divisions.add(strength - sum);
        Collections.sort(divisions);
        Collections.reverse(divisions);
        log.debug("Generated divisions: {} of strength: {}", divisions, strength);
        return divisions;
    }

    @Transactional
    @Override
    public List<TroopVo> createNewTroop(TroopForm form) throws TroopException {
        log.info("Trying to generate troops having strength");

        if(form == null) {
            log.debug("TroopForm provided is null");
            throw new TroopException(ArmyErrorCode.ARMY_ATTRIBUTE_UNEXPECTED,
                    new Object[]{ "form", TOABBaseMessageTemplate.MSG_TEMPLATE_NOT_PROVIDED });
        }
        log.debug("Form details: {}", form);

        log.debug("Validating provided attributes of TroopForm");
        Errors err = new DirectFieldBindingResult(form, form.getClass().getSimpleName());
        formValidator.validate(form, err);
        if(err.hasErrors()) {
            log.debug("TroopForm has {} errors", err.getErrorCount());
            ArmyErrorCode ec = ArmyErrorCode.valueOf(err.getFieldError().getCode());
            log.debug("TroopForm error detail: {}", ec);
            throw new TroopException(ec, new Object[] { err.getFieldError().getField() });
        }
        log.debug("All attributes of TroopForm are valid");

        List<UnitVo> unitVoList = soldierServiceClient.getAllUnitDetails();
        List<UnitVo> activeUnits = unitVoList.stream().filter(e -> e.getActive() == true).collect(Collectors.toList());
        if(activeUnits.isEmpty()) {
            log.error("No soldier units available");
            throw new TroopException(ArmyErrorCode.ARMY_ACTION_FAILURE, new Object[]{ "creation", "unable to create troop with strength:" + form.getStrength() });
        }

        List<Integer> divisions = this.generateDivisions(form.getStrength(), activeUnits.size());
        if(divisions.size() != activeUnits.size()) {
            log.error("Available soldier units {} and troop divisions {} do not match", activeUnits.size(), divisions.size());
            throw new TroopException(ArmyErrorCode.ARMY_ACTION_FAILURE, new Object[]{ "creation", "unable to create troop with strength:" + form.getStrength() });
        }

        //List<TroopVo> troops = this.makeTroop(divisions, unitVoList);
        List<TroopEntity> troopEntityList = this.makeTroop(divisions, activeUnits);
        troopEntityList = repository.saveAll(troopEntityList);
        troopEntityList.forEach(e -> {
            log.debug("For division: {} and unit: {} @ transaction: {}", e.getQuantity(), e.getUnitId(), e.getTransactionId());
        });
        List<TroopVo> troops = armyServiceHelper.troopEntity2DetailedVo(troopEntityList);
        log.info("Generated {} troops with a strength of: {}", troops.size(), form.getStrength());
        return troops;
    }
}