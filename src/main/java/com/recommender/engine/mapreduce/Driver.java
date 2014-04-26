package com.recommender.engine.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Driver class for RecommenderEngine processor of input movie rating data. 
 * Runs the Hadoop instance, setting up the Map/Reduce classes, input/output 
 * paths, and the Key/Value classes.
 * 
 * Invoke with 2 arguments, or 1.
 * 
 * @author Michael Hogue
 *
 */
public class Driver extends Configured implements Tool {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		ToolRunner.run(conf, new Driver(), args);
	}

	public int run(String[] args) throws Exception {
		/* Require args to contain the paths */
		if(args.length != 1 && args.length != 2) {
			System.err.println("Error! Usage: \n" +
					"Driver <input dir> <output dir>\n" +
					"Driver <job.xml>");
			System.exit(1);
		}
		
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		
		if(args.length == 2) {
			conf.set("mapred.child.java.opts", "-Xmx2048m");
			
			/* UserRatingMapper outputs (IntWritable, IntArrayWritable(Writable[2])) */
			job.setMapOutputKeyClass(NullWritable.class);
			job.setMapOutputValueClass(Text.class);
			job.setJarByClass(UserRatingMapper.class);
			
			/* AverageValueReducer outputs (IntWritable, FloatWritable) */
			job.setOutputKeyClass(NullWritable.class);
			job.setOutputValueClass(Text.class);
	
			/* Pull input and output Paths from the args */
			FileInputFormat.setInputPaths(job, new Path(args[0]));
			FileOutputFormat.setOutputPath(job, new Path(args[1]));
	
			/* Set to use Mapper and Reducer classes */
			job.setMapperClass(UserRatingMapper.class);
			job.setReducerClass(UserRatingReducer.class);
			
		} else {
			// Return failure since there are not two arguments
			return 1;
		}
		
		job.submit();
		return 0;
	}
}
