CREATE OR REPLACE PACKAGE enrollment_package AS
    PROCEDURE drop_student_from_class (
        p_b#      IN students.b#%TYPE,
        p_classid IN classes.classid%TYPE
    );
END enrollment_package;
/

CREATE OR REPLACE PACKAGE BODY enrollment_package AS
    PROCEDURE drop_student_from_class (
        p_b#      IN students.b#%TYPE,
        p_classid IN classes.classid%TYPE
    ) AS
        student_status   VARCHAR2(20);
        class_exists     NUMBER;
        enrollment_count NUMBER;
        current_semester VARCHAR2(20);
        student_in_class NUMBER;
        student_exists   NUMBER;
        semester_year    VARCHAR2(10);
        student_year_sem NUMBER;
    BEGIN
        -- Check if the B# exists in the Students table
        SELECT COUNT(*)
        INTO student_exists
        FROM students
        WHERE B# = p_B#;

        IF student_exists = 0 THEN
            DBMS_OUTPUT.PUT_LINE('The B# is invalid.');
            RETURN;
        END IF;

        -- Check if the B# corresponds to a graduate student
        SELECT count(*)
        INTO student_status
        FROM students s
        WHERE s.B# = p_B# AND s.ST_LEVEL = 'graduate';

        IF student_status = 0 THEN
            DBMS_OUTPUT.PUT_LINE('This is not a graduate student.');
            RETURN;
        END IF;

        -- Check if the classid exists in the Classes table
        SELECT COUNT(*)
        INTO class_exists
        FROM classes
        WHERE classid = p_classid;

        IF class_exists = 0 THEN
            DBMS_OUTPUT.PUT_LINE('The classid is invalid.');
            RETURN;
        END IF;

        -- Check if the student is enrolled in the class
        SELECT COUNT(*)
        INTO student_in_class
        FROM g_enrollments
        WHERE g_b# = p_b#
          AND classid = p_classid;

        IF student_in_class = 0 THEN
            DBMS_OUTPUT.PUT_LINE('The student is not enrolled in the class.');
            RETURN;
        END IF;

        SELECT COUNT(*)
        INTO student_year_sem
        from CLASSES
        WHERE classid = p_classid AND (year = 2021 AND semester = 'Spring');

        if student_year_sem = 0 THEN
                DBMS_OUTPUT.PUT_LINE('Only enrollment in the current semester can be dropped.');
                RETURN;
        END IF;


        if (current_semester <> 'Spring') and (semester_year <> '2021') THEN
                DBMS_OUTPUT.PUT_LINE('Only enrollment in the current semester can be dropped.');
                RETURN;
        END IF;

        -- Check if the class is the last class for the student in Spring 2021
        SELECT COUNT(*)
        INTO enrollment_count
        FROM g_enrollments ge
        WHERE ge.g_b# = p_b#
          AND classid IN (
              SELECT classid
              FROM classes
              WHERE semester = 'Spring'
                AND year = 2021
          );

        IF enrollment_count = 1 THEN
            DBMS_OUTPUT.PUT_LINE('This is the only class for this student in Spring 2021 and cannot be dropped.');
            RETURN;
        END IF;

        -- -- If all checks pass, drop the student from the class
        DELETE FROM g_enrollments
        WHERE g_b# = p_b#
          AND classid = p_classid;

        -- -- Update the class size of the class
        -- UPDATE classes
        -- SET class_size = class_size - 1
        -- WHERE classid = p_classid;

        DBMS_OUTPUT.PUT_LINE('Student dropped from the class successfully.');
    END drop_student_from_class;
END enrollment_package;
/



---------------------------------------
--TESTS
SET SERVEROUTPUT ON;

BEGIN
 
    enrollment_package.drop_student_from_class('B00000099', 'c0002');
    enrollment_package.drop_student_from_class('B00000005', 'c0002');
    enrollment_package.drop_student_from_class('B00000001', 'c0014');
    enrollment_package.drop_student_from_class('B00000010', 'c0004');
    enrollment_package.drop_student_from_class('B00000010', 'c0009');
    enrollment_package.drop_student_from_class('B00000004', 'c0005');
    enrollment_package.drop_student_from_class('B00000001', 'c0001');



END;
/