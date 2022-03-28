import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;


public class makeCollection {

    private String data_path;
    private String output_file = "src/Collection.xml";
    public makeCollection(String path) {
            this.data_path = path;
    }
    public void makeXml(){
        try {
            //String path = "C:\\Users\\Danawa\\Desktop\\kuir\\2weektraining";
            File dir = new File(data_path);
            File[] FileList = dir.listFiles();

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document document = docBuilder.newDocument();

            document.setXmlStandalone(true);

            Element docs = document.createElement("docs");
            document.appendChild(docs);
            for (int i = 0; i < FileList.length; i++) {

                org.jsoup.nodes.Document html = Jsoup.parse(FileList[i], "UTF-8");
                String titleData = html.title();
                String bodyData = html.body().text();

                Element doc = document.createElement("doc");
                docs.appendChild(doc);

                doc.setAttribute("id", String.valueOf(i));

                Element title = document.createElement("title");
                title.appendChild(document.createTextNode(titleData));
                doc.appendChild(title);

                Element body = document.createElement("body");
                body.appendChild(document.createTextNode(bodyData));
                doc.appendChild(body);
            }

            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();


            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileOutputStream(new File(output_file)));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

   /* public void Collection(String path) {
        try {
            //String path = "C:\\Users\\Danawa\\Desktop\\kuir\\2weektraining";
            File dir = new File(path);
            File[] FileList = dir.listFiles();

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document document = docBuilder.newDocument();

            document.setXmlStandalone(true);

            Element docs = document.createElement("docs");
            document.appendChild(docs);
            for(int i= 0; i<FileList.length;i++) {

                org.jsoup.nodes.Document html = Jsoup.parse(FileList[i], "UTF-8");
                String titleData = html.title();
                String bodyData = html.body().text();

                Element doc = document.createElement("doc");
                docs.appendChild(doc);

                doc.setAttribute("id", String.valueOf(i));

                Element title = document.createElement("title");
                title.appendChild(document.createTextNode(titleData));
                doc.appendChild(title);

                Element body = document.createElement("body");
                body.appendChild(document.createTextNode(bodyData));
                doc.appendChild(body);
            }

            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();


            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileOutputStream(new File("src/Collection.xml")));
            transformer.transform(source, result);

        }catch(Exception e) {
            e.printStackTrace();
        }
    }*/



