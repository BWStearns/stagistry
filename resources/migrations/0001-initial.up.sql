CREATE TABLE art-pieces
(
  -- Art Data
  ID INT PRIMARY KEY NOT NULL,
  name CHAR(200) NOT NULL,
  description TEXT,
  price INT NULL,

  -- Relations
  artists_id INT NULL

);

--;;

CREATE TABLE artists
(
  -- Art Data
  ID INT PRIMARY KEY NOT NULL,
  name CHAR(200) NOT NULL
);