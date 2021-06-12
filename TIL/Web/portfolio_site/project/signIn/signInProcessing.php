<?php
  include $_SERVER['DOCUMENT_ROOT'].'/common/session.php';//session파일 include
  include $_SERVER['DOCUMENT_ROOT'].'/common/mysql.php';//데이터베이스 접속

  $email = $_POST['userEmail'];
  $pw = $_POST['userPw'];
  $checked = $_POST['idSaveCheck'];

  function goSignInPage($alert){ //로그인 정보가 일치하지 않으면
    // echo $alert.'<br>';
    // echo "<a href='./signInForm.php'>로그인 폼으로 이동</a>";
    // echo '<script>alert("message successfully sent")</script>';
    // Header("Location:../signIn/signInForm.php");//로그인 정보 일치하지 않으면 남아있기
    // echo "체크 여부 : ";

// 다이얼로그창
    echo "<script>
        window.alert('$alert');
        window.history.back();
    </script>";
    // echo "<br>";
    // echo "<a href='/signUp/signUpForm.php'>회원가입 폼으로 이동</a>";
    return;

  }

  //유효성 검사
  //이메일 검사
  if (!filter_Var($email, FILTER_VALIDATE_EMAIL)) {
    goSignInPage('올바른 이메일이 아닙니다.');
    exit;
  }

  //비밀번호 검사
  if ($pw == null || $pw == '') {
    goSignInPage('비밀번호를 입력해 주세요.');
    exit;
  }

  $pw = sha1('sogha586'.$pw);
  // $pw = hash('sha512', $pw);

  $sql = "SELECT email, nickName, memberID FROM member ";
  $sql .= "WHERE email = '{$email}' AND pw = '{$pw}'";
  $result = $dbConnect->query($sql);

  if ($result) {
    if($result->num_rows == 0){
      goSignInPage('로그인 정보가 일치하지 않습니다.');
      exit;
    } else {

        if(isset($_POST['idSaveCheck'])){ // 체크박스에 체크 되었을 때
          // echo "<script></script>";
          // 쿠키 저장하기
          //userID : 쿠키명, time()+3600 = 현재시간부터 1시간(60*60), 쿠키 적용 범위 : '/' 최상단 파일
          setcookie('memberID',$email,time()+86400,'/signIn');
          // setcookie('autologin','autologin',time()+86400,'/');
        } else { // 체크가 해제되었을 때
          setcookie('memberID',$email,time()-100,'/signIn');//-100을 현재보다 앞으로 적용해 삭제
        }

      $memberInfo = $result->fetch_array(MYSQLI_ASSOC);
      $_SESSION['memberID'] = $memberInfo['memberID'];
      $_SESSION['nickName'] = $memberInfo['nickname'];
      $_SESSION['email'] = $memberInfo['email'];
      Header("Location:../sovpdlwl/");//로그인 정보 일치하면 넘어가기
      exit;
//          window.alert('자동로그인 설정을 하시겠습니까?');

// 자동로그인 설정 스크립트
      // echo "<script>
      //     var con_test = confirm('자동로그인 설정을 하시겠습니까?');
      //     if(con_test == true){
      //       window.alert('자동로그인이 설정되었습니다.');
      //     }
      //     else if(con_test == false){
      //       document.cookie = 'autologin=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
      //     }
      //     location.href='/sovpdlwl/index.php';
      // </script>";
    }
  }
 ?>
