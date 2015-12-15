package dblp;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.*;
import org.xml.sax.ext.DefaultHandler2;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Alvin Natawiguna on 12/14/2015.
 */
public class DBLPEntryHandler extends DefaultHandler2 {
    protected static final String MDATE_ATTRIBUTE = "mdate";
    protected static final String KEY_ATTRIBUTE = "key";

    protected static final String AUTHOR_ENTITY = "author";
    protected static final String TITLE_ENTITY = "title";

    private Locator locator;

    protected final List<Entry> entryList;

    protected Entry currentEntry;

    protected StringBuilder currentValue = new StringBuilder();
    protected String currentTag;

    public DBLPEntryHandler(List<Entry> entryList) {
        assert(entryList != null);
        this.entryList = entryList;
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    @Override
    public void startElement(String namespaceURI, String localName, String rawName, Attributes atts)
            throws SAXException
    {
        if (!StringUtils.isBlank(rawName)) {
            if (rawName.equals("dblp")) {
                return;
            }
            // handle the element tag
            String key;
            String mdate;

            key = atts.getValue(KEY_ATTRIBUTE);
            mdate = atts.getValue(MDATE_ATTRIBUTE);

            if (key != null && mdate != null) {
                currentEntry = new Entry(rawName);
                currentEntry.setKey(key)
                        .setMdate(mdate);
            } else {
                // handle the entity tags
                if (currentEntry == null) {
                    throw new SAXParseException("No entry defined for tag " + rawName, locator);
                }

                currentTag = rawName;
                currentValue.setLength(0);
            }
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String rawName) throws SAXException {
        if (!StringUtils.isBlank(rawName)) {
            if (rawName.equals(currentEntry.getType())) {
                entryList.add(currentEntry);
                currentEntry = null;
            } else if (rawName.equals("dblp")) {
                // just ignore
            } else {
                if (!StringUtils.isBlank(currentTag)) {
                    if (currentEntry == null) {
                        throw new SAXParseException("No entry defined for tag " + rawName, locator);
                    }

                    switch (currentTag) {
                        case TITLE_ENTITY:
                            currentEntry.setTitle(currentValue.toString());
                            break;
                        case AUTHOR_ENTITY:
                            if (!currentEntry.getAuthors().contains(currentValue.toString())) {
                                currentEntry.addAuthor(currentValue.toString());
                            }
                            break;
                        default:
                            currentEntry.setProperty(currentTag, currentValue.toString());
                            break;
                    }

                    currentTag = "";
                }
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (!StringUtils.isBlank(currentTag)) {

            currentValue.append(ch, start, length);
        }
    }

    @Override
    public InputSource getExternalSubset (String name, String baseURI) throws SAXException, IOException {
        InputStream is = ClassLoader.class.getResourceAsStream("dblp.dtd");
        return new InputSource(is);
    }

    @Override
    public InputSource resolveEntity (String name, String publicId, String baseURI, String systemId)
            throws SAXException, IOException
    {
        InputStream is = ClassLoader.class.getResourceAsStream("dblp.dtd");
        return new InputSource(is);
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException {
        InputStream is = ClassLoader.class.getResourceAsStream("dblp.dtd");
        return new InputSource(is);
    }
}
