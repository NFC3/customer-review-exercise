/*    */ package de.hybris.platform.customerreview.services;
/*    */ 
/*    */ import de.hybris.platform.core.model.product.ProductModel;
/*    */ import de.hybris.platform.core.model.user.UserModel;
/*    */ import de.hybris.platform.customerreview.CustomerReviewService;
/*    */ import de.hybris.platform.customerreview.model.CustomerReviewModel;
/*    */ import de.hybris.platform.product.ProductService;
/*    */ import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
/*    */ import de.hybris.platform.servicelayer.user.UserService;
/*    */ import de.hybris.platform.util.Config;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.TreeSet;
/*    */ import javax.annotation.Resource;
/*    */ import org.junit.After;
/*    */ import org.junit.Assert;
/*    */ import org.junit.Before;
/*    */ import org.junit.Test;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CustomerReviewServiceTest
/*    */   extends ServicelayerTransactionalTest
/*    */ {
/*    */   @Resource
/*    */   private CustomerReviewService customerReviewService;
/*    */   @Resource
/*    */   private ProductService productService;
		   @Resource
		   private CustomCustomerReviewService customCustomerReviewService;
/*    */   @Resource
/*    */   private UserService userService;
/*    */   private UserModel userModel01;
/*    */   private UserModel userModel02;
/*    */   private UserModel userModel03;
/*    */   private ProductModel productModel01;
/*    */   private String oldMinimalRating;
/*    */   private String oldMaximalRating;
/*    */   
/*    */   @Before
/*    */   public void setUp()
/*    */     throws Exception
/*    */   {
/* 61 */     createCoreData();
/* 62 */     createDefaultCatalog();
/* 63 */     this.productModel01 = this.productService.getProduct("testProduct1");
/* 64 */     this.userModel01 = this.userService.getUser("anonymous");
/* 65 */     this.userModel02 = this.userService.getCurrentUser();
			 this.userModel03 = this.userService.getUser("anonymous2");
/*    */     
/* 67 */     this.oldMinimalRating = Config.getParameter("customerreview.minimalrating");
/* 68 */     this.oldMaximalRating = Config.getParameter("customerreview.maximalrating");
/*    */     
/*    */ 
/* 71 */     Config.setParameter("customerreview.minimalrating", String.valueOf(0));
/* 72 */     Config.setParameter("customerreview.maximalrating", String.valueOf(4));
/*    */   }
/*    */   
/*    */   @After
/*    */   public void tearDown()
/*    */   {
/* 78 */     Config.setParameter("customerreview.minimalrating", this.oldMinimalRating);
/* 79 */     Config.setParameter("customerreview.maximalrating", this.oldMaximalRating);
/*    */   }
/*    */   
			@Test
			public void testCustomCustomerReviewService()  {
				this.customCustomerReviewService.createCustomerReview(Double.valueOf(1.0D), "headline_anonymous", "comment_anonymous", this.userModel01, 
						this.productModel01);
				this.customCustomerReviewService.createCustomerReview(Double.valueOf(3.0D), "headline_admin", "comment_admin", this.userModel02, 
						this.productModel01);
				Assert.assertEquals("Number of reviews within range is 1", 1.0D, this.customCustomerReviewService.getNumberOfReviews(this.productModel01, 1, 2).doubleValue(), 0.001D);
				//Testing if the correct total number of results is returned when min and max ratings are inclusive
				Assert.assertEquals("Number of reviews within range is 2", 1.0D, this.customCustomerReviewService.getNumberOfReviews(this.productModel01, 1, 3).doubleValue(), 0.001D);
				
				try {
						this.customCustomerReviewService.createCustomerReview(Double.valueOf(3.0D), "headline_anonymous2", "comment contains curse_word_1", this.userModel03, 
								this.productModel01);
						Assert.fail("CurseWordsFoundException expected");
				}
				catch (CurseWordsFoundException e){					
				}
				
				try {
					this.customCustomerReviewService.createCustomerReview(Double.valueOf(-1.0D), "headline_anonymous2", "comment", this.userModel03, 
							this.productModel01);
					Assert.fail("RatingException expected");
				}
				catch (RatingException e){					
				}							
/*    */   }
/*    */ }


/* Location:              /Users/TJL4646/CustomerReview_Assignment/customerreviewserver.jar!/de/hybris/platform/customerreview/services/CustomerReviewServiceTest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */