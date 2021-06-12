<?php
  include $_SERVER['DOCUMENT_ROOT'].'/common/mysql.php';//데이터베이스 접속

  $sql = "CREATE TABLE member (";
  $sql .= "memberID int(10) unsigned NOT NULL auto_increment,";
  $sql .= "email varchar(40) UNIQUE NOT NULL,";
  $sql .= "nickname varchar(10) NOT NULL,";
  $sql .= "pw varchar(40) default NULL,";
  $sql .= "birthday varchar(10) NOT NULL,";
  $sql .= "regTime int(11) NOT NULL,";
  $sql .= "primary key (memberID)";
  $sql .= ") charset=utf8";

  $res = $dbConnect->query($sql);

  if ( $res ) {
    echo "테이블 생성 완료";
  } else {
    echo "테이블 생성 실패";
  }
 ?>
