package com.recommender.engine.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class UserRatingReducer extends Reducer<NullWritable,Text,NullWritable,Text>{
	
	/**
	 * Assumes that the values iterator returns {IntWritable, IntWritable, IntWritable} 
	 * array. No need to do processing here, so we just write the values out.
	 */
	@Override
	protected void reduce(NullWritable key, Iterable<Text> values,
			Context context) throws IOException, InterruptedException {
		
		for(Text value : values){
			context.write(key, value);
		}
		
	}
}
