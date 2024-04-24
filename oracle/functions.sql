CREATE OR REPLACE FUNCTION check_student_existence(p_b# IN students.b#%TYPE)
RETURN NUMBER
IS
    student_exists NUMBER;
BEGIN
    -- Check if the B# exists in the Students table
    SELECT COUNT(*)
    INTO student_exists
    FROM students
    WHERE B# = p_B#;
    
    RETURN student_exists;
END check_student_existence;


CREATE OR REPLACE FUNCTION check_graduate_student(p_b# IN students.b#%TYPE)
RETURN NUMBER
IS
    student_status NUMBER;
BEGIN
    -- Check if the B# corresponds to a graduate student
    SELECT COUNT(*)
    INTO student_status
    FROM students s
    WHERE s.B# = p_B# AND s.ST_LEVEL = 'graduate';

    RETURN student_status;
END check_graduate_student;


CREATE OR REPLACE FUNCTION check_class_existence(p_classid IN classes.classid%TYPE)
RETURN NUMBER
IS
    class_exists NUMBER;
BEGIN
    -- Check if the classid exists in the Classes table
    SELECT COUNT(*)
    INTO class_exists
    FROM classes
    WHERE classid = p_classid;
    
    RETURN class_exists;
END check_class_existence;