CREATE DATABASE bitrock_airways;

CREATE TABLE airport (
                         id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
                         name VARCHAR(100) NOT NULL,
                         international_code VARCHAR(3) NOT NULL,
                         active BOOLEAN NOT NULL,
                         date_inactivated timestamptz NULL,
                         CONSTRAINT airport_pk PRIMARY KEY (id)
);

CREATE TABLE customer (
                          id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
                          name VARCHAR(50) NOT NULL,
                          surname VARCHAR(50) NOT NULL,
                          gender VARCHAR(10) NOT NULL,
                          email VARCHAR(320) NOT NULL,
                          phone VARCHAR(50) NOT NULL,
                          birthday DATE NOT NULL,
                          handicap BOOLEAN NOT NULL,
                          passport_number VARCHAR(15) NULL,
                          id_number VARCHAR(15) NULL,
                          active BOOLEAN NOT NULL,
                          date_inactivated timestamptz NULL,
                          CONSTRAINT client_pk PRIMARY KEY (id)
);

CREATE TABLE role (
                      id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
                      name VARCHAR(50) NOT NULL,
                      active BOOLEAN NOT NULL,
                      date_inactivated timestamptz NULL,
                      CONSTRAINT role_pk PRIMARY KEY (id)
);

CREATE TABLE vaccination (
                             id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
                             name VARCHAR(50) NOT NULL,
                             active BOOLEAN NOT NULL,
                             date_inactivated timestamptz NULL,
                             CONSTRAINT vaccination_pk PRIMARY KEY (id)
);

CREATE TABLE site (
                      id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
                      name VARCHAR(50) NOT NULL,
                      street VARCHAR(50) NOT NULL,
                      city VARCHAR(50) NOT NULL,
                      county VARCHAR(50) NOT NULL,
                      nation VARCHAR(50) NOT NULL,
                      is_headquarter BOOLEAN NOT NULL,
                      company_id INT NOT NULL,
                      active BOOLEAN NOT NULL,
                      date_inactivated timestamptz NULL,
                      CONSTRAINT site_pk PRIMARY KEY (id)
);

CREATE TABLE company (
                         id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
                         name VARCHAR(50) NOT NULL,
                         share money NOT NULL,
                         active BOOLEAN NOT NULL,
                         date_inactivated timestamptz NULL,
                         CONSTRAINT company_pk PRIMARY KEY (id)
);

CREATE TABLE route (
                       id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
                       departure_airport_id INT NOT NULL,
                       arrival_airport_id INT NOT NULL,
                       active BOOLEAN NOT NULL,
                       date_inactivated timestamptz NULL,
                       CONSTRAINT route_pk PRIMARY KEY (id),
                       CONSTRAINT route_from_fk FOREIGN KEY (departure_airport_id) REFERENCES airport(id),
                       CONSTRAINT route_to_fk FOREIGN KEY (arrival_airport_id) REFERENCES airport(id)
);

CREATE TABLE customers_vaccination (
                                       id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
                                       customer_id INT NOT NULL,
                                       vaccination_id INT NOT NULL,
                                       active bool NOT NULL,
                                       date_inactivated timestamptz NULL,
                                       CONSTRAINT customers_vaccination_pk PRIMARY KEY (id),
                                       CONSTRAINT customers_vaccination_fk FOREIGN KEY (customer_id) REFERENCES customer(id),
                                       CONSTRAINT customers_vaccination_fk_1 FOREIGN KEY (vaccination_id) REFERENCES vaccination(id)
);

CREATE TABLE employee (
                          id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
                          name VARCHAR(50) NOT NULL,
                          surname VARCHAR(50) NOT NULL,
                          email VARCHAR(320) NOT NULL,
                          phone VARCHAR(50) NOT NULL,
                          hiring_date DATE NOT NULL,
                          role_id INT NOT NULL,
                          site_id INT NOT NULL,
                          active bool NOT NULL,
                          date_inactivated timestamptz NULL,
                          CONSTRAINT employee_pk PRIMARY KEY (id),
                          CONSTRAINT employee_site_fk FOREIGN KEY (site_id) REFERENCES site(id),
                          CONSTRAINT employee_role_fk FOREIGN KEY (role_id) REFERENCES role(id)
);

CREATE TABLE fidelity_points (
                                 id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
                                 customer_id INT NOT NULL,
                                 points INT NOT NULL,
                                 active BOOLEAN NOT NULL,
                                 date_inactivated timestamptz NULL,
                                 CONSTRAINT fidelity_points_pk PRIMARY KEY (id),
                                 CONSTRAINT fidelity_points_fk FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE TABLE plane (
                       id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
                       model VARCHAR(50) NOT NULL,
                       seats_count INT NOT NULL,
                       active BOOLEAN NOT NULL,
                       date_inactivated timestamptz NULL,
                       CONSTRAINT plane_pk PRIMARY KEY (id)
);

CREATE TABLE flight (
                        id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
                        route_id INT NOT NULL,
                        plane_id INT NOT NULL,
                        depart_time timestamptz NOT NULL,
                        arrival_time timestamptz NOT NULL,
                        active BOOLEAN NOT NULL,
                        date_inactivated timestamptz NULL,
                        CONSTRAINT flight_pk PRIMARY KEY (id),
                        CONSTRAINT flight_fk FOREIGN KEY (route_id) REFERENCES route(id),
                        CONSTRAINT flight_fk_1 FOREIGN KEY (plane_id) REFERENCES plane(id)
);

CREATE TABLE routes_constraint (
                                   id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
                                   route_id INT NOT NULL,
                                   plane_id INT NOT NULL,
                                   active BOOLEAN NOT NULL,
                                   date_inactivated timestamptz NULL,
                                   CONSTRAINT routes_constraint_pk PRIMARY KEY (id),
                                   CONSTRAINT routes_constraint_fk FOREIGN KEY (route_id) REFERENCES route(id),
                                   CONSTRAINT routes_constraint_fk_1 FOREIGN KEY (plane_id) REFERENCES plane(id)
);

CREATE TABLE ticket (
                        id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
                        customer_id INT NOT NULL,
                        flight_id INT NOT NULL,
                        reservation_code VARCHAR(15) NOT NULL,
                        seat_number VARCHAR(10) NOT NULL,
                        date_bought timestamptz NOT NULL,
                        price money NULL,
                        promotion BOOLEAN NULL,
                        active BOOLEAN NOT NULL,
                        date_inactivated timestamptz NULL,
                        CONSTRAINT ticket_pk PRIMARY KEY (id),
                        CONSTRAINT ticket_customers_fk FOREIGN KEY (customer_id) REFERENCES customer(id),
                        CONSTRAINT ticket_flights_fk FOREIGN KEY (flight_id) REFERENCES flight(id)
);