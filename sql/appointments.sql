CREATE TABLE appointments (
	date TIMESTAMP,
	status VARCHAR(30),
	patientID INTEGER REFERENCES users(id),
	doctorID INTEGER REFERENCES users(id),
	appointmentID INTEGER PRIMARY KEY
); 