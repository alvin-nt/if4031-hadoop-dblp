import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by Alvin Natawiguna on 12/13/2015.
 */
public class DBLPSumReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
    public static final String REDUCER_MODE_KEY = "reducer.mode";
    public static final String REDUCER_MODE_SUM = "sum";

    protected static final int REDUCER_MODE_NUMBER_SUM = 0;

    // reducer modes:
    // 0 --> all
    // 1+ --> top n
    protected int mode = REDUCER_MODE_NUMBER_SUM;

     @Override
     public void setup(Context context) throws IOException, InterruptedException {
         Configuration config = context.getConfiguration();
         String mode = config.get(REDUCER_MODE_KEY);

         if (!StringUtils.isBlank(mode)) {
             if (mode.equalsIgnoreCase(REDUCER_MODE_SUM)) {
                 this.mode = REDUCER_MODE_NUMBER_SUM;
             } else {
                 try {
                     this.mode = Integer.parseInt(mode);
                     if (this.mode < REDUCER_MODE_NUMBER_SUM) {
                         throw new InterruptedException("Invalid mode: " + mode);
                     }
                 } catch (NumberFormatException e) {
                     throw new InterruptedException("Invalid mode: " + mode);
                 }
             }
         } else {
             this.mode = REDUCER_MODE_NUMBER_SUM;
         }
     }

    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        long sum = 0;
        for (LongWritable value: values) {
            sum += value.get();
        }

        context.write(key, new LongWritable(sum));
    }
}
