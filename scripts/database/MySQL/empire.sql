CREATE DATABASE if not exists emp_soldier_db;
CREATE DATABASE if not exists emp_army_db;

CREATE USER if not exists 'emp_soldier_manager'@'%' identified by '$()Ld!3r';
CREATE USER if not exists 'emp_army_manager'@'%' identified by 'ArMy';

GRANT SELECT,INSERT,UPDATE ON emp_soldier_db.* TO 'emp_soldier_manager'@'%';
GRANT SELECT,INSERT,UPDATE ON emp_army_db.* TO 'emp_army_manager'@'%';

create table if not exists emp_soldier_db.unit (
	id int AUTO_INCREMENT,
    name varchar(50) not null,
    description varchar(200),
    created_on timestamp default current_timestamp,
    created_by int not null,
    modified_on timestamp default current_timestamp,
    modified_by int not null,
    active_sw bigint default 1,
    version int default 0,
    constraint pk_soldier_unit primary key (id),
    constraint uq_soldier_unit_name unique (name),
    index idx_soldier_unit_name (name)
);

create table if not exists emp_army_db.troop (
	id int AUTO_INCREMENT,
    transaction_id varchar(250) not null,
    soldier_unit_id varchar(50) not null,
    soldier_quantity int not null,
    created_on timestamp default current_timestamp,
    created_by int not null,
    modified_on timestamp default current_timestamp,
    modified_by int not null,
    active_sw bigint default 1,
    version int default 0,
    constraint pk_army_troop primary key (id),
    constraint uq_army_troop_tx_id_unit_id_quantity unique (transaction_id, soldier_unit_id, soldier_quantity),
    constraint chk_army_troop_soldier_quantity check (soldier_quantity > 0),
    index idx_army_troop_tx_id (transaction_id)
);