package com.intecon.documentparser.service.impl;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.ImageIOUtil;

import com.github.jaiimageio.impl.plugins.tiff.TIFFImageWriter;
import com.github.jaiimageio.impl.plugins.tiff.TIFFImageWriterSpi;

public class ConverterService {

	public static String converterPdfTotiff( String pdfPath) throws Exception {
        // got the solution from
        System.out.println("Needs to convert pdf to tiff...");

        String filename = "YOURFILENAMEHERE.pdf";

        // open the document
        PDDocument doc = PDDocument.load(new File(pdfPath));

        boolean b;
        List<PDPage> pages = doc.getDocumentCatalog().getAllPages();
//        URL input = new URL("https://memorynotfound.com/wp-content/uploads/java-duke-to-jpg.jpg");
//        BufferedImage image = ImageIO.read(new File("bwpage-14.png"));
//
//        ImageIO.write(image, "png", new File(pdfPath.replace(".pdf", ".png")));
//        ImageIO.write(image, "gif", new File(pdfPath.replace(".pdf", ".gif")));
//        ImageIO.write(image, "bmp", new File(pdfPath.replace(".pdf", ".bmp")));
//        ImageIO.write(image, "tiff", new File(pdfPath.replace(".pdf", ".tiff")));
        for (int p = 0; p < pages.size(); ++p)
        {
            // RGB image with 300 dpi
            //BufferedImage bim = pages.get(p).convertToImage(BufferedImage.TYPE_INT_RGB, 300);
            
            // save as PNG with default metadata
//            b = ImageIO.write(bim, "png", new File("rgbpage" + (p+1) + ".png"));
//            if (!b)
//            {
//                // error handling
//            }

            // B/W image with 300 dpi
        	
        	
            BufferedImage bim = pages.get(p).convertToImage(BufferedImage.TYPE_BYTE_BINARY, 400);
            RenderedImage rendImage = bim;
            boolean d = ImageIO.write(rendImage, "tif", new java.io.File(pdfPath.replace(".pdf", ".tiff")));
            //d = ImageIO.write(bim, "tif", new java.io.File(pdfPath.replace(".pdf", "1.tiff")));
            // save as TIF with dpi in bim metadata
            // PDFBox will choose the best compression for you - here: CCITT G4
            // you need to add jai_imageio.jar to your classpath for this to work
            //d = ImageIO.write(bim, "TIF", new java.io.File(pdfPath.replace(".pdf", "2.tiff")));
            //OutputStream output=new FileOutputStream(new File(pdfPath.replace(".pdf", "4.tiff")));
            //writeUsingMMCComputingImageIO(bim,output);
//           b = ImageIOUtil.writeImage(
//        	          bim, "tst.tiff", 300);
        }
        return pdfPath.replace(".pdf", ".tiff");
    }
	
	public static void writeUsingMMCComputingImageIO(BufferedImage bi,OutputStream os) {
		  TIFFImageWriterSpi tiffspi=new TIFFImageWriterSpi();
		  ImageOutputStream ios=null;
		  ImageWriter writer;
		  try {
		    writer=tiffspi.createWriterInstance();
		    ios=ImageIO.createImageOutputStream(os);
		    writer.setOutput(ios);
		    writer.write(bi);
		  }
		 catch (  IOException e) {
		    e.printStackTrace();
		  }
		 finally {
		    if (ios != null) {
		      try {
		        ios.flush();
		        ios.close();
		      }
		 catch (      IOException e) {
		        e.printStackTrace();
		      }
		    }
		  }
	}
		
}
