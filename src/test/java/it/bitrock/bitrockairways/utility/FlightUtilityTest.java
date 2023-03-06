package it.bitrock.bitrockairways.utility;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FlightUtilityTest {
    private final String PLANE_787 = "787";
    private final String PLANE_747 = "747";
    private final int ROWS_NUMBER_787 = 40;
    private final int COLUMNS_NUMBER_787 = 10;

    private FlightUtility flightUtility;

    @Test(expected = Exception.class)
    public void givenNonExistingPlaneType_whenGeneratingNewAvailableSeat_ExceptionIsThrown() throws Exception {
        flightUtility = new FlightUtility(PLANE_747);
        List<String> occupiedSeats = new ArrayList<>(List.of("1A", "10B", "20C"));
        flightUtility.getRandomAvailableSeat(occupiedSeats);
    }

    @Test(expected = Exception.class)
    public void givenAllOccupiedSeats_whenGeneratingNewOne_ExceptionIsThrown() throws Exception {
        flightUtility = new FlightUtility(PLANE_787);
        List<String> occupiedSeats = generateAll787Seats();
        flightUtility.getRandomAvailableSeat(occupiedSeats);
    }

    @Test
    public void givenSetOfFreeSeats_whenGeneratingNewOne_isOneOfThem() throws Exception {
        flightUtility = new FlightUtility(PLANE_787);
        List<String> freeSeats = new ArrayList<>(List.of("1A", "10B", "20C"));
        List<String> occupiedSeats = generateAll787Seats();
        freeSeats.forEach(occupiedSeats::remove); //Removing free seats from occupied seats list

        String randomAvailableSeat = flightUtility.getRandomAvailableSeat(occupiedSeats);

        Assert.assertTrue(freeSeats.contains(randomAvailableSeat));
    }


    private List<String> generateAll787Seats() {
        List<String> planeRows;
        List<String> planeColumns;

        planeRows = generate787Rows();
        planeColumns = generate787Columns();

        List<String> allSeats = new ArrayList<>();
        planeRows.forEach(r -> {
            planeColumns.forEach((c -> allSeats.add(r + c)));
        });

        return allSeats;
    }

    private List<String> generate787Rows() {
        List<String> planeRows = new ArrayList<>();
        for (int i = 1; i <= ROWS_NUMBER_787; i++) {
            planeRows.add(String.valueOf(i));
        }
        return planeRows;
    }

    private List<String> generate787Columns() {
        List<String> planeColumns = new ArrayList<>();
        for (char c = 'A'; c < 'A' + COLUMNS_NUMBER_787; c++) {
            planeColumns.add(String.valueOf(c));
        }
        return planeColumns;
    }
}