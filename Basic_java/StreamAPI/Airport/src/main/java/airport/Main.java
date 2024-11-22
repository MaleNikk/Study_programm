package airport;

import com.skillbox.airport.Airport;
import com.skillbox.airport.Flight;
import com.skillbox.airport.Terminal;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static long findCountAircraftWithModelAirbus(Airport airport, String model) {
        //TODO Метод должен вернуть количество самолетов указанной модели.
        // подходят те самолеты, у которых name начинается со строки model
        return airport.getAllAircrafts().stream()
                .filter(name -> name.getModel().contains(model))
                .count();
    }

    public static Map<String, Integer> findMapCountParkedAircraftByTerminalName(Airport airport) {
        //TODO Метод должен вернуть словарь с количеством припаркованных самолетов в каждом терминале.
        return airport.getTerminals().stream()
                .collect(Collectors.toMap(Terminal::getName, value -> value.getParkedAircrafts().size()));
    }

    public static List<Flight> findFlightsLeavingInTheNextHours(Airport airport, int hours) {
        //TODO Метод должен вернуть список отправляющихся рейсов в ближайшее количество часов.
        Instant timeDepartureAircraft = Instant.now();
        return airport.getTerminals().stream()
                .flatMap(terminal -> terminal.getFlights().stream())
                .filter(flight -> flight.getType().equals(Flight.Type.DEPARTURE))
                .filter(dateTime -> dateTime.getDate().isAfter(timeDepartureAircraft)
                        && dateTime.getDate().isBefore(timeDepartureAircraft.plusSeconds(hours * 3600L)))
                .toList();
    }

    public static Optional<Flight> findFirstFlightArriveToTerminal(Airport airport, String terminalName) {
        //TODO Найти ближайший прилет в указанный терминал.
        return airport.getTerminals().stream()
                .filter(terminal -> terminal.getName().contains(terminalName))
                .flatMap(aircraft -> aircraft.getFlights().stream())
                .filter(flight -> flight.getType().equals(Flight.Type.ARRIVAL))
                .min(Comparator.comparing(Flight::getDate));
    }
}