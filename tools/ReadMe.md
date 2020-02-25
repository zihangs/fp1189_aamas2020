# explain how to use jar file to run the experiments.

there are tools avaible

BPM_Simulation.jar: the tool to replicate table 1

```sh
java -cp BPM_Simulation.jar Table_1_Simulator <dataset> <goals>
```
for example: using "env_prmt_64" as for the dataset and there are 5 goals in that domain problem.
```sh
java -cp BPM_Simulation.jar Table_1_Simulator env_prmt_64 5
```
you can find the output in https://github.com/zihangs/fp1189_aamas2020/tree/master/outputs

the source codes are in ./src


after creating the simulation output, we need to analysis the performence in terms of precision and recall:

the method of calculating precision and recall: TODO
