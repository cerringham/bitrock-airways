CREATE TABLE airports (
    id int NOT NULL GENERATED ALWAYS AS IDENTITY,
    "name" varchar NOT NULL,
    active bool NOT NULL,
    date_inactivated timestamptz NULL,
    CONSTRAINT airports_pk PRIMARY KEY (id)
);

CREATE TABLE customers (
    id int NOT NULL GENERATED ALWAYS AS IDENTITY,
    "name" varchar NOT NULL,
    surname varchar NOT NULL,
    gender varchar NOT NULL,
    birthday date NOT NULL,
    handicap bit(1) NOT NULL,
    passport_number varchar NULL,
    id_number varchar NULL,
    active bool NOT NULL,
    date_inactivated timestamptz NULL,
    CONSTRAINT clients_pk PRIMARY KEY (id)
);

CREATE TABLE roles (
    id int NOT NULL GENERATED ALWAYS AS IDENTITY,
    "name" varchar NOT NULL,
    active bool NOT NULL,
    date_inactivated timestamptz NULL,
    CONSTRAINT roles_pk PRIMARY KEY (id)
);

CREATE TABLE vaccinations (
    id int NOT NULL GENERATED ALWAYS AS IDENTITY,
    "name" varchar NOT NULL,
    active bool NOT NULL,
    date_inactivated timestamptz NULL,
    CONSTRAINT vaccinations_pk PRIMARY KEY (id)
);

CREATE TABLE sites (
    id int NOT NULL GENERATED ALWAYS AS IDENTITY,
    "name" varchar NOT NULL,
    address varchar NULL,
    company_id int NOT NULL,
    active bool NOT NULL,
    date_inactivated timestamptz NULL,
    CONSTRAINT site_pk PRIMARY KEY (id)
);

CREATE TABLE companies (
    id int NOT NULL GENERATED ALWAYS AS IDENTITY,
    "name" varchar NOT NULL,
    "share" money NOT NULL,
    headquarters_id int NOT NULL,
    active bool NOT NULL,
    date_inactivated timestamptz NULL,
    CONSTRAINT company_pk PRIMARY KEY (id),
    CONSTRAINT company_fk FOREIGN KEY (headquarters_id) REFERENCES sites(id)
);

ALTER TABLE sites ADD CONSTRAINT site_fk FOREIGN KEY (company_id) REFERENCES companies(id);

CREATE TABLE routes (
    id int NOT NULL GENERATED ALWAYS AS IDENTITY,
    from_airport_id int NOT NULL,
    to_airport_id int NOT NULL,
    active bool NOT NULL,
    date_inactivated timestamptz NULL,
    CONSTRAINT routes_pk PRIMARY KEY (id),
    CONSTRAINT routes_from_fk FOREIGN KEY (from_airport_id) REFERENCES airports(id),
    CONSTRAINT routes_to_fk FOREIGN KEY (to_airport_id) REFERENCES airports(id)
);

CREATE TABLE customers_vaccinations (
    customer_id int NOT NULL,
    vaccination_id int NOT NULL,
    active bool NOT NULL,
    date_inactivated timestamptz NULL,
    CONSTRAINT customers_vaccinations_fk FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT customers_vaccinations_fk_1 FOREIGN KEY (vaccination_id) REFERENCES vaccinations(id)
);

CREATE TABLE employees (
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

CREATE TABLE flights_employees (
    flight_id int NOT NULL,
    employee_id int NOT NULL,
    active bool NOT NULL,
    date_inactivated timestamptz NULL,
    CONSTRAINT flights_employees_fk FOREIGN KEY (flight_id) REFERENCES flights(id),
    CONSTRAINT flights_employees_fk_1 FOREIGN KEY (employee_id) REFERENCES employees(id)
);

CREATE TABLE planes (
    id int NOT NULL GENERATED ALWAYS AS IDENTITY,
    model varchar NOT NULL,
    seats_number int NOT NULL,
    company_id int NOT NULL,
    active bool NOT NULL,
    date_inactivated timestamptz NULL,
    CONSTRAINT planes_pk PRIMARY KEY (id),
    CONSTRAINT planes_fk FOREIGN KEY (company_id) REFERENCES companies(id)
);

CREATE TABLE routes_constraint (
    route_id int NOT NULL,
    plane_id int NOT NULL,
    active bool NOT NULL,
    date_inactivated timestamptz NULL,
    CONSTRAINT routes_constraint_fk FOREIGN KEY (route_id) REFERENCES routes(id),
    CONSTRAINT routes_constraint_fk_1 FOREIGN KEY (plane_id) REFERENCES planes(id)
);

CREATE TABLE tickets (
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