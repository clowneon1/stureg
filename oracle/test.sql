SELECT * from CLASSES;
SELECT * from Students; 
SELECT * from LOGS;
SELECT * from COURSES;
SELECT * from COURSE_CREDIT;
SELECT * from PREREQUISITES;
SELECT * from G_ENROLLMENTS;
SELECT * from SCORE_GRADE;

UPDATE g_enrollments SET classid = 'c0010' WHERE g_B# = 'B00000010' AND classid = 'c0006';
UPDATE STUDENTS SET ST_LEVEL = 'graduate' WHERE B# = 'B00000004';
DELETE FROM G_ENROLLMENTS WHERE G_B# = 'B00000013' and CLASSID = 'c0010';

delete from g_enrollments where classid = 'c0001' and g_B# = 'B00000001';
insert into g_enrollments values ('B00000001', 'c0001', 76); 
UPDATE g_enrollments SET classid = 'c0010' WHERE g_B# = 'B00000010' AND classid = 'c0006';
update classes set class_size = 11, limit = 20 where CLASSID= 'c0001';
insert into students values ('B00000004', 'Barbara', 'Callan', 'sophomore', 2.5, 'callan@bu.edu', '18-OCT-95');


drop table students;
create table students (sid char(4) primary key check (sid like 'B%'),
    firstname varchar2(15) not null, lastname varchar2(15) not null, status varchar2(10) 
    check (status in ('freshman', 'sophomore', 'junior', 'senior', 'graduate')), 
    gpa number(3,2) check (gpa between 0 and 4.0), email varchar2(20));


CREATE OR REPLACE  PROCEDURE show_logs IS

    BEGIN
        OPEN logs_cursor FOR SELECT * FROM logs;
    END show_logs;

CREATE OR REPLACE PROCEDURE show_logs_nocursor IS
    BEGIN
        FOR log_rec IN (SELECT * FROM logs) LOOP
            DBMS_OUTPUT.PUT_LINE('Log#: ' || log_rec.log# ||
                                 ', User Name: ' || log_rec.user_name ||
                                 ', Operation Time: ' || log_rec.op_time ||
                                 ', Table Name: ' || log_rec.table_name ||
                                 ', Operation: ' || log_rec.operation ||
                                 ', Tuple Key Value: ' || log_rec.tuple_keyvalue);
        END LOOP;
    END show_logs_nocursor;
    

CREATE OR REPLACE PROCEDURE show_logs_nocursor IS
    log_line VARCHAR2(4000);
BEGIN
    FOR log_rec IN (SELECT * FROM logs) LOOP
        log_line := 'Log#: ' || log_rec.log# ||
                    ', User Name: ' || log_rec.user_name ||
                    ', Operation Time: ' || log_rec.op_time ||
                    ', Table Name: ' || log_rec.table_name ||
                    ', Operation: ' || log_rec.operation ||
                    ', Tuple Key Value: ' || log_rec.tuple_keyvalue;
        DBMS_OUTPUT.PUT_LINE(log_line);
    END LOOP;
END show_logs_nocursor;
