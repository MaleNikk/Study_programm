package com.example.skillboxsecondtask.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class StudentEvent {
    private final UUID id;
    private final String message;
    private final LocalDateTime dateTime;

    @Override
    public String toString() {
        return "\nEvent !".
                concat("\n\tId : ").concat(String.valueOf(id)).
                concat("\n\tMessage : ").concat(message).
                concat("\n\tDate create event : ").concat(String.valueOf(dateTime));
    }
}
