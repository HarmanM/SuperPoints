<?php
    define('DB_SERVER', "superpointsdb.cuwiyoumlele.ca-central-1.rds.amazonaws.com");
    define('DB_USERNAME', "admin");
    define('DB_PASSWORD', "zxcasdqwe123");

    function getUser() {
        $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

        if (mysqli_connect_errno($con)) {
            echo "Failed to connect to database: " . mysqli_connect_error();
        }
        $where = isset($_GET['whereClause']) ? "WHERE " . $_GET['whereClause'] : '';
        $result = mysqli_query($con,"SELECT * FROM superpoints.Users $where", MYSQLI_STORE_RESULT);
        $row_count = mysqli_num_rows($result);

        if ($row_count <= 1) {
            $row = mysqli_fetch_array($result);
            $userid = $row['userID'];
            $businessid = $row['businessID'];
            $userName = $row['userName'];
            $settings = $row['settings'];
            if ($businessid != "") {
              echo $userid . " " . $businessid . " " . $userName . " " . $settings;
            } else {
              echo $userid . " " . $userName . " " . $settings;
            }
        } else {
            while($row_data = mysqli_fetch_array($result)) {
                $userid = $row_data['userID'];
                $businessid = $row_data['businessID'];
                $username = $row_data['userName'];
                $settings = $row_data['settings'];
                echo $userid . " " . $businessid . " " . $username . " " . $settings;
                echo "<br>";
            }
        }
        mysqli_close($con);
    }

    function getVisits() {
        $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

        if (mysqli_connect_errno($con)) {
            echo "Failed to connect to database: " . mysqli_connect_error();
        }
        $where = isset($_GET['whereClause']) ? "WHERE" . $_GET['whereClause'] : '';
        $result = mysqli_query($con,"SELECT * FROM superpoints.Visits $where", MYSQLI_STORE_RESULT);
        $row_count = mysqli_num_rows($result);

        if ($row_count <= 1) {
            $row = mysqli_fetch_array($result);
            $col_count = mysqli_num_fields($result);
            for ($i = 0; $i < $col_count; $i++) {
                echo $row[$i] . " ";
            }
        } else {
            while($row_data = mysqli_fetch_array($result)) {
                $visitid = $row_data['visitID'];
                $userid = $row_data['userID'];
                $businessid = $row_data['businessID'];
                $duration = $row_data['duration'];
                $date = $row_data['date'];
                echo $visitid . " " . $userid . " " . $businessid . " " . $duration . " " . $date;
                echo "<br>";
            }
        }
        mysqli_close($con);
    }

    function getPromotion() {
        $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

        if (mysqli_connect_errno($con)) {
            echo "Failed to connect to database: " . mysqli_connect_error();
        }
        $where = isset($_GET['whereClause']) ? "WHERE" . $_GET['whereClause'] : '';
        $result = mysqli_query($con,"SELECT * FROM superpoints.Promotions $where", MYSQLI_STORE_RESULT);
        $row_count = mysqli_num_rows($result);

        if ($row_count <= 1) {
            $row = mysqli_fetch_array($result);
            $col_count = mysqli_num_fields($result);
            for ($i = 0; $i < $col_count; $i++) {
                echo $row[$i] . " ";
            }
        } else {
            while($row_data = mysqli_fetch_array($result)) {
                $visitid = $row_data['promotionID'];
                $businessid = $row_data['businessID'];
                $tiersid = $row_data['tierID'];
                $details = $row_data['details'];
                $clicks = $row_data['clicks'];
                echo $visitid . " " . $businessid . " " . $tiersid . " " . $details . " " . $clicks;
                echo "<br>";
            }
        }
        mysqli_close($con);
    }

    // NOTE: When updating one specific column, please pass back in the previous values for the other columns
    function handleUser() {
        $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

        if (mysqli_connect_errno($con)) {
            echo "Failed to connect to database: " . mysqli_connect_error();
        }

        $userid = $_GET['USER_ID'];
        $businessid = $_GET['BUSINESS_ID'];
        $username = $_GET['USERNAME'];
        $password = $_GET['PASSWORD'];
        $setting = $_GET['SETTING'];

        if ($userid != "") {
            $result = mysqli_query($con,"INSERT INTO `superpoints`.`Users`
                (`password`,`userName`,`settings`) VALUES ('$password', '$username', '$setting');", MYSQLI_STORE_RESULT);
        } else {
            $result = mysql_query($con, "UPDATE `superpoints`.`Users` SET password = '$password', businessid = '$businessid',
              username = '$username', setting = '$setting'
              WHERE (`userID` = '$userid');", MYSQLI_STORE_RESULT);
        }
    }

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

        if ($businessid != "") {
          echo $userid . " " . $businessid . " " . $userName . " " . $settings;
        } else {
          echo $userid . " " . $userName . " " . $settings;
        }
        mysqli_close($con);
    }

    function updateSettings() {
        $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

        if (mysqli_connect_errno($con)) {
            echo "Failed to connect to database: " . mysqli_connect_error();
        }

        $userID = $_GET['uid'];
        $setting = $_GET['setting'];
        $result = mysqli_query($con,"UPDATE `superpoints`.`Users` SET `settings` = '$setting'
            WHERE (`userID` = '$userID');", MYSQLI_STORE_RESULT);
        echo $result ? 'true' : 'false';

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
        $new = str_replace(' ', '&', $detail);
        $result = mysqli_query($con,"INSERT INTO `superpoints`.`Promotions` (`businessID`, `tierID`, `details`)
            VALUES ('$businessid', '$tierid', '$new');", MYSQLI_STORE_RESULT);
        echo $result ? 'true' : 'false';
        mysqli_close($con);
    }

    function getApplicablePromotions() {
        $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

        if (mysqli_connect_errno($con)) {
            echo "Failed to connect to database: " . mysqli_connect_error();
        }
        $userID = $_GET['uid'];
        $result = mysqli_query($con,"SELECT promotionID, superpoints.Promotions.businessID, tierID, details, clicks, businessName
            FROM superpoints.Promotions INNER JOIN superpoints.Businesses ON
            superpoints.Promotions.businessID = superpoints.Businesses.businessID
            INNER JOIN superpoints.Points ON superpoints.Promotions.businessID = superpoints.Points.businessID
            WHERE (SELECT points FROM superpoints.Points WHERE userID = '$userID' AND superpoints.Points.businessID = superpoints.Promotions.businessID) >
				(SELECT minPoints FROM superpoints.Tiers WHERE tierID = superpoints.Promotions.tierID);", MYSQLI_STORE_RESULT);

        while($row_data = mysqli_fetch_array($result)) {
            $promoid = $row_data['promotionID'];
            $businessid = $row_data['businessID'];
            $tierid = $row_data['tierID'];
            $details = $row_data['details'];
            $clicks = $row_data['clicks'];
            $businessName = $row_data['businessName'];
            echo $promoid . " " . $businessid . " " . $tierid . " " . $details . " " . $clicks . " " . $businessName;
            echo "<br>";
        }
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

    function selectBusiness() {
        $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

        if (mysqli_connect_errno($con)) {
            echo "Failed to connect to database: " . mysqli_connect_error();
        }

        $lat = $_GET['lat'];
        $long = $_GET['long'];
        $result = mysqli_query($con,"SELECT * FROM superpoints.Businesses
            WHERE 6371 * acos( cos( radians('$lat') )
            * cos( radians( superpoints.Businesses.latitude ) )
            * cos( radians( superpoints.Businesses.longitude ) - radians('$long') ) + sin( radians('$lat') )
            * sin(radians(superpoints.Businesses.latitude)) ) < 1;", MYSQLI_STORE_RESULT);
        while($row_data = mysqli_fetch_array($result)) {
            $businessid = $row_data['businessID'];
            $businessname = $row_data['businessName'];
            $latitude = $row_data['latitude'];
            $longitude = $row_data['longitude'];
            echo $businessid . " " . $businessname . " " . $latitude . " " . $longitude . " ";
            echo "<br>";
        }

        mysqli_close($con);
    }

    function getTier() {
        $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

        if (mysqli_connect_errno($con)) {
            echo "Failed to connect to database: " . mysqli_connect_error();
        }

        $tierid = $_GET['tid'];
        $result = mysqli_query($con,"SELECT * FROM superpoints.Users where
        userName='$username' and password='$password'", MYSQLI_STORE_RESULT);
        $row = mysqli_fetch_array($result);
        $userid = $row[0];
        $businessid = $row[1];
        $userName = $row[3];
        $settings = $row[4];

        echo $userid . " " . $businessid . " " . $userName . " " . $settings;
    }

function calcAvgVisitsWeek () {
	$con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

	if (mysqli_connect_errno($con)) {
		echo "Failed to connect to database: " . mysqli_connect_error();
	}

    $currentYear = date('Y'); //1992
    $month = date('m');//0-12
    $daysInMonth = cal_days_in_month(CAL_GREGORIAN, $month, $currentYear); //
    $daysInPreviousMonth = cal_days_in_month(CAL_GREGORIAN, $month - 1, $currentYear);
    $currentDay = date('d'); //1-31

    $daysFromPrevMonth = 0;

    if($currentDay < 7)
    {
        $daysFromPrevMonth = 7 - $currentDay;
    }
        //01 - 31

	$result = mysqli_query($con,
    "
    SELECT
        COUNT(userID)/7
    FROM
        superpoints.Visits
    WHERE
        $daysFromPrevMonth > 0
        AND YEAR(superpoints.Visits.date) = $currentYear
        AND MONTH(superpoints.Visits.date) = $month
        AND DAY(superpoints.Visits.date) BETWEEN 1 AND $currentDay
        OR (MONTH(superpoints.Visits.date) = $month - 1 OR $month = 1 AND YEAR(superpoints.Visits.date) = $currentYear - 1 AND MONTH(superpoints.Visits.date) = 12)
            AND DAY(superpoints.Visits.date) BETWEEN $daysInPreviousMonth - $daysFromPrevMonth AND $daysInPreviousMonth
        OR YEAR(superpoints.Visits.date) = $currentYear
            AND MONTH(superpoints.Visits.date) = $month
            AND DAY(superpoints.Visits.date) BETWEEN $currentDay - 7 AND $currentDay
        ", MYSQLI_STORE_RESULT);
    $row = mysqli_fetch_array($result);
    //$count = $result[0];

    echo $row[0];
    mysqli_close($con);
    }


    function calcAvgDurationPerVisitWeek () {
	$con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

	if (mysqli_connect_errno($con)) {
		echo "Failed to connect to database: " . mysqli_connect_error();
	}

    $currentYear = date('Y'); //1992
    $month = date('m');//0-12
    $daysInMonth = cal_days_in_month(CAL_GREGORIAN, $month, $currentYear); //
    $daysInPreviousMonth = cal_days_in_month(CAL_GREGORIAN, $month - 1, $currentYear);
    $currentDay = date('d'); //1-31

    $daysFromPrevMonth = 0;

    if($currentDay < 7)
    {
        $daysFromPrevMonth = 7 - $currentDay;
    }
        //01 - 31

	$result = mysqli_query($con,
    "
    SELECT
            SUM(duration)/COUNT(visitID)
        FROM
            superpoints.Visits
        WHERE
            $daysFromPrevMonth > 0
            AND YEAR(superpoints.Visits.date) = $currentYear
            AND MONTH(superpoints.Visits.date) = $month
            AND DAY(superpoints.Visits.date) BETWEEN 1 AND $currentDay
            OR (MONTH(superpoints.Visits.date) = $month - 1 OR $month = 1 AND YEAR(superpoints.Visits.date) = $currentYear - 1 AND MONTH(superpoints.Visits.date) = 12)
                AND DAY(superpoints.Visits.date) BETWEEN $daysInPreviousMonth - $daysFromPrevMonth AND $daysInPreviousMonth
            OR YEAR(superpoints.Visits.date) = $currentYear
                AND MONTH(superpoints.Visits.date) = $month
                AND DAY(superpoints.Visits.date) BETWEEN $currentDay - 7 AND $currentDay", MYSQLI_STORE_RESULT);
    $row = mysqli_fetch_array($result);

    echo $row[0];
    mysqli_close($con);
    }

    $func = $_GET['function'];
    switch ($func) {
        case "getUser":
            getUser();
            break;
        case "login":
            login();
            break;
        case "getVisit":
            getVisits();
            break;
        case "getPromo":
            getPromotion();
            break;
        case "user":
            handleUser();
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
        case "getApplicablePromos":
            getApplicablePromotions();
            break;
        case "settings":
            updateSettings();
            break;
        case "getBusinesses":
            selectBusiness();
            break;
        case "calcAverageVisits":
            calcAvgVisitsWeek();
            break;
        case "calcAverageDuration":
            calcAvgDurationPerVisitWeek();
            break;
    }


?>
