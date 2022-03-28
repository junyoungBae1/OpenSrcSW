import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;

public class indexer {
    private String input_file;
    private String output_file = "index.post";
    public indexer(String path) {
        this.input_file = path;
    }
    public void makePost(){
        try {

            //String path = "src\\index.xml";
            File file = new File(input_file);

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document LoadDocument = docBuilder.parse(file);
            LoadDocument.getDocumentElement().normalize();

            NodeList nList = LoadDocument.getElementsByTagName("doc"); //XML 데이터중 <doc> 태그의 내용을 가져온다.

            int N = nList.getLength(); //전체 문서의 수
            FileOutputStream fileStream = new FileOutputStream(output_file);//post파일 만듬
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);
            HashMap IndexMap = new HashMap();
            String valueList;
            String[] List = new String[N]; //id와 가중치를 받을 string

            for (int i = 0; i < N; i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    String bodyData = eElement.getElementsByTagName("body").item(0).getTextContent();

                    String[] WF = bodyData.split("#"); // WF = word:freqence
                    for (int j = 0; j < WF.length; j++) {

                        double weight = 0;
                        StringBuilder sb = new StringBuilder();
                        String[] split = WF[j].split(":");
                        String Word = split[0];
                        int Freq = Integer.parseInt(split[1]);
                        int TF = Freq;
                        for (int k = 0; k < N; k++) { // 새로운 Node생성~
                            int cnt = 0;
                            Node nNode2 = nList.item(k);
                            Element eElement2 = (Element) nNode2;
                            String idData2 = eElement2.getAttribute("id");
                            String bodyData2 = eElement2.getElementsByTagName("body").item(0).getTextContent();
                            String[] WF2 = bodyData2.split("#");
                            for (int l = 0; l < WF2.length; l++) {

                                String[] split2 = WF2[l].split(":");
                                String Word2 = split2[0];
                                int Freq2 = Integer.parseInt(split2[1]);
                                if (Word.equals(Word2)) {
                                    for(int m = 0; m < N;m++) {
                                        Node nNode3 = nList.item(m);
                                        Element eElement3 = (Element) nNode3;
                                        String bodyData3 = eElement3.getElementsByTagName("body").item(0).getTextContent();
                                        String[] WF3 = bodyData3.split("#");
                                        for (int n = 0; n < WF3.length; n++) {
                                            String[] split3 = WF3[n].split(":");
                                            String Word3 = split3[0];
                                            if(Word.equals(Word3)) {
                                                cnt++;
                                            }
                                        }
                                    }
                                        TF = Freq2;
                                }
                            }
                            int DF = cnt;
                            if(DF != 0){
                                weight = Math.round((TF * Math.log((double) N /(double)DF)) * 100) / 100.0;
                            }else{
                                weight = 0;
                            }
                            List[k] = idData2 + " "+weight + " ";
                            sb.append(List[k]);
                        }
                        valueList = sb.toString();
                        if(!IndexMap.containsKey(Word)){
                            IndexMap.put(Word,valueList);
                        }
                    }
                }
            }

            objectOutputStream.writeObject(IndexMap);
            objectOutputStream.close();

            FileInputStream fileInputStream = new FileInputStream(output_file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Object object = objectInputStream.readObject();
            objectInputStream.close();
            System.out.println("읽어온 객체의 type : " + object.getClass());

            HashMap hashMap = (HashMap) object;
            Iterator<String> it = hashMap.keySet().iterator();

            while(it.hasNext()){
                String key = it.next();
                String value = (String)hashMap.get(key);
                System.out.println(key + " → " + value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}