<!DOCTYPE html>

<html lang="en">
    <head>
        <meta charset="utf-8" />
        <title>SMP Admin</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="css/superhero/bootstrap.min.css" rel="stylesheet" media="screen">
    </head>

<?php
define('DB_SERVER', "superpointsdb.cuwiyoumlele.ca-central-1.rds.amazonaws.com");
define('DB_USERNAME', "admin");
define('DB_PASSWORD', "zxcasdqwe123");


// Calculates monthly visits for each month for one year
function calcMonthlyVisits($arr)
{

    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);
    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $businessid   = $arr['Business ID'];
    $businessname = $arr['Business Name'];

    $result = mysqli_query($con, "
  SELECT COUNT(userID) AS numVisits, CONCAT(YEAR(date), '.', RIGHT(CONCAT('00', MONTH(date)), 2)) as timeSpan
  FROM superpoints.Visits
  WHERE businessID = '$businessid'
  AND (MONTH(CURDATE()) + YEAR(CURDATE()) * 12) - (MONTH(date) + YEAR(date) * 12) < 12
  GROUP BY timeSpan
  ", MYSQLI_USE_RESULT);

    $newArr = array();
    $times  = array();

    while ($row_data = mysqli_fetch_array($result)) {
        $timeSpan  = $row_data['timeSpan'];
        $numVisits = $row_data['numVisits'];
        if (isset($timeSpan) && $timeSpan != "") {
            $times[$timeSpan] = $numVisits;
        }
    }
    $toAdd = array(
        "Business ID" => $businessid,
        "Business Name" => $businessname
    );
    for ($i = 0; $i < 12; $i++) {
        $currMonth = date('20y.m', strtotime("-$i month"));
        if (isset($times[$currMonth])) {
            $toAdd[$currMonth] = $times[$currMonth];
        } else {
            $toAdd[$currMonth] = "N/A";
        }
    }
    array_push($newArr, $toAdd);
    mysqli_free_result($result);
    return $newArr;
}

function calcNewOldUsers($arr)
{

    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);
    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

    $businessid   = $arr['Business ID'];
    $businessname = $arr['Business Name'];

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
  ", MYSQLI_USE_RESULT);

    $newArr = array();
    $newOldArr = array();
    $toAdd = array(
        "Business ID" => $businessid,
        "Business Name" => $businessname
    );

    while ($row_data = mysqli_fetch_array($result)) {
        $numUsers = $row_data['numUsers'];
        $newOld   = $row_data['newOld'];
        $newOldArr[$newOld] = $numUsers;
    }
    if (isset($newOldArr['new'])) {
      $toAdd['new'] = $newOldArr['new'];
    }
    if (isset($newOldArr['old'])) {
      $toAdd['old'] = $newOldArr['old'];
    }
    array_push($newArr, $toAdd);
    mysqli_free_result($result);
    return $newArr;
}

function calcVisitorsPerTier($arr)
{

    $con = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);
    if (mysqli_connect_errno($con)) {
        echo "Failed to connect to database: " . mysqli_connect_error();
    }

        $businessid   = $arr['Business ID'];
        $businessname = $arr['Business Name'];

    $result = mysqli_query($con, "
  SELECT COUNT(tierID) AS numVisits, tierID AS tierName
  FROM (
    SELECT v.userID, v.businessID, (SELECT name FROM superpoints.PointTiers WHERE minPoints < p.points ORDER BY minPoints DESC LIMIT 1) AS tierID
    FROM superpoints.Visits v
    JOIN superpoints.Points p ON v.userID = p.userID AND v.businessID = p.businessID
    WHERE MONTH(date) = MONTH(CURDATE()) AND YEAR(date) = YEAR(CURDATE())
    AND v.businessID = '$businessid'
    GROUP BY v.userID, v.businessID) t
    GROUP BY tierID
    ", MYSQLI_USE_RESULT);

        $newArr = array();
        $tiers = array();
        $toAdd = array(
            "Business ID" => $businessid,
            "Business Name" => $businessname
        );

        while ($row_data = mysqli_fetch_array($result)) {
            $numVisits = $row_data['numVisits'];
            $tierName  = $row_data['tierName'];
            $tiers[$tierName] = $numVisits;
        }
        if (isset($tiers['Copper'])) {
          $toAdd['Copper'] = $tiers['Copper'];
        } else {
          $toAdd['Copper'] = 0;
        }

        if (isset($tiers['Bronze'])) {
          $toAdd['Bronze'] = $tiers['Bronze'];
        } else {
          $toAdd['Bronze'] = 0;
        }

        if (isset($tiers['Silver'])) {
          $toAdd['Silver'] = $tiers['Silver'];
        }  else {
          $toAdd['Silver'] = 0;
        }

        if (isset($tiers['Gold'])) {
          $toAdd['Gold'] = $tiers['Gold'];
        }  else {
          $toAdd['Gold'] = 0;
        }

        if (isset($tiers['Platinum'])) {
          $toAdd['Platinum'] = $tiers['Platinum'];
        }  else {
          $toAdd['Platinum'] = 0;
        }

        if (isset($tiers['Diamond'])) {
          $toAdd['Diamond'] = $tiers['Diamond'];
        }  else {
          $toAdd['Diamond'] = 0;
        }

        array_push($newArr, $toAdd);
        mysqli_free_result($result);
        return $newArr;
}

$con    = mysqli_connect(DB_SERVER, DB_USERNAME, DB_PASSWORD);
$result = mysqli_query($con, "SELECT businessID, businessName FROM superpoints.Businesses", MYSQLI_USE_RESULT);
$count  = 0;
$arr    = array();
while ($row_data = mysqli_fetch_array($result)) {
    $businessid    = $row_data['businessID'];
    $businessname  = $row_data['businessName'];
    $ids[$count]   = $businessid;
    $names[$count] = $businessname;

    array_push($arr, array(
        "Business ID" => $businessid,
        "Business Name" => $businessname
    ));

    $count++;
}
mysqli_free_result($result);

$var1 = 'BusinessID';
$var2 = 'BusinessName';
$var3 = 'Time Span';
$var4 = 'Number of Visits';

$monthlyVisits = array();
$newUsers = array();
$tieredVisits = array();
for ($i = 0; $i < $count; $i++) {
    $tmp = calcMonthlyVisits($arr[$i]);
    for ($j = 0; $j < count($tmp); $j++) {
        array_push($monthlyVisits, $tmp[$j]);
    }
    $tmp2 = calcNewOldUsers($arr[$i]);
    for ($j = 0; $j < count($tmp2); $j++) {
        array_push($newUsers, $tmp2[$j]);
    }
    $tmp3 = calcVisitorsPerTier($arr[$i]);
    for ($j = 0; $j < count($tmp3); $j++) {
        array_push($tieredVisits, $tmp3[$j]);
    }
}


?>

<?php
if (count($monthlyVisits) > 0):
?>

          <div class="container">
              <div class="row">
<table class="table table-condensed table-striped table-hover">
  <thead>
    <tr>
      <th><?php
    echo implode('</th><th>', array_keys(current($monthlyVisits)));
?></th>
    </tr>
  </thead>
  <tbody>
<?php
    foreach ($monthlyVisits as $row):
        array_map('htmlentities', $row);
?>
   <tr>
      <td><?php
        echo implode('</td><td>', $row);
?></td>
    </tr>
<?php
    endforeach;
?>
 </tbody>
</table>
</div>
</div>
<?php
endif;
?>

<?php
if (count($newUsers) > 0):
?>

<div class="container">
<div class="row">
<table class="table table-condensed table-striped table-hover">
  <thead>
    <tr>
      <th><?php
    echo implode('</th><th>', array_keys(current($newUsers)));
?></th>
    </tr>
  </thead>
  <tbody>
<?php
    foreach ($newUsers as $row):
        array_map('htmlentities', $row);
?>
   <tr>
      <td><?php
        echo implode('</td><td>', $row);
?></td>
    </tr>
<?php
    endforeach;
?>
 </tbody>
</table>
</div>
</div>
<?php
endif;
?>

<?php
if (count($tieredVisits) > 0):
?>

<div class="container">
<div class="row">
<table class="table table-condensed table-striped table-hover">
  <thead>
    <tr>
      <th><?php
    echo implode('</th><th>', array_keys(current($tieredVisits)));
?></th>
    </tr>
  </thead>
  <tbody>
<?php
    foreach ($tieredVisits as $row):
        array_map('htmlentities', $row);
?>
   <tr>
      <td><?php
        echo implode('</td><td>', $row);
?></td>
    </tr>
<?php
    endforeach;
?>
 </tbody>
</table>
</div>
</div>
<?php
endif;
?>
