import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;


public class makeKeyword {

    private String input_file;
    private String output_file = "src/index.xml";
    public makeKeyword(String path) {
        this.input_file = path;
    }
    public void convertXml(){
        try {
            //String path = "C:\\Users\\Danawa\\Desktop\\kuir\\src\\Collection.xml";
            File file = new File(input_file);

            KeywordExtractor ke = new KeywordExtractor(); // 형태소 분석 객체 생성

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance(); // 빌더 팩토리 생성
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder(); // 빌더 팩토리로부터 빌더 생성

            Document LoadDocument = docBuilder.parse(file); //빌더를 통해 XML 문서를 파싱해서 Document 객체로 가져온다.
            LoadDocument.getDocumentElement().normalize(); // DOM Tree가 XML 문서의 구조로 변환

            Document NewDocument = docBuilder.newDocument(); //새로운 Document 객체 가져온다.

            Element docs = NewDocument.createElement (LoadDocument.getDocumentElement().getNodeName()); //collection.xml의 docs가 변하면 여기서도 변한다.
            NewDocument.appendChild(docs);

            NewDocument.setXmlStandalone(true); //standalone="no" 를 없애준다.

            NodeList nList = LoadDocument.getElementsByTagName("doc"); //XML 데이터중 <doc> 태그의 내용을 가져온다.

            for(int i = 0; i< nList.getLength(); i++){ //doc태그의 갯수만큼 하위 노드들을 가져온다
                Node nNode = nList.item(i);
                if(nNode.getNodeType() == Node.ELEMENT_NODE) { //자식 노드가 Element일때만 실행한다.
                    Element eElement = (Element) nNode;

                    String idData = eElement.getAttribute("id");
                    String titleData = eElement.getElementsByTagName("title").item(0).getTextContent();
                    String bodyData = eElement.getElementsByTagName("body").item(0).getTextContent();

                    Element doc = NewDocument.createElement("doc");
                    docs.appendChild(doc);

                    doc.setAttribute("id",idData);

                    Element title = NewDocument.createElement("title");
                    title.appendChild(NewDocument.createTextNode(titleData));
                    doc.appendChild(title);


                    Element body = NewDocument.createElement("body");

                    KeywordList kl = ke.extractKeyword(bodyData, true);
                    String[] str;
                    str = new String[kl.size()];
                    for( int j = 0; j < kl.size(); j++ ) {
                        Keyword kwrd = kl.get(j);
                        str[j] = kwrd.getString() + ":" + kwrd.getCnt()+"#";
                        body.appendChild(NewDocument.createTextNode(str[j]));
                    }
                    doc.appendChild(body);
                }
            }

            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");

            DOMSource source = new DOMSource(NewDocument);
            StreamResult result = new StreamResult(new FileOutputStream(new File(output_file)));
            transformer.transform(source, result);

        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
