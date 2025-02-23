package com.modsen.booking.mapper;

import com.modsen.booking.dto.request.BookingRequest;
import com.modsen.booking.dto.response.BookingResponse;
import com.modsen.booking.model.Booking;
import org.mapstruct.Mapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * @author Alexander Dudkin
 */
@Mapper(componentModel = SPRING, unmappedTargetPolicy = IGNORE, injectionStrategy = CONSTRUCTOR)
public interface BookingMapper {
    Booking toBooking(BookingRequest bookingRequest);

    BookingResponse toResponse(Booking booking);

    default Booking updateToBooking(BookingRequest bookingRequest, Booking booking) {
        Booking.BookingBuilder builder = booking.toBuilder();
        for (RecordComponent component : BookingRequest.class.getRecordComponents()) {
            try {
                Method accessor = component.getAccessor();
                Object value = accessor.invoke(bookingRequest);

                if (value != null) {
                    String fieldName = component.getName();
                    Method setter = Booking.BookingBuilder.class.getMethod(fieldName, component.getType());
                    setter.invoke(builder, value);
                }
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                throw new RuntimeException("Failed to map field: " + component.getName(), e);
            }
        }

        builder.createdTime(booking.createdTime());
        builder.updatedTime(booking.updatedTime());
        return builder.build();
    }

}
