/*
 * Created on 8 Apr 2014 18:58:13
 */
package bdi4jade.examples.blocksworld;

import jade.BootProfileImpl;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.PlatformController;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import bdi4jade.event.GoalEvent;
import bdi4jade.event.GoalListener;
import bdi4jade.examples.AgentStarter;
import bdi4jade.examples.blocksworld.domain.On;
import bdi4jade.examples.blocksworld.domain.Thing;
import bdi4jade.examples.blocksworld.goal.AchieveBlocksStacked;

/**
 * @author ingrid
 * 
 */
public class BlocksWorldApp implements GoalListener {

	private static final On[] target = { new On(Thing.BLOCK_5, Thing.TABLE),
			new On(Thing.BLOCK_4, Thing.BLOCK_5),
			new On(Thing.BLOCK_3, Thing.BLOCK_4),
			new On(Thing.BLOCK_2, Thing.BLOCK_3),
			new On(Thing.BLOCK_1, Thing.BLOCK_2) };

	public static void main(String[] args) {
		PropertyConfigurator.configure(AgentStarter.class
				.getResource("log4j.properties"));
		new BlocksWorldApp();
	}

	private ProfileImpl bootProfile;
	private final Log log;
	private jade.core.Runtime runtime;

	public BlocksWorldApp() {
		log = LogFactory.getLog(this.getClass());

		List<String> params = new ArrayList<String>();
		params.add("-gui");
		params.add("-detect-main:false");

		log.info("Plataform parameters: " + params);

		this.bootProfile = new BootProfileImpl(params.toArray(new String[0]));

		this.runtime = jade.core.Runtime.instance();
		PlatformController controller = runtime
				.createMainContainer(bootProfile);
		try {
			BlocksWorldAgent agent = new BlocksWorldAgent();
			BlocksWorldView view = new BlocksWorldView(agent.getCapabilities()
					.iterator().next().getBeliefBase());
			createAndShowUI(view);

			AgentController ac = ((AgentContainer) controller).acceptNewAgent(
					agent.getClass().getSimpleName(), agent);
			ac.start();

			agent.addGoal(new AchieveBlocksStacked(target), this);
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void createAndShowUI(BlocksWorldView view) {
		final JFrame frame = new JFrame();
		frame.setTitle(BlocksWorldApp.class.getSimpleName());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(false);
		frame.setContentPane(view);

		frame.pack();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame.setVisible(true);
			}
		});
	}

	@Override
	public void goalPerformed(GoalEvent event) {
		if (event.getGoal() instanceof AchieveBlocksStacked) {
			log.info("Goal achieved!!");
		}
	}

}
