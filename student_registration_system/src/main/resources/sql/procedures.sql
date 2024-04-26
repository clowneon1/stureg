CREATE OR REPLACE PACKAGE student_management_package AS

    --Procedures declatarion

    --task 2
    PROCEDURE show_students;
    PROCEDURE show_courses;
    PROCEDURE show_prerequisites;
    PROCEDURE show_course_credit;
    PROCEDURE show_classes;
    PROCEDURE show_score_grade;
    PROCEDURE show_g_enrollments;
    PROCEDURE show_logs;

    --task 3
    PROCEDURE list_students_in_class(class_id_in IN classes.classid%TYPE);

    --task 4
    PROCEDURE show_prerequisites(dept_code_in IN courses.dept_code%TYPE, course_num_in IN courses.course#%TYPE);

    --task 5 
    PROCEDURE enroll_student_into_class (
        p_B#      IN students.B#%TYPE,
        p_classid IN classes.classid%TYPE
    );

    --task 6
    PROCEDURE drop_student_from_class (
        p_b#      IN students.b#%TYPE,
        p_classid IN classes.classid%TYPE
    );

    --task 7
    PROCEDURE delete_student(
        p_b# IN students.b#%TYPE
    );

END student_management_package;
/

CREATE OR REPLACE PACKAGE BODY student_management_package AS

    --task 2 procedures.

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

    --task 3 procedures.
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
    
    --task 4 procedures

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

    --task 5 procedures

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
        student_exists := check_student_existence(p_B#);

        IF student_exists = 0 THEN
            DBMS_OUTPUT.PUT_LINE('The B# is invalid.');
            RETURN;
        END IF;

        -- Check if the B# corresponds to a graduate student
        student_status := check_graduate_student(p_B#);

        IF student_status = 0 THEN
            DBMS_OUTPUT.PUT_LINE('This is not a graduate student.');
            RETURN;
        END IF;

        -- Check if the classid exists in the Classes table
        class_exists := check_class_existence(p_classid);

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


        -- If all checks pass, enroll the student into the class
        INSERT INTO g_enrollments (g_B#, classid)
        VALUES (p_B#, p_classid);

        -- -- Update the class size of the class
        -- UPDATE classes
        -- SET class_size = class_size + 1
        -- WHERE classid = p_classid;

        DBMS_OUTPUT.PUT_LINE('Enrollment successful.');
    END enroll_student_into_class;

    --task 6 procedures
    
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
        student_exists := check_student_existence(p_B#);


        IF student_exists = 0 THEN
            DBMS_OUTPUT.PUT_LINE('The B# is invalid.');
            RETURN;
        END IF;

        -- Check if the B# corresponds to a graduate student
        student_status := check_graduate_student(p_B#);


        IF student_status = 0 THEN
            DBMS_OUTPUT.PUT_LINE('This is not a graduate student.');
            RETURN;
        END IF;

        -- Check if the classid exists in the Classes table
        class_exists := check_class_existence(p_classid);

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

    --task 7 procedures
     PROCEDURE delete_student(
        p_b# IN students.b#%TYPE
    ) AS
        student_exists NUMBER;
    BEGIN
        -- Check if the B# exists in the Students table
        student_exists := check_student_existence(p_B#);


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