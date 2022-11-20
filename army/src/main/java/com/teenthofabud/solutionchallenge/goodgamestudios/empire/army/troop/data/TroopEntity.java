package com.teenthofabud.solutionchallenge.goodgamestudios.empire.army.troop.data;

import com.teenthofabud.core.common.data.entity.TOABBaseEntity;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Comparator;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "troop")
@EntityListeners(AuditingEntityListener.class)
public class TroopEntity extends TOABBaseEntity implements Comparable<TroopEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long id;
    @ToString.Include
    @Column(name = "transaction_id")
    private String transactionId;
    @ToString.Include
    @Column(name = "soldier_unit_id")
    private String unitId;
    @ToString.Include
    @Column(name = "soldier_quantity")
    private Integer quantity;

    @Override
    public int compareTo(TroopEntity o) {
        return Comparator.comparing(TroopEntity::getTransactionId)
                .thenComparing(TroopEntity::getQuantity)
                .thenComparing(TroopEntity::getUnitId)
                .compare(this, o);
    }

    public TroopEntity(String transactionId, String unitId, Integer quantity) {
        this.transactionId = transactionId;
        this.unitId = unitId;
        this.quantity = quantity;
    }
}
