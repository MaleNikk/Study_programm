package com.example.skillboxsecondtask.storage;

import com.example.skillboxsecondtask.component.StudentEvent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;

@Data
@NoArgsConstructor
public class CollectionEvents {
    private final LinkedList<StudentEvent> events = new LinkedList<>();
}
