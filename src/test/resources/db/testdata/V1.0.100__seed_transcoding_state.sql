-- Test data: one STATE item (ISTAT code, Italian name, Belfiore code) valid from 2000-01-01 to the far future,
-- so that it is always valid for "today". All rows share the same id: they are one list item.
INSERT INTO transcoding (id, list, domain, language, value, valid_from, valid_to, created_by, updated_by)
VALUES
    ('11111111-1111-4111-8111-111111111111', 'state', 'istat',    'xx', '100',    '2000-01-01', '9999-12-31', 'test', 'test'),
    ('11111111-1111-4111-8111-111111111111', 'state', 'nexo',     'IT', 'Italia', '2000-01-01', '9999-12-31', 'test', 'test'),
    ('11111111-1111-4111-8111-111111111111', 'state', 'belfiore', 'xx', 'Z000',   '2000-01-01', '9999-12-31', 'test', 'test');
