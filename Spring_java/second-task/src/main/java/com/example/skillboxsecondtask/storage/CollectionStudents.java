package com.example.skillboxsecondtask.storage;

import com.example.skillboxsecondtask.component.Student;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@NoArgsConstructor
@Getter
@Component
public class CollectionStudents {
    private final Map<Integer, Student> students = new HashMap<>();
}
