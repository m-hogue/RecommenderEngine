package com.recommender.engine;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class PearsonRecommender {

	public static void main(String[] args) throws TasteException, IOException {
		int userNeighborhoodSize = 7;
		// retrieve data model
		DataModel model = new FileDataModel(new File("1M_ratings.csv"));
		
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
		
		// Ask for 5 movie recommendations for user 809671
		List<RecommendedItem> recommendations = recommender.recommend(809671, 5);
		
		for(RecommendedItem item : recommendations){
			System.out.println(item);
		}
	}
}
