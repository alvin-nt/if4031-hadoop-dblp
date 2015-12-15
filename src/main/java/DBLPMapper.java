
import dblp.DBLPEntryHandler;
import dblp.Entry;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.xml.sax.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * Created by Alvin Natawiguna on 12/13/2015.
 */
public class DBLPMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
    public static final String TAGS_KEY = "mapper.tags";
    private static final LongWritable ONE = new LongWritable(1);

    protected String tagToExtract;

    protected List<Entry> entries = new LinkedList<>();

    @Override
    public void setup(Context context) throws IOException, InterruptedException {
        Configuration config = context.getConfiguration();
        tagToExtract = config.get(TAGS_KEY);

        if (StringUtils.isBlank(tagToExtract)) {
            throw new InterruptedException("No tag defined for mapping.");
        }
    }

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // setup the parser
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);

        SAXParser parser;
        try {
            parser = factory.newSAXParser();

            DBLPEntryHandler handler = new DBLPEntryHandler(entries);
            System.out.println(value.toString());

            parser.parse(new InputSource(new StringReader(value.toString())), handler);

            for (Entry entry : entries) {
                switch (tagToExtract) {
                    case Entry.ALL_PUBLICATION_TYPE:
                        context.write(new Text(entry.getType()), ONE);
                        break;
                    case Entry.ARTICLE_TYPE:
                    case Entry.MASTERTHESIS_TYPE:
                    case Entry.PHDTHESIS_TYPE:
                    case Entry.WWW_TYPE:
                        String entryType = entry.getType();
                        if (entryType.equals(tagToExtract)) {
                            context.write(new Text(entryType), ONE);
                        }
                        break;
                    case Entry.AUTHOR_ENTITY:
                        for (String author: entry.getAuthors()) {
                            context.write(new Text(author), ONE);
                        }
                        break;
                    default:
                        throw new InterruptedException("Unknown tag: " + tagToExtract);
                }
            }

        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException(e);
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        entries.clear();
    }
}
