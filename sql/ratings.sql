CREATE TABLE ratings (
	stars INTEGER,
	comments TEXT,
	patientID INTEGER REFERENCES users(id) NOT NULL,
	doctorID INTEGER REFERENCES users(id) NOT NULL,
	appointmentID INTEGER REFERENCES appointments(id) NOT NULL
);