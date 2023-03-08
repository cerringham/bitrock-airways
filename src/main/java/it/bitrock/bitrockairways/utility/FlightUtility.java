package it.bitrock.bitrockairways.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlightUtility {
    private static final String PLANE_787 = "787";
    private static final String PLANE_777 = "777";
    private static final int ROWS_NUMBER_787 = 40;
    private static final int COLUMNS_NUMBER_787 = 10;
    private static final int ROWS_NUMBER_777 = 32;
    private static final int COLUMNS_NUMBER_777 = 10;

    private final String planeType;
    public FlightUtility(String planeType) {
        this.planeType = planeType;
    }

    public String getRandomAvailableSeat(List<String> occupiedSeats) throws Exception {
        List<String> availableSeats = getAvailableSeats(occupiedSeats);

        //Random init
        Random generator = new Random();
        int randomIndex = generator.nextInt(availableSeats.size());

        return availableSeats.get(randomIndex);
    }

    private List<String> getAvailableSeats(List<String> occupiedSeats) throws Exception {
        if (occupiedSeats.size() == getPlaneCapacity()){
            throw new Exception("No seats available");
        }
        List<String> availableSeats = generateAllSeats();
        occupiedSeats.forEach(availableSeats::remove);
        return availableSeats;
    }

    private int getPlaneCapacity() throws Exception {
        int planeCapacity;
        if (planeType.equals(PLANE_787)){
            planeCapacity = ROWS_NUMBER_787 * COLUMNS_NUMBER_787;
        } else if (planeType.equals(PLANE_777)) {
            planeCapacity = ROWS_NUMBER_777 * COLUMNS_NUMBER_777;
        }
        else {throw new Exception("Plane type not existing");}
        return planeCapacity;
    }

    private List<String> generateAllSeats() throws Exception {
        List<String> planeRows;
        List<String> planeColumns;
        if (planeType.equals(PLANE_787)){
            planeRows = generatePlaneRows(ROWS_NUMBER_787);
            planeColumns = generatePlaneColumns(COLUMNS_NUMBER_787);
        } else if (planeType.equals(PLANE_777)) {
            planeRows = generatePlaneRows(ROWS_NUMBER_777);
            planeColumns = generatePlaneColumns(COLUMNS_NUMBER_777);
        } else {throw new Exception("Plane type not existing");}

        List<String> allSeats = new ArrayList<>();
        planeRows.forEach(r -> planeColumns.forEach((c -> allSeats.add(r + c))));

        return allSeats;
    }

    private List<String> generatePlaneRows(int rowsNumber) {
        List<String> planeRows = new ArrayList<>();
        for (int i = 1; i <= rowsNumber; i++) {
            planeRows.add(String.valueOf(i));
        }
        return planeRows;
    }

    private List<String> generatePlaneColumns(int columnsNumber) {
        List<String> planeColumns = new ArrayList<>();
        for (char c = 'A'; c < 'A' + columnsNumber; c++) {
            planeColumns.add(String.valueOf(c));
        }
        return planeColumns;
    }

}
