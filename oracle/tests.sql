-- Setting the server output to on.
SET SERVEROUTPUT ON;

--TESTING Task 1
INSERT INTO Logs (user_name, op_time, table_name, operation, tuple_keyvalue) 
VALUES ('John De', SYSTIMESTAMP, 'Students', 'INSERT', '12345');
select * from logs;

--TESTING Task 2
BEGIN
    -- Test each procedure individually
    DBMS_OUTPUT.PUT_LINE('Showing Students:');
    student_management_package.show_students;

    DBMS_OUTPUT.PUT_LINE('Showing Courses:');
    student_management_package.show_courses;

    DBMS_OUTPUT.PUT_LINE('Showing Prerequisites:');
    student_management_package.show_prerequisites;

    DBMS_OUTPUT.PUT_LINE('Showing Course Credit:');
    student_management_package.show_course_credit;

    DBMS_OUTPUT.PUT_LINE('Showing Classes:');
    student_management_package.show_classes;

    DBMS_OUTPUT.PUT_LINE('Showing Score Grade:');
    student_management_package.show_score_grade;

    DBMS_OUTPUT.PUT_LINE('Showing G Enrollments:');
    student_management_package.show_g_enrollments;

    DBMS_OUTPUT.PUT_LINE('Showing Logs:');
    student_management_package.show_logs;
END;
/

--TESTING Task 3
BEGIN
    student_management_package.list_students_in_class('c0001');
    student_management_package.list_students_in_class('c999');
END;
/

--TESTING TASK 4
BEGIN
    student_management_package.show_prerequisites('CS', '532');
    student_management_package.show_prerequisites('CS', '999');
END;
/

--TESTING TASK 5
BEGIN
 
    student_management_package.enroll_student_into_class('B00000099', 'c0002');
    student_management_package.enroll_student_into_class('B00000005', 'c0002');
    student_management_package.enroll_student_into_class('B00000001', 'c0014');
    student_management_package.enroll_student_into_class('B00000013', 'c0001');
    student_management_package.enroll_student_into_class('B00000013', 'c0009');
    student_management_package.enroll_student_into_class('B00000010', 'c0010');
    student_management_package.enroll_student_into_class('B00000010', 'c0013');
    student_management_package.enroll_student_into_class('B00000013', 'c0010');

END;
/

--TESTING TASK 6
BEGIN
 
    student_management_package.drop_student_from_class('B00000099', 'c0002');
    student_management_package.drop_student_from_class('B00000005', 'c0002');
    student_management_package.drop_student_from_class('B00000001', 'c0014');
    student_management_package.drop_student_from_class('B00000010', 'c0004');
    student_management_package.drop_student_from_class('B00000010', 'c0009');
    student_management_package.drop_student_from_class('B00000004', 'c0005');
    student_management_package.drop_student_from_class('B00000001', 'c0001');

END;
/

--TESTING TASK 7
BEGIN
    student_management_package.delete_student('B00000094');
    student_management_package.delete_student('B00000004');
END;
/