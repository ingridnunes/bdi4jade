package bdi4jade.extension.planselection.learningbased;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;

import weka.classifiers.Classifier;
import bdi4jade.extension.planselection.learningbased.util.Utils;
import bdi4jade.goal.Softgoal;
import bdi4jade.plan.Plan;

/**
 * Represents the metadata associated to a plan. It relates a set of influence
 * factors to an outcome and to a specific softgoal. This information is
 * subsequent used by the {@link} LearningAlgorithm class in the plan selection
 * process.
 * 
 * @author João Faccin
 */
public class PlanMetadataElement {

	public static final String METADATA_NAME = PlanMetadataElement.class
			.getSimpleName();

	private final int MIN_PLAN_EXECUTIONS;
	private final int LEARNING_GAP;

	private Plan plan;
	private Softgoal softgoal;
	private Outcome outcome;
	private OptimizationFunction optFunction;
	private ArrayList<InfluenceFactor> influenceFactors;
	private String currentInstance;
	private int planExecutionsCounter;
	private Classifier model;
	private String filePath;

	/**
	 * Creates a new instance of plan metadata relating a plan to a specific
	 * softgoal.
	 * 
	 * @param plan
	 *            A plan which this metadata is related.
	 * @param softgoal
	 *            A softgoal which this metadata refers to.
	 * @param outcome
	 *            An outcome to be monitored in each plan execution.
	 * @param optFunction
	 *            An optimization function.
	 * @param modelClass
	 *            The learning algorithm which will be used in the learning
	 *            process.
	 * @param minPlanExecutions
	 *            An integer indicating the minimum number of plan's executions
	 *            to be performed before the first learning process.
	 * @param learningGap
	 *            An integer indicating the interval of plan's executions
	 *            between two learning processes.
	 */
	public PlanMetadataElement(Plan plan, Softgoal softgoal, Outcome outcome,
			OptimizationFunction optFunction,
			Class<? extends Classifier> modelClass, int minPlanExecutions,
			int learningGap) {
		this.plan = plan;
		this.softgoal = softgoal;
		this.outcome = outcome;
		this.optFunction = optFunction;
		this.influenceFactors = new ArrayList<>();
		this.currentInstance = new String();
		this.planExecutionsCounter = 0;
		this.filePath = FileSystems.getDefault().getPath("").toString();
		this.MIN_PLAN_EXECUTIONS = minPlanExecutions;
		this.LEARNING_GAP = learningGap;
		try {
			model = modelClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new instance of plan metadata relating a plan to a specific
	 * softgoal.
	 * 
	 * @param plan
	 *            A plan which this metadata is related.
	 * @param softgoal
	 *            A softgoal which this metadata refers to.
	 * @param outcome
	 *            An outcome to be monitored in each plan execution.
	 * @param optFunction
	 *            An optimization function.
	 * @param influenceFactors
	 *            A set of influence factors related to an outcome.
	 * @param modelClass
	 *            The learning algorithm which will be used in the learning
	 *            process.
	 * @param minPlanExecutions
	 *            An integer indicating the minimum number of plan's executions
	 *            to be performed before the first learning process.
	 * @param learningGap
	 *            An integer indicating the interval of plan's executions
	 *            between two learning processes.
	 */
	public PlanMetadataElement(Plan plan, Softgoal softgoal, Outcome outcome,
			OptimizationFunction optFunction,
			ArrayList<InfluenceFactor> influenceFactors,
			Class<? extends Classifier> modelClass, int minPlanExecutions,
			int learningGap) {
		this.plan = plan;
		this.softgoal = softgoal;
		this.outcome = outcome;
		this.optFunction = optFunction;
		this.influenceFactors = influenceFactors;
		this.currentInstance = new String();
		this.planExecutionsCounter = 0;
		this.filePath = FileSystems.getDefault().getPath("").toString();
		this.MIN_PLAN_EXECUTIONS = minPlanExecutions;
		this.LEARNING_GAP = learningGap;
		try {
			model = modelClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the prediction model built using the learning algorithm specified
	 * in the {@link} PlanMetadata constructor.
	 * 
	 * @return The prediction model.
	 */
	public Classifier getModel() {
		return model;
	}

	/**
	 * Adds an influence factor to the existing set of influence factors.
	 * 
	 * @param influenceFactor
	 */
	public void addInfluenceFactor(InfluenceFactor influenceFactor) {
		this.influenceFactors.add(influenceFactor);
	}

	public int getLearningGap() {
		return this.LEARNING_GAP;
	}

	public int getMinPlanExecutions() {
		return this.MIN_PLAN_EXECUTIONS;
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

	/**
	 * Increases the plan executions counter.
	 */
	public void increasePlanExecutionsCounter() {
		this.planExecutionsCounter++;
	}

	/**
	 * Represents the notification of a started plan execution.
	 */
	public void getNotifiedAtStartedPlanExecution() {
		updateCurrentInstance();
		this.outcome.startMeasurement();
	}

	/**
	 * Represents the notification of an already ended plan execution and sets
	 * the result to the instances file.
	 */
	public void getNotifiedAtEndedPlanExecution() {
		this.outcome.endMeasurement();
		double currentMeasurement = this.outcome.getMeasurement();
		if (currentMeasurement >= this.outcome.getMax()) {
			this.outcome.setMax(currentMeasurement);
		}
		if (currentMeasurement <= this.outcome.getMin()) {
			this.outcome.setMin(currentMeasurement);
		}
		this.currentInstance = currentInstance + currentMeasurement;
		saveInstance();
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePath() {
		return this.filePath;
	}

	/**
	 * Stores an instance with the current values of influence factors.
	 */
	private void updateCurrentInstance() {
		for (InfluenceFactor influenceFactor : influenceFactors) {
			this.currentInstance = this.currentInstance
					+ influenceFactor.getBeliefValue() + ",";
		}
	}

	/**
	 * Writes the current instance in a specific file.
	 */
	private void saveInstance() {
		String agentName = this.plan.getPlanLibrary().getCapability().getMyAgent().getLocalName();
		String filePath = this.filePath
				+ (agentName + "_" + this.plan.getId() + "_"
						+ this.plan.getClass().getSimpleName() + "_" + this.softgoal)
						.toLowerCase() + ".arff";
		try {
			Utils.writeToFile(filePath, this.currentInstance);
		} catch (IOException e) {
			System.out
					.println("A problem occurred when trying to save a context instance!");
			e.printStackTrace();
		}
		this.currentInstance = "";
	}

	/**
	 * Returns a string with the specific format of a header of the .arff file
	 * used in the learning process.
	 * 
	 * @return An .arff file header.
	 */
	public String getArffFileHeader() {
		String lineSeparator = System.getProperty("line.separator");
		String relation = "@relation " + this.plan.getClass().getSimpleName()
				+ "-" + this.softgoal + lineSeparator;

		StringBuilder header = new StringBuilder();
		header.append(relation);

		for (InfluenceFactor influenceFactor : influenceFactors) {
			header.append(influenceFactor);
			header.append(lineSeparator);
		}

		header.append(outcome);
		header.append(lineSeparator);

		header.append("@data");
		header.append(lineSeparator);
		return header.toString();
	}
}