package aggregation;

import viewmodel.ViewModel;


public interface IMatrixAggregator {

	public AggregationMatrixRow aggregate(ViewModel viewModel, Aggregation a);
}
