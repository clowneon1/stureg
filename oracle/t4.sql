CREATE OR REPLACE PACKAGE list_prerequisites_package AS

    PROCEDURE show_prerequisites(dept_code_in IN courses.dept_code%TYPE, course_num_in IN courses.course#%TYPE);

END list_prerequisites_package;
/

CREATE OR REPLACE PACKAGE BODY list_prerequisites_package AS

    -- Procedure to recursively find all prerequisite courses for a given course
    PROCEDURE find_prerequisites(dept_code_in IN courses.dept_code%TYPE, 
                                  course_num_in IN courses.course#%TYPE,
                                  depth_limit_in IN NUMBER,
                                  current_depth_in IN NUMBER,
                                  indent_in IN VARCHAR2) IS
    BEGIN
        IF current_depth_in > depth_limit_in THEN
            -- Limit the depth of recursion to avoid infinite loop
            RETURN;
        END IF;

        FOR prereq_rec IN (
            SELECT p.pre_dept_code, p.pre_course#
            FROM prerequisites p
            WHERE p.dept_code = dept_code_in
            AND p.course# = course_num_in
        ) LOOP
            DBMS_OUTPUT.PUT_LINE(indent_in || prereq_rec.pre_dept_code || prereq_rec.pre_course#);
            
            -- Recursively find prerequisites of the current prerequisite
            find_prerequisites(prereq_rec.pre_dept_code, prereq_rec.pre_course#, depth_limit_in, current_depth_in + 1, indent_in || '    ');
        END LOOP;
    END find_prerequisites;

    -- Procedure to show all prerequisite courses for a given course
    PROCEDURE show_prerequisites(dept_code_in IN courses.dept_code%TYPE, course_num_in IN courses.course#%TYPE) IS
        course_exists NUMBER;
    BEGIN
        -- Check if the provided (dept_code, course#) exists in the courses table
        SELECT COUNT(*)
        INTO course_exists
        FROM courses
        WHERE dept_code = dept_code_in
        AND course# = course_num_in;

        IF course_exists = 0 THEN
            -- Report if the provided (dept_code, course#) is invalid
            DBMS_OUTPUT.PUT_LINE(dept_code_in || course_num_in || ' does not exist.');
        ELSE
            -- Display the direct and indirect prerequisites of the given course
            DBMS_OUTPUT.PUT_LINE('Prerequisite courses for ' || dept_code_in || course_num_in || ':');
            find_prerequisites(dept_code_in, course_num_in, 10, 1, '');
        END IF;
    END show_prerequisites;

END list_prerequisites_package;
/


---------------------------------
--TESTING

SELECT * from STUDENTS ;
SELECT * from CLASSES;
SELECT * from G_ENROLLMENTS
WHERE G_ENROLLMENTS.CLASSID = 'c0002';
SELECT * from PREREQUISITES;

-- Anonymous PL/SQL block to test the procedures
SET SERVEROUTPUT ON; -- To enable DBMS_OUTPUT


-- Test with a valid course (existing in the database)
BEGIN
  list_prerequisites_package.show_prerequisites('CS', '532');
END;
/

-- Test with an invalid course (not existing in the database)
BEGIN
  list_prerequisites_package.show_prerequisites('CS', '999');
END;
/
