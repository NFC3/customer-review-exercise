/*    */ package de.hybris.platform.customerreview.impl;
/*    */ 
/*    */ import de.hybris.platform.core.model.c2l.LanguageModel;
/*    */ import de.hybris.platform.core.model.product.ProductModel;
/*    */ import de.hybris.platform.core.model.user.UserModel;
/*    */ import de.hybris.platform.customerreview.CustomerReviewService;
/*    */ import de.hybris.platform.customerreview.dao.CustomerReviewDao;
/*    */ import de.hybris.platform.customerreview.jalo.CustomerReview;
/*    */ import de.hybris.platform.customerreview.jalo.CustomerReviewManager;
/*    */ import de.hybris.platform.customerreview.model.CustomerReviewModel;
/*    */ import de.hybris.platform.jalo.product.Product;
/*    */ import de.hybris.platform.jalo.user.User;
/*    */ import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
/*    */ import de.hybris.platform.servicelayer.model.ModelService;
/*    */ import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*    */ import org.springframework.beans.factory.annotation.Required;

public class CustomCustomerReviewService extends DefaultCustomerReviewService implements ValidateCustomerReviewService {

	//Ideally, the curse words would be stored in the database.
	private String curseWordsDataFile;
	
	public String getCurseWordsDataFile() {
		return curseWordsDataFile;
	}

	//Spring injects the value into curseWordsDataFile
	public void setCurseWordsDataFile(String curseWordsDataFile) {
		this.curseWordsDataFile = curseWordsDataFile;
	}

	//1 - Get the number of reviews for a product whose rating is between a given range, inclusive.
	//Ideally, instead of making use of getAllReviews() in the superclass and filtering out the reviews
	//based on criteria, we should run a query on the database that retrieves this result.
	//I decided to make use of getAllReviews() in the superclass since the database structure wasn't completely disclosed.
	public Integer getNumberOfReviews(ProductModel product, int min, int max) {
		
		int reviewCount=0;
		//Using OOTB functionality to get all the review and then filtering based on criteria, min and max rating.
		List<CustomerReview> allReviews = super.getAllReviews(product);
		
		//A list of filtered reviews
		List<CustomerReview> byRatingReviews;
				
		//Filtering all reviews by criteria: minRating and maxRating
		if(allReviews != null) {
			byRatingReviews = allReviews.stream().filter(cr -> cr.getRating() >= min && cs.getRating() <= max);
		}
		
		if(byRatingReviews != null) {
			reviewCount=byRatingReviews.size();
		}
		return reviewCount;
	}
	
	/*Override the default  behaviour of createCustomerReview API to do custom validation.
	*Create customer review if validateRating() and checkCurseWords() does not throw an exception. 
	*/
	@Override
	public CustomerReviewModel createCustomerReview(Double rating, String headline, String comment, User user, Product product) {
		validateRating(rating);
		validateForCurseWords(comment);
		return super.createCustomerReview(rating, headline, comment, user, product);
	}

	//2a - Get the list of curse words from the data file.  Ideally, the curse words would be stored in the database.
	private List<String> getCurseWords() {
		List<String> curseWords = new ArrayList<>();
		
		//read file into stream, try-with-resources
		try (Stream<String> stream = Files.lines(Paths.get(curseWordsDataFile))) {
			curseWords=stream.collect(Collectors.toList());

		} catch (IOException e) {
				e.printStackTrace();
		}
		return curseWords;
	}
	
	//2b - Check that the user's review does not contain curse words as per the exercise instructions: 2b.
	private void validateForCurseWords(String comment) throws CurseWordsFoundException {
		if (getCurseWords().parallelStream().anyMatch(comment::contains)) {
			throw new CurseWordsFoundException("Curse words were found in the comment.");
		}
	}

	//2c - Check that the rating is not < 0 as per the exercise instructions
	private void validateRating(Double rating) throws RatingException {
		if (rating < 0) {
			throw new RatingException("Invalid rating value.  Rating value must be greater than 0.");
		}
	}		
}
