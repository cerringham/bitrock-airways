CREATE DATABASE bitrock_airways;

CREATE TABLE airport (
    id int NOT NULL GENERATED ALWAYS AS IDENTITY,
    "name" VARCHAR(100) NOT NULL,
    international_code VARCHAR(3) NOT NULL,
    active BOOLEAN NOT NULL,
    date_inactivated timestamptz NULL,
    CONSTRAINT airports_pk PRIMARY KEY (id)
);

CREATE TABLE customer (
    id int NOT NULL GENERATED ALWAYS AS IDENTITY,
    "name" VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    birthday DATE NOT NULL,
    handicap BOOLEAN NOT NULL,
    passport_number VARCHAR NULL,
    id_number VARCHAR NULL,
    active BOOLEAN NOT NULL,
    date_inactivated timestamptz NULL,
    CONSTRAINT clients_pk PRIMARY KEY (id)
);

CREATE TABLE role (
    id int NOT NULL GENERATED ALWAYS AS IDENTITY,
    "name" varchar NOT NULL,
    active bool NOT NULL,
    date_inactivated timestamptz NULL,
    CONSTRAINT roles_pk PRIMARY KEY (id)
);

CREATE TABLE vaccination (
    id int NOT NULL GENERATED ALWAYS AS IDENTITY,
    "name" varchar NOT NULL,
    active bool NOT NULL,
    date_inactivated timestamptz NULL,
    CONSTRAINT vaccinations_pk PRIMARY KEY (id)
);

CREATE TABLE site (
    id int NOT NULL GENERATED ALWAYS AS IDENTITY,
    "name" varchar NOT NULL,
    street varchar NOT NULL,
    city varchar NOT NULL,
    county varchar NOT NULL,
    nation varchar NOT NULL,
    is_headquarter BOOLEAN NOT NULL,
    company_id int NOT NULL,
    active bool NOT NULL,
    date_inactivated timestamptz NULL,
    CONSTRAINT site_pk PRIMARY KEY (id)
);

CREATE TABLE company (
    id int NOT NULL GENERATED ALWAYS AS IDENTITY,
    "name" varchar NOT NULL,
    "share" money NOT NULL,
    active bool NOT NULL,
    date_inactivated timestamptz NULL,
    CONSTRAINT company_pk PRIMARY KEY (id),
);

CREATE TABLE route (
    id int NOT NULL GENERATED ALWAYS AS IDENTITY,
    departure_airport_id int NOT NULL,
    arrival_airport_id int NOT NULL,
    active bool NOT NULL,
    date_inactivated timestamptz NULL,
    CONSTRAINT routes_pk PRIMARY KEY (id),
    CONSTRAINT routes_from_fk FOREIGN KEY (from_airport_id) REFERENCES airports(id),
    CONSTRAINT routes_to_fk FOREIGN KEY (to_airport_id) REFERENCES airports(id)
);

CREATE TABLE customers_vaccination (
    customer_id int NOT NULL,
    vaccination_id int NOT NULL,
    active bool NOT NULL,
    date_inactivated timestamptz NULL,
    CONSTRAINT customers_vaccinations_fk FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT customers_vaccinations_fk_1 FOREIGN KEY (vaccination_id) REFERENCES vaccinations(id)
);

CREATE TABLE employee (
    id int NOT NULL GENERATED ALWAYS AS IDENTITY,
    "name" varchar NOT NULL,
    surname varchar NOT NULL,
    hiring_date date NOT NULL,
    role_id int NOT NULL,
    site_id int NOT NULL,
    active bool NOT NULL,
    date_inactivated timestamptz NULL,
    CONSTRAINT employee_pk PRIMARY KEY (id),
    CONSTRAINT employee_fk FOREIGN KEY (site_id) REFERENCES sites(id),
    CONSTRAINT employees_fk FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE fidelity_points (
    customer_id int NOT NULL,
    points int NOT NULL,
    active bool NOT NULL,
    date_inactivated timestamptz NULL,
    CONSTRAINT fidelity_points_fk FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE TABLE flights (
    id int NOT NULL GENERATED ALWAYS AS IDENTITY,
    route_id int NOT NULL,
    depart_time timestamptz NOT NULL,
    arrival_time timestamp NOT NULL,
    active bool NOT NULL,
    date_inactivated timestamptz NULL,
    CONSTRAINT flights_pk PRIMARY KEY (id),
    CONSTRAINT flights_fk FOREIGN KEY (route_id) REFERENCES routes(id)
);

CREATE TABLE plane (
    id int NOT NULL GENERATED ALWAYS AS IDENTITY,
    model varchar NOT NULL,
    seats_number int NOT NULL,
    active bool NOT NULL,
    date_inactivated timestamptz NULL,
    CONSTRAINT planes_pk PRIMARY KEY (id),
);

CREATE TABLE routes_constraint (
 id int NOT NULL GENERATED ALWAYS AS IDENTITY,
    route_id int NOT NULL,
    plane_id int NOT NULL,
    active bool NOT NULL,
    date_inactivated timestamptz NULL,
    CONSTRAINT routes_constraint_pk PRIMARY KEY (id),
    CONSTRAINT routes_constraint_fk FOREIGN KEY (route_id) REFERENCES routes(id),
    CONSTRAINT routes_constraint_fk_1 FOREIGN KEY (plane_id) REFERENCES planes(id)
);

CREATE TABLE ticket (
    id int NOT NULL GENERATED ALWAYS AS IDENTITY,
    customer_id int NOT NULL,
    flight_id int NOT NULL,
    price money NULL,
    promotion bool NULL,
    active bool NOT NULL,
    date_inactivated timestamptz NULL,
    CONSTRAINT tickets_pk PRIMARY KEY (id),
    CONSTRAINT tickets_customers_fk FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT tickets_flights_fk FOREIGN KEY (flight_id) REFERENCES flights(id)
);