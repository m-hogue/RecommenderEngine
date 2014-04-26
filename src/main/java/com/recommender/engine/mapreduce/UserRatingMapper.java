package com.recommender.engine.mapreduce;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * This class assumes it is reading a datafile from the Netflix Prize dataset.
 * They have the following format:
 * 
 * First line is the movie ID, followed by a colon
 * Every other line is in the format: "UserID,RatingValue,RatingDate"
 * UserID is an integer. RatingValue is an integer, 1-5. RatingDate is
 *  YYYY-MM-DD
 * 
 * This Mapper class emits the intermediate tuple: (UserID, MovieID, Rating).
 * MovieID is an IntWritable object, and {UserID, MovieID, Rating} is an IntArrayWritable
 * of three IntWritable objects. 
 * 
 * @author Michael Hogue
 *
 */
public class UserRatingMapper extends Mapper<LongWritable,Text,NullWritable,Text> {
	
	private static Pattern userRatingDate = Pattern.compile("^(\\d+),(\\d+),\\d{4}-\\d{2}-\\d{2}$");
	private static Pattern movieIDPattern = Pattern.compile("\\d+:");

	private Text tuple = new Text();
	private static String movieID;
	private NullWritable NULL = NullWritable.get();
	
	/**
	 * Given a lint of input, if it is a MovieID line, then it extracts that ID and holds onto it.
	 * Otherwise, it is a UserID,RatingValue,RatingDate line,
	 * and it extracts the UserID and RatingValue, and emits (UserID, MovieID, Rating)
	 */
	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

		String line = value.toString();
		String userId, rating;

		/* Use a full blown Matcher, so I can pull out the grouped ID and Rating */
		Matcher userRating = userRatingDate.matcher(line);
		
		if(line.matches(".*\\d+:$")) {
			/* Pull the Movie ID line */
			Matcher m = movieIDPattern.matcher(line);
			if(m.find()){
				System.out.println("Found ID: " + m.group(0));
				String val = m.group(0); 			
				movieID = val.substring(0,val.length()-1);
			} else
				System.out.println("ID not found: " + line);
		} else if (userRating.matches()) {
			/* 1st Regex Group is UserID */
			userId = userRating.group(1);
			/* 2nd Regex Group is Rating */
			rating = userRating.group(2);
			
			tuple.set(userId + "," + movieID + "," + rating);
			
			/* write the key and value */
			context.write(NULL, tuple);
			
		} else {
			/* Should not occur. The input is in an invalid format, or
			 * my regex is wrong.
			 */
		}
	}
}