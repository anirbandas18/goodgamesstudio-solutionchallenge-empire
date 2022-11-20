package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.repository;

import com.teenthofabud.core.common.repository.TOABSimpleEntityBaseRepository;
import com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data.TroopEntity;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

@Repository
public interface TroopRepository extends TOABSimpleEntityBaseRepository<TroopEntity> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public TroopEntity save(TroopEntity entity);

}
