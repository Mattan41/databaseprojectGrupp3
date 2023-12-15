CREATE TABLE planet (
                         planetId INT AUTO_INCREMENT,
                         planetName VARCHAR(255),
                         planetSize INT,
                         planetType VARCHAR(255),
                         planetMid INT,
                         planetSid INT,
                         PRIMARY KEY (planetId),
                         FOREIGN KEY (planetMid) REFERENCES moon(planetMid),

);

CREATE TABLE moon(
                     moonId INT NOT NULL AUTO_INCREMENT,
                     name varchar(100) not null,
                     size DOUBLE,
                     planetId int not null,
                     FOREIGN KEY (planetId) references planet
);
