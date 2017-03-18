package org.universAAL.ucc.configuration.view;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window;


/**
 * 
 * An simple yes/no dialog to ask the user simple yes/no questions.
 * 
 * @author Sebastian Schoebinger
 *
 */

@SuppressWarnings("serial")
public class YesNoDialog extends Window implements Button.ClickListener {

    Callback callback;
    Button yes;
    Button no;

    public YesNoDialog(String caption, String question, Callback callback) {
        super(caption);
        
        yes = new Button("Yes", this);
        no = new Button("No", this);
        yes.setClickShortcut(KeyCode.ENTER);
        
        setModal(true);

        this.callback = callback;

        if (question != null) {
            addComponent(new Label(question));
        }

        HorizontalLayout hl = new HorizontalLayout();
        hl.addComponent(yes);
        hl.addComponent(no);
        addComponent(hl);
    }

    public void buttonClick(ClickEvent event) {
        if (getParent() != null) {
            ((Window) getParent()).removeWindow(this);
        }
        callback.onDialogResult(event.getSource() == yes);
    }

    public interface Callback {

        public void onDialogResult(boolean resultIsYes);
    }

}