CREATE TABLE diagnoses (
	appointmentID REFERENCES appointments(appointmentID) NOT NULL,
	decease VARCHAR(30),
	details TEXT
);