package com.modsen.booking.service.impl;

import com.modsen.booking.dto.request.BookingRequest;
import com.modsen.booking.dto.response.BookingResponse;
import com.modsen.booking.mapper.BookingMapper;
import com.modsen.booking.model.Booking;
import com.modsen.booking.model.Page;
import com.modsen.booking.repository.BookingRepository;
import com.modsen.booking.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

/**
 * @author Alexander Dudkin
 */
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingMapper bookingMapper, BookingRepository bookingRepository) {
        this.bookingMapper = bookingMapper;
        this.bookingRepository = bookingRepository;
    }

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        Booking booking = bookingMapper.toBooking(bookingRequest);
        Booking resultBooking = bookingRepository.save(booking);
        return bookingMapper.toResponse(resultBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse findBookingById(UUID bookingId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        return bookingMapper.toResponse(bookingOptional
                .orElseThrow(() -> {
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found with id " + bookingId);
                }));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingResponse> findAllBookings(UUID lastFetchedId, int size) {
        Iterable<Booking> bookingIterable = bookingRepository.findAll(lastFetchedId, size);
        List<BookingResponse> content = StreamSupport.stream(bookingIterable.spliterator(), false)
                .map(bookingMapper::toResponse)
                .toList();

        return new Page<>(content, size, content.getLast().id());
    }

    @Override
    @Transactional
    public BookingResponse updateBooking(UUID bookingId, BookingRequest bookingRequest) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking with id '%s' not found".formatted(bookingRequest)));
        Booking updatedBooking = bookingMapper.updateToBooking(bookingRequest, booking);
        Booking resultBooking = bookingRepository.save(updatedBooking);
        return bookingMapper.toResponse(resultBooking);
    }

    @Override
    @Transactional
    public BookingResponse deleteBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking != null) {
            bookingRepository.delete(booking);
        }
        return bookingMapper.toResponse(booking);
    }

}
