Requirements -:

1. Postgres installed in PC.
2. PgAdmin to view the database.
3. Set spring.jpa.hibernate.ddl-auto is create when running the application for the first time, afterwards set it to update.

Runs on port - 8080
test url -: http://localhost:8080/user/hello - returns Hello World.

Apis -:

User Apis -:

1. Signup User - Signs Up user and isActive false, set otp which is valid for 15 mins, return the otp value.
Method - POST
url - http://localhost:8080/user/signup
request body -:
{
    "email":"nilay.gupta@scaler.com",
    "password":"nilay",
    "mobileNumber":"9982990010",
    "role":"admin"
}

2. Verify User - Checks otp validity and activates user.
Method - POST
url - http://localhost:8080/user/verify?otp={otp_value}&email={email_id_to_activate}

3. Login User - Logins in User, sets isLogged flag to true.
Method - POST
url - http://localhost:8080/user/login?email={user_email}&password={password}

4. Log Out user - logs the user, set isLogged flag = false.
Method - POST
url - http://localhost:8080/user/login?email={user_email}&password={password}

5. Filter Jobs - Filters job based on location and speciality
Method - GET
url -: http://localhost:8080/user/filterjob/{user_email}?location={location}&speciality={speciality}

6. View Jobs - View All the jobs
Method - GET
url -: http://localhost:8080/user/viewjoba/{user_email}


Admin Apis -:

1. Add user - Add user to the database (Only admin can do that)
Method - POST
url - http://localhost:8080/admin/user/add/{admin_email}
{
    "email":"nilay.gupta@scaler.com",
    "password":"nilay",
    "mobileNumber":"9982990010",
    "role":"admin"
}

2. View User - Get all the users (Only admin can do that)
Method - GET
url - http://localhost:8080/admin/user/add/{admin_email}

3. Update User - Update User info
Method - POST
url - http://localhost:8080/admin/user/update/{admin_email}
{
    "email":"nilay.gupta@scaler.com",
    "password":"nilay",
    "mobileNumber":"9982990010",
    "role":"admin"
}

4. Delete User - Deletes User form DB (Only admin can do that)
Method - POST
url - http://localhost:8080/admin/user/update/{admin_email}?userEmail={user_email_id}


5. Add Jobs - Add jobs to database. (Only admin can do that)
url - http://localhost:8080/admin/job/add/{admin_email}
Request Body -
{
    "location" : "delhi",
    "speciality": "java",
    "details": "Needed SD2/3 for Sql."
}


6. View Jobs - Gets all Jobs
url -:  http://localhost:8080/admin/job/view/{admin_email}

7. Update Job - Update a job (only admin can do that)
Method - POST
url -: http://localhost:8080/admin/job/view/{admin_email}?jobId={id_of_job}
Request Body -
{
    "location" : "delhi",
    "speciality": "java",
    "details": "Needed SD2/3 for Sql."
}


8. Delete Job - deletes job from DB (only admin can do that)
Method - POST
url -: http://localhost:8080/admin/job/delete/{admin_email}?jobId={id_of_job}

