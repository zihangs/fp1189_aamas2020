# Archived Datasets

In the 5th section (experimental results) of our paper, we conducted 4 series of experiments to test our GR system under many different scenarios. Accordingly, we recorded and formatted the statistical outputs into 4 tables shown in our paper. The datasets used for conducting experiments are grouped by table and we will explain the details of datasets table by table as below.



### Table 1: Domains from Process Mining Community

The archived dataset ``table_1.zip`` contains 3 domains from process mining community.

1. **Activities of daily living of several individuals**

   More details about this dataset, see: https://data.4tu.nl/articles/Activities_of_daily_living_of_several_individuals/12674873.

   We pre-processed the original dataset (XES files) and we stored the pre-processed outputs under the directory ``activities_64/`` and ``activities_82/``. 

   Within each directory, either ``activities_64/`` or ``activities_82/``, the event logs (``.xes`` files) are from original datasets and each event log contains the traces towards to the same goal. There are 8 event logs (from ``0.xes`` to ``7.xes``) representing the traces towards to 8 different goals in this domain. After pre-processing, we can get another 8 event logs (from ``created0.xes`` to ``created7.xes``) which are the event logs containing the traces for training models, and we can also get another 8 directories (from ``goal0/`` to ``goal7/``) which contain all the traces for GR (testing) corresponding to each goal. 

   Notice the suffix ``_64`` simply means after pre-processing, we split the original dataset and keep 60% of the traces for training and 40% traces for testing. Similar, for the suffix ``_82`` (80% traces for training, 20% percent trace for testing). 

   The ``.tsml`` files are intermediate files. They are generated during the process mining phase (training phase) when using the *transition system miner* (TSM).

2. **BPI Challenge 2015**

   More details about this dataset, see: https://data.4tu.nl/collections/BPI_Challenge_2015/5065424.

   Similar pre-processing logic as above and pre-processed outputs are stored in ``build_prmt_64/`` and ``build_prmt_82/``.

3. **Environmental permit application process (‘WABO’), CoSeLoG project**

   More details about this dataset, see: https://data.4tu.nl/collections/Environmental_permit_application_process_WABO_CoSeLoG_project/5065529.

   Similar pre-processing logic as above and pre-processed outputs are stored in ``env_prmt_64/`` and ``env_prmt_82/``.

```sh
# General command for running the integrated simulation of domains from process mining community.
java -cp PM_Simulation.jar PM_Simulation <dataset> <goals>

# for example: using "env_prmt_64" as for the dataset and there are 5 goals in that domain.
java -cp PM_Simulation.jar PM_Simulation env_prmt_64 5
```

Notice: before running this simulation, you need to extract the archived dataset and put the data in correct directory, details of instructions see [tools](https://github.com/zihangs/fp1189_aamas2020/tree/master/tools).



### Table 2: The Grids Domain from Classical Planning Community

The archived dataset ``table_2.zip`` contains data for running experiments on grids domain, which is a typical domain used by many classical planning research. In the experiments, we controlled the variables and tested how does a particular factor affect the performance of our GR system. The 3 factors we tested are the *size of world*, the *number of goals* and the *optimality of traces*.

**Size of world**: we used 3 different settings, 10 by 10 grids, 20 by 20 grids and 30 by 30 grids

**Number of goals**: there can be 3 or 6 or 9 goal states located in a grids domain

**Optimality of traces**: for every traces towards to a particular goal, there should exist at least one optimal trace for achieving that goal. The optimal trace simply means a traces that takes least costs (steps) to reach the goal state. The optimality just measures how much a traces deviates from the optimal trace. We use 10% sub-optimal traces, which means costs of traces are between optimal cost (``1*opt``) and ``(1+10%)*opt``. Similarly, 20% means traces are between ``(1+10%)*opt`` and ``(1+20%)*opt``, 30% means between ``(1+20%)*opt`` and ``(1+30%)*opt``.

All the traces are synthetic using the [K-star planner](https://github.com/ctpelok77/kstar) (we are still improving the planner for generating traces). The traces are splitted into 2 directories ``training/`` and ``testing/``, each directory contains many sub-directories with different settings ``sizeOfWorld_numberOfGoals_optimalityOfTraces/``. 

For example, ``training/10_3_10/`` contains event logs (``.xes`` files) in a 10 by 10 grids domain with 3 goal states and all the costs of traces in the event logs are between ``optimal`` and ``(1+10%)*opt``. These traces are used for training models and we use the *transition system miner* (TSM) for mining (training). The ``.pnml`` files are the returned process models after training. Another example for testing set, ``testing/10_3_10/`` contains 3 sub-directories corresponding to 3 different goal states. Each sub-directory contains all the traces towards to the same goal, the testing traces are in ``sas_plan`` format. 

Generally, the traces in ``testing/10_3_10/`` are used for testing the GR performance after training with traces from ``training/10_3_10/``. So, sub-directories in ``training/`` and ``testing/`` are paired with each other accordingly.

```sh
# The command for running the simulation in grid domain
java -cp Grids_Simulation.jar Grids_Simulation
```

Notice: before running this simulation, you need to extract the archived dataset and put the data in correct directory, details of instructions see [tools](https://github.com/zihangs/fp1189_aamas2020/tree/master/tools).



### Table 3:  still grids, vary the training set

```sh
java -cp IncreasingTraces.jar IncreasingTraces
```

world size = 10, number of goals = 6, optimality = (20% - 30%]

We keep all other variables fixed, only modify the number of traces for training model.

training dataset: 10 traces, 100 traces, 1000 traces

testing: always use 100 traces



### Table 4: Classical Planning Domain, Blocksworld, Comparison

From IPC dataset, blocksworld, preprocessed by our trace generator to created traces for training and testing

Python Scripts: new repo or just in there?

```sh
java -cp IPC_domains.jar IPC_domains blocks-world
```

explain the structure of this data in details.

Need to unzip the archived dataset, in some tmp place for running experiments