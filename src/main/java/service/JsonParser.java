package service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Hotel;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class JsonParser {
    public static List<Hotel> parseHotels(String filePath) throws IOException {
        Gson gson = new Gson();
        Type hotelListType = new TypeToken<List<Hotel>>() {}.getType();
        FileReader reader = new FileReader(filePath);
        List<Hotel> hotels = gson.fromJson(reader, hotelListType);
        reader.close();
        return hotels;
    }
}
