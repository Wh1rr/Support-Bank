package training.supportbank;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Person {
    String name;
    BigDecimal total= BigDecimal.ZERO;
    ArrayList<Transaction> transactions = new ArrayList<>();
    static DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);
    private static final Logger LOGGER = LogManager.getLogger(Person.class.getName());

    public Person(String name) throws IOException, CsvException {
        System.out.println("\nCreating person..." + name);
        this.name = name;
        CsvParser.createTransactions(this);
        JsonParser.createTransactions(this);
    }

    private void jsonCreate(){

    }

    public void allTransactions(){
        this.transactions.stream().map(Transaction::getAll).forEach(System.out::println);
    }
    public BigDecimal getTotal(){return this.total;}
}
