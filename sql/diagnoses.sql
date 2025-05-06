CREATE TABLE diagnoses (
	appointmentID REFERENCES appointments(appointmentID),
	decease VARCHAR(30),
	details TEXT
);