package training.supportbank;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.text.html.Option;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Stream;

public class CsvParser{
    private static final Logger LOGGER = LogManager.getLogger(CsvParser.class.getName());
    static DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

    public static void readFile(String filename, Boolean check, ArrayList<Person> people) throws IOException, CsvException {
        LOGGER.info("Opening " + filename);
        List<String[]> csv;
        if (!check){
            CSVReader reader = new CSVReader(new FileReader("Transactions2014.csv"));
            LOGGER.info("Opening Transactions2014.csv");
            CSVReader read2 = new CSVReader(new FileReader("DodgyTransactions2015.csv"));
            LOGGER.info("Opening DogdyTransactions2015.csv");
            List<String[]> dodgeCsv = read2.readAll();
            csv = reader.readAll();
            csv.addAll(dodgeCsv);
        try {
            CSVReader xtraFile = new CSVReader(new FileReader(filename));
            csv.addAll(xtraFile.readAll());
        } catch(Exception e){
            LOGGER.error(filename + " - not found");
        }
        for (int i = 1; i <= csv.size() - 1; i++) {
            String record = Arrays.toString(csv.get(i));
            String[] splitRecord = record.split(",", 5);
            String name = splitRecord[1].substring(1);
            if (name.equals("From")) {
                LOGGER.warn("Header parsed!");
            } else if (people.size() == 0) {
                people.add(new Person(name, csv));
            } else {
                for (int j = 0; j <= people.size(); j++) {
                    if (name.equals(people.get(j).name)) {
                        break;
                    } else if (j == people.size() - 1) {
                        people.add(new Person(name, csv));
                        break;
                    }
                }
            }
        }
    }

//    private static Optional<Transaction> processLine(String line){
//        LOGGER.debug("Parsing line " + line);
//        String[] fields = line.split(",");
//        if (fields.length != 5){
//            LOGGER.error(line + "incorrect number of fields");
//            return Optional.empty();
//        }
//        try{
//            LocalDate date = LocalDate.parse(fields[0], format);
//            String from = fields[1];
//            String to = fields[2];
//            String reason = fields[3];
//            BigDecimal amount = new BigDecimal(fields[4]);
//            return Optional.of(new Transaction(amount, reason, date, to));
//        } catch (DateTimeParseException e){
//            LOGGER.error(line + " - Incorrect date format");
//        } catch (NumberFormatException e) {
//            LOGGER.error(line + "Invalid price");
//        }
    }
}
