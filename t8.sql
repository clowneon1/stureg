-- Trigger for student deletion from the Students table
CREATE OR REPLACE TRIGGER student_delete_trigger
AFTER DELETE ON students
FOR EACH ROW
BEGIN
    INSERT INTO logs (user_name, op_time, table_name, operation, tuple_keyvalue)
    VALUES (USER, SYSTIMESTAMP, 'Students', 'Delete', :OLD.B#);

    DBMS_OUTPUT.PUT_LINE('log trigger : 1 ');
END student_delete_trigger;
/

-- Trigger for successful student enrollment into a class
CREATE OR REPLACE TRIGGER enrollment_insert_trigger
AFTER INSERT ON g_enrollments
FOR EACH ROW
BEGIN
    INSERT INTO logs (user_name, op_time, table_name, operation, tuple_keyvalue)
    VALUES (USER, SYSTIMESTAMP, 'G_Enrollments', 'Insert', :NEW.g_B# || ',' || :NEW.classid);
    DBMS_OUTPUT.PUT_LINE('log trigger : 2');
END enrollment_insert_trigger;
/

-- Trigger for successful student drop from a class
CREATE OR REPLACE TRIGGER enrollment_delete_trigger
AFTER DELETE ON g_enrollments
FOR EACH ROW
BEGIN
        INSERT INTO logs (user_name, op_time, table_name, operation, tuple_keyvalue)
        VALUES (USER, SYSTIMESTAMP, 'G_Enrollments', 'Delete', :OLD.g_B# || ',' || :OLD.classid);
END enrollment_delete_trigger;
/

