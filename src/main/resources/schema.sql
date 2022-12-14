DROP SEQUENCE SQ_USER;
CREATE SEQUENCE SQ_USER
    INCREMENT BY 1 START WITH 1
    MINVALUE 1
    MAXVALUE 99999999;

DROP SEQUENCE SQ_ROLE;
CREATE SEQUENCE SQ_ROLE
    INCREMENT BY 1 START WITH 1
    MINVALUE 1
    MAXVALUE 99999999;

DROP TABLE TB_USER CASCADE CONSTRAINTS;
DROP TABLE TB_ROLE CASCADE CONSTRAINTS;
DROP TABLE TB_USER_ROLE CASCADE CONSTRAINTS;

CREATE TABLE TB_USER(
    ID NUMBER NOT NULL PRIMARY KEY,
    EMAIL VARCHAR2(1000) UNIQUE,
    PASSWORD VARCHAR2(1000),
    USERNAME VARCHAR2(1000)
);

CREATE TABLE TB_ROLE(
    ID NUMBER NOT NULL PRIMARY KEY,
    NAME VARCHAR2(1000) UNIQUE
);

CREATE TABLE TB_USER_ROLE (
    USER_ID NUMBER NOT NULL,
    ROLE_ID NUMBER NOT NULL,
    PRIMARY KEY (USER_ID, ROLE_ID) -- 복합키 속성 2이상을 조합해서 기본키로 만드는것
);


COMMIT;