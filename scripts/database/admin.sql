CREATE DATABASE emp_soldier_db;
CREATE DATABASE emp_army_db;

CREATE USER 'emp_soldier_manager'@'127.0.0.1' identified by '$()Ld!3r';
CREATE USER 'emp_army_manager'@'127.0.0.1' identified by 'ArMy';

GRANT SELECT,INSERT,UPDATE ON emp_soldier_db.* TO 'emp_soldier_manager'@'127.0.0.1';
GRANT SELECT,INSERT,UPDATE ON emp_army_db.* TO 'emp_army_manager'@'127.0.0.1';