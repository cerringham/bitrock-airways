package it.bitrock.bitrockairways.utility;

import it.bitrock.bitrockairways.model.dto.CustomerFidelityPointDTO;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class FidelityProgramUserXlsExporterTest {

    private final String filePath = "/tmp/most-fidelity-customer.xls";
    private final List<CustomerFidelityPointDTO> users = generateFidelityProgramUsersList();
    private final FidelityProgramUserXlsExporter exporter = new FidelityProgramUserXlsExporter(filePath, users);


    @Test
    public void givenListOfFidelityProgramUsers_whenSavingToXlsFile_NoExceptionIsThrown() throws IOException {
        File xlsFile = new File(filePath);
        xlsFile.delete(); //I delete the file in case is already existing
        assertFalse(xlsFile.exists());

        exporter.export(); //I create the file again

        assertTrue(xlsFile.exists());
    }

    private List<CustomerFidelityPointDTO> generateFidelityProgramUsersList() {
        List<CustomerFidelityPointDTO> users = new ArrayList<>();

        Random rand = new Random();

        for (int i = 0; i < 10; i++) {
            int totalPoints = rand.nextInt(51) * 100; // Generates a random multiple of 100 in the range between 0 and 5000

            CustomerFidelityPointDTO user = new CustomerFidelityPointDTO("User " + i,
                            "Surname " + i, "user" + i + "@example.com", (long) totalPoints);
            users.add(user);
        }

        return users;
    }


}