-- Aktualizacja constraintu CHECK i wartości size w tabeli booths

ALTER TABLE booths DROP CONSTRAINT IF EXISTS booths_size_check;

UPDATE booths SET size = 'SIZE_1X1' WHERE size IN ('1x1');
UPDATE booths SET size = 'SIZE_2X1' WHERE size IN ('2x1', '4x1');

ALTER TABLE booths
    ADD CONSTRAINT booths_size_check
        CHECK (size IN ('SIZE_1X1', 'SIZE_2X1'));
