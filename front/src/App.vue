<template>
  <div id="app">
    <img alt="Vue logo" src="./assets/logo.png" width="15%" height="15%">
    <HelloWorld :cpuStats="cpuStats" :chartData="toChartData" :chartOptions="chartOptions"/>
  </div>
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'

Array.prototype.enqueue = function(val) {
  if(this.length === 61) { // max 60 elements + title
    this.shift()
    this.push(val)
  } else {
    this.push(val)
  }
  return this
}

export default {
  name: 'App',
  components: {
    HelloWorld
  },
  data() {
    return {
      cpuStats: new Array(),
      chartTitle: null,
      chartOptions: {
        title: 'CPU Stats',
        curveType: 'function',

        vAxis: { viewWindow: { min: -5, max: 105}, title: 'Percentage' },
        hAxis: { ticks: [0, 10, 20, 30, 40, 50, 60], title: 'Seconds' },
        height: 600
      }
    }
  },
  created() {
    var connection = new WebSocket("ws://localhost:9000/cpu-stats")
    connection.onmessage = event => {
      var json = JSON.parse(event.data)
      if (this.chartTitle === null) {
        var title = json.map(core => { return core.name })
        title.unshift('seconds') // side-effect :p
        this.chartTitle = title
      }
      this.cpuStats.enqueue(json)
    }
  },
  computed: {
    toChartData: function() {
      var stats = this.cpuStats.map(function(stat, idx) {
        return [idx].concat(stat.map(core => { return core.active }))
      })
      return [this.chartTitle].concat(stats)
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
