package gui.actions;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.AbstractAction;

import viewmodel.ViewModel;

/**
 * Created by peltzer on 2/27/14.
 */
public class HelpAction extends AbstractAction {
    /**
	 * 
	 */
	private static final long serialVersionUID = -1344518146010209014L;

    public HelpAction(ViewModel viewModel, String title){
        super(title);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            //TODO Set our own URL here ;-)
            URI uri = new URI("http://bit.ly/1iJgKmX");
            Desktop dt = Desktop.getDesktop();
            try {
                dt.browse(uri);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
    }
}
