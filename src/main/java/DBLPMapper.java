import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * Created by Alvin Natawiguna on 12/13/2015.
 */
public class DBLPMapper extends Mapper<LongWritable, Text, NullWritable, Text> {


    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String xmlFile = value.toString();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();

        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException(e);
        }
    }
}
