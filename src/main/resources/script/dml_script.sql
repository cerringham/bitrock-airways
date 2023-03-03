INSERT INTO customer (name, surname, gender, email, phone, birthday, handicap, passport_number, id_number, active, date_inactivated)
VALUES
('Paolo', 'Rossi', 'Male', 'paolo@gmail.com','3404535465', '1990-05-22', FALSE, 'AH3943', NULL, TRUE, NULL),
('Laura', 'Bianchi', 'Female', 'laura@gmail.com','3501245685', '1975-02-18', TRUE, NULL, 'HU457857', TRUE, NULL),
('Mauro', 'Neri', 'Male', 'mauro@gmail.com','4512454585', '1987-11-30', FALSE, NULL, 'AB45124', TRUE, NULL),
('Michela', 'Verdi', 'Female', 'michela@gmail.com','3201245856', '2000-07-10', FALSE, 'SE85452', NULL, TRUE, NULL);

INSERT INTO airport(name, international_code, active, date_inactivated)
VALUES
('Milano Malpensa', 'MXP', TRUE, NULL),
('New York', 'NYC', TRUE, NULL),
('Miami', 'MIA', TRUE, NULL),
('Los Angeles', 'LAX', TRUE, NULL),
('Boston', 'BOS', TRUE, NULL),
('Tokyo', 'TYO', TRUE, NULL);

INSERT INTO company(name, share, active, date_inactivated)
VALUES
('Bitrock Airways', 1500000, TRUE, NULL);

INSERT INTO vaccination(name, active, date_inactivated)
VALUES
('COVID I dose', TRUE, NULL),
('COVID II doses', TRUE, NULL),
('COVID III doses', TRUE, NULL),
('COVID IV doses', TRUE, NULL),
('COVID V doses', TRUE, NULL);

INSERT INTO customers_vaccination (customer_id, vaccination_id, active, date_inactivated)
VALUES
(1, 3, TRUE, NULL),
(2, 4, TRUE, NULL),
(3, 1, TRUE, NULL),
(4, 3, TRUE, NULL);

INSERT INTO role(name, active, date_inactivated)
VALUES
('PRESIDENT', TRUE, NULL),
('BOARD OF DIRECTORS', TRUE, NULL),
('PORTERS', TRUE, NULL),
('PILOT', TRUE, NULL),
('HOSTESS', TRUE, NULL),
('EMPLOYEE', TRUE, NULL),
('TECHNICIAN', TRUE, NULL),
('WORKER', TRUE, NULL);

INSERT INTO site(name, street, city, county, nation, is_headquarter, company_id, active, date_inactivated)
VALUES
('Bitrock Airways Italy', '8,  Montenapoleone Street', 'Milan', 'MI', 'Italy', TRUE, 1, TRUE, NULL),
('Bitrock Airways Rome', '58,  Condotti Street', 'Rome', 'RM', 'Italy', FALSE, 1, TRUE, NULL),
('Bitrock Airways Florence', '2,  Dei Martelli Street', 'Florence', 'FI', 'Italy', FALSE, 1, TRUE, NULL),
('Bitrock Airways France', '67, Place de la Concorde', 'Paris', 'XXX', 'France', FALSE, 1, TRUE, NULL),
('Bitrock Airways UK', '15, Downing Street', 'London', 'XXX', 'United Kingdom', TRUE, 1, TRUE, NULL),
('Bitrock Airways USA', '4578,  Seventh Ave', 'New York', 'XXX', 'USA', TRUE, 1, TRUE, NULL);

INSERT INTO employee(name, surname, email, phone, hiring_date, role_id, site_id, active, date_inactivated)
VALUES
('Vittorio', 'Marchesi', 'vittorio@gmail.com', '4581516265', '2000-02-23', 1, 1, TRUE, NULL),
('Marina', 'Bianchi', 'marina@gmail.com', '6541285486', '2015-07-12', 2, 1, TRUE, NULL),
('Anselmo', 'Rosso', 'anselmo@gmail.com', '7546352485', '2008-11-03', 2, 1, TRUE, NULL),
('Massimo', 'Pitti', 'massimo@gmail.com', '7851625486', '2020-05-30', 3, 2, TRUE, NULL),
('Lara', 'Neri', 'lara@gmail.com', '1452156245', '2019-12-03', 4, 6, TRUE, NULL),
('Andrea', 'Michetti', 'andrea@gmail.com', '156321456', '2010-10-16', 4, 5, TRUE, NULL),
('Michelle', 'Black', 'michelle@gmail.com', '1472536256', '2022-09-13', 5, 6, TRUE, NULL),
('Antony', 'Green', 'antony@gmail.com', '7415263562', '2017-07-08', 6, 6, TRUE, NULL),
('Paul', 'Jones', 'paul@gmail.com', '1495241635', '2014-11-21', 7, 5, TRUE, NULL),
('Lorenzo', 'Gotti', 'lorenzo@gmail.com', '7451249857', '2023-01-01', 8, 1, TRUE, NULL),
('Patrizio', 'Germano', 'patrizio@gmail.com', '7895462154', '2016-02-17', 8, 1, TRUE, NULL);

INSERT INTO plane(model, quantity, seats_count, active, date_inactivated)
VALUES
('Boeing 787', 2, 400, TRUE, NULL),
('Boeing 777', 2, 320, TRUE, NULL);

INSERT INTO fidelity_points(customer_id, points, active, date_inactivated)
VALUES
(1, 0, FALSE, NULL),
(2, 100, TRUE, NULL),
(3, 500, TRUE, NULL),
(4, 0, FALSE, NULL);

INSERT INTO route (departure_airport_id, arrival_airport_id, active, date_inactivated)
VALUES
(1, 2, TRUE, NULL),
(2, 1, TRUE, NULL),
(1, 3, TRUE, NULL),
(3, 1, TRUE, NULL),
(2, 4, TRUE, NULL),
(4, 2, TRUE, NULL),
(3, 5, TRUE, NULL),
(5, 3, TRUE, NULL),
(2, 6, TRUE, NULL),
(6, 2, TRUE, NULL);

INSERT INTO routes_constraint(route_id, plane_id, active, date_inactivated)
VALUES
(9, 1, TRUE, NULL),
(10, 1, TRUE, NULL);

INSERT INTO flight(route_id,plane_id,depart_time,arrival_time,active,date_inactivated)
VALUES
(1, 1, '2023-02-28 19:10:00-00','2023-02-28 20:10:00-00', TRUE, NULL),
(3, 1, '2023-03-15 08:00:00-00','2023-03-15 10:50:00-00', TRUE, NULL),
(7, 1, '2023-03-16 12:45:00-00','2023-03-16 13:45:00-00', TRUE, NULL),
(5, 1, '2023-03-17 14:15:00-00','2023-03-17 19:25:00-00', TRUE, NULL),
(8, 1, '2023-03-18 17:50:00-00','2023-03-18 21:50:00-00', TRUE, NULL);

INSERT INTO ticket(customer_id, flight_id, reservation_code, seat_number, date_bought, price, promotion, active, date_inactivated)
VALUES
(1,1,'AX45D5','34F', '2023-01-15 19:10:00-00', 160, FALSE,TRUE,NULL),
(1,1,'YU7JD9','2A', '2023-02-10 11:10:00-00', 100, FALSE,TRUE,NULL),
(1,1,'GH457H','15D', '2023-02-02 08:45:00-00', 85, TRUE,TRUE,NULL);