
DROP TABLE IF EXISTS reservation_services CASCADE;
DROP TABLE IF EXISTS reservations CASCADE;
DROP TABLE IF EXISTS additionalServices CASCADE;
DROP TABLE IF EXISTS booths CASCADE;
DROP TABLE IF EXISTS users CASCADE;

--create booths
CREATE TABLE booths (
                        id BIGSERIAL PRIMARY KEY,
                        booth_number VARCHAR(50) NOT NULL UNIQUE,
                        x INTEGER NOT NULL,
                        y INTEGER NOT NULL,
                        width INTEGER NOT NULL,
                        height INTEGER NOT NULL,
                        price INTEGER NOT NULL,
                        size VARCHAR(10) NOT NULL CHECK (size IN ('1x1', '2x1')),
                        status VARCHAR(20) NOT NULL CHECK (status IN ('AVAILABLE', 'RESERVED', 'OCCUPIED')),
                        company VARCHAR(255)
);

-- Additional Services
CREATE TABLE additionalServices (
                                    id BIGSERIAL PRIMARY KEY,
                                    service_code VARCHAR(50) NOT NULL UNIQUE,
                                    name VARCHAR(255) NOT NULL,
                                    description TEXT,
                                    price INTEGER NOT NULL,
                                    vat INTEGER NOT NULL,
                                    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Users
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       name VARCHAR(255) NOT NULL,
                       is_logged_in BOOLEAN NOT NULL DEFAULT FALSE,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       last_login_at TIMESTAMP
);

-- Reservations
CREATE TABLE reservations (
                              id BIGSERIAL PRIMARY KEY,
                              booth_id BIGINT NOT NULL REFERENCES booths(id) ON DELETE CASCADE,
                              company_name VARCHAR(255) NOT NULL,
                              industry VARCHAR(255),
                              website VARCHAR(500),
                              contact_name VARCHAR(255) NOT NULL,
                              contact_email VARCHAR(255) NOT NULL UNIQUE,
                              contact_phone VARCHAR(50) NOT NULL,
                              invoice_company_name VARCHAR(255) NOT NULL,
                              invoice_street VARCHAR(255) NOT NULL,
                              invoice_postal_code VARCHAR(20) NOT NULL,
                              invoice_city VARCHAR(255) NOT NULL,
                              invoice_country VARCHAR(255) DEFAULT 'Polska',
                              invoice_nip VARCHAR(50) NOT NULL,
                              total_amount INTEGER NOT NULL,
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              agreed_to_terms BOOLEAN NOT NULL,
                              agreed_to_participation BOOLEAN NOT NULL,
                              agreed_to_conditions BOOLEAN NOT NULL
);

-- Junction Table: Reservation ↔ Services
CREATE TABLE reservation_services (
                                      reservation_id BIGINT NOT NULL REFERENCES reservations(id) ON DELETE CASCADE,
                                      service_id BIGINT NOT NULL REFERENCES additionalServices(id) ON DELETE CASCADE,
                                      PRIMARY KEY (reservation_id, service_id)
);

-- ========================
-- INDEXES
-- ========================
CREATE INDEX idx_booths_status ON booths(status);
CREATE INDEX idx_booths_number ON booths(booth_number);
CREATE INDEX idx_reservations_email ON reservations(contact_email);
CREATE INDEX idx_reservations_booth ON reservations(booth_id);
CREATE INDEX idx_services_active ON additionalServices(is_active);

-- ========================
-- 🧰 INITIAL DATA
-- ========================

-- Booths
INSERT INTO booths (booth_number, x, y, width, height, price, size, status) VALUES
                                                                                ('A1', 50, 50, 120, 60, 1300, '1x1', 'AVAILABLE'),
                                                                                ('A2', 190, 50, 120, 60, 1300, '1x1', 'AVAILABLE'),
                                                                                ('A3', 330, 50, 120, 60, 1300, '1x1', 'RESERVED'),
                                                                                ('A4', 470, 50, 120, 60, 1300, '1x1', 'AVAILABLE'),

                                                                                ('B1', 50, 130, 240, 60, 1600, '2x1', 'AVAILABLE'),
                                                                                ('B2', 310, 130, 240, 60, 1600, '2x1', 'OCCUPIED'),
                                                                                ('B3', 570, 130, 240, 60, 1600, '2x1', 'AVAILABLE'),

                                                                                ('C1', 50, 210, 120, 60, 1300, '1x1', 'AVAILABLE'),
                                                                                ('C2', 190, 210, 120, 60, 1300, '1x1', 'AVAILABLE'),
                                                                                ('C3', 330, 210, 120, 60, 1300, '1x1', 'RESERVED'),
                                                                                ('C4', 470, 210, 120, 60, 1300, '1x1', 'AVAILABLE'),

                                                                                ('D1', 50, 290, 240, 60, 1600, '2x1', 'AVAILABLE'),
                                                                                ('D2', 310, 290, 240, 60, 1600, '2x1', 'AVAILABLE');

-- Update Booths
UPDATE booths SET company = 'Tech Corp' WHERE booth_number = 'B2';

-- Additional Services
INSERT INTO additionalServices (service_code, name, description, price, vat, is_active) VALUES
                                                                                            ('ELEC01', 'Elektryczność', 'Gniazdko elektryczne 230V przy stoisku', 150, 23, TRUE),
                                                                                            ('TABLE01', 'Dodatkowy stolik', 'Dodatkowy stolik konferencyjny', 100, 23, TRUE),
                                                                                            ('CHAIR01', 'Krzesła', 'Dwa dodatkowe krzesła', 80, 23, TRUE),
                                                                                            ('WIFI01', 'Internet', 'Dostęp do Wi-Fi premium', 50, 23, TRUE);

-- ========================
-- COMMENTS (Documentation)
-- ========================
COMMENT ON TABLE booths IS 'Stanowiska targowe ';
COMMENT ON COLUMN booths.size IS 'Rozmiar stoiska: 1x1 lub 2x1';
COMMENT ON TABLE reservations IS 'Rezerwacje stanowisk z pełnym adresem fakturowym';
COMMENT ON COLUMN reservations.invoice_street IS 'Ulica i numer budynku';
COMMENT ON COLUMN reservations.invoice_postal_code IS 'Kod pocztowy';
COMMENT ON COLUMN reservations.invoice_city IS 'Miasto';
COMMENT ON COLUMN reservations.invoice_country IS 'Kraj (domyślnie Polska)';
COMMENT ON TABLE additionalServices IS 'Dodatkowe usługi dla stoisk targowych';
COMMENT ON TABLE users IS 'Użytkownicy systemu (firmy lub administratorzy)';
