package com.teenthofabud.solutionchallenge.goodgamestudios.empire.soldier.unit.data;

import com.teenthofabud.core.common.data.entity.TOABBaseEntity;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "unit")
@EntityListeners(AuditingEntityListener.class)
public class UnitEntity extends TOABBaseEntity implements Comparable<UnitEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long id;
    @ToString.Include
    private String name;
    @ToString.Include
    private String description;

    @Override
    public int compareTo(UnitEntity o) {
        return this.getId().compareTo(o.getId());
    }
}
