import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a name of a country: ");
        String country = scanner.nextLine().toLowerCase();

        System.out.print("Enter the temperature scale (fahrenheit, kelvin, celsius): ");
        String scale = scanner.nextLine().toLowerCase();

        String apiKey = "f382b89206254918eb7231d985b598d1";
        String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + country + "&appid=" + apiKey;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();

            int res = connection.getResponseCode();
            if(res == 200) {

                String line;
                while((line = reader.readLine())!= null) {
                    response.append(line);
                }
                reader.close();

                JsonObject jsonResponse = new Gson().fromJson(response.toString(), JsonObject.class);
                float temperature = jsonResponse.get("main").getAsJsonObject().get("temp").getAsFloat();
                float fahrenheitTemp = (float) ((temperature - 273.15) * 1.8 + 32);
                float celsiusTemp = temperature - 273.15f;

                switch (scale) {
                    case "fahrenheit":
                        System.out.println("The average temperature in " + country + " is " + String.format("%.2f", fahrenheitTemp) + " degrees Fahrenheit");
                        break;
                    case "kelvin":
                        System.out.println("The average temperature in " + country + " is " + String.format("%.2f", temperature) + " degrees Kelvin");
                        break;
                    case "celsius":
                        System.out.println("The average temperature in " + country + " is " + String.format("%.2f", celsiusTemp) + " degrees Celsius");
                        break;
                }
            } else {
                System.out.println("Response code: " + res);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}