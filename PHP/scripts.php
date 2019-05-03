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

        $result = mysqli_query($con,"SELECT promotionID, businessID, pt.*, details, clicks, businessName, shortDescription
            FROM (SELECT p.*, b.businessName FROM superpoints.Promotions p
			INNER JOIN superpoints.Businesses b ON p.businessID = b.businessID) t
			INNER JOIN superpoints.PointTiers pt ON t.minTierID = pt.tierID
            $where", MYSQLI_STORE_RESULT);

        $row_count = mysqli_num_rows($result);

        if ($row_count <= 1) {
            $row = mysqli_fetch_array($result);
            $visitid = $row['promotionID'];
            $businessid = $row['businessID'];
            $tierID = $row['tierID'];
            $minPoints = $row['minPoints'];
            $name = $row['name'];
            $details = $row['details'];
            $clicks = $row['clicks'];
            $businessName = $row['businessName'];
            $shortDescription = $row['shortDescription'];

            if (isset($businessid) && $businessid != "") {
              echo $visitid . "~s" . $businessid . "~s" . $tierID . "~s" . $minPoints . "~s" . $name . "~s" . $details . "~s" . $clicks . "~s" . $businessName . "~s" . $shortDescription;
            }
        } else {
            while($row_data = mysqli_fetch_array($result)) {
                $visitid = $row_data['promotionID'];
                $businessid = $row_data['businessID'];
				        $tierID = $row_data['tierID'];
				        $minPoints = $row_data['minPoints'];
				        $name = $row_data['name'];
                $details = $row_data['details'];
                $clicks = $row_data['clicks'];
                $businessName = $row_data['businessName'];
                $shortDescription = $row_data['shortDescription'];

                if (isset($businessid) && $businessid != "") {
                  echo $visitid . "~s" . $businessid . "~s" . $tierID . "~s" . $minPoints . "~s" . $name . "~s" . $details . "~s" . $clicks . "~s" . $businessName . "~s" . $shortDescription . "~n";
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
        $result = mysqli_query($con,"SELECT *, (SELECT name FROM superpoints.PointTiers WHERE minPoints <= p.points ORDER BY minPoints DESC LIMIT 1) AS tier FROM superpoints.Points p $where", MYSQLI_STORE_RESULT);
        $row_count = mysqli_num_rows($result);

        if ($row_count <= 1) {
            $row = mysqli_fetch_array($result);
            $userid = $row['userID'];
            $businessid = $row['businessID'];
            $points = $row['points'];
			$tier = $row['tier'];
            if(isset($userid) && isset($businessid) && isset($points) && isset($tier))
            {
                echo $userid . "~s" . $businessid . "~s" . $points . "~s" . $tier . "~n";
            }
            else
            {
                echo "";
            }
        } else {
            while($row_data = mysqli_fetch_array($result)) {
				$userid = $row_data['userID'];
				$businessid = $row_data['businessID'];
				$points = $row_data['points'];
			$tier = $row_data['tier'];
                if(isset($userid) && isset($businessid) && isset($points) && isset($tier))
                {
                    echo $userid . "~s" . $businessid . "~s" . $points . "~s" . $tier . "~n";
                }
                else
                {
                    echo "";
                }
            }
        }
        mysqli_close($con);
    }

	function getTiers() {
        $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

        if (mysqli_connect_errno($con)) {
            echo "Failed to connect to database: " . mysqli_connect_error();
        }
        $where = isset($_GET['whereClause']) ? "WHERE " . $_GET['whereClause'] : '';
        $result = mysqli_query($con,"SELECT * FROM superpoints.PointTiers $where", MYSQLI_STORE_RESULT);
        $row_count = mysqli_num_rows($result);

        if ($row_count <= 1) {
            $row = mysqli_fetch_array($result);
            $tierID = $row['tierID'];
            $minPoints = $row['minPoints'];
            $name = $row['name'];
            if(isset($tierID) && isset($minPoints) && isset($name))
            {
                echo $tierID . "~s" . $minPoints . "~s" . $name . "~n";
            }
            else
            {
                echo "";
            }
        } else {
            while($row_data = mysqli_fetch_array($result)) {
				$tierID = $row_data['tierID'];
				$minPoints = $row_data['minPoints'];
				$name = $row_data['name'];
                if(isset($tierID) && isset($minPoints) && isset($name))
                {
                    echo $tierID . "~s" . $minPoints . "~s" . $name . "~n";
                }
                else
                {
                    echo "";
                }
            }
        }
        mysqli_close($con);
    }

    function getBusinesses() {
      $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

      if (mysqli_connect_errno($con)) {
          echo "Failed to connect to database: " . mysqli_connect_error();
      }
      $where = isset($_GET['whereClause']) ? "WHERE " . $_GET['whereClause'] : '';
      $result = mysqli_query($con,"SELECT * FROM superpoints.Businesses $where", MYSQLI_STORE_RESULT);
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

    function getBusinessSettings() {
      $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

      if (mysqli_connect_errno($con)) {
          echo "Failed to connect to database: " . mysqli_connect_error();
      }
      $where = isset($_GET['whereClause']) ? "WHERE " . $_GET['whereClause'] : '';
      $result = mysqli_query($con,"SELECT * FROM (
        SELECT bs.businessID, s.*, bs.value
	       FROM `superpoints`.`BusinessSettings` bs
		       JOIN `superpoints`.`Settings` s ON bs.settingID = s.settingID) t $where", MYSQLI_STORE_RESULT);
      $row_count = mysqli_num_rows($result);

      if ($row_count <= 1) {
          $row = mysqli_fetch_array($result);
          $businessid = $row['businessID'];
          $settingid = $row['settingID'];
          $settingname = $row['settingName'];
          $valuetype = $row['valueType'];
          $value = $row['value'];

          if (isset($businessid) && $businessid != "") {

          echo $businessid . "~s" . $settingid . "~s" . $settingname . "~s" . $valuetype . "~s" . $value;
        }
      } else {
        while($row_data = mysqli_fetch_array($result)) {
            $businessid = $row_data['businessID'];
            $settingid = $row_data['settingID'];
            $settingname = $row_data['settingName'];
            $valuetype = $row_data['valueType'];
            $value = $row_data['value'];

            if (isset($businessid) && $businessid != "") {

            echo $businessid . "~s" . $settingid . "~s" . $settingname . "~s" . $valuetype . "~s" . $value . "~n";
          }
        }
      }
      mysqli_close($con);
    }

    function getUserSettings() {
      $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

      if (mysqli_connect_errno($con)) {
          echo "Failed to connect to database: " . mysqli_connect_error();
      }
      $where = isset($_GET['whereClause']) ? "WHERE " . $_GET['whereClause'] : '';
      $result = mysqli_query($con,"SELECT * FROM ( SELECT us.userID, s.*, us.value
	FROM `superpoints`.`UserSettings` us
		JOIN `superpoints`.`Settings` s ON us.settingID = s.settingID) t $where", MYSQLI_STORE_RESULT);
      $row_count = mysqli_num_rows($result);

      if ($row_count <= 1) {
          $row = mysqli_fetch_array($result);
          $userid = $row['userID'];
          $settingid = $row['settingID'];
          $settingname = $row['settingName'];
          $valuetype = $row['valueType'];
          $value = $row['value'];

          if (isset($userid) && $userid != "") {

          echo $userid . "~s" . $settingid . "~s" .  $settingname . "~s" . $valuetype . "~s" . $value;
        }
      } else {
        while($row_data = mysqli_fetch_array($result)) {
            $userid = $row_data['userID'];
            $settingid = $row_data['settingID'];
            $settingname = $row_data['settingName'];
            $valuetype = $row_data['valueType'];
            $value = $row_data['value'];

            if (isset($userid) && $userid != "") {

            echo $userid . "~s" . $settingid  . "~s" . $settingname . "~s" . $valuetype . "~s" . $value . "~n";
          }
        }
      }
      mysqli_close($con);
    }

    function getSettings() {
      $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

      if (mysqli_connect_errno($con)) {
          echo "Failed to connect to database: " . mysqli_connect_error();
      }
      $where = isset($_GET['whereClause']) ? "WHERE " . $_GET['whereClause'] : '';
      $result = mysqli_query($con,"SELECT * FROM superpoints.Settings $where", MYSQLI_STORE_RESULT);
      $row_count = mysqli_num_rows($result);

      if ($row_count <= 1) {
          $row = mysqli_fetch_array($result);
          $settingid = $row['settingID'];
          $settingname = $row['settingName'];
          $valuetype = $row['valueType'];

          if (isset($businessid) && $businessid != "") {

          echo $settingid . "~s" .  $settingname . "~s" . $valuetype;
        }
      } else {
        while($row_data = mysqli_fetch_array($result)) {
        $settingid = $row_data['settingID'];
        $settingname = $row_data['settingName'];
        $valuetype = $row_data['valueType'];

            if (isset($businessid) && $businessid != "") {

            echo $settingid . "~s" .  $settingname . "~s" . $valuetype . "~n";
          }
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

        if ($businessid == -1) {
            $businessid = "";
        }

        if ($userid == -1) {
          $userid = "";
        }

        if ($userid == "") {
			       $password = $_GET['PASSWORD'];

             if ($businessid != "") {
               $result = mysqli_query($con,"INSERT INTO `superpoints`.`Users`
                   (`businessID`,`password`,`userName`,`settings`) VALUES ($businessid, '$password', '$username', '$setting');", MYSQLI_STORE_RESULT);
               $result = $result ? "true" : "";

               if ($result == "true") {
                 $result2 = mysqli_query($con, "SELECT * FROM `superpoints`.`Users` WHERE userName = '$username' AND password = '$password'", MYSQLI_STORE_RESULT);
                 $row = mysqli_fetch_array($result2);
                 echo $row[0];
            }
          } else {
            $result = mysqli_query($con,"INSERT INTO `superpoints`.`Users`
                 (`password`,`userName`,`settings`) VALUES ('$password', '$username', '$setting');", MYSQLI_STORE_RESULT);
                $result = $result ? "true" : "";

            if ($result == "true") {
              $result2 = mysqli_query($con, "SELECT * FROM `superpoints`.`Users` WHERE userName = '$username' AND password = '$password'", MYSQLI_STORE_RESULT);
              $row = mysqli_fetch_array($result2);
              echo $row[0];
          }
        }
      } else {
            $result = mysqli_query($con, "UPDATE `superpoints`.`Users` SET
            username = '$username', settings = $setting WHERE (`userID` = '$userid');", MYSQLI_STORE_RESULT);
            echo ($result) ? "$userid" : "";
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

      if ($businessid == -1 || !isset($businessid)) {
        $businessid = "";
      }

        $region = "null";


      if ($businessid == "") {
          $result = mysqli_query($con,"INSERT INTO `superpoints`.`Businesses`
              (businessName, latitude, longitude, region) VALUES ('$businessname',
                '$latitude', '$longitude', '$region');", MYSQLI_STORE_RESULT);
            $result = $result ? "true" : "";

            if ($result == "true") {
              $result2 = mysqli_query($con, "SELECT businessID FROM `superpoints`.`Businesses` ORDER BY businessID DESC LIMIT 1");
              $row = mysqli_fetch_array($result2);
              echo $row[0];
            }
      } else {
          $result = mysqli_query($con, "UPDATE `superpoints`.`Businesses` SET businessName = '$businessname',
            latitude = '$latitude', longitude = '$longitude', region = $region WHERE (`businessID` = '$businessid');", MYSQLI_STORE_RESULT);

        echo ($result) ? "$businessid" : "";
      }
    }

    function handleBusinessSettings() {
      $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

      if (mysqli_connect_errno($con)) {
          echo "Failed to connect to database: " . mysqli_connect_error();
      }

      $businessid = $_GET['BUSINESS_ID'];
      $settingid = $_GET['SETTING_ID'];
      $value = $_GET['VALUE'];

      if ($businessid == -1 || !isset($businessid)) {
        $businessid = "";
      }

      if ($businessid == "") {
          $result = mysqli_query($con,"INSERT INTO `superpoints`.`BusinessSettings`
              (businessID, settingID, value) VALUES ('$businessid',
                '$settingid', '$value');", MYSQLI_STORE_RESULT);
            $result = $result ? "true" : "";

            if ($result == "true") {
              $result2 = mysqli_query($con, "SELECT businessID FROM `superpoints`.`BusinessSettings` ORDER BY businessID DESC LIMIT 1");
              $row = mysqli_fetch_array($result2);
              echo $row[0];
            }
      } else {
          $result = mysqli_query($con, "UPDATE `superpoints`.`BusinessSettings` SET businessID = '$businessid',
            settingid = '$settingid', value = '$value' WHERE (`businessID` = '$businessid' AND `settingID = $settingid`);", MYSQLI_STORE_RESULT);

        echo ($result) ? "$businessid" : "";
      }
    }

    function handleUserSettings() {
      $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

      if (mysqli_connect_errno($con)) {
          echo "Failed to connect to database: " . mysqli_connect_error();
      }

      $usersid = $_GET['USER_ID'];
      $settingid = $_GET['SETTING_ID'];
      $value = $_GET['VALUE'];

      if ($usersid == -1 || !isset($usersid)) {
        $usersid = "";
      }

      if ($usersid == "") {
          $result = mysqli_query($con,"INSERT INTO `superpoints`.`UserSettings`
              (userID, settingID, value) VALUES ('$usersid',
                '$settingid', '$value');", MYSQLI_STORE_RESULT);
            $result = $result ? "true" : "";

            if ($result == "true") {
              $result2 = mysqli_query($con, "SELECT userID FROM `superpoints`.`UserSettings` ORDER BY businessID DESC LIMIT 1");
              $row = mysqli_fetch_array($result2);
              echo $row[0];
            }
      } else {
          $result = mysqli_query($con, "UPDATE `superpoints`.`UserSettings` SET userID = '$usersid',
            settingid = '$settingid', value = '$value' WHERE (`userID` = '$usersid' AND `settingID = $settingid`);", MYSQLI_STORE_RESULT);

        echo ($result) ? "$usersid" : "";
      }
    }

    function handlePromotions() {
      $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

      if (mysqli_connect_errno($con)) {
          echo "Failed to connect to database: " . mysqli_connect_error();
      }

      $promotionid = $_GET['PROMOTION_ID'];
      $businessid = $_GET['BUSINESS_ID'];
      $tierid = $_GET['MIN_TIER'];
      $details = $_GET['DETAILS'];
      $clicks = $_GET['CLICKS'];
      $shortDescription = $_GET['SHORT_DESCRIPTION'];

      if ($promotionid == -1) {
        $promotionid = "";
      }


      if ($promotionid == "") {
          $result = mysqli_query($con,"INSERT INTO `superpoints`.`Promotions`
              (businessID, minTierID, details, clicks, shortDescription) VALUES ('$businessid', '$tierid',
                '$details', '$clicks', '$shortDescription');", MYSQLI_STORE_RESULT);

          $result = $result ? "true" : "";

          if ($result == "true") {
            $result2 = mysqli_query($con, "SELECT promotionID FROM `superpoints`.`Promotions` ORDER BY promotionID DESC LIMIT 1");
            $row = mysqli_fetch_array($result2);
            echo $row[0];
          }
      } else {
          $result = mysqli_query($con, "UPDATE `superpoints`.`Promotions` SET businessid = '$businessid',
            minPoints = '$tierid', details = '$details', clicks = $clicks, shortDescription = '$shortDescription' WHERE (`promotionID` = '$promotionid');", MYSQLI_STORE_RESULT);
            echo $result ? "$promotionid" : "";
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

      if ($visitid == -1) {
        $visitid = "";
      }

      if ($visitid == "") {
          $result = mysqli_query($con,"INSERT INTO `superpoints`.`Visits`
              (`userID`, `businessID`,`duration`, `date`) VALUES ($userid, $businessid,
                $duration, convert('$date', DATETIME));", MYSQLI_STORE_RESULT);

                $result = $result ? "true" : "";

                if ($result == "true") {
                  $result2 = mysqli_query($con, "SELECT visitID FROM `superpoints`.`Visits` ORDER BY visitID DESC LIMIT 1");
                  $row = mysqli_fetch_array($result2);
                  echo $row[0];
                }
      } else {
          $result = mysqli_query($con, "UPDATE `superpoints`.`Visits` SET userid = '$userid', businessid = '$businessid',
            duration = '$duration', date = convert($date, DATETIME) WHERE (`visitID` = '$visitid');", MYSQLI_STORE_RESULT);
            echo $result ? "$visitid" : "";
      }

	  $pointsResult = mysqli_query($con,"SELECT points FROM `superpoints`.`Points` WHERE userID = $userid AND businessID = $businessid", MYSQLI_STORE_RESULT);

	  $pointsFunc = function($minutes){
		  $optimalTime = 20;
		  $slope = 30;
		  return (pow(abs($minutes - $optimalTime), 1/3) * (($minutes - $optimalTime < 0) ? -1 : 1)) * $slope;
	  };

    $row_count = mysqli_num_rows($pointsResult);
	  $points = $pointsFunc($duration) + abs($pointsFunc(0));
	  if($row_count == 0) {
		  $pointsResult = mysqli_query($con,"INSERT INTO `superpoints`.`Points` (`businessID`, `userID`, `points`) VALUES ($businessid, $userid, $points);", MYSQLI_STORE_RESULT);
	  } else{
		  $pointsResult = mysqli_query($con,"UPDATE `superpoints`.`Points` SET points = points + $points WHERE businessID = $businessid AND userID = $userid", MYSQLI_STORE_RESULT);
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
        $result = mysqli_query($con,"SELECT DISTINCT promotionID, superpoints.Promotions.businessID, superpoints.PointTiers.tierID, superpoints.PointTiers.minPoints, superpoints.PointTiers.name, details, clicks, businessName, shortDescription
            FROM superpoints.Promotions INNER JOIN superpoints.Businesses ON
				superpoints.Promotions.businessID = superpoints.Businesses.businessID
            INNER JOIN superpoints.Points ON superpoints.Promotions.businessID = superpoints.Points.businessID
            INNER JOIN superpoints.PointTiers ON superpoints.Promotions.minTierID = superpoints.PointTiers.tierID
            WHERE (SELECT points FROM superpoints.Points $where AND superpoints.Points.businessID = superpoints.Promotions.businessID) >
				(SELECT minPoints FROM superpoints.PointTiers WHERE superpoints.Promotions.minTierID = superpoints.PointTiers.tierID);", MYSQLI_STORE_RESULT);

        while($row_data = mysqli_fetch_array($result)) {
            $visitid = $row_data['promotionID'];
            $businessid = $row_data['businessID'];
            $tierID = $row_data['tierID'];
            $minPoints = $row_data['minPoints'];
            $name = $row_data['name'];
            $details = $row_data['details'];
            $clicks = $row_data['clicks'];
            $businessName = $row_data['businessName'];
            $shortDescription = $row_data['shortDescription'];

            if (isset($businessid) && $businessid != "") {
              echo $visitid . "~s" . $businessid . "~s" . $tierID . "~s" . $minPoints . "~s" . $name . "~s" . $details . "~s" . $clicks . "~s" . $businessName . "~s" . $shortDescription . "~n";
            }
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

    function selectPreferredBusinesses() {
        $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

        if (mysqli_connect_errno($con)) {
            echo "Failed to connect to database: " . mysqli_connect_error();
        }

        $where = $_GET['whereClause'];
        $where = isset($_GET['whereClause']) ? "WHERE " . $_GET['whereClause'] : '';
        $result = mysqli_query($con,"SELECT * FROM superpoints.Businesses $where", MYSQLI_STORE_RESULT);

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

    function handlePreferredBusinesses() {
      $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

      if (mysqli_connect_errno($con)) {
          echo "Failed to connect to database: " . mysqli_connect_error();
      }

      $userid = $_GET['USER_ID'];
      $businessid = $_GET['BUSINESS_ID'];

      $result = mysqli_query($con,"INSERT INTO `superpoints`.`PreferredBusinesses`
          (userID, businessID) VALUES ($userid, $businessid);", MYSQLI_STORE_RESULT);

      echo ($result) ? "true" : "";

    }


    function deletePreferredBusiness() {
      $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

      if (mysqli_connect_errno($con)) {
          echo "Failed to connect to database: " . mysqli_connect_error();
      }

      $userid = $_GET['USER_ID'];
      $businessid = $_GET['BUSINESS_ID'];

      $result = mysqli_query($con,"DELETE FROM `superpoints`.`PreferredBusinesses`
                                WHERE userID = $userid AND businessID = $businessid;", MYSQLI_STORE_RESULT);

      echo ($result) ? "true" : "";
    }

    function calcMonthlyVisits(){
      $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

      if (mysqli_connect_errno($con)) {
          echo "Failed to connect to database: " . mysqli_connect_error();
      }

      $business = $_GET['businessID'];
      $result = mysqli_query($con,"
    SELECT COUNT(userID), CONCAT(YEAR(date), '-', RIGHT(CONCAT('00', MONTH(date)), 2)) as timeSpan
    FROM Visits
    WHERE BusinessID = $business
      AND (MONTH(CURDATE()) + YEAR(CURDATE()) * 12) - (MONTH(date) + YEAR(date) * 12) < 12
    GROUP BY timeSpan
    ", MYSQLI_STORE_RESULT);


  while($row_data = mysqli_fetch_array($result)) {
          $timeSpan = $row_data['timeSpan'];
          $numVisits = $row_data['numVisits'];
          if (isset($businessid) && $businessid != "")
      echo $timeSpan . "~s" . $numVisits . "~n";
      }
}

function calcNewOldUsers(){
      $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

      if (mysqli_connect_errno($con)) {
          echo "Failed to connect to database: " . mysqli_connect_error();
      }

      $business = $_GET['businessID'];
      $result = mysqli_query($con,"
    SELECT COUNT(userID) AS numUsers, 'old' as newOld
    FROM (
      SELECT DISTINCT userID
      FROM Visits
      WHERE userID IN (SELECT userID FROM Visits WHERE MONTH(date) != MONTH(CURDATE()) OR YEAR(date) != YEAR(CURDATE()))
        AND MONTH(date) = MONTH(CURDATE()) AND YEAR(date) = YEAR(CURDATE())
        AND businessID = $business) oldUser
    UNION
    SELECT COUNT(userID), 'new'
    FROM (
      SELECT DISTINCT userID
      FROM Visits
      WHERE userID NOT IN (SELECT userID FROM Visits WHERE MONTH(date) != MONTH(CURDATE()) OR YEAR(date) != YEAR(CURDATE()))
        AND MONTH(date) = MONTH(CURDATE()) AND YEAR(date) = YEAR(CURDATE())
        AND businessID = $business) newUsers
    ", MYSQLI_STORE_RESULT);


  while($row_data = mysqli_fetch_array($result)) {
          $numUsers = $row_data['numUsers'];
          $newOld = $row_data['newOld'];
          if (isset($businessid) && $businessid != "")
      echo $newOld . "~s" . $numUsers . "~n";
      }
}

function calcVisitorsPerTier(){
      $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

      if (mysqli_connect_errno($con)) {
          echo "Failed to connect to database: " . mysqli_connect_error();
      }

      $business = $_GET['businessID'];
      $result = mysqli_query($con,"
    SELECT COUNT(tierID) AS numVisits, tierID
    FROM (
      SELECT v.userID, v.businessID, (SELECT tierID FROM PointTiers WHERE minPoints < p.points ORDER BY minPoints DESC LIMIT 1) AS tierID
      FROM Visits v
        JOIN Points p ON v.userID = p.userID AND v.businessID = p.businessID
      WHERE MONTH(date) = MONTH(CURDATE()) AND YEAR(date) = YEAR(CURDATE())
        AND v.businessID = $business
      GROUP BY v.userID, v.businessID) t
    GROUP BY tierID
    ", MYSQLI_STORE_RESULT);


  while($row_data = mysqli_fetch_array($result)) {
          $numVisits = $row_data['numVisits'];
          $tierID = $row_data['tierID'];
          if (isset($businessid) && $businessid != "")
      echo $numVisits . "~s" . $tierID . "~n";
      }
}

    $func = $_GET['function'];
    switch ($func) {
        case "getUser":
            getUser();
            break;
        case "getBusiness":
            getBusinesses();
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
		case "getTiers":
		  getTiers();
		  break;
      case "getBusinessSettings":
        getBusinessSettings();
        break;
        case "getUserSettings":
          getUserSettings();
          break;
          case "getSettings":
            getSettings();
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
        case "setBusinessSettings":
            handleBusinessSettings();
            break;
        case "setUserSettings":
            handleUserSettings();
            break;
        case "promoClick":
            incrementClick();
            break;
        case "getApplicablePromos":
            getApplicablePromotions();
            break;
        case "nearbyBusinesses":
            selectBusiness();
            break;
        case "calcAverageVisits":
            calcAvgVisitsWeek();
            break;
        case "calcAverageDuration":
            calcAvgDurationPerVisitWeek();
            break;
        case "calcMonthlyVisits":
            calcMonthlyVisits();
            break;
        case "calcNewOldUsers":
            calcNewOldUsers();
            break;
        case "calcVisitorsPerTier":
            calcVisitorsPerTier();
            break;
        case "deletePromotion":
            deletePromotion();
            break;
        case "updatePassword":
            updatePassword();
            break;
        case "setPreferredBusiness":
            handlePreferredBusinesses();
            break;
        case "deletePreferredBusiness":
            deletePreferredBusiness();
            break;
        case "getPreferredBusinesses":
            selectPreferredBusinesses();
            break;
    }
?>
