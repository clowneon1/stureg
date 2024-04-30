drop table g_enrollments;
drop table score_grade;
drop table classes;
drop table course_credit;
drop table prerequisites;
drop table courses;
drop table students;
drop table logs;

create table students (B# char(9) primary key check (B# like 'B%'),
first_name varchar2(15) not null, last_name varchar2(15) not null, st_level varchar2(10) 
check (st_level in ('freshman', 'sophomore', 'junior', 'senior', 'graduate')), 
gpa number(3,2) check (gpa between 0 and 4.0), email varchar2(20) unique,
bdate date);

create table courses (dept_code varchar2(4), course# number(3)
check (course# between 100 and 799), title varchar2(20) not null,
primary key (dept_code, course#));

create table prerequisites (dept_code varchar2(4) not null,
course# number(3) not null, pre_dept_code varchar2(4) not null,
pre_course# number(3) not null,
primary key (dept_code, course#, pre_dept_code, pre_course#),
foreign key (dept_code, course#) references courses on delete cascade,
foreign key (pre_dept_code, pre_course#) references courses on delete cascade);

create table course_credit (course# number(3) primary key,
check (course# between 100 and 799), credits number(1) check (credits in (3, 4)),
check ((course# < 500 and credits = 4) or (course# >= 500 and credits = 3)));

create table classes (classid char(5) primary key check (classid like 'c%'), 
dept_code varchar2(4) not null, course# number(3) not null, 
sect# number(2), year number(4), semester varchar2(8) 
check (semester in ('Spring', 'Fall', 'Summer 1', 'Summer 2', 'Winter')), 
limit number(3), class_size number(3), room varchar2(10), 
foreign key (dept_code, course#) references courses on delete cascade, 
unique(dept_code, course#, sect#, year, semester), check (class_size <= limit));


create table score_grade (score number(4, 2) primary key,
lgrade varchar2(2) check (lgrade in ('A', 'A-', 'B+', 'B', 'B-', 'C+', 'C', 'C-','D', 'F', 'I', 'S+')));

create table g_enrollments (g_B# char(9) references students, classid char(5) references classes, 
score number(4, 2) references score_grade, primary key (g_B#, classid));

create table logs (log# NUMBER DEFAULT log_sequence.NEXTVAL primary key, 
user_name varchar2(10) not null, 
op_time date not null, 
table_name varchar2(13) not null, 
operation varchar2(6) not null, 
tuple_keyvalue varchar2(200));

insert into students values ('B00000001', 'Anne', 'Broder', 'graduate', 3.7, 'broder@bu.edu', '17-JAN-94');
insert into students values ('B00000002', 'Terry', 'Buttler', 'senior', 3.0, 'buttler@bu.edu', '28-MAY-93');
insert into students values ('B00000003', 'Tracy', 'Wang', 'junior', 4.0, 'wang@bu.edu','06-AUG-97');
insert into students values ('B00000004', 'Barbara', 'Callan', 'sophomore', 2.5, 'callan@bu.edu', '18-OCT-95');
insert into students values ('B00000005', 'Jack', 'Smith', 'sophomore', 3.2, 'smith@bu.edu', '18-OCT-95');
insert into students values ('B00000006', 'Terry', 'Zillman', 'graduate', 4.0, 'zillman@bu.edu', '15-JUN-92');
insert into students values ('B00000007', 'Becky', 'Lee', 'junior', 4.0, 'lee@bu.edu', '12-NOV-96');
insert into students values ('B00000008', 'Tom', 'Baker', 'senior', null, 'baker@bu.edu', '23-DEC-97');
insert into students values ('B00000009', 'Ben', 'Liu', 'graduate', 3.8, 'liu@bu.edu', '18-MAR-96');
insert into students values ('B00000010', 'praful', 'rajput', 'graduate', 3.9, 'p@rajput.edu', '12-OCT-94');
insert into students values ('B00000011', 'Art', 'Chang', 'freshman', 4.0, 'chang@bu.edu', '08-JUN-93');
insert into students values ('B00000012', 'Tara', 'Ramesh', 'senior', 3.98, 'ramesh@bu.edu', '29-JUL-98');
insert into students values ('B00000013', 'Penguin', 'Mandloi', 'graduate', 3.75, 'penguin@mandloi.com', '28-AUG-98' );


insert into courses values ('CS', 432, 'database systems');
insert into courses values ('Math', 314, 'discrete math');
insert into courses values ('CS', 240, 'data structure');
insert into courses values ('CS', 575, 'algorithms');
insert into courses values ('CS', 532, 'database systems');
insert into courses values ('CS', 550, 'operating systems');
insert into courses values ('CS', 536, 'machine learning');
insert into courses values ('CS', 101, 'AI');
insert into courses values ('CS', 102, 'Apti');
insert into courses values ('CS', 103, 'Java');
insert into courses values ('CS', 104, 'AWS');
insert into courses values ('CS', 105, 'AWS PRO');
insert into courses values ('CS', 106, 'AWS Ass');



insert into prerequisites values ('CS', '432', 'Math', '314');
insert into prerequisites values ('CS', '432', 'CS', '240');
insert into prerequisites values ('CS', '532', 'CS', '432');
insert into prerequisites values ('CS', '536', 'CS', '532');
insert into prerequisites values ('CS', '575', 'CS', '240');
insert into prerequisites values ('CS', '104', 'CS', '103');
insert into prerequisites values ('CS', '101', 'CS', '104');
insert into prerequisites values ('CS', '105', 'CS', '104');
insert into prerequisites values ('CS', '106', 'CS', '104');




insert into course_credit values (432, 4);
insert into course_credit values (314, 4);
insert into course_credit values (240, 4);
insert into course_credit values (536, 3);
insert into course_credit values (532, 3);
insert into course_credit values (550, 3);
insert into course_credit values (575, 3);
insert into course_credit values (101, 4);
insert into course_credit values (102, 4);
insert into course_credit values (103, 4);
insert into course_credit values (104, 4);
insert into course_credit values (105, 4);
insert into course_credit values (106, 4);


insert into classes values ('c0001', 'CS', 432, 1, 2021, 'Spring', 13, 12, 'LH 005');
insert into classes values ('c0002', 'Math', 314, 1, 2020, 'Fall', 13, 12, 'LH 009');
insert into classes values ('c0003', 'Math', 314, 2, 2020, 'Fall', 14, 11, 'LH 009');
insert into classes values ('c0004', 'CS', 432, 1, 2020, 'Spring', 13, 13, 'SW 222');
insert into classes values ('c0005', 'CS', 536, 1, 2021, 'Spring', 14, 13, 'LH 003');
insert into classes values ('c0006', 'CS', 532, 1, 2021, 'Spring', 10, 9, 'LH 005');
insert into classes values ('c0007', 'CS', 550, 1, 2021, 'Spring', 11, 11, 'WH 155');
insert into classes values ('c0008', 'CS', 101, 1, 2024, 'Spring', 30, 11, 'LH 123');
insert into classes values ('c0009', 'CS', 102, 1, 2024, 'Spring', 30, 30, 'WH 123');
insert into classes values ('c0010', 'CS', 103, 1, 2024, 'Spring', 40, 14, 'WH 201');
insert into classes values ('c0011', 'CS', 104, 1, 2024, 'Spring', 30, 16, 'WH 12');
insert into classes values ('c0012', 'CS', 106, 1, 2024, 'Spring', 30, 16, 'WH 12');
insert into classes values ('c0013', 'CS', 105, 1, 2024, 'Spring', 30, 16, 'WH 12');




insert into score_grade values (92, 'A');
insert into score_grade values (79.5, 'B');
insert into score_grade values (84, 'B+');
insert into score_grade values (72.8, 'B-');
insert into score_grade values (76, 'B');
insert into score_grade values (65.35, 'C');
insert into score_grade values (94, 'A');
insert into score_grade values (82, 'B+');
insert into score_grade values (88, 'A-');
insert into score_grade values (68, 'C+');
insert into score_grade values (69, 'S+');

insert into g_enrollments values ('B00000001', 'c0001', 92);
insert into g_enrollments values ('B00000002', 'c0002', 76);
insert into g_enrollments values ('B00000003', 'c0004', 94);
insert into g_enrollments values ('B00000004', 'c0004', 65.35);
insert into g_enrollments values ('B00000004', 'c0005', 82);
insert into g_enrollments values ('B00000005', 'c0006', 79.5);
insert into g_enrollments values ('B00000006', 'c0006', 92);
insert into g_enrollments values ('B00000001', 'c0002', 68);
insert into g_enrollments values ('B00000003', 'c0005', null);
insert into g_enrollments values ('B00000007', 'c0007', 92);
insert into g_enrollments values ('B00000001', 'c0003', 76);
insert into g_enrollments values ('B00000001', 'c0006', 72.8);
insert into g_enrollments values ('B00000001', 'c0004', 94);
insert into g_enrollments values ('B00000001', 'c0005', 76);
insert into g_enrollments values ('B00000003', 'c0001', 84);
insert into g_enrollments values ('B00000005', 'c0001', 76); 
insert into g_enrollments values ('B00000013', 'c0001', 76); 
insert into g_enrollments values ('B00000013', 'c0002', 82); 
insert into g_enrollments values ('B00000010', 'c0012', 69); 
insert into g_enrollments values ('B00000010', 'c0011', 88); 
insert into g_enrollments values ('B00000010', 'c0010', 92); 
insert into g_enrollments values ('B00000010', 'c0009', 76); 
insert into g_enrollments values ('B00000010', 'c0008', 79.5);

