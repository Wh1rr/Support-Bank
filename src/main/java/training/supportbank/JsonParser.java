package training.supportbank;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Stream;

public class JsonParser {
    private static final Logger LOGGER = LogManager.getLogger(JsonParser.class.getName());
    static DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

    public static void readFile(String filename, Boolean check, ArrayList<Person> people) throws IOException {
        LOGGER.info("Opening " + filename);
        String content = null;
        if (!check){
            content = new String(Files.readAllBytes(Paths.get("Transactions2013.json")));
        }
        try {
            String xtraContent = new String(Files.readAllBytes(Paths.get(filename)));
            assert content != null;
            content = content.concat(xtraContent);
        } catch(Exception e){
            LOGGER.error(filename + " - not found");
        }
        Gson gson = buildGson();
        assert content != null;
        String[] contentlist = content.split("}");
        String test = "";
        for (int i = 0; i <= contentlist.length; i++){
            test = test.concat(contentlist[i].replace("[ {", ""));
            System.out.println(test);
        }
        gson.fromJson(content, (Type) people);
    }

    private static Gson buildGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (jsonElement, type, JsonDeserializationContext) ->
                LocalDate.parse(jsonElement.getAsString(), format));
        return gsonBuilder.create();
    }


    //String contents = new String(Files.readAllAbytes(Paths.get(filename)));

    //return Stream.of(gson.fromJson(contents, Transaction[].class))
}
