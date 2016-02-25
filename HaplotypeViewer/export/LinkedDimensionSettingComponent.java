package export;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import settings.HierarchicalSetting;

public class LinkedDimensionSettingComponent extends JPanel implements ChangeListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6018313641581112952L;
	DimensionInput widthInput, heightInput;
	JCheckBox linkDimensions;
	private int prevW, prevH;
	LinkedDimensionSetting mySetting;
	
	public LinkedDimensionSettingComponent(LinkedDimensionSetting s) {		
		super(new GridLayout(0,3));
		mySetting = s;
//		setBorder(BorderFactory.createTitledBorder("Dimensions"));
		
		DocumentListener kl = new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				recomputeDimensions(false);				
			}

			public void insertUpdate(DocumentEvent e) {
				recomputeDimensions(false);			
				}

			public void removeUpdate(DocumentEvent e) {
				recomputeDimensions(false);
			}
		};
//		FocusListener fl = new FocusAdapter() {			
//			public void focusLost(FocusEvent fe) {				
//				recomputeDimensions();
//			}
//		};

		prevW = mySetting.width.getValue();
		prevH = mySetting.height.getValue();
		
		add(new JLabel("Original Width: "+prevW));
		add(Box.createHorizontalGlue());
		add(new JLabel("Original Height: "+prevH));
		
		add(widthInput=new DimensionInput("Width",""+mySetting.width.getValue(),kl));
		add(Box.createHorizontalGlue());
		add(heightInput=new DimensionInput("Height",""+mySetting.height.getValue(),kl));
		
		linkDimensions = new JCheckBox("Preserve aspect ratio");
		linkDimensions.setSelected(mySetting.linkDimensions.getValue());
		
		linkDimensions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				recomputeDimensions(true);
			}
		});
		add(linkDimensions);
	}
	
	protected void initValues() {
		widthInput.getField().setText(Integer.toString(mySetting.width.getValue()));
		heightInput.getField().setText(Integer.toString(mySetting.height.getValue()));
		linkDimensions.setSelected(mySetting.linkDimensions.getValue());
		prevW = mySetting.width.getValue();
		prevH = mySetting.height.getValue();
	}
	
	private boolean noReentry = false;
	
	protected void recomputeDimensions(boolean force) {
		if (noReentry)
			return;
		noReentry = true;
		
		int W = mySetting.width.getValue();
		int H = mySetting.height.getValue();
		
		boolean keepAspect = linkDimensions.isSelected();
		
		try {
			W = Integer.parseInt(widthInput.getField().getText());
		} catch ( NumberFormatException exception ) {}
		
		try {
			H = Integer.parseInt(heightInput.getField().getText());
		} catch ( NumberFormatException exception ) {}
		
		if (keepAspect) {
			double factor;
			
			if (W!=prevW || force) {
				factor = (double)mySetting.height.getValue()/(double)mySetting.width.getValue();
				H = (int)Math.round(factor*W);
				prevH = H;				
				heightInput.getField().setText( ""+H );
			} 	
			if (H!=prevH || force ) { 
				factor = (double)mySetting.width.getValue()/(double)mySetting.height.getValue();
				W = (int)Math.round(factor*H);
				prevW = W;
				widthInput.getField().setText( ""+W );
			}
			
		}					
		prevH = H;
		prevW = W;

		noReentry = false;
	}


	protected class DimensionInput extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = -735599145981787907L;
		JTextField dInput;
		public DimensionInput(String title, String preValue, DocumentListener fl) {
			setLayout(new BorderLayout());
			dInput = new JTextField(preValue);
			JLabel dLabel = new JLabel(title);
			add(dLabel, BorderLayout.WEST);
			add(dInput, BorderLayout.EAST);
			dInput.setColumns( 5 );
			dInput.getDocument().addDocumentListener(fl);
		}
		public JTextField getField() {
			return dInput;
		}
	}


	public JComponent getEditorComponent() {
		return this;
	}
	
	public boolean updateSettingFromEditor(boolean failSilently) {
		mySetting.width.setValue(Integer.parseInt(widthInput.getField().getText()));
		mySetting.height.setValue(Integer.parseInt(heightInput.getField().getText()));
		mySetting.linkDimensions.setValue(linkDimensions.isSelected());
		return true;
	}

	public void stateChanged(ChangeEvent e) {
		initValues();
	}
	
	public boolean needsLabel() {
		return false;
	}

	public HierarchicalSetting getCorrespondingSetting() {
		return mySetting;
	}
}
