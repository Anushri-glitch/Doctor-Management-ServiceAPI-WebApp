# Doctor-Management-ServiceAPI-WebApp
##### :purple_square: This Web Application Contains Service API's for the Doctor and Patient Management. It is based on MySQL and Springboot.

## :one: Frameworks and Languages Used -
    1. SpringBoot
    2. JAVA
    3. Postman
    4. MySQL
    
## :two: Dependency Used
    1. Spring Web
    2. Spring Boot Dev Tools
    3. Lombok
    4. Spring Data JPA
    5. MySQL Connector
    6. Validation
    7. Json
    8. Javax
    9. Swagger UI
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
## :three: Dataflow (Functions Used In)
### :purple_square: 1. Model - Model is used to Iniitialize the required attributes and create the accessable constructors and methods
#### :o: Employee.java
```java
@Entity
@Table
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer employeeId;
    private String employeeName;
    private String employeePhone;
    private String employeeEmail;
    private String jobRole;
    private Integer salary;
    private Integer Age;
    private String password;
```

#### :o: Hr.java
```java
public class Hr {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer hrId;
    private String hrName;

    @Pattern(regexp = "^[a-z0-9]{3,}@[admin]{3,5}[.]{1}[com]{1,3}$")
    private String hrEmail;
    private String hrPassword;
    private Integer age;
    private String hrPhone;

    @OneToMany
    private List<Employee> employeeList = new ArrayList<>();
```

#### :o: AuthTokenEmployee.java
```java
public class AuthTokenUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tokenId;

    private String token;
    private LocalDate tokenCreationDate;

    @OneToOne
    @JoinColumn(nullable = false, name = "fk_employee_Id")
    private Employee employee;
}
```

#### :o: AuthTokenHr.java
```java
public class AuthTokenHr {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long adminTokenId;

    private String adminToken;
    private LocalDate adminTokenCreationDate;

    @OneToOne
    @JoinColumn(nullable = false, name = "fk_Hr_Id")
    private Hr hr;
}
```

#### :o: Rep.java
```java
@Entity
@Table
public class Rep {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer reportId;
    private Integer attendence;
    private String performance;
    private String name;

}
```

##### To See Model
:white_check_mark: [EmployeeManagement-Model](https://github.com/Anushri-glitch/Employee-Management-WebApp/tree/master/src/main/java/com/Anushka/EmployeeManagementWebApp/model)
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------

### :purple_square: 2. Service - This Layer is used to write the logic of our CRUD operaions.
#### :o: EmployeeService.java
```java
@Service
public class UserService {

    @Autowired
    IEmployeeDao employeeDao;

    @Autowired
    IHrDao hrDao;

    @Autowired
    AuthService authService;

    public SignUpOutput SignUp(SignUpInput signUpDto) {

        //Check If the Employee Exists or not based on Employee's Email
        User user = userDao.findFirstByUserEmail(signUpDto.getEmail());

        if(employee != null){
            throw new IllegalStateException("Employee Already Registered!!");
        }
}
```

#### :o: AuthService.java
```java
@Service
public class AuthService {

    @Autowired
    IAuthEmployeeDao authEmployeeDao;

    @Autowired
    IAuthHrDao authHrDao;

    //Save Token For User
    public void saveToken(AuthTokenEmployee token) {
        authEmployeeDao.save(token);
    }
}
```

#### :o: HrService.java
```java
@Service
public class HrService {

    @Autowired
    IHrDao hrDao;

    @Autowired
    AuthService authService;

    @Autowired
    IEmployeeDao employeeDao;

    public SignUpOutput signUp(signUpHrInput signUpDto) {
        //Check Employee is Registered or not
         Hr hr = hrDao.findFirstByHrEmail(signUpDto.getEmail());

        if(hr != null){
            throw new IllegalStateException("Hr Already Registered!!!");
        }
        return new SignUpOutput("Hr registered as - "+ hr.getHrName() , " Hr Created Successfully!!!");
    }
```

#### :o: RepService.java
```java
@Service
public class RepService {


    @Autowired
    IEmployeeDao employeeDao;

    @Autowired
    IReportingDao reportingDao;

    @Autowired
    IHrDao hrDao;

    public String addAttendence(String empEmail) {

        int newAttendence =0;

        //Check that This Employee is existing or not
        Employee employee = employeeDao.findFirstByEmployeeEmail(empEmail);
        if(employee == null){
            throw new IllegalStateException("Employee is Invalid !!...");
        }
       
        //Final Save In Repo
        reportingDao.save(rep);

        return "Your Attendence is Marked !! Thank You " + employee.getEmployeeName();
    }
```

#### To See Service
:white_check_mark: [EmployeeManagement-Service](https://github.com/Anushri-glitch/Employee-Management-WebApp/tree/master/src/main/java/com/Anushka/EmployeeManagementWebApp/service)
----------------------------------------------------------------------------------------------------------------------------------------------------

### :purple_square: 3. Controller - This Controller is used to like UI between Model and Service and also for CRUD Mappings.
#### :o: EmployeeController.java
```java
@RestController
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    //Create Employee
    @PostMapping(value = "/SignUp/HrEmail/{HrEmail}")
    public SignUpOutput SignUp(@RequestBody SignUpInput signUpDto, @PathVariable String HrEmail){
        return employeeService.SignUp(signUpDto, HrEmail);
    }
}
```

#### :o: HrController.java
```java
@RestController
public class HrController {

    @Autowired
    HrService hrService;

    @PostMapping(value = "/HrSignUp")
    public SignUpOutput SignUpHr(@RequestBody signUpHrInput signUpDto){
        return hrService.signUp(signUpDto);
    }
}
```

#### :o: RepController.java
```java
@RestController
public class RepController {

    RepService repService;

    @PostMapping(value = "Rep/{empEmail}")
    public String createAttendence(@PathVariable String empEmail){
        return repService.addAttendence(empEmail);
    }
}
```

#### To See the Controller
:white_check_mark: [EmployeeManagement-Controller](https://github.com/Anushri-glitch/Employee-Management-WebApp/tree/master/src/main/java/com/Anushka/EmployeeManagementWebApp/controller)
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
### :purple_square: 3. Repository: data access object (DAO) is an object that provides an abstract interface to some type of database or other persistence mechanisms.

#### :o: IEmployeeDao.java
```java
@Repository
public interface IEmployeeDao  extends JpaRepository<Employee, Integer> {
    User findFirstByUserEmail(String email);
}
```
#### :o: IHrDao.java
```java
@Repository
public interface IHrDao extends JpaRepository<Hr, Integer> {
    Hr findFirstByHrEmail(String hrEmail);
}
```

#### :o: IAuthEmployeeDao.java
```java
@Repository
public interface IAuthEmployeeDao extends JpaRepository<AuthTokenEmployee,Integer> {
   AuthTokenEmployee findByEmployee(Employee employee);
}
```

#### :o: IAuthHrDao.java
```java
@Repository
public interface IAuthHrDao extends JpaRepository<AuthTokenHr,Integer> {
    AuthTokenHr findByHr(Hr hr);
}
```

#### :o: IRepDao.java
```java
@Repository
public interface IRepDao extends JpaRepository<Rep,Integer> {
}
```

#### To See The Repository
:white_check_mark: [EmployeeManagement-DAO](https://github.com/Anushri-glitch/Employee-Management-WebApp/tree/master/src/main/java/com/Anushka/EmployeeManagementWebApp/repository)
-------------------------------------------------------------------------------------------------------------------------------------------------------

### :purple_square: 3. DTO : Use This to do SignUp and SignIn for the User
#### :o: SignUpInput.java

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpInput {
    private String firstName;
    private String lastName;
    private Integer age;
    private String email;
    private String password;
    private String jobRole;
    private Integer salary;
    private String contact;
}
```

#### :o: SignUpOutput.java

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpOutput {

    String status;
    String message;
}
```

#### :o: SignInInput.java

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInInput {

    private String signInMail;
    private String signInPassword;
}
```

#### :o: SignInOutput.java

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInOutput {

    private String status;
    private String token;
}
```
#### :o: SignUpHrInput.java

```java
@Data
@NoArgsConstructor
public class signUpHrInput {
    private String firstName;
    private String lastName;
    private Integer age;
    private String email;
    private String password;
    private String contact;
}
```

##### To See The DTO
:white_check_mark: [EmployeeManagement-DTO](https://github.com/Anushri-glitch/Employee-Management-WebApp/tree/master/src/main/java/com/Anushka/EmployeeManagementWebApp/dto)
------------------------------------------------------------------------------------------------------------------------------------------------------

## :four: DataStructures Used in Project
    1. List
    2. JsonObject
    3. JsonArray
-------------------------------------------------------------------------------------------------------------------------------------------------------
## :five: DataBase Response In project

:arrow_right: Employee table

```sql
  select * from employee;
+------------+---------------------+-------------------+----------------------------------+----------------+
| emloyee_id | employee_email      | employee_name     | employee_password                | employee_phone |
+------------+---------------------+-------------------+----------------------------------+----------------+
|       1    | Anushka12@gmail.com | AnushkaSrivastava | 1C6B2A130FD59CE767D50D0598E9F4D1 | 1234567890     |
|       2    | Pankaj@gmail.com    | PankajBhartiya    | A6E49C7BACFCCB95D75464C4AB82422D | 1234567890     |
|       3    | Viresh@gmail.com    | VireshRathore     | A86BEFDE3786B3633D46A742FEF61721 | 1234567890     |
+------------+---------------------+-------------------+----------------------------------+----------------+
```

:arrow_right: Hr Table

```sql
  select * from Hr;
+----------+------------------+-------------------------------------+----------+--------------------+
| hr_id    | hr_email            | hr_password                      | hr_phone    | hr_user_name    |
+----------+---------------------+----------------------------------+----------+--------------------+
|        2 | viresh@admin.com    | A86BEFDE3786B3633D46A742FEF61721 | 1234567890  | VireshRathore   |
|        3 | vaibhav22@admin.com | A86BEFDE3786B3633D46A742FEF61721 | 1234567880  | VaibhavSharma   |
+----------+---------------------+----------------------------------+-------------+-----------------+
```

:arrow_right: AuthTokenEmployee Table

```sql
 select * from auth_token_employee;
+----------+-----------------------------------------------+------------------------------+----------------+
| employee_token_id | employee_token                       | employee_token_creation_date | fk_employee_id |
+-------------------+--------------------------------------+------------------------------+----------------+
|                1  | c8a5b6e0-e167-45d4-b3a2-af73239ace38 | 2023-05-21                   | 1              |
|                2  | b49a53d8-cc12-4007-83cc-df97358c5c25 | 2023-05-21                   | 2              |
|                3  | f18b4dd1-aed4-4a41-b72a-a18347de3396 | 2023-05-21                   | 3              |
+----------+--------------------------------------+---------------------+------------+----+----------------+
```

:arrow_right: AuthTokenHr Table

```sql
select * from auth_token_Hr;
+----------------+--------------------------------------+---------------------------+----------------+
| Hr_token_id    | Hr_token                             | Hr_token_creation_date    | fk_employee_id |
+----------------+--------------------------------------+---------------------------+----------------+
|              1 | 188bd41e-48d1-4e3c-9e05-6f3b0a8a66c5 | 2023-05-21                | 1              |
+----------------+--------------------------------------+---------------------------+----------------+
```
----------------------------------------------------------------------------------------------------------------------------------------------------------
## :six: Project Summary
### :o: Generated API's

:small_blue_diamond: SIGNUP Hr : http://localhost:8080/HrSignUp

:small_blue_diamond: SIGNIN Hr : https://localhost:8080/HrSignIn

:small_blue_diamond: ADD Employee List In Hr : https://localhost:8080/storeEmployee/hrEmail/{hrEmail}

:small_blue_diamond: SIGNUP Employee : https://localhost:8080/signUp/HrEmail/{HrEmail}

:small_blue_diamond: SIGNIN Employee : https://localhost:8080/signIn

:small_blue_diamond: GET Employee By Employee Email For Hr : https://localhost:8080/getEmployee/HrEmail/{hrEmail}/empEmail/{empEmail}

:small_blue_diamond: UPDATE Employee JobRole and Salary By Hr: https://localhost:8080/update/hrEmail/{hrEmail}/empEmail/{empEmail}

:small_blue_diamond: UPDATE Password By Employee : https://localhost:8080/changePassword/{empEmail}

:small_blue_diamond: DELETE Employee By Hr : https://localhost:8080/delete/hrEmail/{hrEmail}/{empEmail}

:small_blue_diamond: DELETE SONG : https://localhost:8080/songDetails/delete/songId/{songId}/adminEmail/{adminEMail}

:small_blue_diamond: ADD Reporting : https://localhost:8080/Rep/{empEmail}

:small_blue_diamond: GET Performance REPORT BY Hr : https://localhost:8080/Rep/HrEmail/{hrEmail}/Employee/{empEmail}

--------------------------------------------------------------------------------------------------------------------------------------------------

## :seven: Project Result
### :o: Employee & Hr Response
![Screenshot (865)](https://github.com/Anushri-glitch/Employee-Management-WebApp/assets/47708011/7a170184-a66c-4b01-9ecd-9cc4e7f4fa2f)
![Screenshot (866)](https://github.com/Anushri-glitch/Employee-Management-WebApp/assets/47708011/553b0a03-aa2a-47ac-90a9-c43109b7b1f7)
![Screenshot (867)](https://github.com/Anushri-glitch/Employee-Management-WebApp/assets/47708011/4baac253-21f8-48d7-a963-dd93bee86ae2)
![Screenshot (868)](https://github.com/Anushri-glitch/Employee-Management-WebApp/assets/47708011/276a1510-dba3-4654-9865-ba66dd6d4723)
![Screenshot (869)](https://github.com/Anushri-glitch/Employee-Management-WebApp/assets/47708011/e52cd394-57c5-4c92-a283-1a340f602942)
![Screenshot (870)](https://github.com/Anushri-glitch/Employee-Management-WebApp/assets/47708011/a34171ea-b0bb-4ad7-ae07-ee5dcad8aadc)

### :world_map: Other Good Projects -
#### :o: [Ecommerce Mangement-Service API](https://github.com/Anushri-glitch/Ecommerce-Application)

#### :o: [Restaurent Management-Service API](https://github.com/Anushri-glitch/RestaurentManagement-Application/tree/master)

#### :o: [Stock Management-Service API](https://github.com/Anushri-glitch/Stock-Management-Application)

#### :o: [Visitor Counter-Service API](https://github.com/Anushri-glitch/Visitor-Counter-Application)

#### :o: [Weather API Calling By JAVA](https://github.com/Anushri-glitch/Weather-Forecast-Application)

#### :o: [Sending Mail By JAVA](https://github.com/Anushri-glitch/SendMail-Application)

:arrow_right: ![Screenshot (844)](https://github.com/Anushri-glitch/Music_Streaming-Service-API/assets/47708011/2fe46e6f-0ee4-4e2c-88df-41e30af9f564)
:arrow_right: ![Screenshot (845)](https://github.com/Anushri-glitch/Music_Streaming-Service-API/assets/47708011/99416677-8a7b-4f92-bb60-9c7db2da2174)
:arrow_right: ![Screenshot (846)](https://github.com/Anushri-glitch/Music_Streaming-Service-API/assets/47708011/bced6929-a532-4509-8db5-1bae7508bcdf)
:arrow_right: ![Screenshot (847)](https://github.com/Anushri-glitch/Music_Streaming-Service-API/assets/47708011/42fc552c-c670-4358-ac11-d340dbdf49c0)
:arrow_right: ![Screenshot (848)](https://github.com/Anushri-glitch/Music_Streaming-Service-API/assets/47708011/73e7f501-6fad-4acc-83e6-0239c56ef8bd)
:arrow_right: ![Screenshot (849)](https://github.com/Anushri-glitch/Music_Streaming-Service-API/assets/47708011/bcc0169d-e274-49e9-8afa-177400886a6f)
-----------------------------------------------------------------------------------------------------------------------------------------------------





