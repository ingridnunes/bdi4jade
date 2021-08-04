# ![bdi4jade-small](https://user-images.githubusercontent.com/32344317/124600297-da928d00-de66-11eb-86f7-5930ad53ec1d.png)
*A BDI Layer on Top of JADE*

**BDI4JADE** is an agent platform that implements the BDI (belief-desire-intention) architecture. It consists of a BDI layer implemented on top of [JADE](http://jade.tilab.com/).

> Note: I am currently migrating the old BDI4JADE [website](https://www.inf.ufrgs.br/prosoft/bdi4jade) to GitHub. So far its code has been hosted at [SourceForge](http://sourceforge.net/projects/bdi4jade/). I plan to configure the project with Maven, but I can do it only on my free time, which is a bit scarce :-) There's the draft of a tutorial, but I had the time to rewrite just part of it.

BDI4JADE leverages all the features provided by JADE and reuses it as much as possible. Other highlights of our JADE extension, besides providing BDI abstractions and the reasoning cycle, include:

* **Use of Capabilities** – agents aggregate a set of capabilities, which are a collection of beliefs and plans, and allow modularisation of particular agent functionality.
* **Plan Body is an Extension of JADE Behaviour** – in order to better exploit JADE features, plan bodies are subclasses of JADE behaviours.
* **Java Annotations** – annotations are provided to allow easier configuration of agent components, without compromising its flexibility.
* **Extension Points** – strategies can be easily implemented to extend parts of the reasoning cycle, such as belief revision and plan selection.
* **Listeners and Events** – different events (such as events related to goals and beliefs) can be observed in the platform, allowing listeners to react according to events that occurred.
* **Java Generics for Beliefs** – beliefs can store any kind of information and are associated with a name, and if the value of a belief is retrieved, it must be cast to its specific type, so the use of Java generics allows us to capture incorrect castings at compile time.

As opposed to different BDI platforms that have been proposed, it does not introduce a new programming language nor rely on a domain-specific language (DSL) written in terms of XML files. Because agents are implemented with the constructions of the underlying programming language, [Java](http://www.oracle.com/technetwork/java), we bring two main benefits, as detailed below.

* Features of the Java language, such as annotations and reflection, can be exploited for the development of complex applications.
* It facilitates the integration of existing technologies, e.g. frameworks and libraries, which is essential for the development of large scale enterprise applications, involving multiple concerns such as persistence and transaction management. This also allows a smooth adoption of agent technology.

## BDI Architecture ##

There are several approaches that propose different types of mental attitudes and their relationships. Among them, the most adopted is the belief-desire-intention (BDI) model, originally proposed by Bratman (Bratman 1987) as a philosophical theory of the practical reasoning, explaining the human reasoning with the following attitudes: beliefs, desires and intentions. The essential assumption of the BDI model is that actions are derived from a process named practical reasoning, which is composed of two steps. In the first step, deliberation (of goals), a set of desires is selected to be achieved, according to the current situation of the agent’s beliefs. The second step is responsible for the determination of how these concrete goals produced as a result of the previous step can be achieved by means of the available options for the agent (Wooldridge 2000).

The three mental attitudes that are part of the BDI model are described next.

* **Beliefs.** They represent environment characteristics, which are updated accordingly after the perception of each action. They can be seen as the informative component of the system.
* **Desires.** They store the information of the goals to be achieved, as well as properties and costs associated with each goal. They represent the motivational state of the system.
* **Intentions.** They represent the current action plan chosen. They capture the deliberative component of the system.

Rao & Georgeff (Rao & Georgeff 1995) adopted the BDI model for software agents and presented a formal theory and an abstract BDI interpreter, which is the base for almost all BDI systems, either historical or used at the present. The interpreter operates over beliefs, goals and plans of the agent, which represent the concepts of the mentalistic notions, with small modifications. The most significant change is that goals are a set of consistent concrete desires that can be achieved all together, avoiding the need of a complex phase of goal deliberation. The main task of the interpreter is the realization of the means-end process by means of the selection and execution of plans for a certain goal or event. The first system implemented with success based on this interpreter was the Procedural Reasoning Systems (PRS) (Georgeff & Lansky 1986), which has as a successor the system named dMARS (d’Inverno, Kinny, Luck & Wooldridge 1997, D’Inverno, Luck, Georgeff, Kinny & Wooldridge 2004).

The process of practical reasoning in a BDI agent is presented in the figure below. As shown in this figure, there are seven main components in a BDI agent:

![bdiArch](https://user-images.githubusercontent.com/32344317/124599456-f9445400-de65-11eb-9e7e-6e4886fecaf7.jpeg)

* a set of current *beliefs*, representing information the agent has about its current environment;
* a *belief revision function*, which takes a perceptual input and the agent’s current beliefs, and on the basis of these, determines a new set of beliefs;
* an *option generation function*, (options), which determines the options available to the agent (its desires), on the basis of its current beliefs about its environment and its current intentions;
* a set of current *options*, representing possible courses of actions available to the agent;
* a *filter function* (filter), which represents the agent’s deliberation process, and which determines the agent’s intentions on the basis of its current beliefs, desires, and intentions;
* a set of current *intentions*, representing the agent’s current focus – those states of affairs that it has committed to trying to bring about;
* an *action selection function*, which determines an action to perform on the basis of current intentions.

## Download and Execution

This [link](https://sourceforge.net/projects/bdi4jade/files/) will take you to the SourceForge download page for the latest version of BDI4JADE. And [here](http://www.inf.ufrgs.br/prosoft/bdi4jade/api/) you can access the BDI4JADE API online.

The zip file contains:

* **bdi4jade.jar** – library containing all classes of BDI4JADE.
* **bdi4jade-extensions.jar** – library containing all classes of extensions of BDI4JADE.
* **bdi4jade-examples.jar** – library containing all classes of examples of BDI4JADE.
* bdi4jade source files
* source files of extensions
* source files of examples
* bdi4jade documentation
* required libraries
* release notes, lincense, and readme.

### Requirements: 

* **JADE**: http://jade.tilab.com/
* **log4j**: http://logging.apache.org/log4j
* **Apache commons logging**: http://commons.apache.org/logging/

### Running Examples in BDI4JADE 

#### BDI4JADE 2.0

The provided zip file contains a folder named “examples-src,” in which you can find different examples of the use of BDI4JADE. The class `bdi4jade.examples.BDI4JADEExamplesApp` runs an application, showing a GUI with buttons to execute the different examples. When the application starts, agents of all examples start. Then, when a button is clicked, goals that
cause agents to execute are added to them. Make sure you are running the application with all the necessary libraries (provided in the lib folder) – examples also require BDI4JADE and its extensions libraries.

The class `bdi4jade.examples.BDI4JADEExamplesPanel` has many action inner classes, which are responsible for handling GUI events. The different examples are in the form of different agents or capabilities, in their respective packages in the `bdi4jade.examples` package.

The provided examples are:

* **Hello World Agent**: the famous hello world application
* **Hello World Annotated Capability**: the hello world application, implemented using annotations
* **Ping Pong Agents**: shows how to exchange messages between two agents
* **Composite Goal Agents**: shows how to use sequential and parallel goals
* **Plan Failure Agent**: shows how different plans are executed to achieve a goal, when there are plans that fail
* **Subgoal Goal Agent**: illustrates a goal hierarchy to achieve a top level goal
* **Multi-capability Agent**: shows how to use capability relationships
* **Blocks World**: the widely known application of moving blocks to achieve a particular goal (blocks stacked in a particular way). This examples shows how to use declarative goals.
 
In all examples, you can see how to implement agents, belief bases, plans, etc.

#### BDI4JADE 0.1 and 1.0

The class `AgentStarter` runs an application, starting the agents `BDIAgent1` and `BDIAgent2` (the provided source has the code line that starts `BDIAgent2` as a comment, so you have to uncomment it to start this agent). The different examples are in the form of different capabilities. To run the example with a particular capability you have to uncomment the respective code line in the `BDIAgent1`. The `PingPong` example consists of exchanging messages between `BDIAgent1` and `BDIAgent2`, therefore, if you want to run this example you have to start both agents.

## Releases and News 

### Capability Relationships – Revised and Extended

The paper published entitled “Capability Relationships in BDI Agents” was revised and extended in the EMAS post proceedings. The full reference to this new version of the paper can be seen below.

* NUNES, I. (2014) [Improving the Design and Modularity of BDI Agents with Capability Relationships](http://dx.doi.org/10.1007/978-3-319-14484-9_4). F. Dalpiaz et al. (Eds.): EMAS 2014, LNAI 8758, pp. 58-80. Springer International Publishing Switzerland.

*Published on December 16, 2014.*

### BDI4JADE 2.0 Beta Available: Many New Features! 

A new version of BDI4JADE was released. This version of BDI4JADE provides many new features, and allows easier implementation of BDI agents. This is a beta version because few more tests should be run to consider the release stable.

Check out the new release notes:

* A set of annotations added to:
  * Setup plan body parameters based on goal parameters
  * Setup plan body references to beliefs (whose name is a `String`)
  * Specify capability components (beliefs, plans and associated capabilities)
* New belief types added
  * Predicate (and its implementation `TransientPredicate`): represents a belief whose name is a logic predicate and its value is a boolean that indicates whether the predicate is true or false
  * `DerivedBelief` and `DerivedPredicate`: represent beliefs whose values are derived from a belief base
* Agents can now be one of two types: `SingleCapabilityAgent` and `MultipleCapabilityAgent` (the former has a single root capability, while the latter can have as many as desired)
* Capability can now have different types of relationships (inheritance, association and composition) – check out the paper published in EMAS 2014, by Ingrid Nunes
* `BeliefGoal` and its implementations added: belief goals are used to implement declarative goals. Goals are specified in terms of agent beliefs. Associated with belief goals, there is the BeliefGoalPlanBody, which is a plan to achieve belief goals.
* The goals that a plan can achieve are specified in a set of goal templates (similar to what message templates are to messages). They can be constructed by implementing a goal template or using the `GoalTemplateFactory`.
* Plan bodies can be implemented in a simpler way: now, they must only extend a single class (`AbstractPlanBody`). In previous versions, they had to extend a class (`Behaviour` from JADE) and implement an interface (`PlanInstance`)
* The instantiation of plan bodies whose class is provided for `DefaultPlan` is now possible even if they are non-static inner classes of agents, capabilities and plans.
* Reasoning strategies are now modularised at the capability level. Each capability can have their own reasoning strategy. However, there is still reasoning strategies at the agent level.
* Examples can run all at once in a graphical interface.
* Additional refactorings to the last version were performed.

Because of the addition of these new features, we made some changes in the BDI4JADE infrastructure and this version is incompatible with the previous versions. If you need assistance to adapt your code to use the new BDI4JADE version, please contact us.

We apologise for this inconvenience. BDI4JADE emerged from the need for a BDI platform implemented in pure Java, when we were working on dynamic adaptation of BDI agents in 2011. Since then, BDI4JADE was available but did not evolve. Now, the platform tends to stay stable. However, if you use previous versions of BDI4JADE and you find any bugs in the previous versions, please let us know that will fix them.

*Published on September 23, 2014.*

### Capability Relationships 

In object orientation, there are different relationships between classes: association, aggregation, composition, generalisation/specialisation and dependency. BDI agent platforms that adopt the concept of capabilities allow capabilities to include other capabilities, but this is the only way that capabilities can be related to implement functionality.

In a paper published at EMAS 2014 (International Workshop on Engineering Multi-Agent Systems), Ingrid Nunes explored different types of capability relationships and showed how they can be used. This provides a better modularisation of capabilities and a concise way to specify what is shared among them.

An extended and revised of the paper will be soon available in the EMAS post-proceedings, as well as the implementation of these different types of relationships in BDI4JADE.

Full reference of the published paper:

* NUNES, I. (2014) [Capability Relationships in BDI Agents](http://aamas2014.lip6.fr/proceedings/workshops/AAMAS2014-W11/100000055.pdf), in The 2nd International Workshop on Engineering Multi-Agent Systems (EMAS 2014) at AAMAS 2014. Paris, France.

*Published on August 18, 2014.*

### Utility-based Plan Selection Strategy 

Ingrid Nunes and Michael Luck proposed a new model-driven approach to BDI agent development, which consists of: (i) a meta-model to capture agent softgoals and plan contributions; (ii) a plan selection algorithm using information provided by an instance of the meta-model; and (iii) a model-to-text transformation that generates code based on an instance of the meta-model.

This approach was published at AAMAS 2014, and it is implemented in BDI4JADE (version 1.0 or later). Besides providing an implementation of the meta-model and algorithm, you can run an example of the use of the approach (`bdi4jade.examples.planselection.ExperimentRunner`) and see a template for using it in the package `bdi4jade.examples.planselection.template`. The model-to-text transformation is currently not available.

Full reference of the published paper:

* NUNES, I.; LUCK, M. (2014), [Softgoal-based Plan Selection in Model-driven BDI Agents](http://dl.acm.org/citation.cfm?id=2615731.2615852), in The 13th International Conference on Autonomous Agents and Multiagent Systems (AAMAS 2014), IFAAMAS, Paris, France. p. 749-756.

*Published on May 9, 2014.*

### Version 1.0 

A new version of BDI4JADE was released.

Check out the new release notes:

* Implementation of the `UtilityBasedBDIAgent`, which is an agent that uses the `UtilityBasedPlanSelectionStrategy` to select plans. It uses other concepts as soft goals and plan contributions. See the template for learning how to use it.
* Implementation of the management of nested capabilities. Goals can be dispatched in plans within the scope of a capability, so that only the plans of that capability or its children will be considered to achieve the goal.
* New examples provided
  * Hello World example
  * Nested capabilities example
  * Agent based on utility-based plan selection example/template
* Refactoring of the BDI4JADE packages

*Published on January 9, 2014.*

### Version 0.1 

The first version of BDI4JADE is now available.

*Published on May 6, 2011.*

### BDI4JADE @ ProMAS 2011 / AAMAS 

A paper of the BDI4JADE was published at the 9th International Workshop on Programming Multi-Agent Systems ([ProMAS-2011](http://www.inf.ufrgs.br/promas2011/)), held with the 10th International Joint Conference on Autonomous Agents and Multi-Agent Systems (AAMAS-2011).

The paper is entitled “BDI4JADE: a BDI Layer on Top of JADE” and is available for [download](http://www.inf.ufrgs.br/~ingridnunes/publications/promas-2011.pdf).

*Published on May 6, 2011.*

## The Story behind BDI4JADE

In the second year of my PhD (2010), I worked with dynamic adaptation of user agents. These agents were structured with the BDI architecture. For evaluating the proposed approach, I needed to make an implementation but no existing BDI platform could be used. The agent code writen in these other platforms could not be integrated with general-purpose programming languages and associated technologies (in my case Spring, Hibernate and AspectJ).

This motivated me to implement BDI4JADE - with JADE controlling the distribution, message exchange, and behaviour scheduling, I could focus on the BDI cycle, which has its core implemented on the `AbstractBDIAgent` class. At that time, I didn't think of making it available but, when I mentioned it while presenting papers, people got interested. So I wrote a paper about BDI4JADE (published in the ProMAS workshop - but the paper didn't go to the post-proceedings because a reviewer said he didn't see a contribution on the paper, (s)he not even wanted the paper accepted for the pre/informal proceedings - yes, I still feel the pain). Soon after the presentation at the workshop, I made the platform open source, available on SourceForge.

Over the years, I evolved BDI4JADE with additional features (annotations) and research on BDI agents (capability relationships). However, I haven't worked on it since 2014. It kept being download on SourceForge and, although I received some questions over time, I never received a bug report :-) Moreover, some of my undergraduate and graduate students worked on it, in particular João Guilherme Faccin ( @jfaccin ), who made an extention for plan selection based on learning.

## Publications 

* NUNES, I., LUCENA, C.J.P., LUCK, M. (2011), [BDI4JADE: a BDI layer on top of JADE](http://www.inf.ufrgs.br/~ingridnunes/publications/promas-2011.pdf), in Louise A. Dennis and Olivier Boissier and Rafael H. Bordini, ed., Ninth International Workshop on Programming Multi-Agent Systems (ProMAS 2011), Taipei, Taiwan, pp. 88-103. 

### BDI4JADE Extensions 

* NUNES, I.; FACCIN, J. G. . [Modelling and Implementing Modularised BDI Agents with Capability Relationships](http://dx.doi.org/10.1504/IJAOSE.2016.10001864). International Journal of Agent-Oriented Software Engineering, v. 5(2/3), p. 203-231, 2016.

* FACCIN, J. G. ; NUNES, I. . [BDI-Agent Plan Selection based on Prediction of Plan Outcomes](http://dx.doi.org/10.1109/WI-IAT.2015.58). In: ACM International Conference on Intelligent Agent Technology (IAT'15), IEEE, Singapore. p. 187-173.

* NUNES, I. (2014) [Improving the Design and Modularity of BDI Agents with Capability Relationships](http://dx.doi.org/10.1007/978-3-319-14484-9_4). F. Dalpiaz et al. (Eds.): EMAS 2014, LNAI 8758, pp. 58-80. Springer International Publishing Switzerland.
 
* NUNES, I.; LUCK, M. (2014), [Softgoal-based Plan Selection in Model-driven BDI Agents](http://dl.acm.org/citation.cfm?id=2615731.2615852), in The 13th International Conference on Autonomous Agents and Multiagent Systems (AAMAS 2014), IFAAMAS, Paris, France. p. 749-756.
 
* NUNES, I. (2014) [Capability Relationships in BDI Agents](http://aamas2014.lip6.fr/proceedings/workshops/AAMAS2014-W11/100000055.pdf), in The 2nd International Workshop on Engineering Multi-Agent Systems (EMAS 2014) at AAMAS 2014. Paris, France.
 
### Use of BDI4JADE 

* FACCIN, J. ; NUNES, I. . [Remediating critical cause-effect situations with an extended BDI architecture](https://doi.org/10.1016/j.eswa.2017.11.036). EXPERT SYSTEMS WITH APPLICATIONS, v. 95, p. 190-200, 2018.

* FACCIN, J. G. ; NUNES, I. . [Cleaning up the mess: a formal framework for autonomously reverting BDI agent actions](https://doi.org/10.1145/3194133.3194156). In: International Symposium on Software Engineering for Adaptive and Self-Managing Systems, 2018, Götenburg. SEAMS '18 Proceedings of the 13th International Conference on Software Engineering for Adaptive and Self-Managing Systems. New York: ACM, 2018. p. 108-118.

* SCHARDONG, F. ; NUNES, I. ; SCHAEFFER FILHO, A.E. . [Providing Cognitive Components with a Bidding Heuristic for Emergent NFV Orchestration](https://doi.org/10.1109/NOMS.2018.8406269). In: IEEE/IFIP Network Operations and Management Symposium, 2018, Taipei, Taiwan. 2018 IEEE/IFIP Network Operations and Management Symposium (NOMS 2018). New York: IEEE, 2018.

* FACCIN, J. G. ; NUNES, I. . [A tool-supported development method for improved BDI plan selection](https://doi.org/10.1016/j.engappai.2017.04.008). ENGINEERING APPLICATIONS OF ARTIFICIAL INTELLIGENCE, v. 62, p. 195-213, 2017.

* FACCIN, J. G. ; NUNES, I. . [Modelling and Reasoning about Remediation Actions in BDI Agents](http://dl.acm.org/citation.cfm?id=3091282.3091350) (Extended Abstract). In: International Joint Conference on Autonomous Agents and Multiagents Systems, 2017, São Paulo. AAMAS '17 Proceedings of the 16th Conference on Autonomous Agents and MultiAgent Systems. Richland: IFAAMAS, 2017. p. 1526-1528.

* FACCIN, J. G. ; NUNES, I. ; BAZZAN, A. L. C. . [Understanding the Behaviour of Learning-based BDI Agents in the Braess' Paradox](http://dx.doi.org/10.1007/978-3-319-64798-2_12). In: German Conference on Multiagent System Technologies, 2017, Leipzig. Multiagent System Technologies: 15th German Conference, MATES 2017, Leipzig, Germany, August 23--26, 2017, Proceedings. Cham: Springer International Publishing, 2017. v. 10413. p. 187-204.

* NUNES, I.; SCHARDONG, F.; SCHAEFFER-FILHO, A. . [BDI2DoS: an Application using Collaborating BDI Agents to Combat DDoS Attacks](http://dx.doi.org/10.1016/j.jnca.2017.01.035). Journal of Network and Computer Applications, v. 84, p. 14-24, 2017.

* SCHARDONG, F. ; NUNES, I. ; SCHAEFFER FILHO, A.E. . [A Distributed NFV Orchestrator based on BDI Reasoning](https://doi.org/10.23919/INM.2017.7987270). In: IFIP/IEEE International Symposium on Integrated Network Management, 2017, Lisbon. 2017 IFIP/IEEE International Symposium on Integrated Network Management (IM 2017). New York: IEEE, 2017. p. 107-115. 

* FACCIN, J. G. ; WENG, J. ; NUNES, I. . [SAM: A Tool to Ease the Development of Intelligent Agents](https://ingridnunes.github.io/publications/cbsoft-2016-tools.pdf). In: Congresso Brasileiro de Software: Teoria e Prática, 2016, Maringá. XXIII Sessão de Ferramentas: Sessão de Ferramentas 2016. Porto Alegre: SBC, 2016. p. 89-96.

* NUNES, I., SCHAEFFER-FILHO, A. (2014) [Reengineering Network Resilience Strategies using a BDI Architecture](https://ingridnunes.github.io/publications/autosoft-2014.pdf), in The 5th Workshop on Autonomous Software Systems (AutoSoft 2014). Maceió, Brazil. p. 25-36. 

* NUNES, I., LUCK, M., BARBOSA, S., MILES, S., LUCENA, C.J.P. (2012), [Dynamically Adapting BDI Agents based on High-level User Specifications](http://dx.doi.org/10.1007/978-3-642-27216-5_11), in Francien Deschesne, Hiromitsu Hattori, Adriaan ter Mors, Jose Miguel Such, Danny Weyns and Frank Dignum (eds.), Advanced Agent Technology; proceedings of workshops at AAMAS 2011, Vol. 7068 of LNCS, Springer-Verlag, 2012, p. 139-163.
 
* NUNES, I., BARBOSA, S., LUCK, M., LUCENA, C.J.P. (2011), [Dynamically Adapting BDI Agent Architectures based on High-level User Specifications](http://www.inf.ufrgs.br/prosoft/bdi4jade/wp-content/uploads/2011_05_02-aose-2011.pdf), in Danny Weyns and Jörg P. Müller, ed., The 12th International Workshop on Agent-Oriented Software Engineering (AOSE 2011), Taipei, Taiwan, pp. 105-116.
 
* NUNES, I., BARBOSA, S., LUCENA, C.J.P. (2010), [Increasing Users Trust on Personal Assistance Software using a Domain-neutral High-level User Model](http://dx.doi.org/10.1007/978-3-642-16558-0_40), in Tiziana Margaria and Bernhard Steffen, ed., Leveraging Applications of Formal Methods, Verification, and Validation (ISoLA 2010), Vol. 6415 of LNCS, Springer, Heraclion, Greece, pp. 473-487.
 
* NUNES, I., BARBOSA, S., LUCENA, C.J.P. (2010), [Supporting the Development of Personal Assistance Software](http://www.inf.ufrgs.br/prosoft/bdi4jade/wp-content/uploads/2010_09_26-autosoft-2010.pdf), AUTOSOFT 2010 – I Workshop on Autonomous Software Systems, Vol. 10, SBC, Salvador, Brazil, pp. 31-40.
 
### Previous Experiences with BDI platforms 

* NUNES, I., CIRILO, E.J.R., COWAN, D., LUCENA, C.J.P. (2009), [Fine-grained Variability in the Development of Families of Software Agents](http://www.inf.ufrgs.br/prosoft/bdi4jade/wp-content/uploads/2009_12_18-eumas-2009.pdf), 7th European Workshop on Multi-Agent Systems (EUMAS 2009), Ayia Napa, Cyprus.
 
* NUNES, I., CIRILO, E.J.R., LUCENA, C.J.P. (2009), [Developing a Family of Software Agents with Fine-grained Variability: an Exploratory Study](http://www.lbd.dcc.ufmg.br:8080/colecoes/seas/2009/007.pdf), V Workshop on Software Engineering for Agent-oriented Systems (SEAS 2009), Fortaleza, Brasil, pp. 71-82.
 
* NUNES, I., LUCENA, C.J.P., COWAN, D., ALENCAR, P. (2009), [Building Service-oriented User Agents using a Software Product Line Approach](http://dx.doi.org/10.1007/978-3-642-04211-9_23), in Stephen Edwards and Gregory Kulczycki, ed., Formal Foundations of Reuse and Domain Engineering (ICSR 2009), Vol. 5791 of Lecture Notes in Computer Science (LNCS), Springer Berlin / Heidelberg, Falls Church, United States, pp. 236-245.
 
* NUNES, I. ; KULESZA, U. ; NUNES, C. ; CIRILO, E. ; LUCENA, C. . [Extending Web-Based Applications to Incorporate Autonomous Behavior](http://dx.doi.org/10.1145/1666091.1666112). In: WebMedia 2008: XIV Brazilian Symposium on Multimedia and Web Systems, 2008, Vila Velha. Proceedings of XIV Brazilian Symposium on Multimedia and Web Systems, 2008. p. 115-122.

## References 

* Bratman, M. E. (1987), Intention, Plans, and Practical Reason, Cambridge, MA.
* d’Inverno, M., Kinny, D., Luck, M. & Wooldridge, M. (1997), A formal specification of dMARS, in ‘Agent Theories, Architectures, and Languages’, pp. 155-176.
* D’Inverno, M., Luck, M., Georgeff, M. P., Kinny, D. & Wooldridge, M. J. (2004), ‘The dMARS architecture: A specification of the distributed multi-agent reasoning system’, Autonomous Agents & Multi-Agent Systems 9, 5-53.
* Georgeff, M. & Lansky, A. (1986), Procedural knowledge, in ‘IEEE Special Issue on Knowledge Representation’, Vol. 74, pp. 1383-1398.
* Georgeff, M., Pell, B., Pollack, M., Tambe, M. & Wooldridge, M. (1999), The belief-desireintention model of agency, in J. Muller, M. P. Singh & A. S. Rao, eds, ‘Proceedings of the 5th International Workshop on Intelligent Agents V : Agent Theories, Architectures, and Languages (ATAL-98)’, Vol. 1555, Springer-Verlag: Heidelberg, Germany, pp. 1-10.
* Rao, A. S. & Georgeff, M. P. (1995), BDI-agents: from theory to practice, in ‘Proceedings of the First Intl. Conference on Multiagent Systems’, San Francisco
* Wooldridge, M. (1999), Intelligent agents, in ‘Multiagent systems: a modern approach to distributed artificial intelligence’, MIT Press, Cambridge, MA, USA, pp. 27-77.
* Wooldridge, M. J. (2000), Reasoning about Rational Agents, MIT Press.
