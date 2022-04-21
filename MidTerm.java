import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.util.Scanner;

public class MidTerm {
    private String input_file;
    private String query;
    public MidTerm(String path, String query) {
        this.input_file = path;
        this.query = query;
    }
    public void showSnippet(){
        try {
            File file = new File(input_file);
            String[] snippet = new String[5];
            int[] matching = new int [5];
            for(int i = 0; i<5; i++){
                matching[i] = 0;
            }
            String temp;

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance(); // 빌더 팩토리 생성
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder(); // 빌더 팩토리로부터 빌더 생성

            Document LoadDocument = docBuilder.parse(file); //빌더를 통해 XML 문서를 파싱해서 Document 객체로 가져온다.
            LoadDocument.getDocumentElement().normalize(); // DOM Tree가 XML 문서의 구조로 변환

            NodeList nList = LoadDocument.getElementsByTagName("doc");
            int N = nList.getLength();

            KeywordExtractor ke = new KeywordExtractor();
            KeywordList kl = ke.extractKeyword(query, true);
            String[] key = new String[kl.size()];
            String[] value = new String[kl.size()];

            String[] titleData = new String[nList.getLength()];
            String[] bodyData = new String[nList.getLength()];
            for(int i = 0; i< nList.getLength(); i++){

                Node nNode = nList.item(i);
                Element eElement = (Element) nNode;
                titleData[i] = eElement.getElementsByTagName("title").item(0).getTextContent();
                bodyData[i] = eElement.getElementsByTagName("body").item(0).getTextContent();
            }
            for(int i = 0; i<kl.size();i++){
                Keyword kwrd = kl.get(i);
                key[i] = kwrd.getString(); // 단어
                for(int j = 0; j <N; j++){
                    Scanner sc = new Scanner(bodyData[j]);

                    temp = sc.next();

                        if(bodyData[j].contains(key[i])){

                            matching[j]++;
                            if(temp.length() == 30){
                                snippet[j] = temp;

                            }
                        }

                }
            }


            for(int i = 0; i <N; i++){
                System.out.println(titleData[i] + " " + snippet[i] + " " + matching[i]);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
