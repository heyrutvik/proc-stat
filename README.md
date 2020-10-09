## /Proc/Stat

#### CPU stats

---
##### web front
![screenshot](./public/cpu-stats-graph.png?raw=true "Screenshot")

##### Data flow
![data flow](./public/data-flow.svg?raw=true "Data Flow")

---
##### Projects
* [CPU Stats Reader](https://github.com/heyrutvik/proc-stat/tree/master/reader)
* [Web Service](https://github.com/heyrutvik/proc-stat/tree/master/service)
* [Web Front](https://github.com/heyrutvik/proc-stat/tree/master/front)

##### Run
* start docker (for zookeeper and kafka)
  - `cd cd reader/docker/`
  - `./up`
* start stats reader (in separate terminal)
  - `sbt reader/run`
* start web service (in separate terminal)
  - `sbt service/run`
* start web front (in separate terminal)
  - `cd front`
  - `npm run serve`