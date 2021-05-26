# CustomerStatementProcessor
SpringBoot application as a Client receives monthly deliveries of customer statement records. This information is delivered in JSON Format. These records need to be validated.

# To Start
Clone this repository
Run mvn clean package
Run using maven command - mvn spring-boot:run on cmd where the pom.xml is located

# Running the application
To test the application a REST client like POSTMAN is required.

For uploading a single file choose POST and the following url http://localhost:9999/api/transactions/process

and add JSON data in the body

Sample input:

      [
         {
            "reference": "130498",
            "accountNumber": "NL69ABNA0433647324",
            "description": "Tickets for Peter Theuß",
            "startBalance": "718.78",
            "mutation": "-18.78",
            "endBalance": "700"
         },
         {
            "reference": "167875",
            "accountNumber": "NL93ABNA0585619023",
            "description": "Tickets from Erik de Vries",
            "startBalance": "5939",
            "mutation": "-939",
            "endBalance": "5000"
         },
         {
            "reference": "147674",
            "accountNumber": "NL93ABNA0585619023",
            "description": "Subscription from Peter Dekker",
            "startBalance": "74.69",
            "mutation": "-44.91",
            "endBalance": "29.78"
         }
      ]

The output response is as follows which shows the
    
      {
          "result" : "SUCCESSFUL",
          "errorRecords" : []
      }

# Implementation details
The implementation is a spring boot MVC.
Lombok is used for attribute accessors generator.
Requirement is to send json data to process



# Requirements and Constraints
Compiled and Tested with JDK 1.8

