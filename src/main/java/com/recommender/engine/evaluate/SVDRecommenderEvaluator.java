package com.recommender.engine.evaluate;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDPlusPlusFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.common.RandomUtils;

public class SVDRecommenderEvaluator {

	public static void main(String[] args) throws IOException, TasteException {
		RecommenderBuilder builder = new RecommenderBuilder() {
			
			public Recommender buildRecommender(DataModel model) throws TasteException {
				// The number of features and iterations to use 
				int numFeatures = 50;
				int numIterations = 10;
				
				// Construct the factorized SVD matrix given the data model, 
				// number of features, and the number of iterations.
				SVDPlusPlusFactorizer factorizer = 
						new SVDPlusPlusFactorizer(model, numFeatures, numIterations);
				
				// Construct an SVD recommender that utilizes the factorized
				// matrix of user similarity
				SVDRecommender recommender = 
						new SVDRecommender(model, factorizer); 
				return recommender;
			}
		};
		
		// Use this only if the code is for unit tests and 
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
