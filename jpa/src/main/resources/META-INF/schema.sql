CREATE TABLE planet
(
    planetId   INT AUTO_INCREMENT,
    planetName VARCHAR(255),
    planetSize INT,
    planetType VARCHAR(255),
    planetMid  INT,
    planetSid  INT,
    PRIMARY KEY (planetId),
    FOREIGN KEY (planetMid) REFERENCES moon (planetMid),

);

CREATE TABLE moon
(
    moonId   INT          NOT NULL AUTO_INCREMENT,
    name     varchar(100) not null,
    size DOUBLE,
    planetId int          not null,
    PRIMARY KEY (moonId),
    foreign key (planetId) references planet(planetId)
);
CREATE TABLE student(
                        studentId INT NOT NULL AUTO_INCREMENT,
                        studentSocialSecNum INT,
                        studentName VARCHAR(50),
                        studentAge INT,
                        totResult DOUBLE,
                        CHECK (studentAge > 0),
                        PRIMARY KEY (studentId)
);

CREATE UNIQUE INDEX index_SocSecNum ON student(studentSocialSecNum);

CREATE TABLE test(
                     testId INT NOT NULL AUTO_INCREMENT,
                     testName VARCHAR(50),
                     testScore DOUBLE,
                     studentTestId INT,
                     PRIMARY KEY (testId),
                     FOREIGN KEY (studentTestId) REFERENCES student(studentId)
);



INSERT INTO student (studentSocialSecNum, studentName, studentAge, totResult) VALUES (1234567890, 'Kalle Anka', 13, 0.0);