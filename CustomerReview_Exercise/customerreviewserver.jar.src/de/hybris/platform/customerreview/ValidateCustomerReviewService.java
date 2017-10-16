package de.hybris.platform.customerreview;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import java.util.List;

public interface ValidateCustomerReviewService extends CustomerReviewService {
	public Integer getNumberOfReviews(ProductModel product, int min, int max);
}