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


CREATE OR REPLACE PROCEDURE show_logs(logs_cursor OUT SYS_REFCURSOR) IS
BEGIN
    OPEN logs_cursor FOR SELECT * FROM logs;
END show_logs;


--     CREATE OR REPLACE PROCEDURE get_emp_info (firstname IN VARCHAR2,
--   lastname IN VARCHAR2, emp_cursor IN OUT my_var_pkg.my_refcur_typ) AS
-- BEGIN
-- -- because this procedure uses a weakly typed REF CURSOR, the cursor is flexible
-- -- and the SELECT statement can be changed, as in the following
--   OPEN emp_cursor FOR SELECT e.employee_id, e.first_name, e.last_name, e.email,
--     e.phone_number, e.hire_date, j.job_title FROM employees e
--     JOIN jobs j ON e.job_id = j.job_id
--     WHERE SUBSTR(UPPER(first_name), 1, LENGTH(firstname)) = UPPER(firstname)
--     AND SUBSTR(UPPER(last_name), 1, LENGTH(lastname)) = UPPER(lastname);
-- END get_emp_info;