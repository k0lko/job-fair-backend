-- Create booths table
CREATE TABLE booths (
                        id BIGSERIAL PRIMARY KEY,
                        booth_number VARCHAR(50) NOT NULL UNIQUE,
                        x INTEGER NOT NULL,
                        y INTEGER NOT NULL,
                        width INTEGER NOT NULL,
                        height INTEGER NOT NULL,
                        price INTEGER NOT NULL,
                        size VARCHAR(10) NOT NULL CHECK (size IN ('2x1', '4x1')),
    status VARCHAR(20) NOT NULL CHECK (status IN ('AVAILABLE', 'RESERVED', 'OCCUPIED')),
    company VARCHAR(255)
);

-- Create additionalServices table
CREATE TABLE additionalServices (
                          id BIGSERIAL PRIMARY KEY,
                          service_code VARCHAR(50) NOT NULL UNIQUE,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          price INTEGER NOT NULL,
                          vat INTEGER NOT NULL,
                          is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Create users table
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       name VARCHAR(255) NOT NULL,
                       is_logged_in BOOLEAN NOT NULL DEFAULT FALSE,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       last_login_at TIMESTAMP
);

-- Create reservations table
CREATE TABLE reservations (
                              id BIGSERIAL PRIMARY KEY,
                              booth_id BIGINT NOT NULL REFERENCES booths(id),
                              company_name VARCHAR(255) NOT NULL,
                              industry VARCHAR(255),
                              website VARCHAR(500),
                              contact_name VARCHAR(255) NOT NULL,
                              contact_email VARCHAR(255) NOT NULL UNIQUE,
                              contact_phone VARCHAR(50) NOT NULL,
                              invoice_company_name VARCHAR(255) NOT NULL,
                              invoice_address VARCHAR(500) NOT NULL,
                              invoice_nip VARCHAR(50) NOT NULL,
                              total_amount INTEGER NOT NULL,
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              agreed_to_terms BOOLEAN NOT NULL,
                              agreed_to_participation BOOLEAN NOT NULL,
                              agreed_to_conditions BOOLEAN NOT NULL
);

-- Create junction table for reservations and additionalServices
CREATE TABLE reservation_services (
                                      reservation_id BIGINT NOT NULL REFERENCES reservations(id) ON DELETE CASCADE,
                                      service_id BIGINT NOT NULL REFERENCES additionalServices(id) ON DELETE CASCADE,
                                      PRIMARY KEY (reservation_id, service_id)
);

-- Create indexes for better performance
CREATE INDEX idx_booths_status ON booths(status);
CREATE INDEX idx_booths_number ON booths(booth_number);
CREATE INDEX idx_reservations_email ON reservations(contact_email);
CREATE INDEX idx_reservations_booth ON reservations(booth_id);
CREATE INDEX idx_services_active ON additionalServices(is_active);

-- Insert initial booths data
INSERT INTO booths (booth_number, x, y, width, height, price, size, status) VALUES
-- Stoiska typu A (1x1m) - 1300 zł
('A1', 50, 50, 120, 60, 1300, '2x1', 'AVAILABLE'),
('A2', 190, 50, 120, 60, 1300, '2x1', 'AVAILABLE'),
('A3', 330, 50, 120, 60, 1300, '2x1', 'RESERVED'),
('A4', 470, 50, 120, 60, 1300, '2x1', 'AVAILABLE'),

-- Stoiska typu B (2x1m) - 1600 zł
('B1', 50, 130, 240, 60, 1600, '4x1', 'AVAILABLE'),
('B2', 310, 130, 240, 60, 1600, '4x1', 'OCCUPIED'),
('B3', 570, 130, 240, 60, 1600, '4x1', 'AVAILABLE'),

-- Stoiska typu A (1x1m) - drugi rząd
('C1', 50, 210, 120, 60, 1300, '2x1', 'AVAILABLE'),
('C2', 190, 210, 120, 60, 1300, '2x1', 'AVAILABLE'),
('C3', 330, 210, 120, 60, 1300, '2x1', 'RESERVED'),
('C4', 470, 210, 120, 60, 1300, '2x1', 'AVAILABLE'),

-- Stoiska typu B (2x1m) - trzeci rząd
('D1', 50, 290, 240, 60, 1600, '4x1', 'AVAILABLE'),
('D2', 310, 290, 240, 60, 1600, '4x1', 'AVAILABLE');

-- Update occupied booth with company name
UPDATE booths SET company = 'Tech Corp' WHERE booth_number = 'B2';

-- Insert initial additionalServices data
INSERT INTO additionalServices (service_code, name, description, price, vat, is_active) VALUES
                                                                                  ('chair', 'Krzesło', 'Dodatkowe krzesło na stoisko', 40, 23, TRUE),
                                                                                  ('panel', 'Panel zabudowy', 'Dodatkowy panel zabudowy (tylko stoiska typu B)', 65, 23, TRUE),
                                                                                  ('catering', 'Catering', 'Usługa cateringowa na osobę', 90, 8, TRUE),
                                                                                  ('internet', 'Internet', 'Szybki dostęp do internetu', 50, 23, TRUE),
                                                                                  ('electricity', 'Prąd', 'Podłączenie elektryczne', 100, 23, TRUE);