<?php

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

require 'PHPMailer/PHPMailer/src/Exception.php';
require 'PHPMailer/PHPMailer/src/PHPMailer.php';
require 'PHPMailer/PHPMailer/src/SMTP.php';

define('DB_SERVER_', "superpointsdb.cuwiyoumlele.ca-central-1.rds.amazonaws.com");
define('DB_USERNAME_', "admin");
define('DB_PASSWORD_', "zxcasdqwe123");

$con = mysqli_connect(DB_SERVER_, DB_USERNAME_, DB_PASSWORD_);

if (mysqli_connect_errno($con)) {
    echo "Failed to connect to database: " . mysqli_connect_error();
}

$result = mysqli_query($con,"SELECT value FROM superpoints.UserSettings WHERE userID = 0", MYSQLI_STORE_RESULT);
$row = mysqli_fetch_array($result);
$sendMonthly = $row['value'];




function sendKPIEmail($sendMonthly) {
    if ($sendMonthly == 'true') {
      $mail = new PHPMailer();
      $mail->isSMTP();
      $mail->SMTPAuth = true;
      $mail->SMTPSecure = 'ssl';
      $mail->Host = 'smtp.gmail.com';
      $mail->Port = '465';
      $mail->isHTML();
      $mail->Username = 'spemaileradm@gmail.com';
      $mail->Password = 'Redsaw123qwe';
      $mail->setFrom('spemaileradm@gmail.com');
      $mail->Subject = 'Monthly KPI Report';
      $mail->Body = 'Test';
      $mail->AddAddress('spemaileradm@gmail.com');
      if ($sendMonthly) {
        echo "reached here";
        if(!$mail->send()) {
            echo 'Message was not sent.';
            echo 'Mailer error: ' . $mail->ErrorInfo;
        } else {
            echo 'Message has been sent.';
        }
    }
    }
}


sendKPIEmail($sendMonthly);



 ?>
