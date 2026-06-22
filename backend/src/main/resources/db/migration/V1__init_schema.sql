-- Sequences
CREATE SEQUENCE locations_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE parameters_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE measurements_id_seq START WITH 1 INCREMENT BY 50;

-- Locations Table
CREATE TABLE locations (
    id BIGINT NOT NULL DEFAULT nextval('locations_id_seq'),
    external_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    city VARCHAR(150),
    country VARCHAR(10) NOT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_locations PRIMARY KEY (id),
    CONSTRAINT uq_locations_external_id UNIQUE (external_id)
);

-- Parameters Table
CREATE TABLE parameters (
    id BIGINT NOT NULL DEFAULT nextval('parameters_id_seq'),
    external_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    display_name VARCHAR(100),
    units VARCHAR(50) NOT NULL,
    description VARCHAR(1000),
    CONSTRAINT pk_parameters PRIMARY KEY (id),
    CONSTRAINT uq_parameters_external_id UNIQUE (external_id),
    CONSTRAINT uq_parameters_name UNIQUE (name)
);

-- Measurements Table
CREATE TABLE measurements (
    id BIGINT NOT NULL DEFAULT nextval('measurements_id_seq'),
    location_id BIGINT NOT NULL,
    parameter_id BIGINT NOT NULL,
    value DOUBLE PRECISION NOT NULL,
    measured_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_measurements PRIMARY KEY (id),
    CONSTRAINT fk_measurements_location FOREIGN KEY (location_id) REFERENCES locations (id),
    CONSTRAINT fk_measurements_parameter FOREIGN KEY (parameter_id) REFERENCES parameters (id)
);

-- Indices
CREATE INDEX idx_measurements_location ON measurements (location_id);
CREATE INDEX idx_measurements_parameter ON measurements (parameter_id);
CREATE INDEX idx_measurements_measured_at ON measurements (measured_at);
