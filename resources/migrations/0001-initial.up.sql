CREATE TABLE IF NOT EXISTS art_pieces
(
  -- Art Data
  ID SERIAL PRIMARY KEY,
  title TEXT NOT NULL,
  description TEXT,
  price INT NULL,

  -- Relations
  artists_id INT NULL

);

--;;

CREATE TABLE IF NOT EXISTS artists
(
  -- Art Data
  ID SERIAL PRIMARY KEY,
  name TEXT NOT NULL
);
