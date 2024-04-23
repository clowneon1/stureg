CREATE OR REPLACE PACKAGE list_student_package AS
    PROCEDURE list_students_in_class(class_id_in IN classes.classid%TYPE);

END list_student_package;
/

CREATE OR REPLACE PACKAGE BODY list_student_package AS

    PROCEDURE list_students_in_class(class_id_in IN classes.classid%TYPE) IS
        class_exists NUMBER;
    BEGIN
        -- Check if the provided classid exists in the classes table
        SELECT COUNT(*)
        INTO class_exists
        FROM classes
        WHERE classid = class_id_in;

        IF class_exists = 0 THEN
            DBMS_OUTPUT.PUT_LINE('The classid is invalid.');
        ELSE
            -- Display B#, first name, and last name of every student in the class
            FOR student_rec IN (
                SELECT s.B#, s.first_name, s.last_name
                FROM students s
                JOIN g_enrollments ge ON s.B# = ge.g_B#
                WHERE ge.classid = class_id_in
            ) LOOP
                DBMS_OUTPUT.PUT_LINE('B#: ' || student_rec.B# ||
                                     ', First Name: ' || student_rec.first_name ||
                                     ', Last Name: ' || student_rec.last_name);
            END LOOP;
        END IF;
    END list_students_in_class;

END list_student_package;
/

---------------------------------
--TESTING

SELECT * from STUDENTS ;
SELECT * from CLASSES;
SELECT * from G_ENROLLMENTS
WHERE G_ENROLLMENTS.CLASSID = 'c0002';



-- Anonymous PL/SQL block to test the procedures
SET SERVEROUTPUT ON; -- To enable DBMS_OUTPUT

BEGIN
    list_student_package.list_students_in_class('c0001');
    list_student_package.list_students_in_class('c999');
END;
/
