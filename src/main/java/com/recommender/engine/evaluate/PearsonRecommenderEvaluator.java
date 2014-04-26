package com.recommender.engine.evaluate;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;

public class PearsonRecommenderEvaluator {

	public static void main(String[] args) throws IOException, TasteException {
		
		RecommenderBuilder builder = new RecommenderBuilder() {
			
			public Recommender buildRecommender(DataModel model) throws TasteException {
				int userNeighborhoodSize = 7;
				
				// Construct offline user similarity by determining Pearson Correlation
				// similarity
				UserSimilarity similarity = 
						new PearsonCorrelationSimilarity(model);
				
				// Build user neighborhood given the similarity matrix and data model
				UserNeighborhood neighborhood = 
						new NearestNUserNeighborhood(userNeighborhoodSize, similarity, model);
				
				// Finally, construct a recommender provided with the three sub-components:
				// A data model, a use neighborhood, and a similarity matrix.
				UserBasedRecommender recommender = 
						new GenericUserBasedRecommender(model, neighborhood, similarity);
				return recommender;
			}
		};
		
		//Use this only if the code is for unit tests and 
		// other examples to guarantee repeated results
		RandomUtils.useTestSeed();
		
		// Retrieve data model
		FileDataModel model = new FileDataModel(new File("1M_ratings.csv"));
		
		// Build root mean squared (RMS) evaluator
		RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
		
		// Evaluate model
		double userSimEvaluationScore = evaluator.evaluate(builder, null, model, 0.7, 1.0);
        System.out.println("User Similarity Evaluation score : " + userSimEvaluationScore);

	}

}
