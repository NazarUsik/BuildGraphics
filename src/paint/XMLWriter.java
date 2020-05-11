package paint;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class XMLWriter extends XML<Point>{

   public XMLWriter(ArrayList<Point> arrayOfPoints, String nameFile) {
       super(arrayOfPoints);
       fromXML(nameFile);
   }

   @Override
   protected void fromXML(String nameFile){
       try {
           DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
           DocumentBuilder builder = factory.newDocumentBuilder();
           Document document = builder.newDocument();

           Element root = document.createElement("coordinate");
           Element point;
           Element x;
           Element y;
           Text text;
           document.appendChild(root);

           for (int i = 0; i < super.getArrayOfPointsXml().size(); i++) {
               point = document.createElement("point");
               x = document.createElement("x");
               y = document.createElement("y");
               point.setAttribute("number", Integer.toString(i));
               root.appendChild(point);
               point.appendChild(x);
               point.appendChild(y);
               text = document.createTextNode(Double.toString(super.getArrayOfPointsXml().get(i).getX()));
               x.appendChild(text);
               text = document.createTextNode(Double.toString(super.getArrayOfPointsXml().get(i).getY()));
               y.appendChild(text);
           }


           Transformer transformer = TransformerFactory.newInstance().newTransformer();
           transformer.setOutputProperty(OutputKeys.INDENT, "yes");
           transformer.transform(new DOMSource(document), new StreamResult(new FileOutputStream(nameFile)));
           System.out.println("Write successful");
       }
       catch (ParserConfigurationException | TransformerException | FileNotFoundException ex){
           System.out.println("Error in XMLWriter");
       }
   }
}
