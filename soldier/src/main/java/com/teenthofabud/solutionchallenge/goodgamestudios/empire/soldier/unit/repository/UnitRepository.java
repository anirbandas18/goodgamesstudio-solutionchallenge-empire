package com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.repository;

import com.teenthofabud.core.common.repository.TOABSimpleEntityBaseRepository;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.data.UnitEntity;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

@Repository
public interface UnitRepository extends TOABSimpleEntityBaseRepository<UnitEntity> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public UnitEntity save(UnitEntity entity);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Boolean existsByName(String name);
}
