CREATE TABLE ratings (
	stars INTEGER,
	comments TEXT,
	patientID INTEGER REFERENCES users(id),
	doctorID INTEGER REFERENCES users(id)
);