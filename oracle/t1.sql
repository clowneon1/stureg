---- Step 1: Create the sequence
CREATE SEQUENCE log_sequence START WITH 1000 INCREMENT BY 1;

create table logs (log# NUMBER DEFAULT log_sequence.NEXTVAL primary key, 
user_name varchar2(10) not null, 
op_time date not null, 
table_name varchar2(13) not null, 
operation varchar2(6) not null, 
tuple_keyvalue varchar2(20));
-----------------------------------------
--TESTING
INSERT INTO Logs (user_name, op_time, table_name, operation, tuple_keyvalue) 
VALUES ('John De', SYSTIMESTAMP, 'Students', 'INSERT', '12345');

select * from logs;