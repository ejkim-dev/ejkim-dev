<!-- 차단 하고 차단 확인 창이 뜸 -->
<!-- 클릭했을때 해당 데이터가 차단됨 -->
<?php
  include $_SERVER['DOCUMENT_ROOT'].'/common/session.php';//session파일 include
  include $_SERVER['DOCUMENT_ROOT'].'/common/checkSignSession.php';//로그인 안하면 접근 못함
  include $_SERVER['DOCUMENT_ROOT'].'/common/mysql.php';//데이터베이스 접속

  if (isset($_GET['boardID']) && (int) $_GET['boardID'] > 0) {// $_GET['boardID']가 존재하고 0을 초과하는지 확인
      $boardID = $_GET['boardID'];
      $sql = "UPDATE touch SET checkedMsg = '차단' WHERE boardID = {$boardID} ";
      $result = $dbConnect->query($sql);
    }
?>

<script>
      window.alert('차단이 완료되었습니다.');
      window.history.back();
      // location.href='/board/view.php?boardID=<?//php echo $boardID;?>';
</script>

  <!-- echo "<script>
      var con_test = confirm('해당 메일을 차단 하시겠습니까?');
          if(con_test == true){
            window.alert('차단이 완료되었습니다.');
            location.href='/board/view.php?boardID=23&block=1';
          }
          else if(con_test == false){
            window.alert('차단을 취소하였습니다.');
            location.href='/board/view.php?boardID=23&block=0';
          }

  </script>";
  exit; -->
