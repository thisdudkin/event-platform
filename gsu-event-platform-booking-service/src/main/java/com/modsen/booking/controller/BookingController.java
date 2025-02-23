package com.modsen.booking.controller;

import com.modsen.booking.dto.request.BookingRequest;
import com.modsen.booking.dto.response.BookingResponse;
import com.modsen.booking.model.Page;
import com.modsen.booking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {
    
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody @Valid BookingRequest bookingRequest) {
        BookingResponse createdBooking = bookingService.createBooking(bookingRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdBooking.id())
                .toUri();
        return ResponseEntity.created(location).body(createdBooking);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getOne(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingService.findBookingById(id));
    }

    @GetMapping
    public ResponseEntity<Page<BookingResponse>> getAll(
            @RequestParam(required = false) UUID lastFetchedId,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(bookingService.findAllBookings(lastFetchedId, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> updateOne(
            @PathVariable UUID id,
            @RequestBody @Valid BookingRequest bookingRequest
    ) {
        return ResponseEntity.ok(bookingService.updateBooking(id, bookingRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookingResponse> deleteOne(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingService.deleteBooking(id));
    }
    
}
