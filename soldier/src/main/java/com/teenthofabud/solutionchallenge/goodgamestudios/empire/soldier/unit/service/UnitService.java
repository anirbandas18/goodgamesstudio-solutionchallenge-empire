package com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.service;

import com.teenthofabud.core.common.constant.TOABCascadeLevel;
import com.teenthofabud.core.common.data.form.PatchOperationForm;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.data.UnitException;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.data.UnitForm;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.data.UnitVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public interface UnitService {

    public Set<UnitVo> retrieveAllByNaturalOrdering();

    public UnitVo retrieveDetailsById(String id, Optional<TOABCascadeLevel> optionalCascadeLevel) throws UnitException;

    public List<UnitVo> retrieveAllMatchingDetailsByCriteria(Optional<String> optionalName,
                                                                                    Optional<String> optionalDescription) throws UnitException;

    public String createUnit(UnitForm form) throws UnitException;

    public void updateUnit(String id, UnitForm form) throws UnitException;

    public void deleteUnit(String id) throws UnitException;

    public void applyPatchOnUnit(String id, List<PatchOperationForm> patches) throws UnitException;

}
