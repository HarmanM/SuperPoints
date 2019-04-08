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
            if ($businessid != "" ) {
              if (isset($userName) && $userName != "") {
                echo $userid . "~s" . $businessid . "~s" . $userName . "~s" . $settings;
              }
            } else {
              if (isset($userName) && $userName != "") {
              echo $userid . "~s" . "-1" . "~s" . $userName . "~s" . $settings;
            }
            }
        } else {
            while($row_data = mysqli_fetch_array($result)) {
                $userid = $row_data['userID'];
                $businessid = $row_data['businessID'];
                $username = $row_data['userName'];
                $settings = $row_data['settings'];
                if ($businessid != "") {
                  if (isset($username) && $username != "") {
                  echo $userid . "~s" . $businessid . "~s" . $userName . "~s" . $settings . "~n";
                }
                } else {
                  if (isset($username) && $username != "") {
                  echo $userid . "~s" . "-1" . "~s" . $userName . "~s" . $settings . "~n";
                }
                }
            }
        }
        mysqli_close($con);
    }

    function getVisits() {
        $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

        if (mysqli_connect_errno($con)) {
            echo "Failed to connect to database: " . mysqli_connect_error();
        }
        $where = isset($_GET['whereClause']) ? "WHERE " . $_GET['whereClause'] : '';
        $result = mysqli_query($con,"SELECT * FROM superpoints.Visits $where", MYSQLI_STORE_RESULT);
        $row_count = mysqli_num_rows($result);

        if ($row_count <= 1) {
            $row = mysqli_fetch_array($result);
            $visitid = $row['visitID'];
            $userid = $row['userID'];
            $businessid = $row['businessID'];
            $duration = $row['duration'];
            $date = $row['date'];
            echo $visitid . "~s" . $userid . "~s" . $businessid . "~s" . $duration . "~s" . $date;
        } else {
            while($row_data = mysqli_fetch_array($result)) {
                $visitid = $row_data['visitID'];
                $userid = $row_data['userID'];
                $businessid = $row_data['businessID'];
                $duration = $row_data['duration'];
                $date = $row_data['date'];
                echo $visitid . "~s" . $userid . "~s" . $businessid . "~s" . $duration . "~s" . $date . "~n";
            }
        }
        mysqli_close($con);
    }

    function getPromotion() {
        $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

        if (mysqli_connect_errno($con)) {
            echo "Failed to connect to database: " . mysqli_connect_error();
        }

        $businessID = $_GET['whereClause'];
        $where = isset($_GET['whereClause']) ? "WHERE " . $_GET['whereClause'] : '';

        $result = mysqli_query($con,"SELECT promotionID, businessID, minPoints, details, clicks, businessName
            FROM (SELECT p.*, b.businessName FROM superpoints.Promotions p INNER JOIN superpoints.Businesses b ON p.businessID = b.businessID) t
            $where", MYSQLI_STORE_RESULT);

        $row_count = mysqli_num_rows($result);

        if ($row_count <= 1) {
            $row = mysqli_fetch_array($result);
            $visitid = $row['promotionID'];
            $businessid = $row['businessID'];
            $tiersid = $row['minPoints'];
            $details = $row['details'];
            $clicks = $row['clicks'];
            $businessName = $row['businessName'];

            if (isset($businessid) && $businessid != "") {
              echo $visitid . "~s" . $businessid . "~s" . $tiersid . "~s" . $details . "~s" . $clicks . "~s" . $businessName;
            }
        } else {
            while($row_data = mysqli_fetch_array($result)) {
                $visitid = $row_data['promotionID'];
                $businessid = $row_data['businessID'];
                $tiersid = $row_data['minPoints'];
                $details = $row_data['details'];
                $clicks = $row_data['clicks'];
                $businessName = $row_data['businessName'];

                if (isset($businessid) && $businessid != "") {
                  echo $visitid . "~s" . $businessid . "~s" . $tiersid . "~s" . $details . "~s" . $clicks . "~s" . $businessName . "~n";
                }
            }
        }
        mysqli_close($con);
    }

	function getPoints() {
        $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

        if (mysqli_connect_errno($con)) {
            echo "Failed to connect to database: " . mysqli_connect_error();
        }
        $where = isset($_GET['whereClause']) ? "WHERE " . $_GET['whereClause'] : '';
        $result = mysqli_query($con,"SELECT * FROM superpoints.Points $where", MYSQLI_STORE_RESULT);
        $row_count = mysqli_num_rows($result);

        if ($row_count <= 1) {
            $row = mysqli_fetch_array($result);
            $userid = $row['userID'];
            $businessid = $row['businessID'];
            $points = $row['points'];
            echo $userid . "~s" . $businessid . "~s" . $points;
        } else {
            while($row_data = mysqli_fetch_array($result)) {
				$userid = $row['userID'];
				$businessid = $row['businessID'];
				$points = $row['points'];
				echo $userid . "~s" . $businessid . "~s" . $points . "~n";
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
        $setting = $_GET['SETTING'];

        if ($businessid = 0 && $userid) {
            $businessid = "";
            $userid = "";
        }

        if ($userid == "") {
			$password = $_GET['PASSWORD'];
            $result = mysqli_query($con,"INSERT INTO `superpoints`.`Users`
                (`password`,`userName`,`settings`) VALUES ('$password', '$username', '$setting');", MYSQLI_STORE_RESULT);
            echo ($result) ? "true" : "";
        } else {
            $result = mysqli_query($con, "UPDATE `superpoints`.`Users` SET
            username = '$username', settings = $setting WHERE (`userID` = '$userid');", MYSQLI_STORE_RESULT);
            echo ($result) ? "true" : "";
        }
    }

	function updatePassword() {
        $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

        if (mysqli_connect_errno($con)) {
            echo "Failed to connect to database: " . mysqli_connect_error();
        }

        $userid = $_GET['USER_ID'];
		    $newPW = $_GET['NEW_PASSWORD'];

        $result = mysqli_query($con, "UPDATE `superpoints`.`Users` SET
            password = '$newPW' WHERE (`userID` = '$userid');", MYSQLI_STORE_RESULT);

        echo ($result) ? "true" : "";
	}

    function handleBusiness() {
      $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

      if (mysqli_connect_errno($con)) {
          echo "Failed to connect to database: " . mysqli_connect_error();
      }

      $businessid = $_GET['BUSINESS_ID'];
      $businessname = $_GET['BUSINESS_NAME'];
      $latitude = $_GET['LATITUDE'];
      $longitude = $_GET['LONGITUDE'];
      $region = $_GET['REGION'];

      if ($businessid == 0 || !isset($businessid)) {
        $businessid = "";
      }

      if ($region == 0 || !isset($region)) {
        $region = "null";
      }

      if ($businessid == "") {
          $result = mysqli_query($con,"INSERT INTO `superpoints`.`Businesses`
              (businessName, latitude, longitude, region) VALUES ('$businessname',
                '$latitude', '$longitude', '$region');", MYSQLI_STORE_RESULT);
      } else {
          $result = mysqli_query($con, "UPDATE `superpoints`.`Businesses` SET businessName = '$businessname',
            latitude = '$latitude', longitude = '$longitude', region = $region WHERE (`businessID` = '$businessid');", MYSQLI_STORE_RESULT);
      }
    }

    function handlePromotions() {
      $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

      if (mysqli_connect_errno($con)) {
          echo "Failed to connect to database: " . mysqli_connect_error();
      }

      $promotionid = $_GET['PROMOTION_ID'];
      $businessid = $_GET['BUSINESS_ID'];
      $tierid = $_GET['MIN_POINTS'];
      $details = $_GET['DETAILS'];
      $clicks = $_GET['CLICKS'];

      if ($promotionid = 0) {
        $promotionid = "";
      }

      if ($promotionid == "") {
          $result = mysqli_query($con,"INSERT INTO `superpoints`.`Promotions`
              (businessID, minPoints, details, clicks) VALUES ('$businessid', '$tierid',
                '$details', '$clicks');", MYSQLI_STORE_RESULT);
          echo $result ? "true" : "";
      } else {
          $result = mysqli_query($con, "UPDATE `superpoints`.`Promotions` SET businessid = '$businessid',
            minPoints = '$tierid', details = '$details', clicks = $clicks WHERE (`promotionID` = '$promotionid');", MYSQLI_STORE_RESULT);
            echo $result ? "true" : "";
      }
    }

    function handleVisits() {
      $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

      if (mysqli_connect_errno($con)) {
          echo "Failed to connect to database: " . mysqli_connect_error();
      }

      $visitid = $_GET['VISIT_ID'];
      $userid = $_GET['USER_ID'];
      $businessid = $_GET['BUSINESS_ID'];
      $duration = $_GET['DURATION'];
      $date = $_GET['DATE'];

      if ($visitid == 0) {
        $visitid = "";
      }

      if ($visitid == "") {
          $result = mysqli_query($con,"INSERT INTO `superpoints`.`Visits`
              (`userID`, `businessID`,`duration`, `date`) VALUES ($userid, $businessid,
                $duration, convert('$date', DATETIME));", MYSQLI_STORE_RESULT);
                echo $result ? "true" : "";
      } else {
          $result = mysqli_query($con, "UPDATE `superpoints`.`Visits` SET userid = '$userid', businessid = '$businessid',
            duration = '$duration', date = convert($date, DATETIME) WHERE (`visitID` = '$visitid');", MYSQLI_STORE_RESULT);
            echo $result ? "true" : "";
      }

	  $pointsResult = mysqli_query($con,"SELECT points FROM `superpoints`.`Points` WHERE userID = $userid AND businessID = $businessid", MYSQLI_STORE_RESULT);

	  $pointsFunc = function($minutes){
		  $optimalTime = 20;
		  $slope = 30;
      echo (pow(abs($minutes - $optimalTime), 1/3) * (($minutes - $optimalTime < 0) ? -1 : 1)) * $slope;
		  return (pow(abs($minutes - $optimalTime), 1/3) * (($minutes - $optimalTime < 0) ? -1 : 1)) * $slope;
	  };

	  $points = $pointsFunc($duration) + abs($pointsFunc(0));
	  if($pointsResult == false)
		  $pointsResult = mysqli_query($con,"INSERT INTO superpoints.Points ($businessid, $userid, $points)", MYSQLI_STORE_RESULT);
	  else
		  $pointsResult = mysqli_query($con,"UPDATE superpoints.Points SET points = points + $points WHERE businessID = $businessid AND userID = $userid", MYSQLI_STORE_RESULT);

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
          if (isset($userName) && $userName != "") {
            echo $userid . "~s" . $businessid . "~s" . $username . "~s" . $settings;
          }
        } else {
          if (isset($userName) && $userName != "") {
            echo $userid . "~s" . $username . "~s" . $settings;
          }
        }
        mysqli_close($con);
    }

    function getApplicablePromotions() {
        $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

        if (mysqli_connect_errno($con)) {
            echo "Failed to connect to database: " . mysqli_connect_error();
        }

        // Becomes WHERE userid = userid
        $where = isset($_GET['whereClause']) ? "WHERE " . $_GET['whereClause'] : '';
        //$userID = $_GET['uid'];
        $result = mysqli_query($con,"SELECT promotionID, superpoints.Promotions.businessID, minPoints, details, clicks, businessName
            FROM superpoints.Promotions INNER JOIN superpoints.Businesses ON
            superpoints.Promotions.businessID = superpoints.Businesses.businessID
            INNER JOIN superpoints.Points ON superpoints.Promotions.businessID = superpoints.Points.businessID
            WHERE (SELECT points FROM superpoints.Points $where AND superpoints.Points.businessID = superpoints.Promotions.businessID) >
				minPoints;", MYSQLI_STORE_RESULT);

        while($row_data = mysqli_fetch_array($result)) {
            $promoid = $row_data['promotionID'];
            $businessid = $row_data['businessID'];
            $tierid = $row_data['minPoints'];
            $details = $row_data['details'];
            $clicks = $row_data['clicks'];
            $businessName = $row_data['businessName'];
            echo $promoid . "~s" . $businessid . "~s" . $tierid . "~s" . $details . "~s" . $clicks . "~s" . $businessName . "~n";
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

        $where = $_GET['whereClause'];

        if (isset($where)) {
          $explode = explode(" ", $where);
        }
        $lat = substr($explode[0], 4);
        $long = substr($explode[1], 5);

        $result = mysqli_query($con, "SELECT * FROM superpoints.Businesses
            WHERE 6371 * acos( cos( radians('$lat') )
            * cos( radians( superpoints.Businesses.latitude ) )
            * cos( radians( superpoints.Businesses.longitude ) - radians('$long') ) + sin( radians('$lat') )
            * sin(radians(superpoints.Businesses.latitude)) ) < 20;", MYSQLI_STORE_RESULT);


            $row_count = mysqli_num_rows($result);

            if ($row_count <= 1) {
                $row = mysqli_fetch_array($result);
                    $businessid = $row['businessID'];
                    $businessname = $row['businessName'];
                    $latitude = $row['latitude'];
                    $longitude = $row['longitude'];
                    $region = $row['region'];

                    if (isset($businessid) && $businessid != "") {

                    echo $businessid . "~s" . $businessname . "~s" . $latitude . "~s" . $longitude . "~s" . $region;
                  }
            } else {
                while($row_data = mysqli_fetch_array($result)) {
                    $businessid = $row_data['businessID'];
                    $businessname = $row_data['businessName'];
                    $latitude = $row_data['latitude'];
                    $longitude = $row_data['longitude'];
                    $region = $row_data['region'];

                    if (isset($businessid) && $businessid != "") {

                    echo $businessid . "~s" . $businessname . "~s" . $latitude . "~s" . $longitude . "~s" . $region . "~n";
                  }
                }
            }

        mysqli_close($con);
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
    $where = $_GET['whereClause'];

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
        AND $where
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
    $where = $_GET['whereClause'];

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
                AND DAY(superpoints.Visits.date) BETWEEN $currentDay - 7 AND $currentDay
                AND $where", MYSQLI_STORE_RESULT);
    $row = mysqli_fetch_array($result);

    echo $row[0];
    mysqli_close($con);
    }

    function deletePromotion() {
          $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

          if (mysqli_connect_errno($con)) {
              echo "Failed to connect to database: " . mysqli_connect_error();
          }

          $promotionid = $_GET['PROMOTION_ID'];

          $result = mysqli_query($con,"DELETE FROM `superpoints`.`Promotions`
                                    WHERE promotionID = $promotionid;", MYSQLI_STORE_RESULT);

          echo ($result) ? "true" : "";
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
		    case "getPoints":
			     getPoints();
			     break;
        case "setUser":
            handleUser();
            break;
        case "setVisit":
            handleVisits();
            break;
        case "setPromotion":
            handlePromotions();
            break;
        case "setBusiness":
            handleBusiness();
            break;
        case "promoClick":
            incrementClick();
            break;
        case "getApplicablePromos":
            getApplicablePromotions();
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
        case "deletePromotion":
            deletePromotion();
            break;
        case "updatePassword":
            updatePassword();
            break;
    }


?>
