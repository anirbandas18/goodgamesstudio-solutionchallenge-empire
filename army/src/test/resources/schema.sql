create table if not exists troop (
    id identity not null,
    transaction_id varchar(250) not null,
    soldier_unit_id varchar(50) not null,
    soldier_quantity int not null,
    created_on datetime default current_timestamp,
    created_by int default -1,
    modified_on datetime default current_timestamp,
    modified_by int,
    active_sw boolean default true,
    version int default 0,
    constraint pk_army_troop primary key (id),
    constraint uq_army_troop_tx_id_unit_id_quantity unique (transaction_id, soldier_unit_id, soldier_quantity),
    constraint chk_army_troop_soldier_quantity check (soldier_quantity > 0)
);
create index if not exists idx_army_troop_tx_id on troop(transaction_id);
alter table troop alter column id restart with 1;