# Performance Comparison of Various Solutions

### Factors Considered

* Windowing: `ProcessWindowFunction` vs `AggregateFunction` + `ProcessWindowFunction`

  When using `AggregateFunction`, we can spread the aggregation over the entire windowing period of a window,
  and reduce the work to minimum when a window is complete.

* Sorting uses one timer per event vs one timer per watermark
  
  One timer per event means we will create and fire many timers, this can be mitigated by coalescing timers with the 
  next watermarks. On ther other hand, one timer per watermark requires sorting events up to the watermark and assign 
  timestamp a second time before  

* Sorting with `MapState` vs `ListState`

  With MapState, we can work with the map keys without the need to deserialize the values. This helps sort events based 
  the timestamps (i.e., the map keys) without the actual deserialization of the measurements.

* Sorting with `MapState<Long, List<Measurement>>` vs `MapState<Long, Measurement>`
   
  The former is needed when some measurements have the same timestamp.

* `DataStream<Tuple2<Measurement, Long>>` vs `DataStream<Measurement>`

  The Flink's Window API does not provide access to event time of individual events.
  In order to sort the events based on event time, we need to combine
  the measurement event with its timestamp together, i.e., working with a stream of `Tuple2<Measurement, Long>`.
  If we first sort then window, we can work with a stream of `Measurement` beacuse `ProcessFunction()` in the sorting
  operator has access to event time and the window operator can do the aggregation directly as events are already sorted

## Exercise

DataStream<Tuple2<Measurement, Long>>, AggregateFunction with PriorityQueue + ProcessWindowFunction

    The job fails due to checkpoints are timed out

## Solution2: no pre-aggregation
DataStream<Tuple2<Measurement, Long>>, ProcessWindowFunction

    Latency: 20.7s, Throughput: 5.84k, Checkpoint duration: 25s, checkpoint size: ~715MB

## Solution3: first sort then window

### Solution30

Sort DataStream<Tuple2<Measurement, Long>> with PriorityQueue, one timer per event

    Latency: 25.3s, Throughput: 4.87k, Checkpoint duration: 15s, checkpoint size: ~732KB

### Solution310

Sort DataStream<Tuple2<Measurement, Long>> with MapState<Long, Measurement>, one timer per event

    Latency: 5.5s, Throughput: 22.20k, Checkpoint duration: 4s, checkpoint size: ~977KB

### Solution320

Sort DataStream<Tuple2<Measurement, Long>> with ListState<Tuple2<Measurement, Long>>, one timer per event

    Latency: 12.2s, Throughput: 10.28k, Checkpoint duration: 8s, checkpoint size: ~977KB

### Solution321

Sort DataStream<Tuple2<Measurement, Long>> with ListState<Tuple2<Measurement, Long>>, one timer per watermark

    Latency: 8.2s, Throughput: 13.61k, Checkpoint duration: 6-7s, checkpoint size: ~977KB

### Solution330

Sort DataStream<Tuple2<Measurement, Long>> with MapState<Long, List<Measurement>>, one timer per event

    Latency: 8.9s, Throughput: 11.85k, Checkpoint duration: 8s, Checkpoint size: ~977KB

### Solution331

Sort DataStream<Tuple2<Measurement, Long>> with MapState<Long, List<Measurement>>, one timer per watermark

    Latency: 16.5s, Throughput: 8.9k, Checkpoint duration: 6-15s, Checkpoint size: ~977KB

### Solution332

Sort DataStream<Measurement> with MapState<Long, List<Measurement>>, register one timer per event

    Latency: 8.5s, Throughput: 12.17k, Checkpoint duration: ~8s, Checkpoint size: ~977KB

### Solution333

Two times keyBy() to build the measurement list per key & timestamp, Sort with MapState<Long, List<Measurement>>, two times keyBy, register one timer per event

    Latency: 14.7s, Throughput: 11.38k, Checkpoint duration: ~14s, Checkpoint size: ~977KB

