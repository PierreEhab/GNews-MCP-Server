import java.util.*;
import com.example.gnews.client.*;
public class Tmp {
  public static void main(String[] args) throws Exception {
    var client = new GNewsApiClient("$apiKey");
    var params = new LinkedHashMap<String, String>();
    params.put("lang", "ar");
    params.put("country", "eg");
    params.put("max", "5");
    System.out.println(client.get("/top-headlines", params));
  }
}