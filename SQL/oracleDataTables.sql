-- create database and role 'DAMAP'

--------------------------------------------------------------
-- create basic data tables
--------------------------------------------------------------

CREATE TABLE damap.identifier_type
(
    type VARCHAR2(255 CHAR) NOT NULL,
    CONSTRAINT type PRIMARY KEY (type)
);

insert into damap.identifier_type values ('ORCID');
insert into damap.identifier_type values ('ISNI');
insert into damap.identifier_type values ('OPENID');
insert into damap.identifier_type values ('OTHER');
insert into damap.identifier_type values ('HANDLE');
insert into damap.identifier_type values ('DOI');
insert into damap.identifier_type values ('ARK');
insert into damap.identifier_type values ('URL');
insert into damap.identifier_type values ('FUNDREF');

--------------------------------------------------------------

CREATE TABLE damap.identifier
(
    id NUMBER(19,0) NOT NULL,
    identifier VARCHAR2(255 CHAR),
    type VARCHAR2(255 CHAR),
    PRIMARY KEY (id),
    FOREIGN KEY (type)
        REFERENCES damap.identifier_type (type)
);

---------------------------------------------------------------

CREATE TABLE damap.funding_status
(
    status VARCHAR2(255 CHAR) NOT NULL,
    PRIMARY KEY (status)
);

insert into damap.funding_status values ('PLANNED');
insert into damap.funding_status values ('APPLIED');
insert into damap.funding_status values ('GRANTED');
insert into damap.funding_status values ('REJECTED');
insert into damap.funding_status values ('UNSPECIFIED');

---------------------------------------------------------------

CREATE TABLE damap.funding
(
    id NUMBER(19,0) NOT NULL,
    version NUMBER(10,0) NOT NULL,
    funder_id NUMBER(19,0),
    funding_status VARCHAR2(255 CHAR),
    grant_id NUMBER(19,0),
    PRIMARY KEY (id),
    FOREIGN KEY (funder_id)
        REFERENCES damap.identifier (id),
    FOREIGN KEY (grant_id)
        REFERENCES damap.identifier (id),
    FOREIGN KEY (funding_status)
        REFERENCES damap.funding_status (status)
);

---------------------------------------------------------------

CREATE TABLE damap.person
(
    id NUMBER(19,0) NOT NULL,
    version NUMBER(10,0) NOT NULL,
    person_id NUMBER(19,0),
    university_id VARCHAR2(255 CHAR),
    mbox VARCHAR2(255 CHAR),
    first_name VARCHAR2(255 CHAR),
    last_name VARCHAR2(255 CHAR),
    PRIMARY KEY (id),
    FOREIGN KEY (person_id)
        REFERENCES damap.identifier (id)
);

---------------------------------------------------------------

CREATE TABLE damap.project
(
    id NUMBER(19,0) NOT NULL,
    version NUMBER(10,0) NOT NULL,
    title VARCHAR2(255 CHAR),
    description VARCHAR2(4000 CHAR),
    funding NUMBER(19,0),
    project_start DATE,
    project_end DATE,
    PRIMARY KEY (id),
    FOREIGN KEY (funding)
        REFERENCES damap.funding (id)
);

---------------------------------------------------------------

CREATE TABLE damap.data_kind
(
    data_kind VARCHAR2(255 CHAR) NOT NULL,
    PRIMARY KEY (data_kind)
);

insert into damap.data_kind values ('UNKNOWN');
insert into damap.data_kind values ('NONE');
insert into damap.data_kind values ('SPECIFY');

---------------------------------------------------------------

CREATE TABLE damap.dmp
(
    id NUMBER(19,0) NOT NULL,
    version NUMBER(10,0) NOT NULL,
    created DATE,
    modified DATE,
    title VARCHAR2(255 CHAR),
    description VARCHAR2(4000 CHAR),
    project NUMBER(19,0),
    contact NUMBER(19,0),
    data_kind VARCHAR2(255 CHAR),
    no_data_explanation VARCHAR2(4000 CHAR),
    metadata VARCHAR2(4000 CHAR),
    data_generation VARCHAR2(4000 CHAR),
    structure VARCHAR2(4000 CHAR),
    target_audience VARCHAR2(4000 CHAR),
    personal_information NUMBER(1,0),
    sensitive_data NUMBER(1,0),
    legal_restrictions NUMBER(1,0),
    ethical_issues_exist NUMBER(1,0),
    committee_approved NUMBER(1,0),
    ethics_report VARCHAR2(4000 CHAR),
    optional_statement VARCHAR2(4000 CHAR),
    PRIMARY KEY (id),
    FOREIGN KEY (contact)
        REFERENCES damap.person (id),
    FOREIGN KEY (data_kind)
        REFERENCES damap.data_kind (data_kind),
    FOREIGN KEY (project)
        REFERENCES damap.project (id)
);

--------------------------------------------------------------

CREATE TABLE damap.contributor_role
(
    role VARCHAR2(255 CHAR) NOT NULL,
    PRIMARY KEY (role)
);

--------------------------------------------------------------

CREATE TABLE damap.contributor
(
    id NUMBER(19,0) NOT NULL,
    version NUMBER(10,0) NOT NULL,
    dmp_id NUMBER(19,0),
    person_id NUMBER(19,0),
    contributor_role VARCHAR2(255 CHAR),
    PRIMARY KEY (id),
    FOREIGN KEY (dmp_id)
        REFERENCES damap.dmp (id),
    FOREIGN KEY (person_id)
        REFERENCES damap.person (id),
    FOREIGN KEY (contributor_role)
        REFERENCES damap.contributor_role (role)
);

--------------------------------------------------------------

CREATE TABLE damap.host
(
    id NUMBER(19,0) NOT NULL,
    version NUMBER(10,0) NOT NULL,
    host_id VARCHAR2(255 CHAR),
    dmp_id NUMBER(19,0),
    name VARCHAR2(255 CHAR),
    retrieval_date DATE,
    PRIMARY KEY (id),
    FOREIGN KEY (dmp_id)
        REFERENCES damap.dmp (id)
);

--------------------------------------------------------------

CREATE TABLE damap.dataset
(
    id NUMBER(19,0) NOT NULL,
    version NUMBER(10,0) NOT NULL,
    dmp_id NUMBER(19,0),
    host_id NUMBER(19,0),
    title VARCHAR2(255 CHAR),
    type VARCHAR2(255 CHAR),
    data_size VARCHAR2(255 CHAR),
    dataset_comment VARCHAR2(4000 CHAR),
    publish NUMBER(1,0),
    license VARCHAR2(4000 CHAR),
    start_date DATE,
    reference_hash VARCHAR2(255 CHAR),
    PRIMARY KEY (id),
    FOREIGN KEY (dmp_id)
        REFERENCES damap.dmp (id),
    FOREIGN KEY (host_id)
        REFERENCES damap.host (id)
);

--------------------------------------------------------------

CREATE TABLE damap.function_role
(
    role VARCHAR2(255 CHAR) NOT NULL,
    PRIMARY KEY (role)
);

insert into damap.function_role values ('ADMIN');
insert into damap.function_role values ('SUPPORT');
insert into damap.function_role values ('OWNER');
insert into damap.function_role values ('EDITOR');
insert into damap.function_role values ('GUEST');

--------------------------------------------------------------


CREATE TABLE damap.administration
(
    id NUMBER(19,0) NOT NULL,
    version NUMBER(10,0) NOT NULL,
    university_id VARCHAR2(255 CHAR),
    role VARCHAR2(255 CHAR),
    start_date DATE,
    until_date DATE,
    PRIMARY KEY (id),
    FOREIGN KEY (role)
        REFERENCES damap.function_role (role)
);

--------------------------------------------------------------

CREATE TABLE damap.access_management
(
    id NUMBER(19,0) NOT NULL,
    version NUMBER(10,0) NOT NULL,
    dmp_id NUMBER(19,0),
    university_id VARCHAR2(255 CHAR),
    identifier_id NUMBER(19,0),
    role VARCHAR2(255 CHAR),
    start_date DATE,
    until_date DATE,
    PRIMARY KEY (id),
    FOREIGN KEY (dmp_id)
        REFERENCES damap.dmp (id),
    FOREIGN KEY (identifier_id)
        REFERENCES damap.identifier (id),
    FOREIGN KEY (role)
        REFERENCES damap.function_role (role)
);

--------------------------------------------------------------
-- create id sequence for hibernate
--------------------------------------------------------------

CREATE SEQUENCE damap.hibernate_sequence INCREMENT BY 1 START WITH 1 MINVALUE 1;