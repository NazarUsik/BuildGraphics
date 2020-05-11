package paint;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

abstract public class XMLReader extends XML<Point>{
    public XMLReader() {
    }

    public XMLReader(String file) {
        fromXML(file);
    }

    @Override
    protected void fromXML(String file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(file));

            Element element = document.getDocumentElement();
            readXML(element.getChildNodes());
        } catch (ParserConfigurationException | IOException | SAXException ex) {
            System.out.println("Error in XMLReader");
        }
    }

    private void readXML(NodeList nodeList) {
        Point point = new Point();
        ArrayList<Double> arr = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i) instanceof Element) {
                double value = 0.0;
                if (!nodeList.item(i).getTextContent().trim().isEmpty() && !((Text) nodeList.item(i).getFirstChild()).getData().trim().isEmpty() && !((Text) nodeList.item(i).getFirstChild()).getData().trim().equals("\n")) {
                    Text text = (Text) nodeList.item(i).getFirstChild();
                    value = Double.parseDouble(text.getData().trim());
                    arr.add(value);
                }
                if (nodeList.item(i).hasChildNodes()) {
                    readXML(nodeList.item(i).getChildNodes());
                }
            }
        }
        for (int i = 0; i < arr.size(); i++) {
            if (i % 2 == 0)
                point.setX(arr.get(i));
            else {
                point.setY(arr.get(i));
                super.add(point);
            }
        }
    }

    public ArrayList<Point> getArrayOfPointsXml() {
        return super.getArrayOfPointsXml();
    }
}

