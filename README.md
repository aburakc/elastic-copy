# elastic-copy
Copy an index to another ElasticSearch instance using scroll and bulk apis.

Parameters: 
- "fromUrl" : ElasticSearch url that will copy from 
- "toUrl" : ElasticSearch url that will copy to 
- "bulkSize" : Size of coping indices with each iteration
