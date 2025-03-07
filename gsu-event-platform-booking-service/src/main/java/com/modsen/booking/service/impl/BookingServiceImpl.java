package com.modsen.booking.service.impl;

import com.github.f4b6a3.uuid.UuidCreator;
import com.modsen.booking.dto.event.BookingEvent;
import com.modsen.booking.dto.request.BookingRequest;
import com.modsen.booking.dto.response.BookingResponse;
import com.modsen.booking.mapper.BookingMapper;
import com.modsen.booking.model.Booking;
import com.modsen.booking.model.OutboxEvent;
import com.modsen.booking.model.Page;
import com.modsen.booking.repository.BookingRepository;
import com.modsen.booking.repository.OutboxRepository;
import com.modsen.booking.serializer.Serializer;
import com.modsen.booking.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.StreamSupport;

/**
 * @author Alexander Dudkin
 */
@Service
public class BookingServiceImpl implements BookingService {
    private static final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);

    private final BookingMapper bookingMapper;
    private final OutboxRepository outboxRepository;
    private final BookingRepository bookingRepository;
    private final Serializer<BookingEvent> eventSerializer;
    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;

    public BookingServiceImpl(BookingMapper bookingMapper, OutboxRepository outboxRepository, BookingRepository bookingRepository, Serializer<BookingEvent> eventSerializer, KafkaTemplate<String, BookingEvent> kafkaTemplate) {
        this.bookingMapper = bookingMapper;
        this.kafkaTemplate = kafkaTemplate;
        this.eventSerializer = eventSerializer;
        this.outboxRepository = outboxRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        Booking booking = bookingMapper.toBooking(bookingRequest);
        Booking resultBooking = bookingRepository.save(booking);

        BookingEvent bookingEvent = new BookingEvent(
                resultBooking.id(),
                resultBooking.userId(),
                resultBooking.eventId(),
                resultBooking.amount()
        );

        kafkaTemplate.sendDefault(
                bookingEvent
        ).thenAccept(result -> {
                    log.debug("Event sent to Kafka. Offset: {}", result.getRecordMetadata().offset());
                }
        ).exceptionally(ex -> {
            log.error("Failed to send event to Kafka: {}", ex.getMessage());
            outboxRepository.save(new OutboxEvent(
                    UuidCreator.getTimeOrderedEpoch().toString(),
                    eventSerializer.serializeEvent(bookingEvent),
                    LocalDateTime.now(),
                    false
            ));
            return null;
        });

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
