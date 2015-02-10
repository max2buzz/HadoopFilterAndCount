import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class MainHadoopClass {

	public static class Map
			extends
			Mapper<LongWritable, Text, Text, IntWritable> {

		@Override
		protected void map(LongWritable key,Text value,
				Context context) throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			IntWritable one = new IntWritable(1);
			String line = value.toString();
			
			String fields[] = line.split("::"); //SPliting based on a Delimeter.
			
			if(fields.length>6){
				//Fields[2] will give you the age group
				if(fields[1].equals("M")){
					context.write(new Text(fields[2] + " "+fields[1] ), one);
				}
				else{
					context.write(new Text(fields[2] + " "+fields[1] ), one);
				}
			}
		}
	}
	
	
	public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable >{
		
		@Override
		protected void reduce(Text key, Iterable<IntWritable> values,
				Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			
			int sum = 0; // initialize the sum for each keyword
			for (IntWritable val : values) {
			sum += val.get();
			}
			context.write(key, new IntWritable(sum));
			
		}
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();

		if (otherArgs.length != 2) {
			System.err.println("Usage: WordCount <in> <out>");
			System.exit(2);
		}

		Job job = new Job(conf, "WeatherCrunch");
		job.setJarByClass(MainHadoopClass.class);
		
		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));

		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);

	}

}
