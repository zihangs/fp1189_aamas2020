# explain how to use jar file to run the experiments.

TODO: check initial values, re-compile jar files

PM_Simulation.jar: the tool to replicate table 1

```sh
java -cp PM_Simulation.jar PM_Simulation <dataset> <goals>
```
for example: using "env_prmt_64" as for the dataset and there are 5 goals in that domain problem.
```sh
java -cp PM_Simulation.jar PM_Simulation env_prmt_64 5
```
you can find the output in https://github.com/zihangs/fp1189_aamas2020/tree/master/outputs

the source codes are in ./src


after creating the simulation output, we need to analysis the performence in terms of precision and recall:

the method of calculating precision and recall: TODO





Table 2
run Jar file Grids_Simulation.jar

```sh
java -cp Grids_Simulation.jar Grids_Simulation
```

precision & recall: TODO

Table 3

```sh
java -cp IncreasingTraces.jar IncreasingTraces
```


Table 4
Run Jar file IPC_domains.jar
```sh
java -cp IPC_domains.jar IPC_domains blocks-world
```
the models are mined by using split miner

precision & recall: TODO

#### check the jar files running on other systems, there might be null pointers due to missing initial values

