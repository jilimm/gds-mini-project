# gds-mini-project
GDS SWE Challenge - The mini Project

## Objective
Spring web application with endpoints to query a database and modify/insert data to database with csv file

## REST endpoints
- GET /users
  - parameters
    - min - minimum salary. Defaults to 0
    - max - maximum salary. Defaults to 4000
    - offset - **Optional.** first result among set to be returned. defaults to 0.
    - limit - **Optional.** number of results to include. defaults to no limit.
    - sort - **Optional.** NAME or SALARY, non-case sensitive. Sort only in ascending sequence. Defaults to no sorting.
  - sample response
    - ```
      { 
        "results": [
            {
                "name": "Judy",
                "salary": "0.0"
            },
            {
                "name": "Xin Yi",
                "salary": "2500.0"
            }
      ]
      }
      ```
- POST /upload
  - content type
    - multipart/form
  - Form field name
    - file
  - File constraints
    - headers must be `NAME,SALARY`
    - name is text, salary is floating point number
    - if salary <0 data should be ignored
    - if name exists in DB, data should be updated
    - if error occurred while parsing file / inserting data, all data in file should be ignored
  - sample response
    - ```
      { 
        "success": 1
      }
      ```
    - sample error response
      - ```
        { 
        "success": 0, 
        "error":{ 
            "timestamp": "11-04-2023 11:11:48",
            "message": "Error encountered while processing file", 
        "details":[ 
            { 
                "field": "line 6",
                "invalidValue": "April,pewpew",
                "message": "Unparseable number: 'pewpew'"
            }
        ]}
        }
  
## Object
Data returned or inserted into DB will be of `User` type. <br/>
**User object properties**

* `name` - String
  * name of user
  * MUST match regex `^[a-zA-Z]+[ a-zA-Z]+$`
    * name can only contain a-z (case insensitive)
    * whitespace must be in between characters
* `salary` - Float
  * salary of user

## Database
table: `users`

| name | type  |  settings |  
|---|---|---|
| name | string | primary key | 
| salary | float | |
