CREATE TABLE storage (
     id           UUID DEFAULT uuid_generate_v4() NOT NULL,
     type         TEXT NOT NULL,
     "key"        TEXT NOT NULL,
     content_type TEXT NOT NULL,
     payload      TEXT NOT NULL,
     created_by   TEXT NOT NULL,
     created_at   TIMESTAMPTZ DEFAULT now() NOT NULL,
     updated_by   TEXT NOT NULL,
     updated_at   TIMESTAMPTZ DEFAULT now() NOT NULL,
     CONSTRAINT storage_pk PRIMARY KEY (id),
     CONSTRAINT storage_uq UNIQUE (type, "key")
);