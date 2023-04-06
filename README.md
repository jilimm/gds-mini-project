# gds-mini-project
GDS SWE Challenge - The mini Project


## Assumptions made
1. User
   2. salary is float
   maximum float value is (340 282 346 638 528 860 000 000 000 000 000 000 000)
    assuming  salary is monthly, float should suffice
    3. salary min and max is inclusive

## Design Choices
- DB
  - use Entity manager to allow more custom  SQL queries
    https://www.bezkoder.com/jpa-entitymanager-spring-boot/
- used CQ to allow user defined limit, and offset 

  // https://www.baeldung.com/spring-data-criteria-queries
  // https://reflectoring.io/spring-data-specifications/
  //  https://www.baeldung.com/jpa-and-or-criteria-predicates
  // https://stackoverflow.com/questions/11655870/jpa-2-criteriaquery-using-a-limit
- CSV Error Handling
  - fast failure method chosen
  - I dont wait for entire file to run and collect all exceptions --> if file is huge this is super ineffcient.

Milestones
- [x] populate H2 DB
- [x] able to convert request params SQL Query params
- [x] enum should NOT be case-sensitive
- [x] validate on request params (basic ones?) - https://www.baeldung.com/spring-validate-requestparam-pathvariable
- [X] convert CSV file to list of objects
- [X] validation on csv file
- [X] ensure duplicate name is updated
- [X] ensure csv file is all or nothing operation  - using `@Transactional` & jparepository for now. to see if can improve?
    - [x] username shuold be unique
- [ ] error handling
  - [ ] excpetions
    - [ ] controller validations
    - [ ] BusinessException
    - [ ] CSVExceptions
      - [ ] check if can include the row/line number where csv exception occured. 
 
