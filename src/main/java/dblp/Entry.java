package dblp;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Class that represents an entry in the DBLP xml file
 *
 * @see <a href="http://dblp.uni-trier.de/faq/What+do+I+find+in+dblp+xml">DBLP: What do I find in dblp.xml?</a>
 */
public class Entry {
    public static final String ALL_PUBLICATION_TYPE = "all";
    public static final String ARTICLE_TYPE = "article";
    public static final String PHDTHESIS_TYPE = "phdthesis";
    public static final String MASTERTHESIS_TYPE = "masterthesis";
    public static final String WWW_TYPE = "www";

    public static final String AUTHOR_ENTITY = "author";

    // type of publication
    protected String type;

    // modification date
    protected Date mdate;

    // key of reference
    protected String key;

    // the authors
    protected final List<String> authors = new LinkedList<>();

    // the title of the publication
    protected String title;

    // other properties
    protected final Map<String, String> properties = new HashMap<>();

    public Entry(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Entry setType(String type) {
        this.type = type;
        return this;
    }

    public Entry addAuthor(String author) {
        authors.add(author);
        return this;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getKey() {
        return key;
    }

    public Entry setKey(String key) {
        this.key = key;
        return this;
    }

    public Date getMdate() {
        return mdate;
    }

    public Entry setMdate(String date) {
        this.mdate = Date.from(LocalDate.parse(date, DateTimeFormatter.ISO_DATE).atStartOfDay(ZoneId.of("UTC")).toInstant());
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Entry setTitle(String title) {
        this.title = title;
        return this;
    }

    public Entry setProperty(String key, String value) {
        this.properties.put(key, value);
        return this;
    }

    public String getProperty(String key) {
        return this.properties.get(key);
    }

    public Map<String, String> getProperties() {
        return properties;
    }
}
