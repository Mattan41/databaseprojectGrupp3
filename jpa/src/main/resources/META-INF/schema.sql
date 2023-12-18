CREATE TABLE planet
(
    planetId   INT NOT NULL AUTO_INCREMENT,
    planetName VARCHAR(255),
    planetSize INT,
    planetType VARCHAR(255),
    SolarSystemId INT,
    PRIMARY KEY (planetId),
    FOREIGN KEY (SolarSystemId) REFERENCES SolarSystemId (SolarSystemId)
);

CREATE TABLE SolarSystemId
(
    SolarSystemId   INT NOT NULL AUTO_INCREMENT,
    SolarSystemName VARCHAR(255) not null unique,
    GalaxyName      VARCHAR(255)
);

CREATE TABLE moon
(
    moonId   INT NOT NULL AUTO_INCREMENT,
    name     varchar(100) not null unique,
    size     DOUBLE,
    planetId INT unique not null,
    PRIMARY KEY (moonId),
    foreign key (planetId) references planet (planetId)
);

CREATE TABLE student
(
    studentId           INT NOT NULL AUTO_INCREMENT,
    studentSocialSecNum INT NOT NULL UNIQUE,
    studentName         VARCHAR(50),
    studentAge          INT,
    totResult           DOUBLE,
    CHECK (studentAge > 0),
    PRIMARY KEY (studentId)
);

CREATE TABLE test
(
    testId        INT NOT NULL UNIQUE AUTO_INCREMENT,
    testName      VARCHAR(50) not null ,
    testScore     DOUBLE,
    studentTestId INT,
    PRIMARY KEY (testId),
    FOREIGN KEY (studentTestId) REFERENCES student (studentId)
);
