--triger for incrementing the class size by 1
CREATE OR REPLACE TRIGGER insert_update_class_size_trigger
AFTER INSERT ON g_enrollments
FOR EACH ROW
BEGIN
    UPDATE classes
    SET class_size = class_size + 1
    WHERE classid = :NEW.classid;
    DBMS_OUTPUT.PUT_LINE('Trigger : Class size is increased by 1');
END update_class_size_trigger;
/

--triger for decrementing the class size by 1
CREATE OR REPLACE TRIGGER delete_update_class_size_trigger
AFTER DELETE ON g_enrollments
FOR EACH ROW
BEGIN
    UPDATE classes
    SET class_size = class_size - 1
    WHERE classid = :OLD.classid;
    DBMS_OUTPUT.PUT_LINE('Trigger : Class size is decreased by 1');
END update_class_size_trigger;
/

--triger for deleting all enrollments of student
CREATE OR REPLACE TRIGGER delete_student_enrollments
BEFORE DELETE ON students
FOR EACH ROW
BEGIN
    DELETE FROM g_enrollments
    WHERE g_B# = :OLD.B#;
    DBMS_OUTPUT.PUT_LINE('Trigger: deleted all enrollments of student');
END;
/

-- Trigger to log student deletion from the Students table
CREATE OR REPLACE TRIGGER student_delete_trigger
AFTER DELETE ON students
FOR EACH ROW
BEGIN
    INSERT INTO logs (user_name, op_time, table_name, operation, tuple_keyvalue)
    VALUES (USER, SYSTIMESTAMP, 'Students', 'Delete', :OLD.B#);

    DBMS_OUTPUT.PUT_LINE('Trigger : log student deletion from the Students table');
END student_delete_trigger;
/

-- Trigger to log successful student enrollment into a class
CREATE OR REPLACE TRIGGER enrollment_insert_trigger
AFTER INSERT ON g_enrollments
FOR EACH ROW
BEGIN
    INSERT INTO logs (user_name, op_time, table_name, operation, tuple_keyvalue)
    VALUES (USER, SYSTIMESTAMP, 'G_Enrollments', 'Insert', :NEW.g_B# || ',' || :NEW.classid);
    DBMS_OUTPUT.PUT_LINE('Trigger : log successful student enrollment into a class');
END enrollment_insert_trigger;
/

-- Trigger to log successful student drop from a class
CREATE OR REPLACE TRIGGER enrollment_delete_trigger
AFTER DELETE ON g_enrollments
FOR EACH ROW
BEGIN
    INSERT INTO logs (user_name, op_time, table_name, operation, tuple_keyvalue)
    VALUES (USER, SYSTIMESTAMP, 'G_Enrollments', 'Delete', :OLD.g_B# || ',' || :OLD.classid);
    DBMS_OUTPUT.PUT_LINE('Trigger : log successful student drop from a class');
END enrollment_delete_trigger;
/



--if we want to combine the class INC DEC  triggers

-- CREATE OR REPLACE TRIGGER update_class_size_trigger
-- AFTER INSERT OR DELETE ON g_enrollments
-- FOR EACH ROW
-- BEGIN
--     IF INSERTING THEN
--         UPDATE classes
--         SET class_size = class_size + 1
--         WHERE classid = :NEW.classid;
--     ELSIF DELETING THEN
--         UPDATE classes
--         SET class_size = class_size - 1
--         WHERE classid = :OLD.classid;
--         DBMS_OUTPUT.PUT_LINE('Trigger');
--     END IF;
-- END update_class_size_trigger;
-- /


