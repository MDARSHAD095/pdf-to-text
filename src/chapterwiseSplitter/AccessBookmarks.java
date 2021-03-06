package chapterwiseSplitter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.util.PDFTextStripper;

public class AccessBookmarks {

	private static PDDocument doc;
	
	public static void main(String args[]) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter path of input pdf: ");
		String inputPdf = "/home/lekha/Documents/files/java.pdf";//br.readLine();
		
		doc = PDDocument.load(inputPdf);
		
		PDDocumentOutline root = doc.getDocumentCatalog().getDocumentOutline();
		PDOutlineItem item = root.getFirstChild();
		
		handleItem(item);
		
		
	}
	
	public static void handleItem(PDOutlineItem item) throws IOException
	{
		if(item == null)
		{
			System.out.println("Error: The item is null!");
			return;
		}
		if(item.getFirstChild() == null)
		{
			//it is a leaf
			System.out.println("Leaf: "+item.getTitle());
			
			PDFTextStripper stripper = new PDFTextStripper();
			String outputFilename = item.getTitle()+".out";
			
			//replace below code to remove all illegal characters from filename (if any)
			outputFilename = outputFilename.replace(',', ' ');
			outputFilename = outputFilename.replace('/', ' ');
			
			//code to be replaced ends here
			
			//System.out.println("000000000"+outputFilename);
			
			FileWriter outputStream = new FileWriter(new File(outputFilename));
			stripper.setStartBookmark(item);
			//the problem below is that for a node with no next sibling we get text starting from the node to end of book whereas we just want from the node to next node
			stripper.setEndBookmark(item.getNextSibling());
			stripper.writeText(doc, outputStream);
			outputStream.flush();
			outputStream.close();
			
		}
		else
		{// internal node
			PDOutlineItem child = item.getFirstChild();
			while(child != null)
			{
				handleItem(child);
				child = child.getNextSibling();
			}
		}
		
		
	}
	
	public static void main2(String args[]) throws IOException
	{
			//take user input of path of input pdf
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter path of a pdf to split: ");
			String inputPdf = "/home/lekha/Documents/files/java.pdf";//br.readLine();
			
			//create a pdfdocument object for the input pdf
			PDDocument doc = PDDocument.load(inputPdf);
			
			//get root node of the pdf
			PDDocumentOutline root = doc.getDocumentCatalog().getDocumentOutline();
			//get first child of the root
			PDOutlineItem item = root.getFirstChild();
			PDFTextStripper stripper = new PDFTextStripper();
		        
			while( item != null )
		      {
				
				
		          System.out.println( "Item:" + item.getTitle());
		          
		          PDOutlineItem child = item.getFirstChild();
		          
		          while( child != null )
		          {
		        	  int count = 1;
		              System.out.println( "    Child l1:" + child.getTitle() );
		              PDOutlineItem child2 = child.getFirstChild();
			          while( child2 != null )
			          {
			              System.out.println( "\t    Child l2:" + child2.getTitle() );
			              PDOutlineItem child3 = child2.getFirstChild();
				          while( child3 != null )
				          {
				              System.out.println( "\t\t    Child l3:" + child3.getTitle() );
				              
				              child3 = child3.getNextSibling();
				          }
				          count++;
				          if(count == 3)
				          {
				          FileWriter outputStream = new FileWriter(new File("dummyOutput.txt"));
				          stripper.setStartBookmark(child);

			              stripper.setEndBookmark(child.getNextSibling());
			              stripper.writeText(doc,outputStream);
			              outputStream.flush();
			              outputStream.close();
			              }

				          child2 = child2.getNextSibling();
			          }
			          			          child = child.getNextSibling();
		          }
		          
		          item = item.getNextSibling();
		      }
		
		      
			
	}

}
