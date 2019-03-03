<?php
    define('DB_SERVER', "superpointsdb.cuwiyoumlele.ca-central-1.rds.amazonaws.com");
    define('DB_USERNAME', "admin");
    define('DB_PASSWORD', "zxcasdqwe123");
    //define('DB_DATABASE', "superpointsdb");

    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);
    //$database = mysqli_select_db($con, DB_DATABASE) or die(mysqli_error($con));
    
    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    //$username = $_POST['username'];
    //$password = $_POST['password'];
    $username = "default";
    $password = "password";
    $result = mysqli_query($con,"SELECT * FROM superpoints.Users where 
    userName='$username' and password='$password'", MYSQLI_STORE_RESULT);
    echo $result ? 'true' : 'false';
    $row = mysqli_fetch_array($result);
    $data = $row[0];

    if($data){
       echo $row[0] . " " . $row[1];
    }
    mysqli_close($con);
?>