import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Iterator;

public class searcher {

    private String data_path;
    private String query;
    public searcher(String path,String query) {
        this.data_path = path;
        this.query = query;
    }
    public void printTitle(){
        try {
            double[] Qid = CalcSim(query);

            //title가져오기 위해
            File file = new File("./Collection.xml");
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document LoadDocument = docBuilder.parse(file);
            LoadDocument.getDocumentElement().normalize();
            //title 5개를 다 가져옴
            NodeList nList = LoadDocument.getElementsByTagName("doc");
            String[] titleData = new String[nList.getLength()];
            for(int i = 0; i< nList.getLength(); i++) {
                Node nNode = nList.item(i);
                Element eElement = (Element) nNode;
                titleData[i] = eElement.getElementsByTagName("title").item(0).getTextContent();
            }

            double max = 0;
            int maxIndex = 0;
            int secondIndex = 0;
            int thirdIndex =0;
            for(int i = 0; i<Qid.length; i++){
                if(Qid[i]>max){
                    max = Qid[i];
                    maxIndex =i;

                }
            }
            if(max != 0) {
                System.out.println(titleData[maxIndex]+"(" + Qid[maxIndex]+")");
            }
            else{
                System.out.println("검색된 문서가 없습니다.");
            }
            max =0;
            for(int i = 0; i<Qid.length; i++){
                if(i == maxIndex){

                }
                else if(Qid[i]>max){
                    max = Qid[i];
                    secondIndex = i;
                }
            }
            if(max != 0) {
                System.out.println(titleData[secondIndex]+"(" + Qid[secondIndex]+")");
            }
            max = 0;
            for(int i = 0; i<Qid.length; i++){
                if(i == maxIndex){

                }
                else if(i == secondIndex){

                }
                else if(Qid[i]>max){
                    max = Qid[i];
                    thirdIndex = i;
                }
            }
            if(max != 0) {
                System.out.println(titleData[thirdIndex] +"(" + Qid[thirdIndex]+")");
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double[] InnerProduct(String query) {
        double[] Qid = {0,0,0,0,0};
        try{
            FileInputStream fileInputStream = new FileInputStream(data_path);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Object object = objectInputStream.readObject();
            objectInputStream.close();

            HashMap hashMap = (HashMap) object;
            KeywordExtractor ke = new KeywordExtractor();
            KeywordList kl = ke.extractKeyword(query, true);

            String[] key = new String[kl.size()];
            String[] value = new String[kl.size()];

            for (int i = 0; i < kl.size(); i++) {
                int QTF = 0;
                Keyword kwrd = kl.get(i);
                key[i] = kwrd.getString(); //key값 ex) 라면, 분말,
                QTF = kwrd.getCnt();
                if(hashMap.containsKey(key[i])) {
                    value[i] = (String) hashMap.get(key[i]); // ex) 0 0.0 1 20.92 2 0.0 3 0.0 4 0.0 // 0 0.0 1 1.61 2 0.0 3 0.0 4 0.0
                    String[] split = value[i].split(" ");
                    for (int j = 0; j < split.length; j++) {
                        if (j % 2 == 0) { // id일때
                            Qid[j / 2] += Math.round((Float.parseFloat(split[j + 1]) * QTF)*100)/100.0; //
                        }
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return Qid;
    }
    public double[] CalcSim(String query) {
        double[] Qid = InnerProduct(query);
        double[] Sim = {0,0,0,0,0};
        double QSize = 0;
        double[] idSize = {0,0,0,0,0};
        try{
            FileInputStream fileInputStream = new FileInputStream(data_path);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Object object = objectInputStream.readObject();
            objectInputStream.close();

            HashMap hashMap = (HashMap) object;
            KeywordExtractor ke = new KeywordExtractor();
            KeywordList kl = ke.extractKeyword(query, true);

            String[] key = new String[kl.size()];
            String[] value = new String[kl.size()];

            for (int i = 0; i < kl.size(); i++) {
                int QTF = 0;
                Keyword kwrd = kl.get(i);
                key[i] = kwrd.getString(); //key값 ex) 라면, 분말,
                QTF = kwrd.getCnt(); // 13, 1
                if(hashMap.containsKey(key[i])) {
                    value[i] = (String) hashMap.get(key[i]); // ex) 0 0.0 1 20.92 2 0.0 3 0.0 4 0.0 // 0 0.0 1 1.61 2 0.0 3 0.0 4 0.0
                    String[] split = value[i].split(" ");
                    for (int j = 0; j < split.length; j++) {
                        if (j % 2 == 0) { // id일때
                            idSize[j/2] += Math.pow(Math.round(Double.parseDouble(split[j + 1])*100)/100.0,2);
                        }
                    }
                }
                QSize += Math.pow(QTF,2);
            }
            for(int i =0 ;i<Qid.length;i++){//5번반복
                Sim[i] = Qid[i] /(Math.sqrt(QSize) * Math.sqrt(idSize[i]));
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return Sim;
    }
}
