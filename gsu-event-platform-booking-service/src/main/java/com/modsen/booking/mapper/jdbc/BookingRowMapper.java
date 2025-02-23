package com.modsen.booking.mapper.jdbc;

import com.modsen.booking.enums.BookingStatus;
import com.modsen.booking.model.Booking;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
  * @author Alexander Dudkin
  */
@Component
public class BookingRowMapper implements RowMapper<Booking> {

    @Override
    public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Booking.builder()
                .id(rs.getObject("id", UUID.class))
                .userId(rs.getObject("user_id", UUID.class))
                .eventId(rs.getObject("event_id", UUID.class))
                .amount(rs.getBigDecimal("amount"))
                .status(BookingStatus.valueOf(rs.getString("status")))
                .createdTime(rs.getTimestamp("created_time").toLocalDateTime())
                .updatedTime(rs.getTimestamp("updated_time").toLocalDateTime())
                .build();
    }

}
