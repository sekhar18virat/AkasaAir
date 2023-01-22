# AkasaAir
Steps to run the application :  
  1. Clone this repository.
  2. Open the application in intellij or any application that is suitable for running spring boot application
  3. After the application is started the application will run on port 8081.
  4. Since We are using h2 inmemory database. which can be accessed http://localhost:8081/h2
  5. Connect to the database.
  6. Execute the following queries in the h2 db
  
  
  
  
     insert into flight values(123, 5500, 'BOM',TIMESTAMP '2022-01-20', 'AP');
     insert into flight_seats values('1a', false, 500, 123);
     insert into flight_seats values('1b', false, 1000,  123);
     insert into flight_seats values('1c', false, 1500, 123);
     insert into flight_seats values('1d',false, 2500,  123);
  7. Then from the postman. 
     Hit GET 
      http://localhost:8081/api/auth to get the bearer token and add it in the headers.
      
      After getting the token then 
      we can hit the following requests by passing Authorization and value as token in headers column
      POST http://localhost:8081/api/flight/details
      In the body pass the following json Object
      {
        "origin" : "AP",
        "destination" : "BOM",
        "flightDate" : "2022-01-20"
      }
      
      Pass the token in headers for all the requests 
      
      POST http://localhost:8081/api/flight/book
      In the body pass the following json Object
      
      {
        "flightDetails": {
        "origin": "AP",
        "destination": "BOM",
        "flightDate": "2022-01-20"
            },
        "passengerDetails": [
                {
        "firstName": "ABC",
        "lastName": "XYZ",
        "seat": "1b"
                },
                {
        "firstName": "DEF",
        "lastName": "UVW",
        "seat": "1a"
                }
            ]
      }
      
      Then we get the pnr for the booking 
      
      To get the details pass the PNR value to the following request
      GET http://localhost:8081/api/flight/retrieve?pnr=
     
    
