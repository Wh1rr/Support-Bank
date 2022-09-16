package training.supportbank;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Stream;

public class JsonParser {
    private static final Logger LOGGER = LogManager.getLogger(JsonParser.class.getName());
    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
    private static List<String> loadedJson;

    public static void createFile(String filename, Boolean check, ArrayList<Person> people) throws IOException, CsvException {
        LOGGER.info("Opening " + filename);
        StringBuilder content = null;
        if (!check){
            content = new StringBuilder(new String(Files.readAllBytes(Paths.get("Transactions2013.json"))));
            loadedJson.add("Transactions2013.json");
        }
        if (!Objects.equals(filename, "")) {
            try {
                String xtraContent = new String(Files.readAllBytes(Paths.get(filename)));
                assert content != null;
                content = new StringBuilder(content.toString().concat(xtraContent));
                loadedJson.add(filename);
            } catch (Exception e) {
                LOGGER.error(filename + " - not found");
            }
        }
        Gson gson = buildGson();
        assert content != null;
        String[] contentlist = content.toString().split("}");
        String test;
        for (String i : contentlist){
            test = i.replaceAll("\\n", "")
                    .replaceAll("\\{", "").replaceAll("\\[", "")
                    .replaceAll("\"", "").replaceAll(",", ":");
            String[] update = test.split(":", 8);
            String name = update[4];
            name = name.substring(1);
            if (name.contains("Account")){
                LOGGER.info("Header parsed!");
            } else if (people.size() == 0){
                people.add(new Person(name));
            } else {
                for (int j = 0; j <= people.size(); j++) {
                    if (name.equals(people.get(j).name)) {
                        break;
                    } else if (j == people.size() - 1) {
                        people.add(new Person(name));
                        break;
                    }
                }
            }
        }
        gson.fromJson(content.toString(), (Type) people);
    }

    private static Gson buildGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (jsonElement, type, JsonDeserializationContext) ->
                LocalDate.parse(jsonElement.getAsString(), format));
        return gsonBuilder.create();
    }

    static void createTransactions(Person person) throws IOException{
        LOGGER.info("Opening jsons to create transactions");
        StringBuilder content = null;
        if (getJsons().size() == 1){
            content = new StringBuilder(new String(Files.readAllBytes(Paths.get("Transactions2013.json"))));
        }
        for (String file : getJsons()) {
            try {
                String xtraContent = new String(Files.readAllBytes(Paths.get(file)));
                assert content != null;
                content = new StringBuilder(content.toString().concat(xtraContent));
            } catch (Exception e) {
                LOGGER.error(file + " - not found");
            }
            }
        assert content != null;
        String[] contentlist = content.toString().split("}");
        String test;
        for (String line : contentlist){
            try {
                test = line.replaceAll("\\n", "")
                        .replaceAll("\\{", "").replaceAll("\\[", "")
                        .replaceAll("\"", "").replaceAll(",", ":");
                String[] update = test.split(":", 8);
                LocalDate date = LocalDate.parse(update[0].substring(1), format);
                String from = update[4].substring(1);
                String to = update[6].substring(1);
                String reason = update[8].substring(1);
                int amount = Integer.parseInt(update[10].substring(1).substring(0, update[4].length() - 2));
                BigDecimal tAmount = new BigDecimal(amount);
                if (from.equals(person.name)) {
                    person.transactions.add(new Transaction(tAmount, reason, date, to));
                    person.total = person.total.add(tAmount);
                } else if (to.equals(person.name)) {
                    person.transactions.add(new Transaction(tAmount, reason, date, from));
                    person.total = person.total.subtract(tAmount);
                }
            } catch (DateTimeParseException e){
                LOGGER.error("Incorrect date format" + "- on line number " + e.getStackTrace()[3].getLineNumber());
            } catch (NumberFormatException e) {
                LOGGER.error("Invalid price" + "- on line number " + e.getStackTrace()[3].getLineNumber());
            } catch(Exception e){
                LOGGER.error("Creating transaction - " + e + "- on line number " + e.getStackTrace()[3].getLineNumber());
                person.transactions.remove(person.transactions.size()-1);
            }
        }
        System.out.println("" + person.name + " - JSon file updated");
    }

    static List<String> getJsons(){
        return loadedJson;
    }

    private static void setLoadedJson(String json){
        if (loadedJson.size() == 0) {
            loadedJson.set(0, json);
        } else {loadedJson.add(json);}
    }


    //String contents = new String(Files.readAllAbytes(Paths.get(filename)));

    //return Stream.of(gson.fromJson(contents, Transaction[].class))
}
