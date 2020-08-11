# Archived Datasets

including 4 parts table 1-4

Before using these dataset for import, you need to extract the zip file and put them in some place, the instruction for using see tools explanation.

### Domains from Process Mining Community (Table 1)

The archived dataset ``table_1.zip`` contains 3 domains from process mining community:

1. Activities of daily living of several individuals

   More details about this dataset, see: https://data.4tu.nl/articles/Activities_of_daily_living_of_several_individuals/12674873.

   We pre-processed the original dataset (XES files) and then stored them in ``activities_64/`` (60% traces for training models, 40% traces for testing GR performance) and ``activities_82/`` (80% traces for training, 20% percent trace for testing). 

   

   Within each directory, there are several event logs (``.xes`` file) and each event log contains the traces all towards to the same goal. There are several event logs corresponding to several of goals in this domain.

   

2. BPI Challenge 2015

   More details about this dataset, see: https://data.4tu.nl/collections/BPI_Challenge_2015/5065424.

   Similar pre-process logic as above and pre-processed data stored in ``build_prmt_64/`` and ``build_prmt_82/`` 

3. Environmental permit application process (‘WABO’), CoSeLoG project

   More details about this dataset, see: https://data.4tu.nl/collections/Environmental_permit_application_process_WABO_CoSeLoG_project/5065529.

   Similar pre-process logic as above and pre-processed data stored in ``env_prmt_64/`` and ``env_prmt_82/``  



Command for running:

```sh
java -cp PM_Simulation.jar PM_Simulation <dataset> <goals>
```



### Table 2: Classical Planning Domain, Grids

```sh
java -cp Grids_Simulation.jar Grids_Simulation
```

training dataset + testing dataset

size of world + number of goals + optimality of traces

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