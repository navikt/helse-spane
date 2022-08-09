CREATE TABLE paragrafsøk
(
    id       SERIAL,
    paragraf VARCHAR(32) NOT NULL,
    ledd     int,
    bokstav  char,
    punktum  int,
    PRIMARY KEY (paragraf, ledd, bokstav, punktum),
    UNIQUE(id)

);

CREATE TABLE person_paragrafsøk
(
    fnr VARCHAR(32) references person (fnr),
    id SERIAL references paragrafsøk (id),
    PRIMARY KEY (fnr, id)
);


