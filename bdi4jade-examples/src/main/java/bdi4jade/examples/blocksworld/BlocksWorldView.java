/*
 * Created on 8 Apr 2014 18:09:19
 */
package bdi4jade.examples.blocksworld;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.belief.BeliefBase;
import bdi4jade.belief.BeliefSet;
import bdi4jade.event.BeliefEvent;
import bdi4jade.event.BeliefEvent.Action;
import bdi4jade.event.BeliefListener;
import bdi4jade.examples.blocksworld.domain.Clear;
import bdi4jade.examples.blocksworld.domain.On;
import bdi4jade.examples.blocksworld.domain.Thing;

/**
 * @author Ingrid Nunes
 */
public class BlocksWorldView extends JPanel implements BeliefListener {

	class State {
		String clear;
		String onTable;
		String stack1;
		String stack2;
	}

	private static final long serialVersionUID = -754767782463259272L;

	private final BeliefBase beliefBase;
	private final JTextArea clearTextArea;
	private final Log log;
	private final JTextArea onTableTextArea;
	private final JTextArea stack1TextArea;
	private final JTextArea stack2TextArea;

	public BlocksWorldView(BeliefBase beliefBase) {
		super(new GridLayout(1, 4));
		this.log = LogFactory.getLog(getClass());

		this.beliefBase = beliefBase;
		beliefBase.addBeliefListener(this);
		this.stack1TextArea = new JTextArea();
		stack1TextArea.setBorder(BorderFactory.createTitledBorder("On"));
		this.stack2TextArea = new JTextArea();
		stack2TextArea.setBorder(BorderFactory.createTitledBorder("On"));
		this.clearTextArea = new JTextArea();
		clearTextArea.setBorder(BorderFactory.createTitledBorder("Clear"));
		this.onTableTextArea = new JTextArea();
		onTableTextArea.setBorder(BorderFactory.createTitledBorder("Table"));

		updateText(generateStateText());

		this.add(stack1TextArea);
		this.add(stack2TextArea);
		this.add(clearTextArea);
		this.add(onTableTextArea);
	}

	@Override
	public void eventOccurred(BeliefEvent beliefEvent) {
		log.debug(beliefEvent);
		if (Action.BELIEF_SET_VALUE_REMOVED.equals(beliefEvent.getAction()))
			return;

		// Ignore inconsistent states
		updateText(generateStateText());
		repaint();
	}

	private State generateStateText() {
		State state = new State();

		BeliefSet<String, On> onBelief = (BeliefSet<String, On>) beliefBase
				.getBelief(BlocksWorldCapability.BELIEF_ON);
		BeliefSet<String, Clear> clearBelief = (BeliefSet<String, Clear>) beliefBase
				.getBelief(BlocksWorldCapability.BELIEF_CLEAR);

		List<Thing> tops = new ArrayList<>(2);
		for (On on : onBelief.getValue()) {
			if (clearBelief.hasValue(new Clear(on.getThing1()))
					&& !on.getThing2().equals(Thing.TABLE)) {
				tops.add(on.getThing1());
			}
		}

		state.stack1 = "Empty";
		state.stack2 = "Empty";
		if (!tops.isEmpty()) {
			state.stack1 = stackText(tops.get(0));
		}
		if (tops.size() > 1) {
			state.stack2 = stackText(tops.get(1));
		}

		StringBuffer s = new StringBuffer();
		for (Clear clear : clearBelief.getValue()) {
			s.append(clear.getThing()).append("\n");
		}
		state.clear = s.toString();

		s = new StringBuffer();
		for (On on : onBelief.getValue()) {
			if (on.getThing2().equals(Thing.TABLE)) {
				s.append(on.getThing1()).append("\n");
			}
		}
		state.onTable = s.toString();

		return state;
	}

	private Thing getNext(Thing thing) {
		BeliefSet<String, On> onBelief = (BeliefSet<String, On>) beliefBase
				.getBelief(BlocksWorldCapability.BELIEF_ON);
		for (On on : onBelief.getValue()) {
			if (on.getThing1().equals(thing))
				return on.getThing2();
		}
		return null;
	}

	private String stackText(Thing thing) {
		StringBuffer s = new StringBuffer();
		while (thing != null) {
			s.append(thing).append("\n");
			thing = getNext(thing);
		}
		return s.toString();
	}

	private void updateText(final State state) {
		JOptionPane.showMessageDialog(null, "Proceed?");
		stack1TextArea.setText(state.stack1);
		stack2TextArea.setText(state.stack2);
		clearTextArea.setText(state.clear);
		onTableTextArea.setText(state.onTable);
	}

}
