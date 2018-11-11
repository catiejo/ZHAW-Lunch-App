CREATE TABLE day_schedule (
  _id INTEGER PRIMARY KEY AUTOINCREMENT,
  owner_type TEXT NOT NULL,
  owner_name TEXT NOT NULL COLLATE nocase,
  owner_description TEXT NULL,
  date TEXT NOT NULL,
  last_updated TEXT NOT NULL
);

CREATE TABLE slot (
  _id INTEGER PRIMARY KEY AUTOINCREMENT,
  day_schedule INTEGER NOT NULL REFERENCES day_schedule(_id) ON UPDATE CASCADE ON DELETE CASCADE,
  start TEXT NOT NULL,
  end TEXT NOT NULL
);

CREATE INDEX slot_day_schedule_idx ON slot(day_schedule);

CREATE TABLE event (
  _id INTEGER PRIMARY KEY AUTOINCREMENT,
  day_schedule INTEGER NOT NULL REFERENCES day_schedule(_id) ON UPDATE CASCADE ON DELETE CASCADE,
  name TEXT NOT NULL,
  type TEXT NOT NULL,
  description TEXT,
  room TEXT,
  start TEXT NOT NULL,
  end TEXT NOT NULL
);

CREATE INDEX event_day_schedule_idx ON event(day_schedule);

CREATE TABLE event_lecturer (
  _id INTEGER PRIMARY KEY AUTOINCREMENT,
  event INTEGER NOT NULL REFERENCES event(_id) ON UPDATE CASCADE ON DELETE CASCADE,
  short_name TEXT NOT NULL,
  first_name TEXT,
  last_name TEXT
);

CREATE INDEX event_lecturer_event_idx ON event_lecturer(event);

CREATE TABLE event_school_class (
  _id INTEGER PRIMARY KEY AUTOINCREMENT,
  event INTEGER NOT NULL REFERENCES event(_id) ON UPDATE CASCADE ON DELETE CASCADE,
  name TEXT NOT NULL
);

CREATE INDEX event_school_class_event_idx ON event_school_class(event);

CREATE VIRTUAL TABLE owner USING fts3 (
  type TEXT NOT NULL,
  name TEXT NOT NULL,
  description TEXT,
  last_updated TEXT NOT NULL
);