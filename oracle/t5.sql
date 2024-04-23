CREATE OR REPLACE PACKAGE enrollment_package AS
    PROCEDURE enroll_student_into_class (
        p_B#      IN students.B#%TYPE,
        p_classid IN classes.classid%TYPE
    );
END enrollment_package;
/


CREATE OR REPLACE PACKAGE BODY enrollment_package AS
    PROCEDURE enroll_student_into_class (
        p_B#      IN students.B#%TYPE,
        p_classid IN classes.classid%TYPE
    ) AS
        student_status   NUMBER;
        class_exists     NUMBER;
        enrollment_count NUMBER;
        class_capacity   NUMBER;
        current_semester VARCHAR2(50);
        student_in_class NUMBER;
        student_exists   NUMBER;
        class_limit      NUMBER;
        class_semester   NUMBER;
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

        -- Check if the class is offered in the current current_semester
        SELECT CASE
                   WHEN TO_CHAR(SYSDATE, 'MM') BETWEEN '01' AND '05' THEN 'Spring'
                   WHEN TO_CHAR(SYSDATE, 'MM') BETWEEN '06' AND '08' THEN 'Summer'
                   ELSE 'Fall'
               END
        INTO current_semester
        FROM dual;

            SELECT COUNT(*)
            INTO class_semester
            FROM classes
            WHERE classid = p_classid
            AND year = EXTRACT(YEAR FROM SYSDATE)
            AND semester = current_semester;

        IF class_semester = 0 THEN
            DBMS_OUTPUT.PUT_LINE('Cannot enroll into a class from a previous semester.');
            RETURN;
        END IF;

        -- Check if the class is already full
        SELECT class_size
        INTO class_capacity
        FROM classes
        WHERE classid = p_classid;

        SELECT limit
        INTO class_limit
        FROM classes
        WHERE classes.classid = p_classid;

        IF class_capacity >= class_limit
        THEN
            DBMS_OUTPUT.PUT_LINE('The class is already full.');
            RETURN;
        END IF;

        -- Check if the student is already in the class
        SELECT COUNT(*)
        INTO student_in_class
        FROM g_enrollments
        WHERE g_B# = p_B#
          AND classid = p_classid;

        IF student_in_class > 0 THEN
            DBMS_OUTPUT.PUT_LINE('The student is already in the class.');
            RETURN;
        END IF;

        -- Check if the student is already enrolled in five other classes in the same semester and the same year
        SELECT COUNT(*)
        into enrollment_count
        FROM g_enrollments ge
        JOIN classes c ON ge.classid = c.classid
        WHERE ge.g_B# = p_B#
        AND c.year = EXTRACT(YEAR FROM SYSDATE)
        AND c.semester = current_semester;

       IF enrollment_count >= 5 THEN
            DBMS_OUTPUT.PUT_LINE('Students cannot be enrolled in more than five classes in the same semester.');
            RETURN;
        END IF;


        -- -- If all checks pass, enroll the student into the class
        -- INSERT INTO g_enrollments (g_B#, classid)
        -- VALUES (p_B#, p_classid);

        -- -- Update the class size of the class
        -- UPDATE classes
        -- SET class_size = class_size + 1
        -- WHERE classid = p_classid;

        DBMS_OUTPUT.PUT_LINE('Enrollment successful.');
    END enroll_student_into_class;
END enrollment_package;
/


---------------------------------------
--TESTS
SET SERVEROUTPUT ON;

BEGIN
 
    enrollment_package.enroll_student_into_class('B00000099', 'c0002');
    enrollment_package.enroll_student_into_class('B00000005', 'c0002');
    enrollment_package.enroll_student_into_class('B00000001', 'c0014');
    enrollment_package.enroll_student_into_class('B00000013', 'c0001');
    enrollment_package.enroll_student_into_class('B00000013', 'c0009');
    enrollment_package.enroll_student_into_class('B00000010', 'c0010');
    enrollment_package.enroll_student_into_class('B00000010', 'c0013');
    enrollment_package.enroll_student_into_class('B00000013', 'c0010');

END;
/
