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
#### :o: Doctor.java
```java
@Entity
@Table
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

    private String doctorName;

    @Enumerated(EnumType.STRING)
    private City city;
    private String email;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Specialization specialization;

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;

}
```

#### :o: Patience.java
```java
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;

    @Pattern(regexp = "^[A-Za-z]{3,}$")
    private String patientName;

    @Pattern(regexp = "^[A-Za-z]{1,20}$")
    private String city;
    private Integer age;
    @Column(nullable = false, unique = true)

    private String patientEmail;
    @Column(nullable = false)
    private String patientPassword;

    @Pattern(regexp = "^[0-9]{10}$")
    private String patientContact;

    private String symptom;
}
```

#### :o: AuthToken.java
```java
public class AuthToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;
    private String token;
    private LocalDate tokenCreationDate;

    @OneToOne
    @JoinColumn(nullable = false, name = "fk_patient_ID")
    private Patient patient;

    public AuthToken(Patient patient) {
        this.patient = patient;
        this.tokenCreationDate = LocalDate.now();
        this.token = UUID.randomUUID().toString();
    }
}
```

#### :o: City.java
```java
public enum City {
    Delhi,
    Noida,
    Faridabad
}
```

#### :o: Specialization.java
```java
public enum Specialization {
    ENT,
    ORTHOPEDIC,
    GYNECOLOGY,
    DERMATOLOGY

}
```

#### :o: Appointment.java
```java
public class Appointment {

    @Id
    @EmbeddedId
    private AppointmentKey id;

    @ManyToOne
    @JoinColumn(name = "fk_doctor_doc_id")
    private Doctor doctor;

    @OneToOne
    private Patient patient;
}
```

#### :o: AppointmentKey.java
```java
@Embeddable
public class AppointmentKey implements Serializable {//serializable - bytes

    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long appId;
    public LocalDateTime time;

}
```

##### To See Model
:white_check_mark: [DoctorManagement-Model](https://github.com/Anushri-glitch/Doctor-Management-ServiceAPI-WebApp/tree/master/src/main/java/com/Anushka/DoctorManagementWebAppServiceAPI/model)
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------

### :purple_square: 2. Service - This Layer is used to write the logic of our CRUD operaions.
#### :o: DoctorService.java
```java
@Service
public class DoctorService {

    @Autowired
    IDoctorDao doctorDao;

    public String addDoctor(Doctor doc) {
        Doctor newDoc = doctorDao.findById(doc.getDoctorId()).get();
        if(newDoc != null){
            throw new IllegalStateException("Doctor already Exist...");
        }
        doctorDao.save(doc);
        return "Dr. " + doc.getDoctorName() + " Is Added";
    }
}
```

#### :o: AuthService.java
```java
@Service
public class AuthService {

    @Autowired
    IAuthTokenDao authTokenDao;

    @Autowired
    IPatientDao patientDao;

    public void saveToken(AuthToken token) {
        authTokenDao.save(token);
    }
}
```

#### :o: PatientService.java
```java
@Service
public class PatientService {

    @Autowired
    IPatientDao patientDao;

    @Autowired
    IDoctorDao doctorDao;

    public SignUpOutput signUp(SignUpInput signUpDto) {
        Patient patient = patientDao.findByPatientEmail(signUpDto.getPassword());
        if(patient != null){
            throw new IllegalStateException("Patient already Exists!!!");
        }
        return new SignUpOutput("Patient Registered as " + patient.getPatientName(), "Patient Created Successfully");
    }
}
```

#### :o: AppointmentService.java
```java
@Service
public class AppointmentService {
    @Autowired
    IAppointmentDao appointmentDao;

    public void removeAppointment(AppointmentKey appointKey) {
        appointmentDao.deleteById(appointKey);
    }
}
```

#### To See Service
:white_check_mark: [DoctorManagement-Service](https://github.com/Anushri-glitch/Doctor-Management-ServiceAPI-WebApp/tree/master/src/main/java/com/Anushka/DoctorManagementWebAppServiceAPI/service)
----------------------------------------------------------------------------------------------------------------------------------------------------

### :purple_square: 3. Controller - This Controller is used to like UI between Model and Service and also for CRUD Mappings.
#### :o: DoctorController.java
```java
@RestController
@RequestMapping(value = "/doctor")
public class DoctorController {

    @Autowired
    DoctorService doctorService;

    @PostMapping()
    public String addDoctor(@RequestBody Doctor doc){
        return doctorService.addDoctor(doc);
    }
}
```

#### :o: PatientController.java
```java
@RestController
@RequestMapping(value = "/Patient")
public class PatientController {

    @Autowired
    PatientService patientService;

    @Autowired
    AuthService authService;

    @PostMapping(value = "/signUp")
    public SignUpOutput signUp(@RequestBody SignUpInput signUpDto){
        return patientService.signUp(signUpDto);
    }
}
```

#### :o: AppointmentController.java
```java
@RestController
public class AppointmentController {

    @Autowired
    AppointmentService appointmentService;

    @PostMapping(value = "bookAppointment")
    public void bookAppointment(@RequestBody Appointment appointment){
        appointmentService.BookAppointment(appointment);
    }
}
```

#### To See the Controller
:white_check_mark: [DoctorManagement-Controller](https://github.com/Anushri-glitch/Doctor-Management-ServiceAPI-WebApp/tree/master/src/main/java/com/Anushka/DoctorManagementWebAppServiceAPI/controller)
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
### :purple_square: 3. Repository: data access object (DAO) is an object that provides an abstract interface to some type of database or other persistence mechanisms.

#### :o: IDoctorDao.java
```java
@Repository
public interface IDoctorDao extends JpaRepository<Doctor,Long> {
    Doctor findByDoctorId(Long docId);

    Doctor getDoctorBySpecialization(String orthopedic);
}
```
#### :o: IPatientDao.java
```java
@Repository
public interface IPatientDao extends JpaRepository<Patient,Long> {
    Patient findByPatientEmail(String patientEmail);
}
```

#### :o: IAuthTokenDao.java
```java
@Repository
public interface IAuthTokenDao extends JpaRepository<AuthToken,Long> {
    AuthToken findFirstByToken(Patient patient);

    AuthToken findFirstByToken(String token);
}
```

#### :o: IAppointmentDao.java
```java
@Repository
public interface IAppointmentDao extends JpaRepository<Appointment, AppointmentKey> {
    Appointment findFirstById(AppointmentKey appointKey);
}
```

#### To See The Repository
:white_check_mark: [DoctorManagement-DAO](https://github.com/Anushri-glitch/Doctor-Management-ServiceAPI-WebApp/tree/master/src/main/java/com/Anushka/DoctorManagementWebAppServiceAPI/repository)
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
    private String city;
    private Integer age;
    private String email;
    private String password;
    private String contact;
    private String symptom;
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
    private String patientEmail;
    private String patientPassword;
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

##### To See The DTO
:white_check_mark: [DoctorManagement-DTO](https://github.com/Anushri-glitch/Doctor-Management-ServiceAPI-WebApp/tree/master/src/main/java/com/Anushka/DoctorManagementWebAppServiceAPI/dto)
------------------------------------------------------------------------------------------------------------------------------------------------------

## :four: DataStructures Used in Project
    1. List
    2. JsonObject
    3. JsonArray
-------------------------------------------------------------------------------------------------------------------------------------------------------
## :five: DataBase Response In project

:arrow_right: Patient table

```sql
  select * from patient;
+------------+---------------------+-------------------+----------------------------------+----------------+
| patient_id | patient_email       | patient_name      | patient_password                 | patient_phone  |
+------------+---------------------+-------------------+----------------------------------+----------------+
|       1    | Anushka12@gmail.com | AnushkaSrivastava | 1C6B2A130FD59CE767D50D0598E9F4D1 | 1234567890     |
|       2    | Pankaj@gmail.com    | PankajBhartiya    | A6E49C7BACFCCB95D75464C4AB82422D | 1234567890     |
|       3    | Viresh@gmail.com    | VireshRathore     | A86BEFDE3786B3633D46A742FEF61721 | 1234567890     |
+------------+---------------------+-------------------+----------------------------------+----------------+
```

:arrow_right: Doctor Table

```sql
  select * from Hr;
+-----------+------------------------+---------------+------------------------+---------------+--------------------+
| doctor_id | doctor_name            | city          | email                  | phone         |  specialization    |
+-----------+------------------------+---------------+------------------------+---------------+--------------------+
|        2  | Dr Anushka Srivastava  | DELHI         | anushka23@gmailcom     | 1234567890    |  ORTHOPEDIC        |
|        3  | Dr Vaibhav Shekhawat   | FARIDABAD     | vaibhav1234@gmail.com  | 1234567890    |  ENT               |
+-----------+------------------------+---------------+------------------------+------------------------------------+
```
----------------------------------------------------------------------------------------------------------------------------------------------------------
## :six: Project Summary
### :o: Generated API's

:small_blue_diamond: ADD Doctor : http://localhost:8080/Doctor

:small_blue_diamond: GET Doctor By Id : https://localhost:8080/Doctor/docId/appointment

:small_blue_diamond: SIGNUP Patient : https://localhost:8080/Patient/signUp

:small_blue_diamond: SIGNIN Patient : https://localhost:8080/Patient/signIn

:small_blue_diamond: GET All Doctors : https://localhost:8080/Patient/doctors

:small_blue_diamond: DELETE Appointment : https://localhost:8080/Patient/appointment

:small_blue_diamond: GET Doctor By Symptom: https://localhost:8080/Patient/symptom

:small_blue_diamond: BOOK Appointment : https://localhost:8080/bookAppointment

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





