import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
public class week15 {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.print("검색어를 입력하세요: ");
        String query = sc.nextLine();

        String clientId = "pd6l1zRpBbyIB6VOmdLb";// 애플리케이션 클라이언트 아이디값
        String clientSecret = "QF_9ftHiYm";// 애플리케이션 클라이언트 시크릿값


        try{

            String text = URLEncoder.encode(query, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/movie.json?query=" + text; // json 결과
            // String apiURL = "https://openapi.naver.com/v1/search/movie.xml?query=" + text; // xml 결과
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection(); // server와 client를 url로 연결

            con.setRequestMethod("GET"); // url에 text를 받음
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream())); // inputStream을 저장
            } else { // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream())); // 에러를 저장
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) { // 한 줄 씩 읽어옴
                response.append(inputLine);
            }
            br.close();

            //System.out.println(response);//"lastBuildDate","total","start","display","items"{"title","link","image","subtitle","pubDate","director","actor","userRating"}

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(response.toString());
            JSONArray infoArray = (JSONArray) jsonObject.get("items");

            for (int i = 0; i < infoArray.size(); i++) {
                System.out.println("=item_" + i + "===========================");
                JSONObject itemObject = (JSONObject) infoArray.get(i);
                System.out.println("title:\t" + itemObject.get("title"));
                System.out.println("subtitle:\t" + itemObject.get("subtitle"));
                System.out.println("director:\t" + itemObject.get("director"));
                System.out.println("actor:\t" + itemObject.get("actor"));
                System.out.println("userRating:\t" + itemObject.get("userRating") + "\n");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
