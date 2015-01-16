package bdi4jade.extension.planselection.learningbased;

import java.io.IOException;
import java.util.ArrayList;

import bdi4jade.extension.planselection.learningbased.util.Utils;
import bdi4jade.goal.Softgoal;
import bdi4jade.plan.Plan;

public class PlanMetadata {

	public static final String METADATA_NAME = PlanMetadata.class
			.getSimpleName();
	
	public static final int MIN_PLAN_EXECUTIONS = 50;
	
	private Plan plan;
	private Softgoal softgoal;
	private Outcome outcome;
	private OptimizationFunction optFunction;
	private ArrayList<InfluenceFactor> influenceFactors;
	private String currentInstance;
	private int planExecutionsCounter;

	public PlanMetadata(Plan plan, Softgoal softgoal, Outcome outcome,
			OptimizationFunction optFunction) {
		this.plan = plan;
		this.softgoal = softgoal;
		this.outcome = outcome;
		this.optFunction = optFunction;
		this.influenceFactors = new ArrayList<>();
		this.currentInstance = new String();
		this.planExecutionsCounter = 0;
	}

	public PlanMetadata(Plan plan, Softgoal softgoal, Outcome outcome,
			OptimizationFunction optFunction,
			ArrayList<InfluenceFactor> influenceFactors) {
		this.plan = plan;
		this.softgoal = softgoal;
		this.outcome = outcome;
		this.optFunction = optFunction;
		this.influenceFactors = influenceFactors;
		this.currentInstance = new String();
		this.planExecutionsCounter = 0;
	}

	public void addInfluenceFactor(InfluenceFactor influenceFactor) {
		this.influenceFactors.add(influenceFactor);
	}

	public Outcome getOutcome() {
		return this.outcome;
	}

	public OptimizationFunction getOptimizationFunction() {
		return this.optFunction;
	}

	public ArrayList<InfluenceFactor> getInfluenceFactors() {
		return this.influenceFactors;
	}
	
	public int getPlanExecutionsCounter() {
		return this.planExecutionsCounter;
	}
	
	public void increasePlanExecutionsCounter() {
		this.planExecutionsCounter++;
	}

	public void getNotifiedAtStartedPlanExecution() {
		updateCurrentInstance();
		this.outcome.startMeasurement();
	}

	public void getNotifiedAtEndedPlanExecution() {
		this.outcome.endMeasurement();
		this.currentInstance = currentInstance + this.outcome.getMeasurement();
		saveInstance();
	}

	private void updateCurrentInstance() {
		for (InfluenceFactor influenceFactor : influenceFactors) {
			this.currentInstance = this.currentInstance
					+ influenceFactor.getBeliefValue() + ",";
		}
	}

	private void saveInstance() {
		String filePath = "instances/"
				+ this.plan.getClass().getSimpleName().toLowerCase() + "_"
				+ this.softgoal + ".arff";
		try {
			Utils.writeToFile(filePath, this.currentInstance);
		} catch (IOException e) {
			System.out.println("A problem occurred when trying to save a context instance!");
			e.printStackTrace();
		}
		this.currentInstance = "";
	}

	/*
	 * This method returns a string in the format:
	 * @relation relation
	 * @attribute attr1 numeric 
	 * @attribute attr2 numeric
	 * @data
	 */
	public String getArffFileHeader() {
		String lineSeparator = System.getProperty("line.separator");
		String relation = "@relation " + this.plan.getClass().getSimpleName()
				+ "-" + this.softgoal + lineSeparator;

		StringBuilder header = new StringBuilder();
		header.append(relation);

		for (InfluenceFactor influenceFactor : influenceFactors) {
			header.append("@attribute ");
			header.append(influenceFactor.getBeliefName());
			header.append(" numeric");
			header.append(lineSeparator);
		}

		header.append("@data");
		header.append(lineSeparator);
		return header.toString();
	}
}