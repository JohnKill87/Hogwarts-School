--JOIN-запрос, чтобы получить информацию обо всех студентах
SELECT s.id AS student_id, f.id AS faculty_id, *
FROM student s
JOIN faculty f ON s.faculty_id = f.id;

--JOIN-запрос, чтобы получить только тех студентов, у которых есть аватарки
SELECT s.*,
FROM student s
JOIN avatar a ON s.id = a.student_id;