CREATE SEQUENCE PARTY_ROLE_PK_PARTY_ROLE_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE PARTY_ROLE
(
  PK_PARTY_ROLE BIGINT NOT NULL DEFAULT NEXTVAL('PARTY_ROLE_PK_PARTY_ROLE_SEQ'::regclass),
  FK_PARTY BIGINT NOT NULL,
  FK_LOOKUP_PARTY_ROLE BIGINT,
  CONSTRAINT PK_PARTY_ROLE PRIMARY KEY (PK_PARTY_ROLE),
  CONSTRAINT PR_FK_LOOKUP_PARTY_ROLE_L_PK_LOOKUP FOREIGN KEY (FK_LOOKUP_PARTY_ROLE)
      REFERENCES LOOKUP (PK_LOOKUP) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT PR_FK_PARTY_P_PK_PARTY FOREIGN KEY (FK_PARTY)
      REFERENCES PARTY (PK_PARTY) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);