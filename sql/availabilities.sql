CREATE TABLE availabilities (
	date TIMESTAMP,
	timeFrom TIME,
	timeTo TIME,
	doctorID REFERENCES users(id)
);