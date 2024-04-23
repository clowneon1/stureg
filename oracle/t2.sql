CREATE OR REPLACE PACKAGE project_data_package AS

    PROCEDURE show_students;
    PROCEDURE show_courses;
    PROCEDURE show_prerequisites;
    PROCEDURE show_course_credit;
    PROCEDURE show_classes;
    PROCEDURE show_score_grade;
    PROCEDURE show_g_enrollments;
    PROCEDURE show_logs;

END project_data_package;
/

CREATE OR REPLACE PACKAGE BODY project_data_package AS

    PROCEDURE show_students IS
    BEGIN
        FOR student_rec IN (SELECT * FROM students) LOOP
            DBMS_OUTPUT.PUT_LINE('B#: ' || student_rec.B# ||
                                 ', First Name: ' || student_rec.first_name ||
                                 ', Last Name: ' || student_rec.last_name ||
                                 ', Level: ' || student_rec.st_level ||
                                 ', GPA: ' || student_rec.gpa ||
                                 ', Email: ' || student_rec.email ||
                                 ', Birth Date: ' || student_rec.bdate);
        END LOOP;
    END show_students;
    
    PROCEDURE show_courses IS
    BEGIN
        FOR course_rec IN (SELECT * FROM courses) LOOP
            DBMS_OUTPUT.PUT_LINE('Dept Code: ' || course_rec.dept_code ||
                                 ', Course#: ' || course_rec.course# ||
                                 ', Title: ' || course_rec.title);
        END LOOP;
    END show_courses;
    
    PROCEDURE show_prerequisites IS
    BEGIN
        FOR prereq_rec IN (SELECT * FROM prerequisites) LOOP
            DBMS_OUTPUT.PUT_LINE('Dept Code: ' || prereq_rec.dept_code ||
                                 ', Course#: ' || prereq_rec.course# ||
                                 ', Pre-Dept Code: ' || prereq_rec.pre_dept_code ||
                                 ', Pre-Course#: ' || prereq_rec.pre_course#);
        END LOOP;
    END show_prerequisites;
    
    PROCEDURE show_course_credit IS
    BEGIN
        FOR credit_rec IN (SELECT * FROM course_credit) LOOP
            DBMS_OUTPUT.PUT_LINE('Course#: ' || credit_rec.course# ||
                                 ', Credits: ' || credit_rec.credits);
        END LOOP;
    END show_course_credit;
    
    PROCEDURE show_classes IS
    BEGIN
        FOR class_rec IN (SELECT * FROM classes) LOOP
            DBMS_OUTPUT.PUT_LINE('Class ID: ' || class_rec.classid ||
                                 ', Dept Code: ' || class_rec.dept_code ||
                                 ', Course#: ' || class_rec.course# ||
                                 ', Section#: ' || class_rec.sect# ||
                                 ', Year: ' || class_rec.year ||
                                 ', Semester: ' || class_rec.semester ||
                                 ', Limit: ' || class_rec.limit ||
                                 ', Class Size: ' || class_rec.class_size ||
                                 ', Room: ' || class_rec.room);
        END LOOP;
    END show_classes;
    
    PROCEDURE show_score_grade IS
    BEGIN
        FOR grade_rec IN (SELECT * FROM score_grade) LOOP
            DBMS_OUTPUT.PUT_LINE('Score: ' || grade_rec.score ||
                                 ', Letter Grade: ' || grade_rec.lgrade);
        END LOOP;
    END show_score_grade;
    
    PROCEDURE show_g_enrollments IS
    BEGIN
        FOR enrollment_rec IN (SELECT * FROM g_enrollments) LOOP
            DBMS_OUTPUT.PUT_LINE('B#: ' || enrollment_rec.g_B# ||
                                 ', Class ID: ' || enrollment_rec.classid ||
                                 ', Score: ' || enrollment_rec.score);
        END LOOP;
    END show_g_enrollments;
    
    PROCEDURE show_logs IS
    BEGIN
        FOR log_rec IN (SELECT * FROM logs) LOOP
            DBMS_OUTPUT.PUT_LINE('Log#: ' || log_rec.log# ||
                                 ', User Name: ' || log_rec.user_name ||
                                 ', Operation Time: ' || log_rec.op_time ||
                                 ', Table Name: ' || log_rec.table_name ||
                                 ', Operation: ' || log_rec.operation ||
                                 ', Tuple Key Value: ' || log_rec.tuple_keyvalue);
        END LOOP;
    END show_logs;

    

END project_data_package;
/
---------------------------
--TESTING

-- Anonymous PL/SQL block to test the procedures
SET SERVEROUTPUT ON;

BEGIN
    -- Test each procedure individually
    DBMS_OUTPUT.PUT_LINE('Showing Students:');
    project_data_package.show_students;

    DBMS_OUTPUT.PUT_LINE('Showing Courses:');
    project_data_package.show_courses;

    DBMS_OUTPUT.PUT_LINE('Showing Prerequisites:');
    project_data_package.show_prerequisites;

    DBMS_OUTPUT.PUT_LINE('Showing Course Credit:');
    project_data_package.show_course_credit;

    DBMS_OUTPUT.PUT_LINE('Showing Classes:');
    project_data_package.show_classes;

    DBMS_OUTPUT.PUT_LINE('Showing Score Grade:');
    project_data_package.show_score_grade;

    DBMS_OUTPUT.PUT_LINE('Showing G Enrollments:');
    project_data_package.show_g_enrollments;

    DBMS_OUTPUT.PUT_LINE('Showing Logs:');
    project_data_package.show_logs;
END;
/