<?php
// board 테이블을 생성하는 예제
  include $_SERVER['DOCUMENT_ROOT'].'/common/mysql.php';//데이터베이스 접속

    $sql = "CREATE TABLE touch (";
    $sql .= "boardID int(10) unsigned NOT NULL auto_increment,";
    $sql .= "getName varchar(10) NOT NULL,";
    $sql .= "getEmail varchar(40) UNIQUE NOT NULL,";    -- 유니크 값 취소함
    $sql .= "message longtext NOT NULL,";--  ALTER TABLE touch CHANGE message title varchar(40); 로 수정 완료
-- ALTER TABLE touch ADD message longtext AFTER title; // 내용 추가
    $sql .= "regTime int(10) unsigned NOT NULL,"; -- unsigned를 선언해 주면 그 범위가 양수로 옮겨진다.
    $sql .= "checked enum('o','p','x') default 'x' NOT NULL,"; -- 삭제 후 테이블 추가 ALTER TABLE touch ADD COLUMN checkedMsg enum('회신완료','회신하기','차단') default '회신하기' NOT NULL AFTER checked
    $sql .= "primary key (boardID)";
    $sql .= ") charset=utf8";
// ALTER TABLE touch ADD COLUMN sentTime int(10) unsigned NOT NULL default 0; --회신 날짜 추가
// ALTER TABLE touch ADD COLUMN sentMsg longtext; --보낸 메세지 추가
    $res = $dbConnect->query($sql);

    if ( $res ) {
      echo "테이블 생성 완료";
    } else {
      echo "테이블 생성 실패";
    }

 ?>
