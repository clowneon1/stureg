CREATE OR REPLACE TRIGGER insert_update_class_size_trigger
AFTER INSERT ON g_enrollments
FOR EACH ROW
BEGIN
    UPDATE classes
    SET class_size = class_size + 1
    WHERE classid = :NEW.classid;
    DBMS_OUTPUT.PUT_LINE('Trigger +1');
END update_class_size_trigger;
/




CREATE OR REPLACE TRIGGER delete_update_class_size_trigger
AFTER DELETE ON g_enrollments
FOR EACH ROW
BEGIN
    UPDATE classes
    SET class_size = class_size - 1
    WHERE classid = :OLD.classid;
    DBMS_OUTPUT.PUT_LINE('Trigger -1');
END update_class_size_trigger;
/

delete from g_enrollments where classid = 'c0001' and g_B# = 'B00000001';
insert into g_enrollments values ('B00000001', 'c0001', 76); 
UPDATE g_enrollments SET classid = 'c0010' WHERE g_B# = 'B00000010' AND classid = 'c0006';
update classes set class_size = 11, limit = 20 where CLASSID= 'c0001';
insert into students values ('B00000004', 'Barbara', 'Callan', 'sophomore', 2.5, 'callan@bu.edu', '18-OCT-95');




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


CREATE OR REPLACE TRIGGER delete_student_enrollments
BEFORE DELETE ON students
FOR EACH ROW
BEGIN
    DELETE FROM g_enrollments
    WHERE g_B# = :OLD.B#;
    DBMS_OUTPUT.PUT_LINE('Trigger enrollment');
END;
/
