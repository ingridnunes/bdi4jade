package bdi4jade.extension.planselection.learningbased.learningalgorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import bdi4jade.extension.planselection.learningbased.LearningAlgorithm;
import bdi4jade.extension.planselection.learningbased.PlanMetadata;
import bdi4jade.extension.planselection.learningbased.util.Utils;
import bdi4jade.goal.Softgoal;
import bdi4jade.plan.Plan;

public class LinearRegressionAlgorithm implements LearningAlgorithm {

	private Instances trainingInstances;

	//Ver PlanMetadata
	//private LinearRegression model;

	@Override
	@SuppressWarnings("unchecked")
	public double predictExpectedContribution(Plan plan, Softgoal softgoal) {

		double prediction = 1;
		PlanMetadata planMetadata = ((Map<Softgoal, PlanMetadata>) plan
				.getMetadata(PlanMetadata.METADATA_NAME)).get(softgoal);

		if (planMetadata.getPlanExecutionsCounter() < PlanMetadata.MIN_PLAN_EXECUTIONS) {
			String filePath = "/home/jgfaccin/git/bdi4jade/bdi-jade-extensions/src/bdi4jade/extension/planselection/learningbased/instances/"
					+ (plan.getId() + "_" + plan.getClass().getSimpleName()
							+ "_" + softgoal).toLowerCase() + ".arff";
			if (!new File(filePath).exists()) {
				try {
					Utils.writeToFile(filePath,
							planMetadata.getArffFileHeader());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {

			if (planMetadata.getPlanExecutionsCounter() == PlanMetadata.MIN_PLAN_EXECUTIONS
					|| planMetadata.getPlanExecutionsCounter()
							% PlanMetadata.LEARNING_GAP == 0) {
				learnFromTrainingSet(plan, softgoal);
			}
			//learnFromTrainingSet(plan, softgoal);

			int numOfFactors = planMetadata.getInfluenceFactors().size();

			Instance instance = new DenseInstance(numOfFactors);

			for (int i = 0; i < numOfFactors; i++) {
				// it was Double.valueOf((Integer)
				// planMetadata.getInfluenceFactors...
				instance.setValue(trainingInstances.attribute(i),
						(Double) planMetadata.getInfluenceFactors().get(i)
								.getBeliefValue());
			}

			try {
				//prediction = model.classifyInstance(instance);
				prediction = planMetadata.getModel().classifyInstance(instance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		switch (planMetadata.getOptimizationFunction()) {
		case MINIMIZE:
			return 1 - prediction;
		default:
			return prediction;
		}
	}

	@SuppressWarnings("unchecked")
	private void learnFromTrainingSet(Plan plan, Softgoal softgoal) {
		
		
		PlanMetadata planMetadata = ((Map<Softgoal, PlanMetadata>) plan
				.getMetadata(PlanMetadata.METADATA_NAME)).get(softgoal);
		
		try {
			trainingInstances = new Instances(
					new BufferedReader(
							new FileReader(
									"/home/jgfaccin/git/bdi4jade/bdi-jade-extensions/src/bdi4jade/extension/planselection/learningbased/instances/"
											+ (plan.getId()
													+ "_"
													+ plan.getClass()
															.getSimpleName()
													+ "_" + softgoal)
													.toLowerCase() + ".arff")));

			trainingInstances
					.setClassIndex(trainingInstances.numAttributes() - 1);

			//model = new LinearRegression();
			//model.buildClassifier(trainingInstances);

			planMetadata.getModel().buildClassifier(trainingInstances);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}