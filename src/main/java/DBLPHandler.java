import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by Alvin Natawiguna on 12/13/2015.
 */
public class DBLPHandler extends DefaultHandler {
    private static final String ARTICLE_ELEMENT = "article";
    private static final String INPROCEEDINGS_ELEMENT = "inproceedings";
    private static final String PHDTHESIS_ELEMENT = "phdtesis";
    private static final String MASTERTHESIS_ELEMENT = "masterthesis";
    //private static final String WWW_ELEMENT = "www";

    private static final String AUTHOR_ELEMENT = "author";

    private Locator locator;

    private String key;

    @Override
    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    @Override
    public void startElement(String namespaceURI, String localName, String rawName, Attributes atts)
            throws SAXException
    {
        if (rawName != null) {
            if (rawName.equals(ARTICLE_ELEMENT)) {

            } else if (rawName.equals(INPROCEEDINGS_ELEMENT)) {

            } else if (rawName.equals(PHDTHESIS_ELEMENT)) {

            }
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String rawName) throws SAXException {

    }

    @Override
    public void characters(char[] ch, int start, int length) {

    }
}
