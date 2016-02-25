/*
 * Created on 12.02.2005
 */
package tasks.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * A status bar with some boxes containing
 * {@linkplain StatusBarItem}s.
 * 
 * @author Matthias Zschunke
 *
 */
@SuppressWarnings("serial")
public class StatusBar 
extends JPanel
implements ChangeListener
{    
    private int n;
    private double[] fractions;
    private StatusBarItem[] items;
    private GridBagLayout gridbag;
    private GridBagConstraints gridbagConstraint;
    
    /**
     * Constructs a new status bar with equal-width
     * boxes.
     * 
     * @param n
     */
    public StatusBar(int n)
    {
        this.n=n;
        fractions=new double[n];
        Arrays.fill(fractions,1/n);       
        init();
    }
    
    public StatusBar(double[] fractions)
    {
        this.n=fractions.length;
        this.fractions=fractions;
        init();
    }
    
    private void init()
    {
        items=new StatusBarItem[n];
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE,30));
        this.setMinimumSize(new Dimension(500,20));
        
        
        gridbag = new GridBagLayout();
        gridbagConstraint = new GridBagConstraints();
        gridbagConstraint.fill=GridBagConstraints.BOTH;
        gridbagConstraint.ipadx=5;
        gridbagConstraint.ipady=2;
        //c.anchor=GridBagConstraints.WEST;
        gridbagConstraint.gridheight=1;
        gridbagConstraint.gridwidth=1;
        gridbagConstraint.gridy=0;
        gridbagConstraint.weighty=0.0;
        
        this.setLayout(
            gridbag
        );
        
        for(int i=0; i!=n; ++i)
        {
            gridbagConstraint.gridx=i;
            gridbagConstraint.weightx=this.fractions[i];
            
            //Box box=Box.createHorizontalBox();
            JPanel box=new JPanel();
            box.setLayout(
                new BoxLayout(box,BoxLayout.X_AXIS)
            );
            box.setAlignmentX(JPanel.LEFT_ALIGNMENT);
            
            
            box.setBorder(
                //BorderFactory.createLoweredBevelBorder()
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED)
            );
            
            
            box.setMaximumSize(new Dimension(30,20));
            box.setPreferredSize(box.getMaximumSize());
            
            StatusBarItem comp=new DefaultStatusItem(" ",i);
            box.add(comp.getRenderingComponent());
            this.items[i]=comp;
            gridbag.setConstraints(box,gridbagConstraint);
            add(box);
        }
    }
    
    public void setStatusItemAt(int i, StatusBarItem item)
    {
        //items[i]=item;
        //Box b=(Box)this.getComponent(i);
        JPanel b=(JPanel)this.getComponent(i);
        b.remove(0);
        b.add(item.getRenderingComponent());
    }
    
    public StatusBarItem getStatusItemAt(int i)
    {
        return items[i];
    }
    
	public static class DefaultStatusItem
    extends JLabel
    implements StatusBarItem
    {
        private int position;
        
        public DefaultStatusItem(String label, int position)
        {
            super(label);
            this.position=position;
        }
        
        /* (non-Javadoc)
         * @see mayday.core.tasks.StatusItem#getStatusItemText()
         */
        public String getStatusItemText()
        {
            return getText();
        }

        /* (non-Javadoc)
         * @see mayday.core.tasks.StatusItem#setStatusItemText(java.lang.String)
         */
        public void setStatusItemText(String text)
        {
            setText(text);            
        }

        /* (non-Javadoc)
         * @see mayday.core.tasks.StatusItem#getRenderingComponent()
         */
        public JComponent getRenderingComponent()
        {
            return this;
        }

        /* (non-Javadoc)
         * @see mayday.core.tasks.StatusItem#getPosition()
         */
        public int getPosition()
        {
            return this.position;
        }
        
    }

    /* (non-Javadoc)
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */    
    public void stateChanged(ChangeEvent event)
    {
        if(event.getSource()==null || 
            !(event.getSource() instanceof StatusBarItem))
        {
            
        }else if(event.getSource() instanceof StatusBarItem)
        {
            int pos=((StatusBarItem)event.getSource()).getPosition();
            StatusBarItem item=((StatusBarItem)event.getSource());
            
            pos=(pos>=n?n-1:pos);
            pos=(pos<0?0:pos);
            
            //System.out.println(item.getClass().getName());
            
            setStatusItemAt(pos,item);
            
            this.repaint();
        }
    }
    
}
