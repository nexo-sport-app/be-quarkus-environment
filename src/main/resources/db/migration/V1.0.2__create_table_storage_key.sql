CREATE TABLE storage_key (
     type        TEXT NOT NULL,
     alt_key     TEXT NOT NULL,
     id          UUID NOT NULL,
     created_by  TEXT NOT NULL,
     created_at  TIMESTAMPTZ DEFAULT now() NOT NULL,
     updated_by  TEXT NOT NULL,
     updated_at  TIMESTAMPTZ DEFAULT now() NOT NULL,
     CONSTRAINT storage_key_pk PRIMARY KEY (type, alt_key),
     CONSTRAINT storage_fk FOREIGN KEY (id) REFERENCES storage(id) ON DELETE CASCADE
);