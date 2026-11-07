CREATE TABLE transcoding (
     "id"        UUID DEFAULT uuid_generate_v4() NOT NULL,
     list        TEXT NOT NULL,
     "domain"    TEXT NOT NULL,
     "language"  TEXT NOT NULL,
     value       TEXT NOT NULL,
     valid_from  DATE DEFAULT '1901-01-01' NOT NULL,
     valid_to    DATE DEFAULT '9999-12-31' NOT NULL,
     created_by  TEXT NOT NULL,
     created_at  TIMESTAMPTZ DEFAULT now() NOT NULL,
     updated_by  TEXT NOT NULL,
     updated_at  TIMESTAMPTZ DEFAULT now() NOT NULL,
     CONSTRAINT transcoding_pk PRIMARY KEY (id, list, domain, language, value, valid_from, valid_to),
     CONSTRAINT valid_c01 CHECK (valid_from < valid_to)
);