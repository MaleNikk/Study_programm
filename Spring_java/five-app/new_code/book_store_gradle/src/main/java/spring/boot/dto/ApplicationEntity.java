package spring.boot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Component
public class ApplicationEntity implements Serializable {
    protected final Integer[] integers = new Integer[5];
    protected final String[] strings = new String[5];
}


