create table if not exists unit (
    id identity not null,
    name varchar(50) not null,
    description varchar(200),
    created_on datetime default current_timestamp,
    created_by int default -1,
    modified_on datetime default current_timestamp,
    modified_by int,
    active_sw boolean default true,
    version int default 0,
    constraint pk_soldier_unit primary key (id),
    constraint uq_soldier_unit_name unique (name)
);
create index if not exists idx_soldier_unit_name on unit(name);
alter table unit alter column id restart with 1;