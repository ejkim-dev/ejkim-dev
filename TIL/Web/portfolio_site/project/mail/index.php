<?php
include $_SERVER['DOCUMENT_ROOT'].'/common/session.php';//session파일 include
include $_SERVER['DOCUMENT_ROOT'].'/common/checkSignSession.php';//로그인 안하면 접근 못함
include $_SERVER['DOCUMENT_ROOT'].'/common/mysql.php';//데이터베이스 접속

// $_GET['boardID']가 존재하고 0을 초과하는지 확인
if (isset($_GET['boardID']) && (int) $_GET['boardID'] > 0) {
  $boardID = $_GET['boardID'];

    // boardID 로 데이터 불러오기
    $sql =  "SELECT * FROM touch WHERE boardID = {$boardID} ";
    $result = $dbConnect->query($sql);
    if ($result) {
      $touchInfo = $result->fetch_array(MYSQLI_ASSOC);
      $mymail = $_SESSION['email'];
      $mymailTitle = strip_tags(htmlspecialchars($_POST['mymailTitle']));
      $summernote = $_POST['summernote'];

      echo "작성자 : ".$touchInfo[getName]."<br>";
      echo "작성자 메일 : ".$touchInfo[getEmail]."<br>";
      echo "내 메일 : $mymail<br>";
      echo "제목 : $mymailTitle<br>";
      echo "$summernote";

    }
    else {
      echo "잘못된 접근 입니다.";
      exit;
    }
  }

  else {
    echo "잘못된 접근 입니다.";
    exit;
  }


include_once('PHPMailer/PHPMailer/PHPMailerAutoload.php');
// include $_SERVER['DOCUMENT_ROOT'].'mail/PHPMailer/PHPMailer/PHPMailerAutoload.php';

$fname = "EJKIM";
$fmail = $mymail;
$to = $touchInfo[getEmail];
$subject = $mymailTitle;
$content = $summernote;
$regTime = time();
// $file = array(   );
// $file[] = array( "name" => 'send1.png', "path" => 'images/file1.png' );

  // function mailer($fname, $fmail, $to, $subject, $content, $type=0, $file="", $cc="", $bcc="")
  // {
      if ($type != 1) $content = nl2br($content);
      // type : text=0, html=1, text+html=2
      $mail = new PHPMailer(); // defaults to using php "mail()"
      $mail->IsSMTP();
         //   $mail->SMTPDebug = 2;
      $mail->SMTPSecure = "ssl";
      $mail->SMTPAuth = true;
      $mail->Host = "smtp.gmail.com";
      $mail->Port = 465;
      $mail->Username = "biz.ejkim";
      $mail->Password = "Password";
      $mail->CharSet = 'UTF-8';
      $mail->From = $fmail;
      $mail->FromName = $fname;
      $mail->Subject = $subject;
      $mail->AltBody = ""; // optional, comment out and test
      $mail->msgHTML($content);
      $mail->addAddress($to);
      if ($cc)
            $mail->addCC($cc);
      if ($bcc)
            $mail->addBCC($bcc);
      if ($file != "") {
            foreach ($file as $f) {
                  $mail->addAttachment($f['path'], $f['name']);
            }
      }
      if ( $mail->send()){
        echo "<br><메일이 성공적으로 보내졌습니다.>";
        $touch_sql = "UPDATE touch SET checkedMsg = '회신완료', sentTime = '$regTime', sentMsg = '$summernote' WHERE boardID = $boardID";
        $touch_result = $dbConnect->query($touch_sql);

      }  else{
        echo "<br><메일 발송에 실패 하였습니다.>";
      }
  // }
  // mailer($fname, $fmail, $to, $subject, $content, $type=0, $file="", $cc="", $bcc="");
  // $touch_sql = "UPDATE touch SET checkedMsg = '회신완료', sentTime = '$regTime', sentMsg = '$summernote' WHERE boardID = $boardID";
  // $touch_result = $dbConnect->query($touch_sql);
 ?>
