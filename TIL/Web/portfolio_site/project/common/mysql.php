<?php
  $host = "127.0.0.1";
  $user = "myblog";
  $pw = "비밀번호";
  $dbName = "myblog_db";
  $dbConnect = new mysqli($host, $user, $pw, $dbName);
  $dbConnect->set_charset("utf8");

  // echo $dbName;//연결 db 확인용
  if(mysqli_connect_errno()){
    echo "데이터베이스 {$dbName}에 접속 실패";
  }
?>
