CREATE TABLE gastronomic_facility (
  _id INTEGER PRIMARY KEY AUTOINCREMENT,
  facility_id INTEGER NOT NULL UNIQUE,
  type TEXT NOT NULL,
  name TEXT NOT NULL,
  location TEXT NOT NULL,
  last_update TEXT NOT NULL,
  version TEXT NOT NULL
);

CREATE TABLE service_time_period (
  _id INTEGER PRIMARY KEY AUTOINCREMENT,
  service_time_period_id INTEGER NOT NULL,
  facility_id INTEGER NOT NULL REFERENCES gastronomic_facility(facility_id) ON UPDATE CASCADE ON DELETE CASCADE,
  starts_on TEXT NOT NULL,
  ends_on TEXT NOT NULL,
  opening_time_start_monday TEXT,
  opening_time_end_monday TEXT,
  opening_time_start_tuesday TEXT,
  opening_time_end_tuesday TEXT,
  opening_time_start_wednesday TEXT,
  opening_time_end_wednesday TEXT,
  opening_time_start_thursday TEXT,
  opening_time_end_thursday TEXT,
  opening_time_start_friday TEXT,
  opening_time_end_friday TEXT,
  opening_time_start_saturday TEXT,
  opening_time_end_saturday TEXT,
  opening_time_start_sunday TEXT,
  opening_time_end_sunday TEXT,
  lunch_time_start_monday TEXT,
  lunch_time_end_monday TEXT,
  lunch_time_start_tuesday TEXT,
  lunch_time_end_tuesday TEXT,
  lunch_time_start_wednesday TEXT,
  lunch_time_end_wednesday TEXT,
  lunch_time_start_thursday TEXT,
  lunch_time_end_thursday TEXT,
  lunch_time_start_friday TEXT,
  lunch_time_end_friday TEXT,
  lunch_time_start_saturday TEXT,
  lunch_time_end_saturday TEXT,
  lunch_time_start_sunday TEXT,
  lunch_time_end_sunday TEXT,
  last_update TEXT NOT NULL,
  version TEXT NOT NULL,
  UNIQUE(service_time_period_id, facility_id)
);

CREATE TABLE holiday (
  _id INTEGER PRIMARY KEY AUTOINCREMENT,
  holiday_id INTEGER NOT NULL,
  facility_id INTEGER NOT NULL REFERENCES gastronomic_facility(facility_id) ON UPDATE CASCADE ON DELETE CASCADE,
  name TEXT NOT NULL,
  starts_at TEXT NOT NULL,
  ends_at TEXT NOT NULL,
  last_update TEXT NOT NULL,
  version TEXT NOT NULL,
  UNIQUE(holiday_id, facility_id)
);

CREATE TABLE dish (
  _id INTEGER PRIMARY KEY AUTOINCREMENT,
  dish_id INTEGER NOT NULL,
  facility_id INTEGER NOT NULL REFERENCES gastronomic_facility(facility_id) ON UPDATE CASCADE ON DELETE CASCADE,
  external_price NUMERIC NOT NULL,
  internal_price NUMERIC NOT NULL,
  label TEXT NOT NULL,
  name TEXT NOT NULL,
  price_for_partners NOT NULL,
  offered_on TEXT NOT NULL,
  first_side_dish TEXT,
  second_side_dish TEXT,
  third_side_dish TEXT,
  last_update TEXT NOT NULL,
  version TEXT NOT NULL,
  UNIQUE(dish_id, facility_id)
);