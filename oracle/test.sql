SELECT * from CLASSES;
SELECT * from Students; 
SELECT * from LOGS;
SELECT * from COURSES;
SELECT * from COURSE_CREDIT;
SELECT * from PREREQUISITES;
SELECT * from G_ENROLLMENTS;
SELECT * from SCORE_GRADE;

UPDATE g_enrollments SET classid = 'c0010' WHERE g_B# = 'B00000010' AND classid = 'c0006';
UPDATE STUDENTS SET ST_LEVEL = 'graduate' WHERE B# = 'B0000004';
DELETE FROM G_ENROLLMENTS WHERE G_B# = 'B00000013' and CLASSID = 'c0010';

delete from g_enrollments where classid = 'c0001' and g_B# = 'B0000001';
insert into g_enrollments values ('B0000001', 'c0001', 76); 
UPDATE g_enrollments SET classid = 'c0010' WHERE g_B# = 'B00000010' AND classid = 'c0006';
update classes set class_size = 11, limit = 20 where CLASSID= 'c0001';
insert into students values ('B0000004', 'Barbara', 'Callan', 'sophomore', 2.5, 'callan@bu.edu', '18-OCT-95');


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


-- Generate and insert 50 dummy records into the "students" table

DECLARE
    first_names VARCHAR2(10) := 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    last_names VARCHAR2(10) := 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
    emails VARCHAR2(20) := 'abcdefghijklmnopqrstuvwxyz';
BEGIN
    FOR i IN 1..50 LOOP
        INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
        VALUES (
            'B' || LPAD(i, 7, '0'),
            SUBSTR(first_names, ROUND(DBMS_RANDOM.VALUE(1, 26), 0), 1) || SUBSTR(RAWTOHEX(DBMS_RANDOM.VALUE), 3, 2),
            SUBSTR(last_names, ROUND(DBMS_RANDOM.VALUE(1, 26), 0), 1) || SUBSTR(RAWTOHEX(DBMS_RANDOM.VALUE), 3, 2),
            CASE ROUND(DBMS_RANDOM.VALUE(1, 5), 0)
                WHEN 1 THEN 'freshman'
                WHEN 2 THEN 'sophomore'
                WHEN 3 THEN 'junior'
                WHEN 4 THEN 'senior'
                ELSE 'graduate'
            END,
            ROUND(DBMS_RANDOM.VALUE(0, 4), 2),
            SUBSTR(emails, ROUND(DBMS_RANDOM.VALUE(1, 26), 0), 1) || SUBSTR(RAWTOHEX(DBMS_RANDOM.VALUE), 3, 8) || '@e.com',
            TO_DATE(TRUNC(DBMS_RANDOM.VALUE(TO_CHAR(TO_DATE('1970/01/01', 'YYYY/MM/DD'), 'J'), TO_CHAR(SYSDATE, 'J'))), 'J')
        );
    END LOOP;
    COMMIT;
END;
/
---------------------------------------------------
--dum dum data for student

-- Query 14
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000014', 'Jessica', 'Wilson', 'sophomore', 3.4, 'jessica.wilson@e.com', TO_DATE('1999-12-08', 'YYYY-MM-DD'));

-- Query 15
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000015', 'Daniel', 'Taylor', 'junior', 3.7, 'daniel.taylor@e.com', TO_DATE('1998-07-25', 'YYYY-MM-DD'));

-- Query 16
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000016', 'Sarah', 'Anderson', 'senior', 3.9, 'sarah.anderson@e.com', TO_DATE('1997-05-30', 'YYYY-MM-DD'));

-- Query 17
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000017', 'Ryan', 'Thomas', 'graduate', 3.8, 'ryan.thomas@e.com', TO_DATE('1996-11-18', 'YYYY-MM-DD'));

-- Query 18
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000018', 'Hannah', 'Hernandez', 'freshman', 3.2, 'hannah.hernandez@e.com', TO_DATE('2001-02-14', 'YYYY-MM-DD'));

-- Query 19
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000019', 'Matthew', 'Lopez', 'sophomore', 3.6, 'matthew.lopez@e.com', TO_DATE('2000-09-10', 'YYYY-MM-DD'));

-- Query 20
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000020', 'Emily', 'Gonzalez', 'junior', 3.5, 'emily.gonzalez@e.com', TO_DATE('1999-04-05', 'YYYY-MM-DD'));

-- Query 21
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000021', 'Christopher', 'Perez', 'senior', 3.9, 'christopher.perez@e.com', TO_DATE('1998-01-21', 'YYYY-MM-DD'));

-- Query 22
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000022', 'Ashley', 'Rodriguez', 'graduate', 3.8, 'ashley.rodriguez@e.com', TO_DATE('1997-08-17', 'YYYY-MM-DD'));

-- Query 23
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000023', 'Andrew', 'Martinez', 'freshman', 3.3, 'andrew.martinez@e.com', TO_DATE('2002-03-27', 'YYYY-MM-DD'));

-- Query 24
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000024', 'Samantha', 'Hernandez', 'sophomore', 3.5, 'samantha.hernandez@e.com', TO_DATE('2001-10-14', 'YYYY-MM-DD'));

-- Query 25
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000025', 'Michael', 'Lopez', 'junior', 3.7, 'michael.lopez@e.com', TO_DATE('2000-05-09', 'YYYY-MM-DD'));

-- Query 26
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000026', 'Jessica', 'Gonzalez', 'senior', 3.9, 'jessica.gonzalez@e.com', TO_DATE('1999-02-02', 'YYYY-MM-DD'));

-- Query 27
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000027', 'David', 'Rodriguez', 'graduate', 3.8, 'david.rodriguez@e.com', TO_DATE('1998-07-29', 'YYYY-MM-DD'));

-- Query 28
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000028', 'Madison', 'Perez', 'freshman', 3.2, 'madison.perez@e.com', TO_DATE('2003-04-18', 'YYYY-MM-DD'));

-- Query 29
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000029', 'James', 'Sanchez', 'sophomore', 3.6, 'james.sanchez@e.com', TO_DATE('2002-11-05', 'YYYY-MM-DD'));

-- Query 30
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000030', 'Olivia', 'Wang', 'junior', 3.5, 'olivia.wang@e.com', TO_DATE('2001-06-23', 'YYYY-MM-DD'));

-- Query 31
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000031', 'Ethan', 'Lopez', 'senior', 3.9, 'ethan.lopez@e.com', TO_DATE('2000-03-17', 'YYYY-MM-DD'));

-- Query 32
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000032', 'Ava', 'Nguyen', 'graduate', 3.8, 'ava.nguyen@e.com', TO_DATE('1999-08-11', 'YYYY-MM-DD'));

-- Query 33
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000033', 'William', 'Garcia', 'freshman', 3.3, 'william.garcia@e.com', TO_DATE('2004-01-09', 'YYYY-MM-DD'));


-- Query 34
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000034', 'Sophia', 'Hernandez', 'sophomore', 3.5, 'sophia.hernandez@e.com', TO_DATE('2003-07-26', 'YYYY-MM-DD'));

-- Query 35
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000035', 'Alexander', 'Gonzalez', 'junior', 3.7, 'alexander.gonzalez@e.com', TO_DATE('2002-04-12', 'YYYY-MM-DD'));

-- Query 36
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000036', 'Mia', 'Lopez', 'senior', 3.9, 'mia.lopez@e.com', TO_DATE('2001-01-30', 'YYYY-MM-DD'));

-- Query 37
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000037', 'Liam', 'Perez', 'graduate', 3.8, 'liam.perez@e.com', TO_DATE('2000-09-14', 'YYYY-MM-DD'));

-- Query 38
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000038', 'Charlotte', 'Martinez', 'freshman', 3.2, 'charlotte.martinez@e.com', TO_DATE('2005-02-22', 'YYYY-MM-DD'));

-- Query 39
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000039', 'Benjamin', 'Sanchez', 'sophomore', 3.6, 'benjamin.sanchez@e.com', TO_DATE('2004-11-07', 'YYYY-MM-DD'));

-- Query 40
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000040', 'Amelia', 'Wang', 'junior', 3.5, 'amelia.wang@e.com', TO_DATE('2003-06-15', 'YYYY-MM-DD'));

-- Query 41
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000041', 'Lucas', 'Lopez', 'senior', 3.9, 'lucas.lopez@e.com', TO_DATE('2002-03-02', 'YYYY-MM-DD'));

-- Query 42
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000042', 'Emma', 'Nguyen', 'graduate', 3.8, 'emma.nguyen@e.com', TO_DATE('2001-08-19', 'YYYY-MM-DD'));

-- Query 43
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000043', 'Henry', 'Garcia', 'freshman', 3.3, 'henry.garcia@e.com', TO_DATE('2006-01-05', 'YYYY-MM-DD'));

-- Query 44
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000044', 'Olivia', 'Hernandez', 'sophomore', 3.5, 'olivia.hernandez@e.com', TO_DATE('2005-07-29', 'YYYY-MM-DD'));

-- Query 45
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000045', 'Sebastian', 'Gonzalez', 'junior', 3.7, 'sebastian.gonzalez@e.com', TO_DATE('2004-04-15', 'YYYY-MM-DD'));

-- Query 46
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000046', 'Ava', 'Lopez', 'senior', 3.9, 'ava.lopez@e.com', TO_DATE('2003-01-20', 'YYYY-MM-DD'));

-- Query 47
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000047', 'Logan', 'Perez', 'graduate', 3.8, 'logan.perez@e.com', TO_DATE('2002-09-04', 'YYYY-MM-DD'));

-- Query 48
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000048', 'Mia', 'Martinez', 'freshman', 3.2, 'mia.martinez@e.com', TO_DATE('2007-04-12', 'YYYY-MM-DD'));

-- Query 49
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000049', 'Ethan', 'Sanchez', 'sophomore', 3.6, 'ethan.sanchez@e.com', TO_DATE('2006-10-29', 'YYYY-MM-DD'));

-- Query 50
INSERT INTO students (B#, first_name, last_name, st_level, gpa, email, bdate)
VALUES ('B00000050', 'Avery', 'Wang', 'junior', 3.5, 'avery.wang@e.com', TO_DATE('2005-06-17', 'YYYY-MM-DD'));

COMMIT;