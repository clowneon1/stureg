CREATE OR REPLACE PACKAGE student_management_package AS
    PROCEDURE delete_student(
        p_b# IN students.b#%TYPE
    );
END student_management_package;
/

CREATE OR REPLACE PACKAGE BODY student_management_package AS
    PROCEDURE delete_student(
        p_b# IN students.b#%TYPE
    ) AS
        student_exists NUMBER;
    BEGIN
        -- Check if the B# exists in the Students table
        SELECT COUNT(*)
        INTO student_exists
        FROM students
        WHERE b# = p_b#;

        IF student_exists = 0 THEN
            DBMS_OUTPUT.PUT_LINE('The B# is invalid.');
            RETURN;
        END IF;

        -- Delete the student from the Students table
        DELETE FROM students
        WHERE b# = p_b#;

        DBMS_OUTPUT.PUT_LINE('Student deleted successfully.');
    END delete_student;
END student_management_package;
/


--TESTS
SET SERVEROUTPUT ON;

BEGIN
        student_management_package.delete_student('B00000004');
END;
/