<?php

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

require 'PHPMailer/PHPMailer/src/Exception.php';
require 'PHPMailer/PHPMailer/src/PHPMailer.php';
require 'PHPMailer/PHPMailer/src/SMTP.php';

define('DB_SERVER', "superpointsdb.cuwiyoumlele.ca-central-1.rds.amazonaws.com");
define('DB_USERNAME', "admin");
define('DB_PASSWORD', "zxcasdqwe123");

function getUser()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }
    $where     = isset($_GET['whereClause']) ? "WHERE " . $_GET['whereClause'] : '';
    $result    = mysqli_query($con, "SELECT * FROM superpoints.Users $where", MYSQLI_STORE_RESULT);
    $row_count = mysqli_num_rows($result);

    if ($row_count <= 1) {
        $row        = mysqli_fetch_array($result);
        $userid     = $row['userID'];
        $businessid = $row['businessID'];
        $userName   = $row['userName'];
        if ($businessid != "") {
            if (isset($userName) && $userName != "") {
                echo $userid . "~s" . $businessid . "~s" . $userName;
            }
        } else {
            if (isset($userName) && $userName != "") {
                echo $userid . "~s" . "-1" . "~s" . $userName;
            }
        }
    } else {
        while ($row_data = mysqli_fetch_array($result)) {
            $userid     = $row_data['userID'];
            $businessid = $row_data['businessID'];
            $username   = $row_data['userName'];
            if ($businessid != "") {
                if (isset($username) && $username != "") {
                    echo $userid . "~s" . $businessid . "~s" . $username . "~n";
                }
            } else {
                if (isset($username) && $username != "") {
                    echo $userid . "~s" . "-1" . "~s" . $username . "~n";
                }
            }
        }
    }
    mysqli_close($con);
}

function getVisits()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }
    $where     = isset($_GET['whereClause']) ? "WHERE " . $_GET['whereClause'] : '';
    $result    = mysqli_query($con, "SELECT * FROM superpoints.Visits $where", MYSQLI_STORE_RESULT);
    $row_count = mysqli_num_rows($result);

    if ($row_count <= 1) {
        $row        = mysqli_fetch_array($result);
        $visitid    = $row['visitID'];
        $userid     = $row['userID'];
        $businessid = $row['businessID'];
        $duration   = $row['duration'];
        $date       = $row['date'];
        echo $visitid . "~s" . $userid . "~s" . $businessid . "~s" . $duration . "~s" . $date;
    } else {
        while ($row_data = mysqli_fetch_array($result)) {
            $visitid    = $row_data['visitID'];
            $userid     = $row_data['userID'];
            $businessid = $row_data['businessID'];
            $duration   = $row_data['duration'];
            $date       = $row_data['date'];
            echo $visitid . "~s" . $userid . "~s" . $businessid . "~s" . $duration . "~s" . $date . "~n";
        }
    }
    mysqli_close($con);
}

function getPromotion()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $businessID = $_GET['whereClause'];
    $where      = isset($_GET['whereClause']) ? "WHERE " . $_GET['whereClause'] : '';

    $result = mysqli_query($con, "SELECT * FROM
	(SELECT
		p.promotionID, p.businessID, pts.*, details, clicks, businessName, shortDescription,
		CONCAT('{', GROUP_CONCAT(
			DISTINCT CONCAT(t.tagId,',',t.businessID,',',t.tagName)
			SEPARATOR '},{'
			),
		'}') AS 'Tags'
	FROM
		(SELECT ps.*, b.businessName FROM superpoints.Promotions ps
			INNER JOIN superpoints.Businesses b ON ps.businessID = b.businessID) p
		INNER JOIN superpoints.PointTiers pts ON pts.tierID = p.minTierID
		LEFT JOIN superpoints.PromotionTags pt ON p.promotionID = pt.promotionID
		LEFT JOIN superpoints.Tags t ON t.tagID = pt.tagID
	GROUP BY p.promotionID) t
  $where", MYSQLI_STORE_RESULT);

    $row_count = mysqli_num_rows($result);

    if ($row_count <= 1) {
        $row              = mysqli_fetch_array($result);
        $promotionid          = $row['promotionID'];
        $businessid       = $row['regBusinessID'];
        $tierID           = $row['tierID'];
        $minPoints        = $row['minPoints'];
        $name             = $row['name'];
        $details          = $row['details'];
        $clicks           = $row['clicks'];
        $businessName     = $row['businessName'];
        $shortDescription = $row['shortDescription'];
        $tags = $row['Tags'];

        if (isset($businessid) && $businessid != "") {
            echo $promotionid . "~s" . $businessid . "~s" . $tierID . "~s" . $minPoints . "~s" . $name . "~s" . $details . "~s" . $clicks . "~s" . $businessName . "~s" . $shortDescription . "~s" . $tags;
        }
    } else {
        while ($row_data = mysqli_fetch_array($result)) {
            $promotionid      = $row_data['promotionID'];
            $businessid       = $row_data['businessID'];
            $tierID           = $row_data['tierID'];
            $minPoints        = $row_data['minPoints'];
            $name             = $row_data['name'];
            $details          = $row_data['details'];
            $clicks           = $row_data['clicks'];
            $businessName     = $row_data['businessName'];
            $shortDescription = $row_data['shortDescription'];
            $tags = $row_data['Tags'];

            if (isset($businessid) && $businessid != "") {
                echo $promotionid . "~s" . $businessid . "~s" . $tierID . "~s" . $minPoints . "~s" . $name . "~s" . $details . "~s" . $clicks . "~s" . $businessName . "~s" . $shortDescription . "~s" . $tags . "~n";
            }
        }
    }
    mysqli_close($con);
}

function getPoints()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }
    $where     = isset($_GET['whereClause']) ? "WHERE " . $_GET['whereClause'] : '';
    $result    = mysqli_query($con, "SELECT *, (SELECT name FROM superpoints.PointTiers WHERE minPoints <= p.points ORDER BY minPoints DESC LIMIT 1) AS tier FROM superpoints.Points p $where", MYSQLI_STORE_RESULT);
    $row_count = mysqli_num_rows($result);

    if ($row_count <= 1) {
        $row        = mysqli_fetch_array($result);
        $userid     = $row['userID'];
        $businessid = $row['businessID'];
        $points     = $row['points'];
        $tier       = $row['tier'];
        if (isset($userid) && isset($businessid) && isset($points) && isset($tier)) {
            echo $userid . "~s" . $businessid . "~s" . $points . "~s" . $tier . "~n";
        } else {
            echo "";
        }
    } else {
        while ($row_data = mysqli_fetch_array($result)) {
            $userid     = $row_data['userID'];
            $businessid = $row_data['businessID'];
            $points     = $row_data['points'];
            $tier       = $row_data['tier'];
            if (isset($userid) && isset($businessid) && isset($points) && isset($tier)) {
                echo $userid . "~s" . $businessid . "~s" . $points . "~s" . $tier . "~n";
            } else {
                echo "";
            }
        }
    }
    mysqli_close($con);
}

function getTiers()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }
    $where     = isset($_GET['whereClause']) ? "WHERE " . $_GET['whereClause'] : '';
    $result    = mysqli_query($con, "SELECT * FROM superpoints.PointTiers $where", MYSQLI_STORE_RESULT);
    $row_count = mysqli_num_rows($result);

    if ($row_count <= 1) {
        $row       = mysqli_fetch_array($result);
        $tierID    = $row['tierID'];
        $minPoints = $row['minPoints'];
        $name      = $row['name'];
        if (isset($tierID) && isset($minPoints) && isset($name)) {
            echo $tierID . "~s" . $minPoints . "~s" . $name . "~n";
        } else {
            echo "";
        }
    } else {
        while ($row_data = mysqli_fetch_array($result)) {
            $tierID    = $row_data['tierID'];
            $minPoints = $row_data['minPoints'];
            $name      = $row_data['name'];
            if (isset($tierID) && isset($minPoints) && isset($name)) {
                echo $tierID . "~s" . $minPoints . "~s" . $name . "~n";
            } else {
                echo "";
            }
        }
    }
    mysqli_close($con);
}

function getBusinesses()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }
    $where     = isset($_GET['whereClause']) ? "WHERE " . $_GET['whereClause'] : '';
    $result    = mysqli_query($con, "SELECT * FROM superpoints.Businesses $where", MYSQLI_STORE_RESULT);
    $row_count = mysqli_num_rows($result);


    if ($row_count <= 1) {
        $row          = mysqli_fetch_array($result);
        $businessid   = $row['businessID'];
        $businessname = $row['businessName'];
        $latitude     = $row['latitude'];
        $longitude    = $row['longitude'];
        $region       = $row['region'];

        if (isset($businessid) && $businessid != "") {

            echo $businessid . "~s" . $businessname . "~s" . $latitude . "~s" . $longitude . "~s" . $region;
        }
    } else {
        while ($row_data = mysqli_fetch_array($result)) {
            $businessid   = $row_data['businessID'];
            $businessname = $row_data['businessName'];
            $latitude     = $row_data['latitude'];
            $longitude    = $row_data['longitude'];
            $region       = $row_data['region'];

            if (isset($businessid) && $businessid != "") {

                echo $businessid . "~s" . $businessname . "~s" . $latitude . "~s" . $longitude . "~s" . $region . "~n";
            }
        }
    }
    mysqli_close($con);
}

function getBusinessSettings()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }
    $where     = isset($_GET['whereClause']) ? "WHERE " . $_GET['whereClause'] : '';
    $result    = mysqli_query($con, "SELECT * FROM (
        SELECT bs.businessID, s.*, bs.value
           FROM `superpoints`.`BusinessSettings` bs
               JOIN `superpoints`.`Settings` s ON bs.settingID = s.settingID) t $where", MYSQLI_STORE_RESULT);
    $row_count = mysqli_num_rows($result);

    if ($row_count <= 1) {
        $row         = mysqli_fetch_array($result);
        $businessid  = $row['businessID'];
        $settingid   = $row['settingID'];
        $settingname = $row['settingName'];
        $valuetype   = $row['valueType'];
        $value       = $row['value'];

        if (isset($businessid) && $businessid != "") {

            echo $businessid . "~s" . $settingid . "~s" . $settingname . "~s" . $valuetype . "~s" . $value;
        }
    } else {
        while ($row_data = mysqli_fetch_array($result)) {
            $businessid  = $row_data['businessID'];
            $settingid   = $row_data['settingID'];
            $settingname = $row_data['settingName'];
            $valuetype   = $row_data['valueType'];
            $value       = $row_data['value'];

            if (isset($businessid) && $businessid != "") {

                echo $businessid . "~s" . $settingid . "~s" . $settingname . "~s" . $valuetype . "~s" . $value . "~n";
            }
        }
    }
    mysqli_close($con);
}

function getUserSettings()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }
    $where     = isset($_GET['whereClause']) ? "WHERE " . $_GET['whereClause'] : '';
    $result    = mysqli_query($con, "SELECT * FROM ( SELECT us.userID, s.*, us.value
    FROM `superpoints`.`UserSettings` us
        JOIN `superpoints`.`Settings` s ON us.settingID = s.settingID) t $where", MYSQLI_STORE_RESULT);
    $row_count = mysqli_num_rows($result);

    if ($row_count <= 1) {
        $row         = mysqli_fetch_array($result);
        $userid      = $row['userID'];
        $settingid   = $row['settingID'];
        $settingname = $row['settingName'];
        $valuetype   = $row['valueType'];
        $value       = $row['value'];

        if (isset($userid) && $userid != "") {

            echo $userid . "~s" . $settingid . "~s" . $settingname . "~s" . $valuetype . "~s" . $value;
        }
    } else {
        while ($row_data = mysqli_fetch_array($result)) {
            $userid      = $row_data['userID'];
            $settingid   = $row_data['settingID'];
            $settingname = $row_data['settingName'];
            $valuetype   = $row_data['valueType'];
            $value       = $row_data['value'];

            if (isset($userid) && $userid != "") {

                echo $userid . "~s" . $settingid . "~s" . $settingname . "~s" . $valuetype . "~s" . $value . "~n";
            }
        }
    }
    mysqli_close($con);
}

function getSettings()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }
    $where     = isset($_GET['whereClause']) ? "WHERE " . $_GET['whereClause'] : '';
    $result    = mysqli_query($con, "SELECT * FROM superpoints.Settings $where", MYSQLI_STORE_RESULT);
    $row_count = mysqli_num_rows($result);

    if ($row_count <= 1) {
        $row         = mysqli_fetch_array($result);
        $settingid   = $row['settingID'];
        $settingname = $row['settingName'];
        $valuetype   = $row['valueType'];

        if (isset($businessid) && $businessid != "") {

            echo $settingid . "~s" . $settingname . "~s" . $valuetype;
        }
    } else {
        while ($row_data = mysqli_fetch_array($result)) {
            $settingid   = $row_data['settingID'];
            $settingname = $row_data['settingName'];
            $valuetype   = $row_data['valueType'];

            if (isset($businessid) && $businessid != "") {

                echo $settingid . "~s" . $settingname . "~s" . $valuetype . "~n";
            }
        }
    }
    mysqli_close($con);
}

function getBeacons()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }
    $where     = isset($_GET['whereClause']) ? "WHERE " . $_GET['whereClause'] : '';
    $result    = mysqli_query($con, "SELECT * FROM superpoints.Beacons $where", MYSQLI_STORE_RESULT);
    $row_count = mysqli_num_rows($result);

    if ($row_count <= 1) {
        $row        = mysqli_fetch_array($result);
        $beaconid   = $row['beaconID'];
        $businessid = $row['businessID'];
        $major      = $row['major'];
        $minor      = $row['minor'];
        $txpower    = $row['txPower'];
        $region     = $row['region'];

        echo $beaconid . "~s" . $businessid . "~s" . $major . "~s" . $minor . "~s" . $txpower . "~s" . $region;

    } else {
        while ($row_data = mysqli_fetch_array($result)) {
            $beaconid   = $row_data['beaconID'];
            $businessid = $row_data['businessID'];
            $major      = $row_data['major'];
            $minor      = $row_data['minor'];
            $txpower    = $row_data['txPower'];
            $region     = $row_data['region'];

            if (isset($beaconid) && $beaconid != "") {
                echo $beaconid . "~s" . $businessid . "~s" . $major . "~s" . $minor . "~s" . $txpower . "~s" . $region . "~n";
            }
        }
    }
    mysqli_close($con);
}

function getTags()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }
    $where     = isset($_GET['whereClause']) ? "WHERE " . $_GET['whereClause'] : '';
    $result    = mysqli_query($con, "SELECT * FROM superpoints.Tags $where", MYSQLI_STORE_RESULT);
    $row_count = mysqli_num_rows($result);

    if ($row_count <= 1) {
        $row        = mysqli_fetch_array($result);
        $tagid    = $row['tagID'];
        $businessid = $row['businessID'];
        $tagname    = $row['tagName'];
        echo $tagid . "~s" . $businessid . "~s" . $tagname;
    } else {
        while ($row_data = mysqli_fetch_array($result)) {
            $tagid    = $row_data['tagID'];
            $businessid = $row_data['businessID'];
            $tagname    = $row_data['tagName'];
            echo $tagid . "~s" . $businessid . "~s" . $tagname . "~n";
        }
    }
    mysqli_close($con);
}

function getPromotionTags()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }
    $where     = isset($_GET['whereClause']) ? "WHERE " . $_GET['whereClause'] : '';
    $result    = mysqli_query($con, "SELECT * FROM superpoints.PromotionTags $where", MYSQLI_STORE_RESULT);
    $row_count = mysqli_num_rows($result);

    if ($row_count <= 1) {
        $row        = mysqli_fetch_array($result);
        $tagid    = $row['tagID'];
        $promotionid = $row['promotionID'];
        echo $tagid . "~s" . $promotionid . "~s" . $tagname;
    } else {
        while ($row_data = mysqli_fetch_array($result)) {
            $tagid    = $row_data['tagID'];
            $promotionid = $row_data['promotionID'];
            echo $tagid . "~s" . $promotionid . "~s" . $tagname . "~n";
        }
    }
    mysqli_close($con);
}

// NOTE: When updating one specific column, please pass back in the previous values for the other columns
function handleUser()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $userid     = $_GET['USER_ID'];
    $businessid = $_GET['BUSINESS_ID'];
    $username1  = $_GET['USERNAME'];
    $username2  = str_replace("~amp`", "~amp", $username1);
    $username   = str_replace("~amp", "&", $username2);

    if ($businessid == -1) {
        $businessid = "";
    }

    if ($userid == -1) {
        $userid = "";
    }

    if ($userid == "") {
        $password1 = $_GET['PASSWORD'];
        $password2 = str_replace("~amp`", "~amp", $password1);
        $password  = str_replace("~amp", "&", $password2);

        if ($businessid != "") {
            $result = mysqli_query($con, "INSERT INTO `superpoints`.`Users`
                   (`businessID`,`password`,`userName`) VALUES ($businessid, '$password', '$username');", MYSQLI_STORE_RESULT);
            $result = $result ? "true" : "";

            if ($result == "true") {
                $result2 = mysqli_query($con, "SELECT * FROM `superpoints`.`Users` WHERE userName = '$username' AND password = '$password'", MYSQLI_STORE_RESULT);
                $row     = mysqli_fetch_array($result2);

                echo $row[0];
            }
        } else {
            $result = mysqli_query($con, "INSERT INTO `superpoints`.`Users`
                 (`password`,`userName`) VALUES ('$password', '$username');", MYSQLI_STORE_RESULT);
            $result = $result ? "true" : "";

            if ($result == "true") {
                $result2 = mysqli_query($con, "SELECT * FROM `superpoints`.`Users` WHERE userName = '$username' AND password = '$password'", MYSQLI_STORE_RESULT);
                $row     = mysqli_fetch_array($result2);

                $settingResult = mysqli_query($con, "INSERT INTO `superpoints`.`UserSettings` (`userID`,`settingID`,`value`) VALUES ($row[0], 1, '2');", MYSQLI_STORE_RESULT);

                echo $row[0];
            }
        }
    } else {
        $result = mysqli_query($con, "UPDATE `superpoints`.`Users` SET
            username = '$username' WHERE (`userID` = '$userid');", MYSQLI_STORE_RESULT);
        echo ($result) ? "$userid" : "";
    }
}

function updatePassword()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $userid = $_GET['USER_ID'];
    $newPW  = $_GET['NEW_PASSWORD'];

    $result = mysqli_query($con, "UPDATE `superpoints`.`Users` SET
            password = '$newPW' WHERE (`userID` = '$userid');", MYSQLI_STORE_RESULT);

    echo ($result) ? "true" : "";
}

function handleBusiness()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $businessid   = $_GET['BUSINESS_ID'];
    $businessname = $_GET['BUSINESS_NAME'];
    $latitude     = $_GET['LATITUDE'];
    $longitude    = $_GET['LONGITUDE'];
    $region1      = $_GET['REGION'];
    $region2      = str_replace("~amp`", "~amp", $region1);
    $region       = str_replace("~amp", "&", $region2);

    if ($businessid == -1 || !isset($businessid)) {
        $businessid = "";
    }

    if ($region == -1 || !isset($region)) {
        $region = "null";
    }


    if ($businessid == "") {
        $result = mysqli_query($con, "INSERT INTO `superpoints`.`Businesses`
              (businessName, latitude, longitude, region) VALUES ('$businessname',
                '$latitude', '$longitude', '$region');", MYSQLI_STORE_RESULT);
        $result = $result ? "true" : "";

        if ($result == "true") {
            $result2 = mysqli_query($con, "SELECT businessID FROM `superpoints`.`Businesses` ORDER BY businessID DESC LIMIT 1");
            $row     = mysqli_fetch_array($result2);

            $settingResult = mysqli_query($con, "INSERT INTO `superpoints`.`BusinessSettings`
                  (`businessID`,`settingID`,`value`) VALUES ($row[0], 0, 'duration');", MYSQLI_STORE_RESULT);

            echo $row[0];
        }
    } else {
        $result = mysqli_query($con, "UPDATE `superpoints`.`Businesses` SET businessName = '$businessname',
            latitude = $latitude, longitude = $longitude, region = '$region' WHERE (`businessID` = '$businessid');", MYSQLI_STORE_RESULT);

        echo ($result) ? "$businessid" : "";
    }
}

function handleBeacons()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $beaconid   = $_GET['BEACON_ID'];
    $businessid = $_GET['BUSINESS_ID'];
    $major      = $_GET['MAJOR'];
    $minor      = $_GET['MINOR'];
    $txpower    = $_GET['TX_POWER'];
    $region1    = $_GET['REGION'];
    $region2    = str_replace("~amp`", "~amp", $region1);
    $region     = str_replace("~amp", "&", $region2);

    if ($beaconid == -1 || !isset($beaconid)) {
        $beaconid = "";
    }

    if ($beaconid == "") {
        $result = mysqli_query($con, "INSERT INTO `superpoints`.`Beacons`
              (businessID, major, minor, txPower, region) VALUES ('$businessid',
                '$major', '$minor', '$txpower', '$region');", MYSQLI_STORE_RESULT);
        $result = $result ? "true" : "";

        if ($result == "true") {
            $result2 = mysqli_query($con, "SELECT beaconID FROM `superpoints`.`Beacons` ORDER BY beaconID DESC LIMIT 1");
            $row     = mysqli_fetch_array($result2);

            echo $row[0];
        }
    } else {
        $result = mysqli_query($con, "UPDATE `superpoints`.`Beacons` SET businessID = '$businessid',
            major = '$major', minor = '$minor', txPower = $txPower, region = '$region' WHERE (`beaconID` = '$beaconid');", MYSQLI_STORE_RESULT);

        echo ($result) ? "$beaconid" : "";
    }
}

function handleBusinessSettings()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $businessid = $_GET['BUSINESS_ID'];
    $settingid  = $_GET['SETTING_ID'];
    $value      = $_GET['VALUE'];


    if ($businessid == -1 || !isset($businessid)) {
        $businessid = "";
    }

    if ($businessid == "") {
        $result = mysqli_query($con, "INSERT INTO `superpoints`.`BusinessSettings`
              (businessID, settingID, value) VALUES ('$businessid',
                '$settingid', '$value');", MYSQLI_STORE_RESULT);
        $result = $result ? "true" : "";

        if ($result == "true") {
            $result2 = mysqli_query($con, "SELECT businessID FROM `superpoints`.`BusinessSettings` ORDER BY businessID DESC LIMIT 1");
            $row     = mysqli_fetch_array($result2);
            echo $row[0];
        }
    } else {
        $result = mysqli_query($con, "UPDATE `superpoints`.`BusinessSettings` SET value = '$value' WHERE (businessID = '$businessid' AND settingID = '$settingid')", MYSQLI_STORE_RESULT);

        echo ($result) ? "$businessid" : "";
    }
}

function handleUserSettings()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $userid    = $_GET['USER_ID'];
    $settingid = $_GET['SETTING_ID'];
    $value     = $_GET['VALUE'];

    if ($userid == -1 || !isset($userid)) {
        $userid = "";
    }

    if ($userid == "") {
        $result = mysqli_query($con, "INSERT INTO `superpoints`.`UserSettings`
              (userID, settingID, value) VALUES ('$userid',
                '$settingid', '$value');", MYSQLI_STORE_RESULT);
        $result = $result ? "true" : "";

        if ($result == "true") {
            $result2 = mysqli_query($con, "SELECT userID FROM `superpoints`.`UserSettings` ORDER BY businessID DESC LIMIT 1");
            $row     = mysqli_fetch_array($result2);
            echo $row[0];
        }
    } else {
        $result = mysqli_query($con, "UPDATE `superpoints`.`UserSettings` SET value = '$value' WHERE (userID = '$userid' AND settingID = '$settingid')", MYSQLI_STORE_RESULT);

        echo ($result) ? "$userid" : "";
    }
}

function handlePromotions()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $promotionid       = $_GET['PROMOTION_ID'];
    $businessid        = $_GET['BUSINESS_ID'];
    $tierid            = $_GET['MIN_TIER'];
    $details1          = $_GET['DETAILS'];
    $details2          = str_replace("~amp`", "~amp", $details1);
    $details           = str_replace("~amp", "&", $details2);
    $clicks            = $_GET['CLICKS'];
    $shortDescription1 = $_GET['SHORT_DESCRIPTION'];
    $shortDescription2 = str_replace("~amp`", "~amp", $shortDescription1);
    $shortDescription  = str_replace("~amp", "&", $shortDescription2);
    $tags = $_GET['TAGS'];

    if ($promotionid == -1) {
        $promotionid = "";
    }

    $tagArray = explode(",", $tags);

    if ($promotionid == "") {
        $result = mysqli_query($con, "INSERT INTO `superpoints`.`Promotions`
              (businessID, minTierID, details, clicks, shortDescription) VALUES ('$businessid', '$tierid',
                '$details', '$clicks', '$shortDescription');", MYSQLI_STORE_RESULT);

        $result = $result ? "true" : "";

        for ($i = 0; $i < count($tagArray); $i++) {
              $result3 = mysqli_query($con, "INSERT INTO `superpoints`.`PromotionTags`
                    (promotionID, tagID) VALUES ('$promotionid', '$tagArray[$i]');", MYSQLI_STORE_RESULT);
        }

        if ($result == "true") {
            $result2 = mysqli_query($con, "SELECT promotionID FROM `superpoints`.`Promotions` ORDER BY promotionID DESC LIMIT 1");
            $row     = mysqli_fetch_array($result2);
            echo $row[0];
        }
    } else {
        $result = mysqli_query($con, "UPDATE `superpoints`.`Promotions` SET businessid = '$businessid',
            minTierID = '$tierid', details = '$details', clicks = $clicks, shortDescription = '$shortDescription' WHERE (`promotionID` = '$promotionid');", MYSQLI_STORE_RESULT);


        $result1 = mysqli_query($con, "DELETE FROM `superpoints`.`PromotionTags`
            WHERE promotionID = $promotionid;", MYSQLI_STORE_RESULT);
        for ($i = 0; $i < count($tagArray); $i++) {
            $result3 = mysqli_query($con, "INSERT INTO `superpoints`.`PromotionTags`
                    (promotionID, tagID) VALUES ('$promotionid', '$tagArray[$i]');", MYSQLI_STORE_RESULT);
        }
        echo $result ? "$promotionid" : "";
    }
}

function handlePromotionUsage()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $promotionid = $_GET['PROMOTION_ID'];
    $userid      = $_GET['USER_ID'];

    if ($promotionid == -1) {
        $promotionid = "";
    }

    if ($promotionid != "") {
        $result = mysqli_query($con, "INSERT INTO `superpoints`.`PromotionUsage`
              (promotionID, userID) VALUES ('$promotionid', '$userid');", MYSQLI_STORE_RESULT);

        $result = $result ? "true" : "";

        if ($result == "true") {
            $result2 = mysqli_query($con, "SELECT promotionUsageID FROM `superpoints`.`PromotionUsage` ORDER BY promotionUsageID DESC LIMIT 1");
            $row     = mysqli_fetch_array($result2);
            echo $row[0];
        }
    } else {
        $result = mysqli_query($con, "UPDATE `superpoints`.`PromotionUsage` SET promotionID = '$promotionid',
            userid = '$userid' WHERE (`promotionID` = '$promotionid');", MYSQLI_STORE_RESULT);
        echo $result ? "$promotionid" : "";
    }
}

function handleTags()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $tagid      = $_GET['TAG_ID'];
    $businessid = $_GET['BUSINESS_ID'];
    $tagname    = $_GET['TAG_NAME'];

    if ($tagid == -1) {
        $tagid = "";
    }

    if ($tagid == "") {
        $result = mysqli_query($con, "INSERT INTO `superpoints`.`Tags`
              (businessID, tagName) VALUES ('$businessid', '$tagname');", MYSQLI_STORE_RESULT);

        $result = $result ? "true" : "";

        if ($result == "true") {
            $result2 = mysqli_query($con, "SELECT tagID FROM `superpoints`.`Tags` ORDER BY tagID DESC LIMIT 1");
            $row     = mysqli_fetch_array($result2);
            echo $row[0];
        }
    } else {
        $result = mysqli_query($con, "UPDATE `superpoints`.`Tags` SET businessID = '$businessid',
            tagName = '$tagname' WHERE (`tagID` = '$tagid');", MYSQLI_STORE_RESULT);
        echo $result ? "$tagid" : "";
    }
}

function handleMultipleTags()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $tagid      = $_GET['TAG_ID'];
    $businessid = $_GET['BUSINESS_ID'];
    $tagname    = $_GET['TAG_NAME'];

    $businessArray = explode(",", $businessid);
    $tagArray = explode(",", $tagid);
    $nameArray = explode(",", $tagname);

    for ($i = 0; $i < count($tagArray); $i++) {
    if ($tagArray[$i] != -1) {
          $tagStrings = explode(" ", $tagArray);
          $result = mysqli_query($con, "INSERT INTO `superpoints`.`Tags`
              (businessID, tagName) VALUES ('$businessArray[$i]', '$nameArray[$i]');", MYSQLI_STORE_RESULT);

          $result = $result ? "true" : "";

        if ($result == "true") {
            $result2 = mysqli_query($con, "SELECT tagID FROM `superpoints`.`Tags` ORDER BY tagID DESC LIMIT 1");
            $row     = mysqli_fetch_array($result2);
            echo $row[0];
        }
    } else {
        $result = mysqli_query($con, "UPDATE `superpoints`.`Tags` SET businessID = '$businessArray[$i]',
            tagName = '$nameArray[$i]' WHERE (`tagID` = '$tagArray[$i]');", MYSQLI_STORE_RESULT);
        echo $result ? "$tagid" : "";
    }
  }
}

function handlePromotionTags()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $tagid       = $_GET['TAG_ID'];
    $promotionid = $_GET['PROMOTION_ID'];

    if ($promotionid == -1) {
        $promotionid = "";
    }

    if ($tagid != "") {
        $result = mysqli_query($con, "INSERT INTO `superpoints`.`PromotionTags`
              (promotionID, tagID) VALUES ('$promotionid', '$tagid');", MYSQLI_STORE_RESULT);

        $result = $result ? "true" : "";

        if ($result == "true") {
            $result2 = mysqli_query($con, "SELECT promotionID FROM `superpoints`.`PromotionTags` ORDER BY promotionID DESC LIMIT 1");
            $row     = mysqli_fetch_array($result2);
            echo $row[0];
        }
    } else {
        $result = mysqli_query($con, "UPDATE `superpoints`.`PromotionTags` SET promotionID = '$promotionid',
            tagid = '$tagid' WHERE (`promotionID` = '$promotionid');", MYSQLI_STORE_RESULT);
        echo $result ? "$tagid" : "";
    }
}

function handleVisits()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $visitid    = $_GET['VISIT_ID'];
    $userid     = $_GET['USER_ID'];
    $businessid = $_GET['BUSINESS_ID'];
    $duration   = $_GET['DURATION'];
    $date       = $_GET['DATE'];

    if ($visitid == -1) {
        $visitid = "";
    }

    if ($visitid == "") {
        $result = mysqli_query($con, "INSERT INTO `superpoints`.`Visits`
                (`userID`, `businessID`,`duration`, `date`) VALUES ($userid, $businessid,
                $duration, convert('$date', DATETIME));", MYSQLI_STORE_RESULT);

        $result = $result ? "true" : "";

        if ($result == "true") {
            $result2 = mysqli_query($con, "SELECT visitID FROM `superpoints`.`Visits` ORDER BY visitID DESC LIMIT 1");
            $row     = mysqli_fetch_array($result2);
            echo $row[0];
        }
    } else {
        $result = mysqli_query($con, "UPDATE `superpoints`.`Visits` SET userid = '$userid', businessid = '$businessid',
                duration = '$duration', date = convert($date, DATETIME) WHERE (`visitID` = '$visitid');", MYSQLI_STORE_RESULT);
        echo $result ? "$visitid" : "";
    }

    $pointsResult = mysqli_query($con, "SELECT points FROM `superpoints`.`Points` WHERE userID = $userid AND businessID = $businessid", MYSQLI_STORE_RESULT);

    $pointsFunc = function($minutes)
    {
        $optimalTime = 20;
        $slope       = 30;
        return (pow(abs($minutes - $optimalTime), 1 / 3) * (($minutes - $optimalTime < 0) ? -1 : 1)) * $slope;
    };

    $row_count = mysqli_num_rows($pointsResult);

    $pointAccumulationSetting = mysqli_query($con, "SELECT * FROM `superpoints`.`BusinessSettings` WHERE  businessID = $businessid AND settingID = 0", MYSQLI_STORE_RESULT);
    $row                      = mysqli_fetch_array($pointAccumulationSetting);
    $setting                  = $row['value'];
    if ($setting == "duration") {
        $points = $pointsFunc($duration) + abs($pointsFunc(0));
    } else {
        $points = 250;
    }

    if ($row_count == 0) {
        $pointsResult = mysqli_query($con, "INSERT INTO `superpoints`.`Points` (`businessID`, `userID`, `points`) VALUES ($businessid, $userid, $points);", MYSQLI_STORE_RESULT);
    } else {
        $pointsResult = mysqli_query($con, "UPDATE `superpoints`.`Points` SET points = points + $points WHERE businessID = $businessid AND userID = $userid", MYSQLI_STORE_RESULT);
    }
}

function login()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $username   = $_GET['username'];
    $password   = $_GET['password'];
    $result     = mysqli_query($con, "SELECT * FROM superpoints.Users where
        userName='$username' and password='$password'", MYSQLI_STORE_RESULT);
    $row        = mysqli_fetch_array($result);
    $userid     = $row[0];
    $businessid = $row[1];
    $userName   = $row[3];

    if ($businessid != "") {
        if (isset($userName) && $userName != "") {
            echo $userid . "~s" . $businessid . "~s" . $username;
        }
    } else {
        if (isset($userName) && $userName != "") {
            echo $userid . "~s" . $username;
        }
    }
    mysqli_close($con);
}

function getApplicablePromotions()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    // Becomes WHERE userid = userid
    $where  = isset($_GET['whereClause']) ? "AND " . $_GET['whereClause'] : '';
    //$userID = $_GET['uid'];
    $result = mysqli_query($con, "SELECT promotionID, superpoints.Promotions.businessID, superpoints.PointTiers.tierID, superpoints.PointTiers.minPoints, superpoints.PointTiers.name, details, clicks, businessName, shortDescription
            FROM superpoints.Promotions INNER JOIN superpoints.Businesses ON
                superpoints.Promotions.businessID = superpoints.Businesses.businessID
            INNER JOIN superpoints.Points ON superpoints.Promotions.businessID = superpoints.Points.businessID
            INNER JOIN superpoints.PointTiers ON superpoints.Promotions.minTierID = superpoints.PointTiers.tierID
            WHERE superpoints.Points.businessID = superpoints.Promotions.businessID AND points > minPoints $where;", MYSQLI_STORE_RESULT);

    while ($row_data = mysqli_fetch_array($result)) {
        $visitid          = $row_data['promotionID'];
        $businessid       = $row_data['businessID'];
        $tierID           = $row_data['tierID'];
        $minPoints        = $row_data['minPoints'];
        $name             = $row_data['name'];
        $details          = $row_data['details'];
        $clicks           = $row_data['clicks'];
        $businessName     = $row_data['businessName'];
        $shortDescription = $row_data['shortDescription'];

        if (isset($businessid) && $businessid != "") {
            echo $visitid . "~s" . $businessid . "~s" . $tierID . "~s" . $minPoints . "~s" . $name . "~s" . $details . "~s" . $clicks . "~s" . $businessName . "~s" . $shortDescription . "~s" . "~n";
        }
    }

    mysqli_close($con);
}

function incrementClick()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }
    $promotionid = $_GET['PROMOTION_ID'];


    $result = mysqli_query($con, "UPDATE `superpoints`.`Promotions` SET `clicks` = `clicks` + 1
            WHERE (`promotionID` = $promotionid);", MYSQLI_STORE_RESULT);

    echo $result ? 'true' : 'false';
    mysqli_close($con);
}

function selectBusiness()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $where = $_GET['whereClause'];

    if (isset($where)) {
        $explode = explode(" ", $where);
    }
    $lat  = substr($explode[0], 4);
    $long = substr($explode[1], 5);

    $result = mysqli_query($con, "SELECT * FROM superpoints.Businesses
            WHERE 6371 * acos( cos( radians('$lat') )
            * cos( radians( superpoints.Businesses.latitude ) )
            * cos( radians( superpoints.Businesses.longitude ) - radians('$long') ) + sin( radians('$lat') )
            * sin(radians(superpoints.Businesses.latitude)) ) < 2;", MYSQLI_STORE_RESULT);


    $row_count = mysqli_num_rows($result);

    if ($row_count <= 1) {
        $row          = mysqli_fetch_array($result);
        $businessid   = $row['businessID'];
        $businessname = $row['businessName'];
        $latitude     = $row['latitude'];
        $longitude    = $row['longitude'];
        $region       = $row['region'];

        if (isset($businessid) && $businessid != "") {

            echo $businessid . "~s" . $businessname . "~s" . $latitude . "~s" . $longitude . "~s" . $region;
        }
    } else {
        while ($row_data = mysqli_fetch_array($result)) {
            $businessid   = $row_data['businessID'];
            $businessname = $row_data['businessName'];
            $latitude     = $row_data['latitude'];
            $longitude    = $row_data['longitude'];
            $region       = $row_data['region'];

            if (isset($businessid) && $businessid != "") {

                echo $businessid . "~s" . $businessname . "~s" . $latitude . "~s" . $longitude . "~s" . $region . "~n";
            }
        }
    }

    mysqli_close($con);
}

function selectPreferredBusinesses()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $where  = $_GET['whereClause'];
    $where  = isset($_GET['whereClause']) ? "WHERE " . $_GET['whereClause'] : '';
    $result = mysqli_query($con, "SELECT * FROM superpoints.Businesses $where", MYSQLI_STORE_RESULT);

    $row_count = mysqli_num_rows($result);

    if ($row_count <= 1) {
        $row          = mysqli_fetch_array($result);
        $businessid   = $row['businessID'];
        $businessname = $row['businessName'];
        $latitude     = $row['latitude'];
        $longitude    = $row['longitude'];
        $region       = $row['region'];

        if (isset($businessid) && $businessid != "") {

            echo $businessid . "~s" . $businessname . "~s" . $latitude . "~s" . $longitude . "~s" . $region;
        }
    } else {
        while ($row_data = mysqli_fetch_array($result)) {
            $businessid   = $row_data['businessID'];
            $businessname = $row_data['businessName'];
            $latitude     = $row_data['latitude'];
            $longitude    = $row_data['longitude'];
            $region       = $row_data['region'];

            if (isset($businessid) && $businessid != "") {

                echo $businessid . "~s" . $businessname . "~s" . $latitude . "~s" . $longitude . "~s" . $region . "~n";
            }
        }
    }

    mysqli_close($con);
}

function calcAvgVisitsWeek()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $currentYear         = date('Y'); //1992
    $month               = date('m'); //0-12
    $daysInMonth         = cal_days_in_month(CAL_GREGORIAN, $month, $currentYear); //
    $daysInPreviousMonth = cal_days_in_month(CAL_GREGORIAN, $month - 1, $currentYear);
    $currentDay          = date('d'); //1-31
    $where               = $_GET['whereClause'];

    $daysFromPrevMonth = 0;

    if ($currentDay < 7) {
        $daysFromPrevMonth = 7 - $currentDay;
    }
    //01 - 31

    $result = mysqli_query($con, "
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
    $row    = mysqli_fetch_array($result);
    //$count = $result[0];

    echo $row[0];
    mysqli_close($con);
}


function calcAvgDurationPerVisitWeek()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $currentYear         = date('Y'); //1992
    $month               = date('m'); //0-12
    $daysInMonth         = cal_days_in_month(CAL_GREGORIAN, $month, $currentYear); //
    $daysInPreviousMonth = cal_days_in_month(CAL_GREGORIAN, $month - 1, $currentYear);
    $currentDay          = date('d'); //1-31

    $daysFromPrevMonth = 0;
    $where             = $_GET['whereClause'];

    if ($currentDay < 7) {
        $daysFromPrevMonth = 7 - $currentDay;
    }
    //01 - 31

    $result = mysqli_query($con, "
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
    $row    = mysqli_fetch_array($result);

    echo $row[0];
    mysqli_close($con);
}

function deletePromotion()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $promotionid = $_GET['PROMOTION_ID'];

    $result = mysqli_query($con, "DELETE FROM `superpoints`.`Promotions`
                                    WHERE promotionID = $promotionid;", MYSQLI_STORE_RESULT);

    echo ($result) ? "true" : "";
}

function handlePreferredBusinesses()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $userid     = $_GET['USER_ID'];
    $businessid = $_GET['BUSINESS_ID'];

    $result = mysqli_query($con, "INSERT INTO `superpoints`.`PreferredBusinesses`
          (userID, businessID) VALUES ($userid, $businessid);", MYSQLI_STORE_RESULT);

    echo ($result) ? "true" : "";

}


function deletePreferredBusiness()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $userid     = $_GET['USER_ID'];
    $businessid = $_GET['BUSINESS_ID'];

    $result = mysqli_query($con, "DELETE FROM `superpoints`.`PreferredBusinesses`
                                WHERE userID = $userid AND businessID = $businessid;", MYSQLI_STORE_RESULT);

    echo ($result) ? "true" : "";
}

function deletePromotionTags()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $promotionid = $_GET['PROMOTION_ID'];

    $result = mysqli_query($con, "DELETE FROM `superpoints`.`PromotionTags`
                                WHERE promotionID = $promotionid;", MYSQLI_STORE_RESULT);

    echo ($result) ? "true" : "";
}

function calcMonthlyVisits()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $businessid = $_GET['businessID'];
    $result     = mysqli_query($con, "
    SELECT COUNT(userID) AS numVisits, CONCAT(YEAR(date), '.', RIGHT(CONCAT('00', MONTH(date)), 2)) as timeSpan
    FROM superpoints.Visits
    WHERE businessID = '$businessid'
      AND (MONTH(CURDATE()) + YEAR(CURDATE()) * 12) - (MONTH(date) + YEAR(date) * 12) < 12
    GROUP BY timeSpan
    ", MYSQLI_STORE_RESULT);

    while ($row_data = mysqli_fetch_array($result)) {
        $timeSpan  = $row_data['timeSpan'];
        $numVisits = $row_data['numVisits'];
        if (isset($businessid) && $businessid != "")
            echo $timeSpan . "~s" . $numVisits . "~n";
    }
}

function calcNewOldUsers()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $businessid = $_GET['businessID'];

    $result = mysqli_query($con, "
    SELECT COUNT(userID) AS numUsers, 'old' as newOld
    FROM (
      SELECT DISTINCT userID
      FROM superpoints.Visits
      WHERE userID IN (SELECT userID FROM superpoints.Visits WHERE MONTH(date) != MONTH(CURDATE()) OR YEAR(date) != YEAR(CURDATE()))
        AND MONTH(date) = MONTH(CURDATE()) AND YEAR(date) = YEAR(CURDATE())
        AND businessID = $businessid) oldUser
    UNION
    SELECT COUNT(userID), 'new'
    FROM (
      SELECT DISTINCT userID
      FROM superpoints.Visits
      WHERE userID NOT IN (SELECT userID FROM superpoints.Visits WHERE MONTH(date) != MONTH(CURDATE()) OR YEAR(date) != YEAR(CURDATE()))
        AND MONTH(date) = MONTH(CURDATE()) AND YEAR(date) = YEAR(CURDATE())
        AND businessID = $businessid) newUsers
    ", MYSQLI_STORE_RESULT);


    while ($row_data = mysqli_fetch_array($result)) {
        $numUsers = $row_data['numUsers'];
        $newOld   = $row_data['newOld'];
        if (isset($businessid) && $businessid != "")
            echo $newOld . "~s" . $numUsers . "~n";
    }
}

function calcVisitorsPerTier()
{
    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $businessid = $_GET['businessID'];
    $result     = mysqli_query($con, "
    SELECT COUNT(tierID) AS numVisits, tierID
    FROM (
      SELECT v.userID, v.businessID, (SELECT tierID FROM superpoints.PointTiers WHERE minPoints < p.points ORDER BY minPoints DESC LIMIT 1) AS tierID
      FROM superpoints.Visits v
        JOIN superpoints.Points p ON v.userID = p.userID AND v.businessID = p.businessID
      WHERE MONTH(date) = MONTH(CURDATE()) AND YEAR(date) = YEAR(CURDATE())
        AND v.businessID = '$businessid'
      GROUP BY v.userID, v.businessID) t
    GROUP BY tierID
    ", MYSQLI_STORE_RESULT);


    while ($row_data = mysqli_fetch_array($result)) {
        $numVisits = $row_data['numVisits'];
        $tierID    = $row_data['tierID'];
        if (isset($businessid) && $businessid != "")
            echo $tierID . "~s" . $numVisits . "~n";
    }
}

function calcPromotionUsage() {
  $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);

  if (mysqli_connect_errno($con)) {
      echo "Failed to connect to database: " . mysqli_connect_error();
  }

  $businessid = $_GET['businessID'];

  $result     = mysqli_query($con, "SELECT
	COUNT(pt.tagID) / (
        SELECT
			COUNT(ptsub.tagID)
		FROM
			superpoints.PromotionTags ptsub
			JOIN superpoints.Promotions psub ON ptsub.promotionID = psub.promotionID
		WHERE
			psub.businessID = $businessid
        	AND ptsub.tagID = pt.tagID
    ) AS 'UsesPerPromo',
	t.tagName 'Tag'
FROM
	superpoints.PromotionUsage pu
	JOIN superpoints.Promotions p ON pu.promotionID = p.promotionID
	JOIN superpoints.PromotionTags pt ON pt.promotionID = p.promotionID
	JOIN superpoints.Tags t ON pt.tagID = t.tagID
WHERE
	p.businessID = $businessid
GROUP BY pt.tagID", MYSQLI_STORE_RESULT);


  while ($row_data = mysqli_fetch_array($result)) {
      $promouses = $row_data['UsesPerPromo'];
      $tag    = $row_data['Tag'];
      echo $promouses . "~s" . $tag . "~n";
  }
}

function sendEmail()
{
    $mail = new PHPMailer();
    $mail->isSMTP();
    $mail->SMTPAuth   = true;
    $mail->SMTPSecure = 'ssl';
    $mail->Host       = 'smtp.gmail.com';
    $mail->Port       = '465';
    $mail->isHTML();
    $mail->Username = 'spemaileradm@gmail.com';
    $mail->Password = 'Redsaw123qwe';
    $mail->setFrom('spemaileradm@gmail.com');
    $mail->Subject = 'Monthly KPI Report';
    $mail->Body    = 'Here is the KPI for this month: http://ec2-99-79-49-31.ca-central-1.compute.amazonaws.com/admin.php';
    $mail->AddAddress('spemaileradm@gmail.com');


    if (!$mail->send()) {
        echo 'Message was not sent.';
        echo 'Mailer error: ' . $mail->ErrorInfo;
    } else {
        echo 'Message has been sent.';
    }
}



$func = $_GET['function'];
switch ($func) {
    case "sendEmail":
        sendEmail();
        break;
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
    case "getBeacons":
        getBeacons();
        break;
    case "getTags":
        getTags();
        break;
    case "getPromotionTags":
        getPromotionTags();
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
    case "setBusinessSetting":
        handleBusinessSettings();
        break;
    case "setUserSetting":
        handleUserSettings();
        break;
    case "setBeacon":
        handleBeacons();
        break;
    case "setPromotionUsage":
        handlePromotionUsage();
        break;
    case "setTag":
        handleTags();
        break;
    case "setPromotionTag":
        handlePromotionTags();
        break;
    case "setTags":
        handleMultipleTags();
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
    case "calcPromotionUsage":
        calcPromotionUsage();
        break;
    case "deletePromotion":
        deletePromotion();
        break;
    case "deletePromotionTags":
        deletePromotionTags();
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
