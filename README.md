## /Proc/Stat

#### CPU stats

---

![data flow](./front/public/cpu-stats-graph.svg?raw=true "Data Flow")

---

##### Projects
* Reader
* Web Service
* Front

##### Run
* start docker (for zookeeper and kafka)
  - `cd cd reader/docker/`
  - `./up`
* start reader (in separate terminal)
  - `sbt reader/run`
* start web service (in separate terminal)
  - `sbt service/run`
* start web front (in separate terminal)
  - `cd front`
  - `npm run serve`