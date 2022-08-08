CREATE TABLE person
(
    fnr  VARCHAR(32) NOT NULL,
    data JSON        NOT NULL,
    PRIMARY KEY (fnr)
);

CREATE INDEX idx_person_paragraf
    ON person USING BTREE ((data ->'vedtaksperioder'->'subsumsjoner' ->> 'paragraf'));