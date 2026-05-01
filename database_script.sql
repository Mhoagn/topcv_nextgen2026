CREATE DATABASE nextgen2026;

Use nextgen2026;

CREATE TABLE users (
    id          CHAR(36)        NOT NULL DEFAULT (uuid()),
    email       VARCHAR(255)    NOT NULL,
    role        ENUM('admin', 'staff') NOT NULL DEFAULT 'staff',
    PRIMARY KEY (id),
    UNIQUE KEY uq_users_email (email)
);

CREATE TABLE forms (
    id            CHAR(36)      NOT NULL DEFAULT (uuid()),
    title         VARCHAR(255)  NOT NULL,
    description   TEXT,
    display_order INT           NOT NULL DEFAULT 0,
    status        ENUM('active', 'draft') NOT NULL DEFAULT 'draft',
    created_by    CHAR(36)      NOT NULL,
    created_at    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 
    PRIMARY KEY (id),
    KEY idx_forms_status_order (status, display_order),
    CONSTRAINT fk_forms_created_by FOREIGN KEY (created_by)
        REFERENCES users (id) ON DELETE RESTRICT
);

CREATE TABLE fields (
    id               CHAR(36)     NOT NULL DEFAULT (uuid()),
    form_id          CHAR(36)     NOT NULL,
    label            VARCHAR(255) NOT NULL,
    type             ENUM('text', 'number', 'date', 'color', 'select') NOT NULL,
    display_order    INT          NOT NULL DEFAULT 0,
    required         TINYINT(1)   NOT NULL DEFAULT 0,
    options          JSON,
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 
    PRIMARY KEY (id),
    KEY idx_fields_form_order (form_id, display_order),
    CONSTRAINT fk_fields_form FOREIGN KEY (form_id)
        REFERENCES forms (id) ON DELETE CASCADE
);

CREATE TABLE submissions (
    id            CHAR(36)   NOT NULL DEFAULT (uuid()),
    form_id       CHAR(36)   NOT NULL,
    submitted_by  CHAR(36)   NOT NULL,
    status        ENUM('valid', 'invalid') NOT NULL DEFAULT 'valid',
    submitted_at  DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP,
 
    PRIMARY KEY (id),
    KEY idx_submissions_form    (form_id, submitted_at),
    KEY idx_submissions_user    (submitted_by, submitted_at),
    CONSTRAINT fk_submissions_form FOREIGN KEY (form_id)
        REFERENCES forms (id) ON DELETE RESTRICT,
    CONSTRAINT fk_submissions_user FOREIGN KEY (submitted_by)
        REFERENCES users (id) ON DELETE RESTRICT
);

CREATE TABLE submission_values (
    id            CHAR(36)   NOT NULL DEFAULT (uuid()),
    submission_id CHAR(36)   NOT NULL,
    field_id      CHAR(36)   NOT NULL,
    value         TEXT,
    PRIMARY KEY (id),
    UNIQUE KEY uq_submission_field (submission_id, field_id),
    KEY idx_sv_field (field_id),
    CONSTRAINT fk_sv_submission FOREIGN KEY (submission_id)
        REFERENCES submissions (id) ON DELETE CASCADE,
    CONSTRAINT fk_sv_field FOREIGN KEY (field_id)
        REFERENCES fields (id) ON DELETE RESTRICT
);

INSERT INTO users (id, email, role) VALUES
    ('u-admin-001', 'admin@company.com',  'admin'),
    ('u-staff-001',  'staffa@company.com', 'staff'),
    ('u-staff-002',  'staffb@company.com', 'staff')
