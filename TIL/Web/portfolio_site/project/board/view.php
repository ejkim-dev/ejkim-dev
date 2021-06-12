<?php
include $_SERVER['DOCUMENT_ROOT'].'/common/session.php';//session파일 include
include $_SERVER['DOCUMENT_ROOT'].'/common/checkSignSession.php';//로그인 안하면 접근 못함
include $_SERVER['DOCUMENT_ROOT'].'/common/mysql.php';//데이터베이스 접속
 ?>
<!DOCTYPE html>
<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8">
    <title>회신 하기</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700,800,900" rel="stylesheet">
    <!-- css 파일 수정 후 적용 안될 때 대처 : ?after 부분을 추가해줍니다. 문자열은 아무거나 상관이 없습니다. 요는 브라우저가 다른 CSS를 인식하게끔만 하면 되니까요.-->
    <link rel="stylesheet" href="/blog/css/style.css">
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
    <link href="./editor/summernote-lite.css" rel="stylesheet">
    <script src="./editor/summernote-lite.js"></script>
    <script src="./editor/lang/summernote-ko-KR.js"></script>
    <!-- <script src="https://github.com/summernote/summernote/tree/master/lang/summernote-ko-KR.js"></script> -->
    <script >
      $(document).ready(function() {
        $('#summernote').summernote({
          lang: 'ko-KR', // default: 'en-US'
          height: 400,
          minHeight : null,
          maxHeight : null,
          focus: true
        });
      });
    </script>

  </head>
  <body>
    <!-- 상단 메뉴바 -->
    <header><?php include $_SERVER['DOCUMENT_ROOT'].'/common/adminHeader.php'; ?></header>
    <!-- GET 방식으로 전달받은 boardID의 값을 이용하여 해당 게시물의 내용을 불러옴 -->
    <?php
// var_dump( $_SESSION );//세션 전체 값 불러오기
      if (isset($_GET['boardID']) && (int) $_GET['boardID'] > 0) {// $_GET['boardID']가 존재하고 0을 초과하는지 확인
          $boardID = $_GET['boardID'];// $_GET['boardID']의 값을 변수 $boardID에 대입

          //데이터 가져오기
          $sql = "SELECT * FROM touch WHERE boardID = {$boardID} ";
          $result = $dbConnect->query($sql);
          //
          // $master_sql = "SELECT email FROM member WHERE nickname = 'Master' ";
          // $master_result = $dbConnect->query($sql);
          //
          // if ($master_result) {
          //   $mymail = $result->fetch_array(MYSQLI_ASSOC);// MYSQLI_ASSOC : 컬럼명으로 값을 불러옴
          //   echo "1. $mymail = ".$mymail[email];
          //
          // } else {
          //   echo "잘못된 접근입니다.";
          //   exit;
          // }


          if ($result) {
            $contentInfo = $result->fetch_array(MYSQLI_ASSOC);// MYSQLI_ASSOC : 컬럼명으로 값을 불러옴
            // echo "작성자 : ".$contentInfo[getName]."<br>";
            // echo "작성자 메일 : ".$contentInfo[getEmail]."<br>";
            // echo "수령일 : ".date('Y-m-d A h:i',$contentInfo['regTime'])."<br>";
            // echo "회신일 : ";
            //     if ($contentInfo['sentTime'] == 0) {
            //       echo '미회신';
            //     } else {
            //       echo date('Y-m-d A h:i',$contentInfo['sentTime']);
            //     }
            //
            // echo "<br><br>";
            // // $regDate = date("Y-m-d A h:i");
            // // echo "게시일 : {$regDate}<br><br>";
            // echo "제목 : ".$contentInfo[title]."<br>";
            // echo "내용 <br>";
            // echo $contentInfo[message].'<br>';
            // echo "&emsp;<a href='javascript:history.back()'>
            // <button type='button' class='btn btn-primary btn-sm' >
            // 뒤로가기 </button></a>&emsp;";
            //블록여부
            // if (isset($_GET['block']) && (int) $_GET['block'] > 0) {//블록 이 1 이상
            //   $block = $_GET['block'];
            //   // $sql = "UPDATE touch SET checkedMsg = '차단' WHERE boardID = {$boardID} ";
            //   // $result = $dbConnect->query($sql);
            //   echo "차단됨";
            // }
            // else if (isset($_GET['block']) &&(int) $_GET['block'] == 0)  {
            //   echo "차단아님";
            //   // $sql = "UPDATE touch SET checkedMsg = '회신하기' WHERE boardID = {$boardID} ";
            //   // $dbConnect->query($sql);
            // }

            ?>
            <!-- 메일 형식의 form 만들기 -->

            <div class="comment-form-wrap col-9 ">

              <form name="sendMail"  <?php  echo "action='/mail/?boardID={$boardID}'"; ?> class="p-5 bg-light" method="post">
                <h4 class="mb-5">Mail Box</h4>

                <div class="form-group">
                  <a href='javascript:history.back()'><button type='button' class='btn btn-primary btn-sm' >
                  뒤로가기 </button></a>&emsp;
                  <!-- <input type="submit" value="회신하기" class="btn btn-dark btn-sm"> -->
                  <?php
                  // '회신완료','회신하기','차단'
                            if ($contentInfo[checkedMsg] == '차단') {
                              echo "<a href='/board/blockProcessing0.php?boardID=";
                              echo "{$boardID}'>";
                              echo "<button type='button' class='btn btn-danger btn-sm' >
                              차단해제
                              </button></a>"; //btn-danger
                            }
                            // 차단 아닐 경우
                            else {
                              if ($contentInfo[checkedMsg] == '회신하기') {
                                //회신하기 버튼 클릭하면 데이터 바뀜 = > 메일 보낼 예정
                                echo "<button type='submit' class='btn btn-dark btn-sm' >
                                회신하기 </button>&emsp;";//btn-outline-dark

                              }
                              // 회신완료 버튼 비활성화
                              else {
                                echo "<a href='/board/list.php'>
                                <button type='button' class='btn btn-outline-dark btn-sm' disabled>
                                회신완료 </button></a>&emsp;";//btn-outline-dark
                              }

                              // setcookie('boardID',$contentInfo[getEmail],time()+86400,'/board');
                              echo "<a href='/board/blockProcessing.php?boardID=";
                              echo "{$boardID}'>";
                              echo "<button type='button' class='btn btn-outline-danger btn-sm' >
                              차단하기
                              </button></a>"; //btn-danger
                            } ?>
                </div>
                <div class="form-group">
                  <label for="text">내메일&nbsp;</label>
                  <input type="text" class="form-control-sm col-5" name="myMail" id="myMail"
                  <?php
                  // 내 세션 메일을 불러옴
                  if(isset($_SESSION['email'])) {
                   echo "value={$_SESSION['email']} disabled";
                   } ?>  required  />
                </div>
                <div class="form-group">
                  <label for="datetime">회신일&nbsp;</label>
                  <input type="datetime" class="form-control-sm col-5"
                  <?php
                  if ($contentInfo['sentTime'] == 0) {
                    echo "value='미회신'";
                  } else {
                    echo 'value='.date('Y-m-d D A h:i',$contentInfo['sentTime']);
                  }
                   ?>  disabled />
                </div>
                <div class="form-group">
                  <label for="text">제목&emsp;&nbsp;</label>

                  <input type="text" class="form-control-sm col-5" name="mymailTitle" id="mymailTitle"
                  value="RE : EJKIM 포트폴리오에 남긴 메세지에 대한 회신입니다." required />
                </div>

                <div class="form-group">
                  <!-- 무조건 textarea 태그를 사용할 필요는 없지만 textarea 태그를 사용하면 form 태그 안에 배치했을 시 submit 할 때 에디터 내용이 같이 전송됨 -->
                  <!-- <input type="textarea" class="form-control-sm col-5" id="summernote" name="summernote" required /> -->
              <textarea class="form-control-sm col-5" id="summernote" name="summernote">
                <?php if (!empty($contentInfo[sentMsg])) { echo $contentInfo[sentMsg]; } else {?>
                안녕하세요, 김은진 입니다.
                <br><br>제 포트폴리오에 관심 가져주셔서 감사합니다.
                <br><br>감사합니다.
                <br><br>김은진 드림.
                <br><br>-----Original Message-----
                <br>Name :  &nbsp; <?php echo $contentInfo[getName]; ?>
                <br>From : &nbsp; <?php echo $contentInfo[getEmail]; ?>
                <br>Sent : &nbsp; <?php echo date('Y-m-d A h:i',$contentInfo['regTime']); ?>
                <br>Subject : &nbsp; <?php echo $contentInfo[title]; ?><br><br>
                <?php echo $contentInfo[message]; } ?>
              </textarea>
                </div>
              </form>

            </div>

            <?php
          } else {
            echo "잘못된 접근입니다.";
            exit;
          }
      } else {
        echo "잘못된 접근 입니다.";
        exit;
      }

     ?>
  </body>
</html>
