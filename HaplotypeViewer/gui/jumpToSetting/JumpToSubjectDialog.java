package gui.jumpToSetting;

import java.awt.Window;

import settings.typed.JumpToSubjectSetting;
import viewmodel.ViewModel;

public class JumpToSubjectDialog extends JumpToSettingDialog {

	private JumpToSubjectSetting setting;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6536687329829963129L;
	
	private ViewModel viewModel;
	
	public JumpToSubjectDialog(Window owner, ViewModel viewModel) {
		super(owner, new JumpToSubjectSetting());
		this.setting = (JumpToSubjectSetting)getSetting();
		this.viewModel = viewModel;
	}

	@Override
	protected void jumpTo() {
		String value = this.setting.getValue();
		
		switch(this.setting.getMethod()) {
		case JumpToSubjectSetting.SUBJECT_INDEX:
			Integer indexInDataset = Integer.parseInt(value);
			int indexInVis = viewModel.getRowInVis(indexInDataset);
			viewModel.jumpToRow(indexInVis);
			break;
		case JumpToSubjectSetting.SUBJECT_NAME:
			indexInDataset = viewModel.getDataSet().getSubjectByName(value);
			indexInVis = viewModel.getRowInVis(indexInDataset);
			viewModel.jumpToRow(indexInVis);
			break;
		}
	}
}
