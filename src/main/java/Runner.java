import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

/**
 * Created by Alvin Natawiguna on 12/14/2015.
 */
public class Runner {
    public static void main(String args[]) throws IOException, ClassNotFoundException, InterruptedException {
        if (args.length < 3) {
            printUsage();
            System.exit(1);
        }

        // set the configuration first
        Configuration conf = new Configuration();
        conf.set(DBLPMapper.TAGS_KEY, args[2]);
        conf.set(XMLInputFormat.START_TAG_KEY, "<dblp>");
        conf.set(XMLInputFormat.END_TAG_KEY, "</dblp>");
        conf.set("io.serializations",
                "org.apache.hadoop.io.serializer.JavaSerialization,"
                + "org.apache.hadoop.io.serializer.WritableSerialization");

        if (args.length >= 4) {
            conf.set(DBLPSumReducer.REDUCER_MODE_KEY, args[3]);
        }

        Job job = Job.getInstance(conf, "DBLP SumReducer");

        job.setJarByClass(Runner.class);
        job.setInputFormatClass(XMLInputFormat.class);
        job.setMapperClass(DBLPMapper.class);

        job.setCombinerClass(DBLPSumReducer.class);
        job.setReducerClass(DBLPSumReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        Path outputPath = new Path(args[1]);

        LocalFileSystem fs = FileSystem.getLocal(conf);
        if (fs.exists(outputPath)) {
            fs.delete(outputPath, true);
        }

        FileOutputFormat.setOutputPath(job, outputPath);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    protected static void printUsage() {
        System.out.println("Usage:\n\n" +
                "hadoop jar <jar_name> <input_path> <output_path> <mode> (filter_to_top_n)\n" +
                "\n" +
                "Available modes:\n" +
                "all - print the number of publications in the database\n" +
                "article - print the number of articles\n" +
                "phdthesis - print the number of Ph.D theses\n" +
                "masterthesis - print the number of master theses\n" +
                "authors - print the authors.");
    }
}
