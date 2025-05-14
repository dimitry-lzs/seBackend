package com.softwareengineering.dto;

import java.sql.Timestamp;
import java.util.List;

public class AvailabilityBatchBody {
    public List<Timestamp> slots;
    public Integer doctorID;
}
