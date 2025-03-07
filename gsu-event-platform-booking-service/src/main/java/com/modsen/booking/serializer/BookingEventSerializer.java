package com.modsen.booking.serializer;

import com.modsen.booking.dto.event.BookingEvent;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author Alexander Dudkin
 */
@Component
public class BookingEventSerializer implements Serializer<BookingEvent> {

    @Override
    public byte[] serializeEvent(BookingEvent event) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(event);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new SerializationException("Failed to serialize BookingEvent", e);
        }
    }

}
