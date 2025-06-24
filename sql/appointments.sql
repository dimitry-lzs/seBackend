CREATE TABLE appointments (
	slotID INTEGER REFERENCES availabilities(availabilityID),
	status VARCHAR(30),
	reason TEXT,
	patientID INTEGER REFERENCES users(id) NOT NULL,
	doctorID INTEGER REFERENCES users(id) NOT NULL,
	appointmentID INTEGER PRIMARY KEY
); 