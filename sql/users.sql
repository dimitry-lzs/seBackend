CREATE TABLE
    users (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        email text UNIQUE,
        password text,
        fullName text,
        userType text,
        phone text,
        amka text,
        licenceID text,
        speciality text,
        bio text,
        officeLocation text,
        rating real,
        avatar text,
        is_dark INTEGER DEFAULT 0,
    );