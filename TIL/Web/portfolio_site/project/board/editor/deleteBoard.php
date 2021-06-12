<?php
  include $_SERVER['DOCUMENT_ROOT'].'/common/session.php';//session파일 include
  include $_SERVER['DOCUMENT_ROOT'].'/common/checkSignSession.php';//로그인 안하면 접근 못함
  include $_SERVER['DOCUMENT_ROOT'].'/common/mysql.php';//데이터베이스 접속

  $boardID = $_GET['boardID'];

  //해당 게시판 삭제
  $sql = "DELETE FROM board WHERE boardID = {$boardID} ";
  $result = $dbConnect->query($sql);

  if ($result) {
    echo "<script>
        window.alert('삭제하였습니다.');
        location.href='/blog/';
    </script>";

      exit;
  } else {
    echo "<script>
        window.alert('실패 : 관리자에게 문의');
        location.href='/blog/';
    </script>";
    exit;
  }

 ?>
