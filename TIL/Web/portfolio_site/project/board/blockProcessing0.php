<!-- 차단 하고 차단 확인 창이 뜸 -->
<!-- 클릭했을때 해당 데이터가 차단됨 -->
<?php
  include $_SERVER['DOCUMENT_ROOT'].'/common/session.php';//session파일 include
  include $_SERVER['DOCUMENT_ROOT'].'/common/checkSignSession.php';//로그인 안하면 접근 못함
  include $_SERVER['DOCUMENT_ROOT'].'/common/mysql.php';//데이터베이스 접속

  if (isset($_GET['boardID']) && (int) $_GET['boardID'] > 0) {// $_GET['boardID']가 존재하고 0을 초과하는지 확인
      $boardID = $_GET['boardID'];
      // $sql = "UPDATE touch SET checkedMsg = '회신하기' WHERE boardID = {$boardID} AND sentTime = 0 ";
      // $sql = "UPDATE touch SET checkedMsg = '회신완료' WHERE boardID = {$boardID} AND NOT sentTime = 0 ";
      $sql = "UPDATE touch SET checkedMsg = if (sentTime = 0, '회신하기', '회신완료') WHERE boardID = {$boardID} ";
      $result = $dbConnect->query($sql);
    }
?>

<script>
      window.alert('차단을 취소하였습니다');
      window.history.back();
</script>
<!--       $sql = "UPDATE touch
                SET checkedMsg = CASE WHEN sentTime = 0
                THEN checkedMsg = '회신하기'
                ELSE checkedMsg = '회신완료'
                WHERE boardID = {$boardID} "; -->
