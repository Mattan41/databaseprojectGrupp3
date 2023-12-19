-- create
-- add solarSystem
INSERT INTO SolarSystem (SolarSystemName, GalaxyName) VALUES ('Solar System', 'Milky Way');

-- add planets
INSERT INTO planet (planetName, planetSize, planetType, SolarSystemId) VALUES ('Mercury', 4879, 'Terrestrial', 1);
INSERT INTO planet (planetName, planetSize, planetType, SolarSystemId) VALUES ('Venus', 12104, 'Terrestrial', 1);
INSERT INTO planet (planetName, planetSize, planetType, SolarSystemId) VALUES ('Earth', 12742, 'Terrestrial', 1);
INSERT INTO planet (planetName, planetSize, planetType, SolarSystemId) VALUES ('Mars', 6779, 'Terrestrial', 1);
INSERT INTO planet (planetName, planetSize, planetType, SolarSystemId) VALUES ('Jupiter', 139820, 'Gas Giant', 1);
INSERT INTO planet (planetName, planetSize, planetType, SolarSystemId) VALUES ('Saturn', 116460, 'Gas Giant', 1);
INSERT INTO planet (planetName, planetSize, planetType, SolarSystemId) VALUES ('Uranus', 50724, 'Ice Giant', 1);
INSERT INTO planet (planetName, planetSize, planetType, SolarSystemId) VALUES ('Neptune', 49244, 'Ice Giant', 1);

-- add moon
INSERT INTO moon (name, size, planetId) VALUES ('Moon', 3474.2, 3);
INSERT INTO moon (name, size, planetId) VALUES ('Phobos', 22.4, 4);
INSERT INTO moon (name, size, planetId) VALUES ('Deimos', 12.4, 4);
INSERT INTO moon (name, size, planetId) VALUES ('Io', 3643.2, 5);
INSERT INTO moon (name, size, planetId) VALUES ('Europa', 3121.6, 5);
INSERT INTO moon (name, size, planetId) VALUES ('Ganymede', 5268.2, 5);
INSERT INTO moon (name, size, planetId) VALUES ('Callisto', 4820.6, 5);
INSERT INTO moon (name, size, planetId) VALUES ('Titan', 5151.8, 6);
INSERT INTO moon (name, size, planetId) VALUES ('Rhea', 1527.6, 6);

-- add students
INSERT INTO student (studentSocialSecNum, studentName, studentAge, totResult) VALUES (1234567890, 'Kalle Anka', 13, 0.0);
INSERT INTO student (studentSocialSecNum, studentName, studentAge, totResult) VALUES (123456789, 'Kajsa Anka', 20, 85.5);
INSERT INTO student (studentSocialSecNum, studentName, studentAge, totResult) VALUES (987654321, 'Joakim von Anka', 22, 90.0);
INSERT INTO student (studentSocialSecNum, studentName, studentAge, totResult) VALUES (112233445, 'Alexander Lukas', 19, 92.3);

-- add tests
INSERT INTO test (testName, testScore, studentTestId) VALUES ('Physics', 89.5, 2);
INSERT INTO test (testName, testScore, studentTestId) VALUES ('Chemistry', 91.2, 3);
INSERT INTO test (testName, testScore, studentTestId) VALUES ('Mathematics', 95.0, 1);
INSERT INTO test (testName, testScore, studentTestId) VALUES ('planet size', 100.0, 4);

-- read
SELECT * FROM planet;
SELECT * FROM moon;
SELECT * FROM moon WHERE moonId = 4;
SELECT * FROM student WHERE totResult > 89;
SELECT planet.planetName, moon.name AS name
FROM planet
INNER JOIN moon ON planet.planetId = moon.planetId
WHERE planet.planetId = 5;

-- update
UPDATE moon SET moon.size = 23.4 WHERE moonId = 2;
UPDATE planet SET planet.planetType = 'Big Gas Giant' WHERE planetName = 'Jupiter';

-- delete
DELETE FROM planet WHERE planetName = 'Test';
DELETE FROM moon WHERE size < 10;

-- statistics
-- average moon size
SELECT AVG(moon.size) FROM moon;
-- smallest planet
SELECT planetName, planetSize
FROM planet
ORDER BY planetSize ASC
LIMIT 1;
