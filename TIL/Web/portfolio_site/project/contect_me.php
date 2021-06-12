<?php
session_start();//먼저 세션을 시작해주고,
//php 에서는 mail 이라고 하는 메일 발송 함수가 있다. 그런데 이걸 쓰려면 자체적인 메일서버를 가지고 있어야 한다. 그래서 개인 PC를 서버로 이용중인 사람은 사용하기가 어렵다.
//출처 : https://itlove.tistory.com/1716
header("Content-type: application/json; charset=utf-8"); // utf-8인코딩
// header('Content-Type: text/html; charset=utf-8'); // utf-8인코딩
// $conn = new mysqli("52.79.226.186","root","Zosel123?","blog_users");// 'DB호스트주소','DB아이디','DB암호','DB이름'
// $conn->set_charset("utf8");//DB문자열 utf-8 인코딩
// $header.= "MIME-Version: 1.0";
// $header.= "Content-Type: text/html; charset=utf-8";
// $header.= "X-Mailer: PHP";
// if( empty($_POST['name']) ||
//     empty($_POST['email']) ||
//     empty($_POST['message']) ||
//     !filter_var ($_POST['email'], FILTER_VALIDATE_EMAIL))
//     {
//       echo "No argument Provided!";
//       return false;
//     }

    $name = strip_tags(htmlspecialchars($_POST['name']));
    $email_address = strip_tags(htmlspecialchars($_POST['email']));
    $message = strip_tags(htmlspecialchars($_POST['message']));

    $to = 'kej820@nate.com';

    $email_subject = "$name 님께서 보냄";
    $email_body = "당신의 포트폴리오에서 문의 메일이 도착했습니다.\n\n"."이름: $name\n\nEmail: $email_address\n\n내용: $message";
    $headers = "From: ubuntu@www.ejkim.site\n";
    $headers .= "Reply-To: $email_address";
    // $result = mail($to, $email_subject, $email_body, $headers);

    if (!$result) {
      $result = array('rst_code'=>'false', 'rst_msg'=>'전송실패');
  } else {
      $result = array('rst_code'=>'false', 'rst_msg'=>'전송성공');
  }

  // $sql  = "
  //     INSERT INTO QA_BOARD (
  //         BOARD_USER,
  //         BOARD_MAIL,
  //         BOARD_TITLE,
  //         BOARD_CONTENT
  //     ) VALUES (
  //         $name,
  //         $email_address,
  //         $email_subject,
  //         $message
  //     )";
  // $result = mysqli_query($conn, $sql);
  // echo $result;
  // if(!$result){
  //     echo "실패";
  //     echo mysqli_error($conn);
  // }
  // else {
  //   echo "데이터가 업로드 되었습니다";
  // }


echo   $email_subject."\n";
echo   $email_body."\n";
echo   $headers."\n";
echo   $result."\n";
echo   $to."\n";
 echo json_encode($result, JSON_UNESCAPED_UNICODE);
 ?>
