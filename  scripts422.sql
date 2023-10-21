--Создание таблиц и связи между ними
CREATE TABLE person (
id int PRIMARY KEY,
name text,
age int,
have_driveLicense bool,
vehicle_id int,
FOREIGN KEY (vehicle_id) REFERENCES vehicle(id)
);


CREATE TABLE vehicle (
id int PRIMARY KEY,
mark text,
model text,
price int
);