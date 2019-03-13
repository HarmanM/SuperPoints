<?php
    define('DB_SERVER', "superpointsdb.cuwiyoumlele.ca-central-1.rds.amazonaws.com");
    define('DB_USERNAME', "admin");
    define('DB_PASSWORD', "zxcasdqwe123");


    function login() {
        $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);
    
        if (mysqli_connect_errno($con)) {
            echo "Failed to connect to database: " . mysqli_connect_error();
        }
        
        $username = $_GET['username'];
        $password = $_GET['password'];
        $result = mysqli_query($con,"SELECT * FROM superpoints.Users where 
        userName='$username' and password='$password'", MYSQLI_STORE_RESULT);
        $row = mysqli_fetch_array($result);
        $userid = $row[0];
        $businessid = $row[1];
        $userName = $row[3];
        $settings = $row[4];
        
        echo $userid . " " . $businessid . " " . $userName . " " . $settings;
        
        mysqli_close($con);
    }

    function register() {
        $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);
    
        if (mysqli_connect_errno($con)) {
            echo "Failed to connect to database: " . mysqli_connect_error();
        }
        $username = $_GET['username'];
        $password = $_GET['password'];
        $result = mysqli_query($con,"INSERT INTO `superpoints`.`Users`
            (`password`,`userName`,`settings`) VALUES ('$password', '$username', 0);", MYSQLI_STORE_RESULT);
        echo $result ? 'true' : 'false';
        
        mysqli_close($con);
    }

    function insertVisits() {
        $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);
    
        if (mysqli_connect_errno($con)) {
            echo "Failed to connect to database: " . mysqli_connect_error();
        }
        $userid = $_GET['uid'];
        $businessid = $_GET['bid'];
        $duration = $_GET['dur'];
        $date = $_GET['date'];
        $result = mysqli_query($con,"INSERT INTO `superpoints`.`Visits` (`userID`,`businessID`,`duration`,`date`) VALUES
            ('$userid', '$businessid', '$duration', '$date');", MYSQLI_STORE_RESULT);
        echo $result ? 'true' : 'false';
        mysqli_close($con);
    }

    function insertPromotions() {
        $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);
    
        if (mysqli_connect_errno($con)) {
            echo "Failed to connect to database: " . mysqli_connect_error();
        }
        $businessid = $_GET['bid'];
        $tierid = $_GET['tid'];
        $detail = $_GET['detail'];
        // replaces the %20s in the url string
        $new = str_replace('%20', ' ', $detail);
        $result = mysqli_query($con,"INSERT INTO `superpoints`.`Promotions` (`businessID`, `tierID`, `details`)
            VALUES ('$businessid', '$tierid', '$new');", MYSQLI_STORE_RESULT);
        echo $result ? 'true' : 'false';
        mysqli_close($con);
    }

    function selectPromotion() {
        $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);
    
        if (mysqli_connect_errno($con)) {
            echo "Failed to connect to database: " . mysqli_connect_error();
        }
        
        $promoid = $_GET['pid'];
        $result = mysqli_query($con,"SELECT * FROM superpoints.Promotions
            WHERE promotionID = '$promoid';", MYSQLI_STORE_RESULT);
        $row = mysqli_fetch_array($result);
        $promoid = $row[0];
        $businessid = $row[1];
        $tierid = $row[2];
        $details = $row[3];
        $clicks = $row[4];
        
        echo $promoid . " " . $businessid . " " . $tierid . " " . $details . " " . $clicks;
        
        mysqli_close($con);
    }

    function incrementClick() {
        $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);
    
        if (mysqli_connect_errno($con)) {
            echo "Failed to connect to database: " . mysqli_connect_error();
        }
        $promoid = $_GET['pid'];
        
        // Get number of clicks before increment
        $select = mysqli_query($con,"SELECT clicks FROM superpoints.Promotions
            WHERE promotionID = '$promoid';", MYSQLI_STORE_RESULT);
        $clickNo = mysqli_fetch_array($select);
        // increment
        $clicks = $clickNo[0] + 1;
        
        $result = mysqli_query($con,"UPDATE `superpoints`.`Promotions` SET `clicks` = '$clicks' 
            WHERE (`promotionID` = '$promoid');", MYSQLI_STORE_RESULT);
        
        echo $result ? 'true' : 'false';
        mysqli_close($con);
    }

    $func = $_GET['function'];
    switch ($func) {
        case "login":
            login();
            break;
        case "register":
            register();
            break;
        case "visit":
            insertVisits();
            break;
        case "addPromo":
            insertPromotions();
            break;
        case "promoClick":
            incrementClick();
            break;
        case "getPromo":
            selectPromotion();
            break;
    }
    
?>