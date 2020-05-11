package paint;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


class ReportUtils {


    static void saveFiles(
            File file,
            WritableImage snapshot,
            String text
    ) throws IOException {
        PdfWriter pdfWriter = new PdfWriter(file.toString());
        Document document = new Document(new PdfDocument(pdfWriter));
        addTitle(document, text);
        addImage(document, snapshot);
        document.close();
    }

    private static void addTitle(Document document, String text) {
        document.add(new Paragraph("Program\n" + "Course work program\n" + "Name project: " + text));
    }


    private static void addImage(Document document, WritableImage image) throws IOException {
        File file = File.createTempFile("image", ".png");
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        ImageData imageData = ImageDataFactory.create(file.toURI().toURL());
        Image pdfImage = new Image(imageData);
        document.add(pdfImage);

//        imageData = ImageDataFactory.create("D:\\Programming\\JavaPrograms\\untitled2\\diagram.png");
//        pdfImage = new Image(imageData);
//        document.add(pdfImage);
    }


}
