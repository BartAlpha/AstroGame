package rug.astro.control;

import rug.astro.model.Game;
import rug.astro.view.MainMenuFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class QuitAction extends AbstractAction {
    /**
     * Reference of the game that we close.
     */
    private Game game;

    /**
     * Construct a new quit action. This calls the parent constructor to give the action a name.
     */
    public QuitAction(Game game) {
        super("Quit");
        this.game = game;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param event The event to be processed.
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        JMenuItem source = (JMenuItem) event.getSource();
        JPopupMenu p = (JPopupMenu) source.getParent();
        JMenu m = (JMenu) p.getInvoker();
        JFrame f = (JFrame) m.getTopLevelAncestor();
        f.dispose();
        MainMenuFrame frame = new MainMenuFrame();
    }
}
