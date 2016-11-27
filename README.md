#AWS Lambdas

Let's put this Bash script in the cloud:
 
```
#!/bin/bash
{
  curl -s http://fcs001.xreflector.net/mitte.html | grep FCS | awk -F">" '{print $14 ", " $22}' | awk -F"<" '{print $1 " " $2}' | awk '{print $3 "-" $4 "\t" $1}';
  curl -s http://fcs002.xreflector.net/mitte.html | grep FCS | awk -F">" '{print $14 ", " $22}' | awk -F"<" '{print $1 " " $2}' | awk '{print $3 "-" $4 "\t" $1}';
  curl -s http://laboenligne.ca/fcs003/mitte.html | grep FCS | awk -F">" '{print $14 ", " $22}' | awk -F"<" '{print $1 " " $2}' | awk '{print $3 "-" $4 "\t" $1}';
} | sort | uniq

```

This bash/lambda displays amateur radio stations currently connected to the [XReflector](http://xreflector.net/) system. Isn't it nice knowing which reflector is most popular at the moment?

`curl -s https://somethingsecret.execute-api.us-west-1.amazonaws.com/prod | python -m json.tool`

gives you a computable fusion reflector roster:

```
[
    [
        "01",
        "JA2UCX",
        "FCS001 21",
        "2342791",
        "IO92UO",
        "0001646334DE",
        "Internet"
    ],
    [
        "02",
        "AA9YR ",
        "FCS001 21",
        "3117630",
        "EN62AE",
        "0201739D7F07",
        "Internet"
    ],
...
```


### Guides
 - [Writing AWS Lambda Functions in Scala](https://aws.amazon.com/blogs/compute/writing-aws-lambda-functions-in-scala/)
 - Wrap the Lambda with a public API: [Build the API Step By Step](http://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-create-resource-and-methods.html)


### Building

- `sbt clean compile assembly`

### Trivia
  - Most popular reflectors as of this commit:
  ```
  curl -s https://o51kt1xrri.execute-api.us-west-1.amazonaws.com/prod | sed 's/\[/\n/g' | grep FCS | awk -F'"' '{print $6}' | sed 's/ /-/g' | sort | uniq -c | sort -k1 -rn | head
  ```
  
```
     29 FCS001-01
     28 FCS002-02
     17 FCS002-90
     17 FCS001-06
     16 FCS001-55
     16 FCS001-21
     10 FCS003-70
     10 FCS001-09
      8 FCS002-14
      7 FCS002-23
```
  