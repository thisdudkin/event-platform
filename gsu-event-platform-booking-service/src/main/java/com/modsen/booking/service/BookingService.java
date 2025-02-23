package com.modsen.booking.service;

import com.modsen.booking.dto.request.BookingRequest;
import com.modsen.booking.dto.response.BookingResponse;
import com.modsen.booking.model.Page;

import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public interface BookingService {
    BookingResponse createBooking(BookingRequest bookingRequest);
    BookingResponse findBookingById(UUID bookingId);
    Page<BookingResponse> findAllBookings(UUID lastFetchedId, int size);
    BookingResponse updateBooking(UUID bookingId, BookingRequest bookingRequest);
    BookingResponse deleteBooking(UUID bookingId);
}
