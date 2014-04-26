package com.recommender.engine;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDPlusPlusFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

public class SVDMatrixFactorizationRecommender {

	public static void main(String[] args) throws IOException, TasteException {
		// Retrieve the (userID, movieID, rating) tuple data model
		DataModel model = new FileDataModel(new File("1M_ratings.csv"));
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
		
		// Ask for 5 recommendations for userID 809671
		List<RecommendedItem> recommendations = 
				recommender.recommend(809671, 5);
		
		for(RecommendedItem item : recommendations){
			System.out.println(item);
		}
	}
}
