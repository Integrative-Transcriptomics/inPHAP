package aggregation;

import viewmodel.ViewModel;

public interface IMetaAggregator {

	public AggregationMetaRow aggregate(ViewModel viewModel, Aggregation a);	
}
