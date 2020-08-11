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