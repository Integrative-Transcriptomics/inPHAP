package gui.jumpToSetting;

import java.awt.Window;

import settings.typed.JumpToSNVSetting;
import viewmodel.ViewModel;

public class JumpToSNVDialog extends JumpToSettingDialog {

	private JumpToSNVSetting setting;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1837665781094443480L;
	
	private ViewModel viewModel;
	
	public JumpToSNVDialog(Window owner, ViewModel viewModel) {
		super(owner, new JumpToSNVSetting());
		this.setting = (JumpToSNVSetting)getSetting();
		this.viewModel = viewModel;
	}

	@Override
	protected void jumpTo() {
		String value = this.setting.getValue();
		
		switch(this.setting.getMethod()) {
		case JumpToSNVSetting.SNV_ID:
			int indexInDataSet = viewModel.getDataSet().getSNPIndex(value);
			Integer columnInVis = viewModel.getColumnInVis(indexInDataSet);
			viewModel.jumpToColumn(columnInVis);
			break;
		case JumpToSNVSetting.SNV_INDEX:
			int columnInDataSet = Integer.parseInt(value);
			columnInVis = viewModel.getColumnInVis(columnInDataSet);
			viewModel.jumpToColumn(columnInVis);
			break;
		case JumpToSNVSetting.SNV_POS:
			int snpPos = Integer.parseInt(value);
			columnInDataSet = viewModel.getDataSet().getSNPByPosition(snpPos);
			columnInVis = viewModel.getColumnInVis(columnInDataSet);
			viewModel.jumpToColumn(columnInVis);
			break;
		}
	}
}
