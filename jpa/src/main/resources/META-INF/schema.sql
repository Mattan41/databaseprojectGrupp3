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

