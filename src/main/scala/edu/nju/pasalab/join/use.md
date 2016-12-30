This library adds the skewJoin operation to RDD[(K, V)] where possible (certain implicit typeclasses are required for K and V).
A skew join is just like a normal join except that keys with large amounts of values are not processed by a single task
but instead spread out across many tasks. This is achieved by replicating key-value pairs for one side of the join in such way
that they go to multiple tasks, while randomly spreading out key-value pairs for the other side of the join across the same tasks
(the actual implementation is symmetrical and can replicate and spread out at the same time for both sides). A skew join is
like a bucket/block join except the replication factor is adjusted per key instead of being fixed. To be able to pick how much
replication a key needs the skew join first estimates counts for all keys on both sides of the join, using a count-min-sketch (CMS)
probabilistic data structure. This means the operation will process the RDDs twice:

once for the approximate count of values per key, an immediate action
once for the actual join, a RDD transformation

Besides skewJoin this library also adds the blockJoin operation to RDD[(K, V)]. A block join is a skew j
oin with fixed replication factors for both sides of the join. Because the replication factors are fixed
no approximate count needs to be done either, avoiding a Spark immediate action for this count.